package com.cn.uca.ui.view.home.footprint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.footprint.CityNameAdapter;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.home.footprint.CityNameBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.yusheng.YuShengActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.StatusView;
import com.cn.uca.view.datepicker.DatePickDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFootActivity extends AppCompatActivity implements View.OnClickListener,OnSureLisener{

    private TextView close,cityName,time,place,choose,finish,yearNum,cityNum;
    private ImageView pic;
    private EditText content;
    private RelativeLayout llTitle;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private File bais;
    private String code,name;
    private int year_num,city_num;
    private List<CityNameBean> listCity;
    private int cityId = 0;
    private String travelTime,travelPlace,travelContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_add_foot);

        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            code = intent.getStringExtra("code");
            name = intent.getStringExtra("name");
            year_num = intent.getIntExtra("yearNum",0);
            city_num = intent.getIntExtra("cityNum",0);
        }
    }

    private void initView() {
        llTitle = (RelativeLayout)findViewById(R.id.llTitle);
        StatusMargin.setRelativeLayout(this,llTitle);
        close = (TextView)findViewById(R.id.close);
        cityName = (TextView)findViewById(R.id.name);
        time = (TextView)findViewById(R.id.time);
        place = (TextView)findViewById(R.id.place);
        choose = (TextView)findViewById(R.id.choose);
        finish = (TextView)findViewById(R.id.finish);
        yearNum = (TextView)findViewById(R.id.yearNum);
        cityNum  = (TextView)findViewById(R.id.cityNum);
        content = (EditText)findViewById(R.id.content);

        yearNum.setText(year_num+"");
        cityNum.setText(city_num+"");
        cityName.setText(name);
        pic = (ImageView)findViewById(R.id.pic);

        close.setOnClickListener(this);
        place.setOnClickListener(this);
        time.setOnClickListener(this);
        choose.setOnClickListener(this);
        finish.setOnClickListener(this);

        listCity = new ArrayList<>();
        getCityName();
    }

    private void getCityName(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("code",code);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        final String sign = SignUtil.sign(map);

        HomeHttp.getCityName(sign, time_stamp, account_token, code, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONArray("data");
                            List<CityNameBean> bean = gson.fromJson(array.toString(),new TypeToken<List<CityNameBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                listCity.addAll(bean);
                            }
                            break;
                    }
                }
                catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close:
                this.finish();
                break ;
            case R.id.place:
                showCityDialog();
                break;
            case R.id.time:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.choose:
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(this, arrayString, title, onDialogClick);
                dialog.show();
                break;
            case R.id.finish:
                addFootprintChina();
                break;
        }
    }

    private void showCityDialog(){
        final Dialog dialog = new Dialog(this,R.style.dialog_style);
        View inflate = LayoutInflater.from(this).inflate(R.layout.choose_city_dialog, null);
        ListView listView = (ListView)inflate.findViewById(R.id.listView);
        Button btn_cancel = (Button)inflate.findViewById(R.id.btn_cancel);


        CityNameAdapter adapter = new CityNameAdapter(listCity,this);
        listView.setAdapter(adapter);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cityId = listCity.get(i).getCity_id();
                travelPlace = listCity.get(i).getCity_name();
                place.setText(travelPlace);
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        SetLayoutParams.setFrameLayout(inflate,MyApplication.width,MyApplication.height*4/9);
        StatusMargin.setFrameLayoutBottom(AddFootActivity.this,inflate,20);
        dialog.show();//显示对话框
    }
    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(60);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(this);
        dialog.show();
    }

    private void addFootprintChina(){
        if (StringXutil.isEmpty(travelTime)){
            ToastXutil.show("时间线不能为空");
        }else {
            if (StringXutil.isEmpty(travelPlace)){
                ToastXutil.show("地点不能为空");
            }else{
                travelContent = content.getText().toString().trim();
                if (StringXutil.isEmpty(travelContent)){
                    ToastXutil.show("请编辑您的旅行回忆");
                }else{
                    Map<String,Object> map = new HashMap<>();
                    String account_token = SharePreferenceXutil.getAccountToken();
                    map.put("account_token", account_token);
                    String time_stamp = SystemUtil.getCurrentDate2();
                    map.put("time_stamp",time_stamp);
                    map.put("city_id",cityId);
                    map.put("travel_time",travelTime);
                    map.put("content",travelContent);
                    String sign = SignUtil.sign(map);
                    HomeHttp.addFootprintChina(sign, time_stamp, account_token, cityId, travelTime, travelContent, bais, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            if (i == 200){
                                try {
                                    JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                                    Log.i("123",jsonObject.toString());
                                    int code = jsonObject.getInt("code");
                                    switch (code){
                                        case 0:
                                            ToastXutil.show("添加成功");
                                            Intent intent = new Intent();
                                            intent.putExtra("cityName",name);
                                            setResult(0,intent);
                                            AddFootActivity.this.finish();
                                            break;
                                    }
                                }catch (Exception e){

                                }
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                        }
                    });
                }
            }
        }
    }
    // 对话框
    DialogInterface.OnClickListener onDialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    startCamearPicCut(dialog);// 开启照相
                    break;
                case 1:
                    startImageCaptrue(dialog);// 开启图库
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 打开相机
     *
     * @param dialog
     */
    public void startCamearPicCut(DialogInterface dialog) {
        dialog.dismiss();
        // 调用系统的拍照功能
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("camerasensortype", 2);// 调用前置摄像头
            intent.putExtra("autofocus", true);// 自动对焦
            intent.putExtra("fullScreen", false);// 全屏
            intent.putExtra("showActionIcons", false);
            // 指定调用相机拍照后照片的储存路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(MyApplication.tempFile));
            startActivityForResult(intent, Constant.PHOTO_REQUEST_TAKEPHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 开启图库
     *
     * @param dialog
     */
    public void startImageCaptrue(DialogInterface dialog) {
        dialog.dismiss();
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, Constant.PHOTO_REQUEST_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 将图片显示到UI界面上
    private void setPicToView(File picdata) {
        bais = picdata;
        final Bitmap bitmap= BitmapFactory.decodeFile(picdata.toString());
        new Thread() {
            @Override
            public void run() {
                if (bitmap != null) {
                    handler.obtainMessage(0, bitmap).sendToTarget();
                    //将bitmap转换成File类型
                } else {
                    handler.obtainMessage(-1, null).sendToTarget();
                }
            }
        }.start();
    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.obj != null) {
                Drawable drawable = new BitmapDrawable((Bitmap) msg.obj);
                pic.setVisibility(View.VISIBLE);
                pic.setImageDrawable(drawable);
            }
        };
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.PHOTO_REQUEST_TAKEPHOTO:
                setPicToView(MyApplication.tempFile);
                break;
            case Constant.PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    setPicToView(new File(SystemUtil.getRealPathFromURI(data.getData(),this)));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSure(Date date) {
        try {
            if (date.after(SystemUtil.StringToUtilDate(SystemUtil.getCurrentDateOnly()))){
                ToastXutil.show("时间线不能大于今天");
            }else{
                travelTime = SystemUtil.UtilDateToString(date);
                time.setText(travelTime);
            }
        }catch (Exception e){
        }
    }
}

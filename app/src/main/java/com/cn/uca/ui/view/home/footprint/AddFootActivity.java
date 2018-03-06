package com.cn.uca.ui.view.home.footprint;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.cn.uca.ui.view.util.BaseHideActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusBarUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.datepicker.DatePickDialog;
import com.cn.uca.view.dialog.LoadDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFootActivity extends BaseHideActivity implements View.OnClickListener,OnSureLisener{

    private TextView close,cityName,time,place,choose,finish,yearNum,cityNum;
    private ImageView pic;
    private EditText content;
    private RelativeLayout llTitle;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private Bitmap bais;
    private String codeCity,name,cityS,timeS,contentS,urlS;
    private int year_num,city_num;
    private List<CityNameBean> listCity;
    private int cityId = 0;
    private int id = 0;
    private String travelTime,travelPlace,travelContent,type;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_foot);
        StatusBarUtil.immersive(this);
        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            type = intent.getStringExtra("type");
            switch (type){
                case "chinaMap":
                    codeCity = intent.getStringExtra("code");
                    name = intent.getStringExtra("name");
                    year_num = intent.getIntExtra("yearNum",0);
                    city_num = intent.getIntExtra("cityNum",0);
                    break;
                case "chinaClick":
                    codeCity = intent.getStringExtra("code");
                    name = intent.getStringExtra("name");
                    year_num = intent.getIntExtra("yearNum",0);
                    city_num = intent.getIntExtra("cityNum",0);
                    travelPlace = name;
                    break;
                case "updateChina":
                    codeCity = intent.getIntExtra("code",0)+"";
                    cityId = intent.getIntExtra("cityId",0);
                    id = intent.getIntExtra("id",0);
                    Log.e("456",id+"----");
                    name = intent.getStringExtra("name");
                    cityS = intent.getStringExtra("city");
                    timeS = intent.getStringExtra("time");
                    contentS = intent.getStringExtra("content");
                    urlS = intent.getStringExtra("url");
                    year_num = intent.getIntExtra("yearNum",0);
                    city_num = intent.getIntExtra("cityNum",0);
                    travelTime = timeS;
                    travelContent = contentS;
                    travelPlace = cityS;
                    break;
                case "world":
                    codeCity = intent.getStringExtra("code");
                    name = intent.getStringExtra("name");
                    year_num = intent.getIntExtra("yearNum",0);
                    city_num = intent.getIntExtra("cityNum",0);
                    break;
                case "updateWorld":
                    id = intent.getIntExtra("id",0);
                    cityId = intent.getIntExtra("countryId",0);
                    name = intent.getStringExtra("name");
                    timeS = intent.getStringExtra("time");
                    contentS = intent.getStringExtra("content");
                    urlS = intent.getStringExtra("url");
                    year_num = intent.getIntExtra("yearNum",0);
                    city_num = intent.getIntExtra("cityNum",0);
                    travelContent = contentS;
                    travelTime = timeS;
                    break;
            }

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

        switch (type){
            case "chinaMap":
                getCityName();
                break;
            case "chinaClick":
                place.setVisibility(View.GONE);
                break;
            case "updateChina":
                getCityName();
                place.setText(cityS);
                this.time.setText(timeS);
                this.content.setText(contentS);
                if (!StringXutil.isEmpty(urlS)){
                    pic.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(urlS,pic);
                }
                break;
            case "world":
                place.setVisibility(View.GONE);
                break;
            case "updateWorld":
                place.setVisibility(View.GONE);
                this.time.setText(timeS);
                this.content.setText(contentS);
                if (!StringXutil.isEmpty(urlS)){
                    pic.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(urlS,pic);
                }
                break;
        }
    }

    private void getCityName(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("code",codeCity);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        final String sign = SignUtil.sign(map);
        HomeHttp.getCityName(sign, time_stamp, account_token, codeCity, new CallBack() {
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
                switch (type){
                    case "chinaMap":
                        addFootprintChina();
                        break;
                    case "chinaClick":
                        addFootprintChina();
                        break;
                    case "updateChina":
                        updateFootPrintChain();
                        break;
                    case "world":
                        addFootprintWorld();
                        break;
                    case "updateWorld":
                        updateFootprintWorld();
                        break;
                }
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

    //添加中国足迹
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
                    LoadDialog.show(this);
                    Map<String,Object> map = new HashMap<>();
                    String account_token = SharePreferenceXutil.getAccountToken();
                    map.put("account_token", account_token);
                    String time_stamp = SystemUtil.getCurrentDate2();
                    map.put("time_stamp",time_stamp);
                    map.put("travel_time",travelTime);
                    map.put("content",travelContent);
                    File file = null;
                    if (bais != null){
                        file = PhotoCompress.comp(bais);
                    }
                    String sign;
                    switch (type){
                        case "chinaMap":
                            map.put("city_id",cityId);
                           sign = SignUtil.sign(map);
                            HomeHttp.addFootprintChina(sign, time_stamp, account_token, cityId,"", travelTime, travelContent, file, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    if (i == 200){
                                        LoadDialog.dismiss(AddFootActivity.this);
                                        try {
                                            JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                                            int code = jsonObject.getInt("code");
                                            switch (code){
                                                case 0:
                                                    ToastXutil.show("添加成功");
                                                    Intent intent = new Intent();
                                                    intent.putExtra("type","province");
                                                    intent.putExtra("cityName",name);
                                                    setResult(0,intent);
                                                    AddFootActivity.this.finish();
                                                    break;
                                                default:
                                                    ToastXutil.show("添加失败");
                                                    LoadDialog.dismiss(AddFootActivity.this);
                                                    break;
                                            }
                                        }catch (Exception e){
                                            LoadDialog.dismiss(AddFootActivity.this);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                    LoadDialog.dismiss(AddFootActivity.this);
                                }
                            });
                            break;
                        case "chinaClick":
                            map.put("gaode_code",codeCity);
                            sign = SignUtil.sign(map);
                            HomeHttp.addFootprintChina(sign, time_stamp, account_token, 0,codeCity,travelTime, travelContent, file, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    if (i == 200){
                                        LoadDialog.dismiss(AddFootActivity.this);
                                        try {
                                            JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                                            int code = jsonObject.getInt("code");
                                            switch (code){
                                                case 0:
                                                    ToastXutil.show("添加成功");
                                                    Intent intent = new Intent();
                                                    intent.putExtra("type","city");
                                                    intent.putExtra("cityName",codeCity);
                                                    setResult(0,intent);
                                                    AddFootActivity.this.finish();
                                                    break;
                                                default:
                                                    ToastXutil.show("添加失败");
                                                    LoadDialog.dismiss(AddFootActivity.this);
                                                    break;
                                            }
                                        }catch (Exception e){
                                            LoadDialog.dismiss(AddFootActivity.this);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                    LoadDialog.dismiss(AddFootActivity.this);
                                }
                            });
                            break;
                    }

                }
            }
        }
    }

    //修改中国足迹
    private void updateFootPrintChain(){
        LoadDialog.show(this);
        travelContent = content.getText().toString();
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("city_id",cityId);
        map.put("travel_time",travelTime);
        map.put("content",travelContent);
        map.put("footprint_city_id",id);
        File file = null;
        if (bais != null){
            file = PhotoCompress.comp(bais);
        }
        String sign = SignUtil.sign(map);
        HomeHttp.updateFootprintChina(sign, time_stamp, account_token, cityId, travelTime, travelContent, id, file, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200){
                    LoadDialog.dismiss(AddFootActivity.this);
                    try {
                        JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                        Log.e("456",jsonObject.toString()+"-");
                        int code = jsonObject.getInt("code");
                        switch (code) {
                            case 0:
                                LoadDialog.dismiss(AddFootActivity.this);
                                ToastXutil.show("修改成功");
                                Intent intent = new Intent();
                                setResult(1,intent);
                                AddFootActivity.this.finish();
                                break;
                            default:
                                LoadDialog.dismiss(AddFootActivity.this);
                                ToastXutil.show("修改失败");
                                break;
                        }
                    }catch (Exception e){
                        Log.e("456",e.getMessage()+"*");
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
    //添加世界足迹
    private void addFootprintWorld(){
        if (StringXutil.isEmpty(travelTime)){
            ToastXutil.show("时间线不能为空");
        }else {
            travelContent = content.getText().toString().trim();
            if (StringXutil.isEmpty(travelContent)){
                ToastXutil.show("请编辑您的旅行回忆");
            }else{
                LoadDialog.show(this);
                Map<String,Object> map = new HashMap<>();
                String account_token = SharePreferenceXutil.getAccountToken();
                map.put("account_token", account_token);
                String time_stamp = SystemUtil.getCurrentDate2();
                map.put("time_stamp",time_stamp);
                map.put("country_id",codeCity);
                map.put("travel_time",travelTime);
                map.put("content",travelContent);
                String sign = SignUtil.sign(map);
                File file = null;
                if (bais != null){
                   file = PhotoCompress.comp(bais);
                }
                HomeHttp.addFootprintWorld(sign, time_stamp, account_token, codeCity, travelTime, travelContent, file, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        LoadDialog.dismiss(AddFootActivity.this);
                        if (i == 200){
                            try {
                                JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                                Log.i("123",jsonObject.toString());
                                int code = jsonObject.getInt("code");
                                switch (code){
                                    case 0:
                                        ToastXutil.show("添加成功");
                                        Intent intent = new Intent();
                                        intent.putExtra("code",codeCity);
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
                        LoadDialog.dismiss(AddFootActivity.this);
                    }
                });
            }
        }
    }
    //修改世界足迹
    private void updateFootprintWorld(){
        LoadDialog.show(this);
        travelContent = content.getText().toString();
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("footprint_country_id",id);
        map.put("content",travelContent);
        map.put("country_id",cityId);
        map.put("travel_time",travelTime);
        String sign = SignUtil.sign(map);
        File file = null;
        if (bais != null){
            file = PhotoCompress.comp(bais);
        }
        HomeHttp.updateFootprintWorld(sign,time_stamp, account_token, id, travelContent, cityId, travelTime, file, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200){
                    LoadDialog.dismiss(AddFootActivity.this);
                    try {
                        JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                        Log.e("456",jsonObject.toString()+"-");
                        int code = jsonObject.getInt("code");
                        switch (code) {
                            case 0:
                                LoadDialog.dismiss(AddFootActivity.this);
                                ToastXutil.show("修改成功");
                                Intent intent = new Intent();
                                setResult(1,intent);
                                AddFootActivity.this.finish();
                                break;
                            default:
                                LoadDialog.dismiss(AddFootActivity.this);
                                ToastXutil.show("修改失败");
                                break;
                        }
                    }catch (Exception e){
                        Log.e("456",e.getMessage()+"*");
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
    // 对话框
    DialogInterface.OnClickListener onDialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    autoObtainCameraPermission();// 开启照相
                    break;
                case 1:
                    autoObtainStoragePermission();// 开启图库
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastXutil.show("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (SystemUtil.hasSDCard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(this, "com.cn.uca.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                takePicture();
            } else {
                ToastXutil.show("设备没有SD卡！");
            }
        }
    }
    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            openPic();
        }
    }
    /**
     * 调用系统相机
     */
    public  void takePicture() {
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentCamera, Constant.PHOTO_REQUEST_TAKEPHOTO);
    }
    /**
     * 打开相册的请求码
     */
    public void openPic() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Constant.PHOTO_REQUEST_GALLERY);
    }
    /**
     * 权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (SystemUtil.hasSDCard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(this, "com.cn.uca.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                            takePicture();
                    } else {
                        ToastXutil.show("设备没有SD卡！");
                    }
                } else {
                    ToastXutil.show("请允许打开相机！！");
                }
                break;

            }
            case Constant.STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPic();
                } else {
                    ToastXutil.show("请允许打操作SDCard！！");
                }
                break;
        }
    }
    // 将图片显示到UI界面上
    private void setPicToView(File picdata) {
        if (picdata != null){
            try{
                bais= BitmapFactory.decodeFile(picdata.toString());
                new Thread() {
                    @Override
                    public void run() {
                        if (bais != null) {
                            handler.obtainMessage(0, bais).sendToTarget();
                            //将bitmap转换成File类型
                        } else {
                            handler.obtainMessage(-1, null).sendToTarget();
                        }
                    }
                }.start();
            }catch (Exception e){

            }
        }
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
        File file = null;
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case Constant.PHOTO_REQUEST_TAKEPHOTO:
                    setPicToView(fileUri);
                    break;
                case Constant.PHOTO_REQUEST_GALLERY:
                    if (data.getData() != null) {
                        file = new File(SystemUtil.getRealPathFromURI(data.getData(),this));
                        setPicToView(file);
                    }
                    break;
            }
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

package com.cn.uca.ui.view.home.lvpai;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.CallBackBean;
import com.cn.uca.bean.home.lvpai.ImgAndContentBean;
import com.cn.uca.bean.home.lvpai.LableBean;
import com.cn.uca.bean.home.samecityka.SendImgFileBean;
import com.cn.uca.bean.util.ImageItem;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.CallBackValue;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.ui.view.util.CityActivity;
import com.cn.uca.ui.view.util.CountyActivity;
import com.cn.uca.ui.view.util.PhotoPickerActivity;
import com.cn.uca.ui.view.util.PreviewPhotoActivity;
import com.cn.uca.util.Bimp;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.dialog.LoadDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMerchantActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,send;
    private EditText lvpaiTitle,price;
    private RadioButton abroad,domestic;
    private RelativeLayout layout1,layout2,layout3,layout4;
    private List<Bitmap> mResults = new ArrayList<>();
    private GridView gridview;
    private GridAdapter adapter;
    public static Bitmap bimap;
    private static final int PICK_PHOTO = 1;
    private ArrayList<String> result;
    private int lvpaiType = 0;//旅拍类型
    private Dialog lableDialog;//推荐风格弹窗
    private View inflateLable;//推荐风格布局
    private FluidLayout lable;
    private EditText lable_text;
    private TextView lablename,config;
    private List<String> listLable;
    private List<String> selectList;
    private int day,countryId;
    private String title,lvpaiPrice,restDay,cityCode,city;
    private static List<SendImgFileBean> list;
    private static ImgAndContentBean service,recommended,photo;
    private static String description;
    private List<String> listPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_merchant);
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused);
        mResults.add(bimap);
        initView();
    }

    public AddMerchantActivity(){
        Log.e("456",SystemUtil.getCurrentDate()+"--");
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        lvpaiTitle = (EditText)findViewById(R.id.lvpaiTitle);
        price = (EditText)findViewById(R.id.price);
        lablename = (TextView)findViewById(R.id.lablename);
        abroad = (RadioButton)findViewById(R.id.abroad);
        domestic = (RadioButton)findViewById(R.id.domestic);
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        layout3 = (RelativeLayout)findViewById(R.id.layout3);
        layout4 = (RelativeLayout)findViewById(R.id.layout4);
        gridview = (GridView)findViewById(R.id.gridview);
        send = (TextView)findViewById(R.id.send);

        list = new ArrayList<>();
        listPic = new ArrayList<>();
        selectList = new ArrayList<>();
        listLable = new ArrayList<>();
        getStyleLable();
        back.setOnClickListener(this);
        abroad.setOnClickListener(this);
        domestic.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        send.setOnClickListener(this);
        adapter = new GridAdapter(this);
        adapter.update();
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mResults.size() - 1 || i == Bimp.tempSelectBitmap.size()) {
                    Intent intent = new Intent(AddMerchantActivity.this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
                    // 总共选择的图片数量
                    intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, Bimp.tempSelectBitmap.size());
                    startActivityForResult(intent, PICK_PHOTO);
                } else {
                    Intent intent = new Intent(AddMerchantActivity.this,
                            PreviewPhotoActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });
    }

    private void getStyleLable(){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getStyleLable(time_stamp, sign, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONArray("data");
                            List<LableBean> bean = gson.fromJson(array.toString(),new TypeToken<List<LableBean>>() {
                            }.getType());
                            for (int i = 0;i<bean.size();i++){
                                listLable.add(bean.get(i).getStyle_lable_name());
                            }
                            break;
                    }
                }catch (Exception e){

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.abroad:
                lvpaiType = 2;
                break;
            case R.id.domestic:
                lvpaiType = 1;
                break;
            case R.id.layout1:
                //区域地点
                switch (lvpaiType){
                    case 0:
                        ToastXutil.show("请先选择旅拍类型");
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.setClass(AddMerchantActivity.this, CityActivity.class);
                        intent.putExtra("type","lvpai");
                        startActivityForResult(intent,2);
                        break;
                    case 2:
                        Intent intent2 = new Intent();
                        intent2.setClass(AddMerchantActivity.this, CountyActivity.class);
                        intent2.putExtra("type","lvpai");
                        startActivityForResult(intent2,3);
                        break;
                }
                break;
            case R.id.layout2:
                //推荐风格
                showLable();
                break;
            case R.id.config:
                lableDialog.dismiss();
                if (selectList.size() > 0) {
                    String text = lable_text.getText().toString();
                    if (!StringXutil.isEmpty(text)) {
                        if (selectList.size() < 3) {
                            listLable.add(text);
                            selectList.add(text);
                        } else {
                            listLable.add(text);
                            ToastXutil.show("标签最多添加3个，请重新选择");
                        }
                    } else {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < selectList.size(); i++) {
                            if (i + 1 == selectList.size()) {
                                builder.append(selectList.get(i));
                            } else {
                                builder.append(selectList.get(i) + ",");
                            }
                        }
                        lablename.setText(builder.toString());
                    }
                }
                break;
            case R.id.layout3:
                startActivityForResult(new Intent(AddMerchantActivity.this,DatePickerActivity.class),6);
                break;
            case R.id.layout4:
                Intent intent = new Intent();
                intent.setClass(AddMerchantActivity.this,LvPaiDetailActivity.class);
                startActivityForResult(intent,5);
                break;
            case R.id.send:
                releaseTripShoot();
                break;
        }
    }
    private void showLable(){
        lableDialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
        inflateLable = LayoutInflater.from(this).inflate(R.layout.add_lable_dialog, null);
        //初始化控件
        lable = (FluidLayout)inflateLable.findViewById(R.id.lable);
        genTag(listLable,lable);
        lable_text = (EditText)inflateLable.findViewById(R.id.lable_text);
        config = (TextView)inflateLable.findViewById(R.id.config);
        config.setOnClickListener(this);
        //将布局设置给Dialog
        lableDialog.setContentView(inflateLable);
        //获取当前Activity所在的窗体
        Window dialogWindow = lableDialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialogWindow.setAttributes(params);
        StatusMargin.setFrameLayoutBottom(AddMerchantActivity.this,inflateLable,0);
        lableDialog.show();//显示对话框
    }
    private void genTag(final List<String> list, FluidLayout fluidLayout) {
        fluidLayout.removeAllViews();
        fluidLayout.setGravity(Gravity.TOP);
        for (int i = 0; i < list.size(); i++) {
            final CheckBox tv = new CheckBox(this);
            tv.setText(list.get(i));
            tv.setTextSize(13);
            tv.setBackgroundResource(R.drawable.text_lable_gray_bg);
            tv.setTextColor(getResources().getColor(R.color.grey2));
            tv.setButtonDrawable(0);
            tv.setPadding(20,0,20,0);
            for (int a = 0;a < selectList.size();a++){
                if (list.get(i).equals(selectList.get(a))){
                    tv.setChecked(true);
                    tv.setBackgroundResource(R.drawable.text_lable_bg);
                    tv.setTextColor(getResources().getColor(R.color.ori));
                }
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv.isChecked()){
                        if (selectList.size()<=2){
                            tv.setBackgroundResource(R.drawable.text_lable_bg);
                            tv.setTextColor(getResources().getColor(R.color.ori));
                            selectList.add(tv.getText().toString());
                        }else{
                            ToastXutil.show("最多添加3个标签");
                        }
                    }else{
                        tv.setBackgroundResource(R.drawable.text_lable_gray_bg);
                        tv.setTextColor(getResources().getColor(R.color.grey2));
                        for (int b =0;b < selectList.size();b++){
                            if (selectList.get(b).equals(tv.getText().toString())){
                                selectList.remove(b);
                            }
                        }
                        if (selectList.size() > 2){
                            ToastXutil.show("最多添加3个标签");
                        }
                    }
                }
            });
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 12, 20, 12);
            fluidLayout.addView(tv,params);
        }
    }

    private void releaseTripShoot(){
        Gson gson = new Gson();
        title = lvpaiTitle.getText().toString();
        if (StringXutil.isEmpty(title)){
            ToastXutil.show("旅拍标题不能为空");
        }else{
            lvpaiPrice = price.getText().toString();
            if (StringXutil.isEmpty(lvpaiPrice)){
                ToastXutil.show("请设置旅拍标题");
            }else{
                switch (lvpaiType){
                    case 1:
                        if (StringXutil.isEmpty(cityCode)){
                            ToastXutil.show("请选择国内城市");
                        }else{
                            if (day == 0){
                                ToastXutil.show("请填写接单时长");
                            }else{
                                if (StringXutil.isEmpty(restDay)){
                                    ToastXutil.show("休假日不不能为空");
                                }else{
                                    if (StringXutil.isEmpty(gson.toJson(selectList.toString()))){
                                        ToastXutil.show("请选择旅拍样式标签");
                                    }else{
                                        if (StringXutil.isEmpty(service.getContent())){
                                            ToastXutil.show("服务概述不能为空");
                                        }else{
                                            if (StringXutil.isEmpty(description)){
                                                ToastXutil.show("说明须知不能为空");
                                            }else{
                                                if (result.size()<=0){
                                                    ToastXutil.show("请上传商品图片");
                                                }else{
                                                    LoadDialog.show(this);
                                                    List<SendImgFileBean> listPhoto = new ArrayList<>();
                                                    for (int a= 0 ;a<result.size();a++){
                                                        String picName = "图片_"+System.currentTimeMillis();
                                                        listPic.add(picName);
                                                        Bitmap bitmap = BitmapFactory.decodeFile(result.get(a));
                                                        SendImgFileBean bean = new SendImgFileBean();
                                                        bean.setImgName(picName);
                                                        bean.setFile(bitmap);
                                                        listPhoto.add(bean);
                                                    }
                                                    list.addAll(listPhoto);
                                                    Map<String,Object> map = new HashMap<>();
                                                    String account_token = SharePreferenceXutil.getAccountToken();
                                                    map.put("account_token", account_token);
                                                    String time_stamp = SystemUtil.getCurrentDate2();
                                                    map.put("time_stamp",time_stamp);
                                                    map.put("trip_shoot_type_id",lvpaiType);
                                                    map.put("days",day);
                                                    map.put("rest_day",restDay);
                                                    map.put("title",title);
                                                    map.put("price",lvpaiPrice);
                                                    map.put("style_label_ids","1,2,3");
                                                    map.put("summary",service.getContent());
                                                    map.put("trip",recommended.getContent());
                                                    map.put("photo",photo.getContent());
                                                    map.put("notice",description );
                                                    map.put("pictures",StringXutil.ListtoString(listPic));
                                                    map.put("gaode_code",cityCode);
                                                    map.put("country_id",0);
                                                    String sign = SignUtil.sign(map);
                                                    Log.e("456",time_stamp+sign+account_token+lvpaiType+""+day+""+restDay+title+lvpaiPrice+StringXutil.ListtoString(selectList)+service.getContent()+recommended.getContent()+photo.getContent()+description+gson.toJson(listPic)+cityCode+0+list);
                                                    HomeHttp.releaseTripShoot(time_stamp, sign, account_token, lvpaiType, day, restDay, title, lvpaiPrice,
                                                            "1,2,3", service.getContent(), recommended.getContent(),
                                                            photo.getContent(), description, StringXutil.ListtoString(listPic), cityCode, 0, list, new AsyncHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                                            LoadDialog.dismiss(AddMerchantActivity.this);
                                                            try {
                                                                if (i == 200) {
                                                                    JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                                                                    Log.e("456", jsonObject.toString() + "--");
                                                                    int code = jsonObject.getInt("codee");
                                                                    switch (code){
                                                                        case 0:
                                                                            ToastXutil.show("发布成功");
                                                                            AddMerchantActivity.this.finish();
                                                                            break;
                                                                    }
                                                                }
                                                            }catch (Exception e){
                                                                LoadDialog.dismiss(AddMerchantActivity.this);
                                                               Log.e("789",e.getMessage());
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                                            LoadDialog.dismiss(AddMerchantActivity.this);
                                                            try {
                                                                if (i == 200) {
                                                                    JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                                                                    Log.e("456", jsonObject.toString() + "--");
                                                                }
                                                            }catch (Exception e){
                                                                LoadDialog.dismiss(AddMerchantActivity.this);
                                                                Log.e("789",e.getMessage());
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case 2:
                        if (countryId == 0){
                            ToastXutil.show("请选择国家");
                        }
                        break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_PHOTO:
                result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                showResult(result);
                break;
            case 2://国内城市
               cityCode = data.getStringExtra("code");
               city = data.getStringExtra("city");
                break;
            case 3://国家
                countryId = data.getIntExtra("id",0);
                break;
            case 6:
                day = data.getIntExtra("day",0);
                restDay = data.getStringExtra("list");
                Log.e("456",day+"--"+restDay);
                break;
        }
    }

//    onAtt
    private void showResult(ArrayList<String> paths) {
        if (mResults == null) {
            mResults = new ArrayList<>();
        }
        if (paths.size() != 0) {
            mResults.remove(mResults.size() - 1);
        }
        for (int i = 0; i < paths.size(); i++) {
            // 压缩图片
            Bitmap bitmap = PhotoCompress.decodeSampledBitmapFromFd(paths.get(i), 400, 500);
            // 针对小图也可以不压缩
//            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
            mResults.add(bitmap);

            ImageItem takePhoto = new ImageItem();
            takePhoto.setBitmap(bitmap);
            Bimp.tempSelectBitmap.add(takePhoto);
        }
        mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
        adapter.notifyDataSetChanged();
    }

    public static void  getMessage(Object obj){
        CallBackBean bean = (CallBackBean) obj;
        service = bean.getService();
        recommended = bean.getRecommended();
        photo = bean.getPhoto();
        description = bean.getDescription();
        list.addAll(service.getBeen());
        list.addAll(recommended.getBeen());
        list.addAll(photo.getBeen());
    }
    /**
     * 适配器
     */
    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }
        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 6) {
                return 6;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return mResults.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_gridview, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.imageView1);
                LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) holder.image.getLayoutParams(); //取控件textView当前的布局参数
                linearParams.height = MyApplication.width/3;// 控件的高强制设成屏幕的三分之一
                linearParams.width = MyApplication.width/3;// 控件的宽强制设成屏幕的三分之一
                holder.image.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_focused));
                if (position == 3) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        Log.i("123","我在这里更新");
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading (){
            new Thread(new Thread(){
                @Override
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }
    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
    }
}

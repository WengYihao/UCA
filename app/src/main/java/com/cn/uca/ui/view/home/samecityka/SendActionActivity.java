package com.cn.uca.ui.view.home.samecityka;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.home.samecityka.LableBean;
import com.cn.uca.bean.home.samecityka.PositionBean;
import com.cn.uca.bean.home.samecityka.SendActionBean;
import com.cn.uca.bean.home.samecityka.SendImgFileBean;
import com.cn.uca.bean.home.samecityka.SetTicketInfoBean;
import com.cn.uca.bean.home.samecityka.TicketBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.datepicker.DatePickDialog;
import com.cn.uca.view.dialog.LoadDialog;
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

/**
 * 同城咖-发布活动
 */
public class SendActionActivity extends BaseBackActivity implements View.OnClickListener,OnSureLisener{

    private TextView back,edit,actionType,type_start,cardname,add_lable,send;
    private EditText actionTitle;
    private LinearLayout layout1;
    private RelativeLayout layout2,layout3,layout4,layout5;
    private ImageView pic;
    private TextView enlistType;
    private TextView startTime,endTime;
    private TextView addPic;//添加海报封面图片
    private TextView man;
    private TextView woman;
    private TextView btn_cancel,config;
    private Dialog dialog,lableDialog;
    private View inflate,inflateLable;
    private FluidLayout lable,lable_item;
    private List<String> listLable;
    private List<String> selectList;
    private EditText lable_text;
    private int cardId,actionTypeId;
    private int type;
    private String start_time,end_time;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private Uri imageUri;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File bais;
    private static String detail;//活动详情
    private static List<SendImgFileBean> listImg;
    private ArrayList<SetTicketInfoBean> listTicket;
    private ArrayList<String> listInfo;
    private String position;
    private int enListtType;//是否需要报名
    private int isCharge = 0;//是否收费
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_action);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        actionTitle= (EditText)findViewById(R.id.actionTitle);
        actionType = (TextView)findViewById(R.id.actionType);//活动形式
        edit = (TextView)findViewById(R.id.edit);
        addPic = (TextView)findViewById(R.id.addPic);
        pic = (ImageView)findViewById(R.id.pic);
        type_start = (TextView)findViewById(R.id.type_start);
        cardname = (TextView)findViewById(R.id.cardname);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        layout3 = (RelativeLayout)findViewById(R.id.layout3);
        layout4 = (RelativeLayout)findViewById(R.id.layout4);
        layout5 = (RelativeLayout)findViewById(R.id.layout5);
        lable_item = (FluidLayout)findViewById(R.id.lable_item);
        add_lable = (TextView) findViewById(R.id.add_lable);
        startTime = (TextView)findViewById(R.id.startTime);
        endTime = (TextView)findViewById(R.id.endTime);
        enlistType = (TextView)findViewById(R.id.enlistType);
        send = (TextView)findViewById(R.id.send);

        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        addPic.setOnClickListener(this);
        type_start.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        add_lable.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        send.setOnClickListener(this);
        selectList = new ArrayList<>();
        listLable = new ArrayList<>();
        getCafeLabel();
        listInfo = new ArrayList<>();
        listTicket = new ArrayList<>();
        listImg = new ArrayList<>();

    }

    private void show(){
        dialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.choose_sex_dialog, null);
        //初始化控件
        man = (TextView) inflate.findViewById(R.id.man);
        man.setText("线上活动");
        man.setTextColor(getResources().getColor(R.color.grey));
        woman = (TextView) inflate.findViewById(R.id.woman);
        woman.setText("线下活动");
        woman.setTextColor(getResources().getColor(R.color.grey));
        btn_cancel = (TextView)inflate.findViewById(R.id.btn_cancel);
        btn_cancel.setVisibility(View.GONE);
        man.setOnClickListener(this);
        woman.setOnClickListener(this);
//        btn_cancel.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialogWindow.setAttributes(params);
        StatusMargin.setFrameLayoutBottom(SendActionActivity.this,inflate,20);
        dialog.show();//显示对话框
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
        StatusMargin.setFrameLayoutBottom(SendActionActivity.this,inflateLable,0);
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
                    if (tv.getText().toString().equals("添加+")){
                        show();
                    }else{
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
    private void genTagItem(List<String> list, FluidLayout fluidLayout) {
        fluidLayout.removeAllViews();
        fluidLayout.setGravity(Gravity.RIGHT);
        for (int i = 0; i < list.size(); i++) {
            CheckBox tv = new CheckBox(this);
            tv.setText(list.get(i));
            tv.setTextSize(10);
            tv.setBackgroundResource(R.drawable.text_lable_bg);
            tv.setTextColor(getResources().getColor(R.color.ori));
            tv.setButtonDrawable(0);
            tv.setPadding(20,0,20,0);
            for (int a = 0;a < selectList.size();a++){
                if (list.get(i).equals(selectList.get(a))){
                    tv.setBackgroundResource(R.drawable.text_lable_bg);
                    tv.setTextColor(getResources().getColor(R.color.ori));
                }
            }
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 20, 0);
            fluidLayout.addView(tv,params);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.addPic:
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(this, arrayString, title, onDialogClick);
                dialog.show();
                break;
            case R.id.edit:
                Intent intent = new Intent();
                intent.setClass(SendActionActivity.this,CardEditActivity.class);
                intent.putExtra("id",cardId);
                intent.putExtra("type","edit");
                startActivity(intent);
                break;
            case R.id.type_start:
                startActivityForResult(new Intent(SendActionActivity.this,CardChoiceActivity.class),0);
                break;
            case R.id.man:
                actionType.setText("线上活动");
                actionTypeId = 1;
                this.dialog.dismiss();
                break;
            case R.id.woman:
                actionType.setText("线下活动");
                actionTypeId = 2;
                Intent intent1 = new Intent();
                intent1.setClass(SendActionActivity.this,MapChoiceActivity.class);
                intent1.putExtra("type","samecityka");
                startActivityForResult(intent1,7);
                this.dialog.dismiss();
                break;
            case R.id.startTime:
                type = 1;
                showDatePickDialog(DateType.TYPE_YMDHM);
                break;
            case R.id.endTime:
                type = 2;
                showDatePickDialog(DateType.TYPE_YMDHM);
                break;
            case R.id.layout2:
                show();
                break;
            case R.id.layout3:
                startActivityForResult(new Intent(SendActionActivity.this,EnListActivity.class),3);//报名设置
                break;
            case R.id.layout4:
                startActivity(new Intent(SendActionActivity.this,SendDetailActivity.class));//详情
                break;
            case R.id.add_lable:
                showLable();
                break;
            case R.id.config:
                lableDialog.dismiss();
                if (selectList.size() > 0){
                    String text = lable_text.getText().toString();
                    if (!StringXutil.isEmpty(text)){
                        if (selectList.size()<3){
                            listLable.add(text);
                            selectList.add(text);
                            lable_item.setVisibility(View.VISIBLE);
                            genTagItem(selectList,lable_item);
                        }else{
                            listLable.add(text);
                            ToastXutil.show("标签最多添加3个，请重新选择");
                        }
                    }else{
                        lable_item.setVisibility(View.VISIBLE);
                        genTagItem(selectList,lable_item);
                    }
                }else{
                    lable_item.removeAllViews();
                    lable_item.setVisibility(View.GONE);
                }
                break;
            case R.id.send:
                sendAction();
                break;
        }
    }
    private String ListtoString(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i<list.size();i++){
            if (i != list.size()-1){
                stringBuilder.append(list.get(i)).append(",");
            }else{
                stringBuilder.append(list.get(i));
            }
        }
        return stringBuilder.toString();
    }
    private void getCafeLabel(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getCafeLabel(account_token, time_stamp, sign, new CallBack() {
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
                                listLable.add(bean.get(i).getLable_name());
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
    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(0);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(this);
        dialog.show();
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
            }catch (Exception e){

            }
        }
    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.obj != null) {
                bais = PhotoCompress.comp((Bitmap) msg.obj);
                Bitmap bitmap= BitmapFactory.decodeFile(bais.toString());
                Drawable drawable = new BitmapDrawable(bitmap);
                pic.setVisibility(View.VISIBLE);
                pic.setImageDrawable(drawable);
            }
        };
    };
    @Override
    public void onSure(Date date) {
        switch (type){
            case 1:
                start_time = SystemUtil.UtilDateToString3(date);
                startTime.setText(start_time);
                break;
            case 2:
                end_time = SystemUtil.UtilDateToString3(date);
                endTime.setText(end_time);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = null;
        switch (requestCode){
            case 0:
                if (data != null){
                    cardId = data.getIntExtra("id",0);
                    String name = data.getStringExtra("name");
                    cardname.setText(name);
                }
                break;
            case Constant.PHOTO_REQUEST_TAKEPHOTO:
                if (fileUri != null){
                    setPicToView(fileUri);
                }
                break;
            case Constant.PHOTO_REQUEST_GALLERY:
                if (data.getData() != null) {
                    file = new File(SystemUtil.getRealPathFromURI(data.getData(),this));
                    setPicToView(file);
                }
                break;
            case 3:
                if (data != null){
                    enListtType = data.getIntExtra("type",0);
                    switch (enListtType){
                        case 1:
                            //在线报名
                            enlistType.setText("在线报名");
                            listTicket = data.getParcelableArrayListExtra("ticketList");
                            listInfo = data.getStringArrayListExtra("infoList");
                            Log.e("456",listInfo.toString());
                            break;
                        case 2 :
                            //无需报名
                            enlistType.setText("无需报名");
                            isCharge = data.getIntExtra("isCharge",0);
                            Log.e("456",isCharge+"/*/*");
                            break;

                    }
                }
                break;
            case 7:
                if (data != null){
                    PositionBean positionBean = new PositionBean();
                    String address = data.getStringExtra("address");
                    actionType.setText(address);
                    double lat = data.getDoubleExtra("lat",0);
                    double lng = data.getDoubleExtra("lng",0);
                    String code = data.getStringExtra("code");
                    positionBean.setAddress(address);
                    positionBean.setLat(lat);
                    positionBean.setLng(lng);
                    positionBean.setGaode_code(code);
                    positionBean.setRemarks("");
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("lng",lng);
                        jsonObject.put("lat",lat);
                        jsonObject.put("gaode_code",code);
                        jsonObject.put("address",address);
                        jsonObject.put("remarks","");
                        position = jsonObject.toString();
                    }catch (Exception e){

                    }
                }
                break;

        }
    }

    public static void  getMessage(List<SendImgFileBean> list1,List list2){
       listImg = list1;
        Gson gson = new Gson();
        detail = gson.toJson(list2);
    }
    private void sendAction(){
        Gson gson = new Gson();
        SendActionBean bean = new SendActionBean();
        if (StringXutil.isEmpty(actionTitle.getText().toString())){
            ToastXutil.show("活动标题不能为空");
        }else{
            bean.setTitle(actionTitle.getText().toString());
            if (bais != null){
                bean.setCover(bais);
                if (cardId != 0){
                    bean.setUser_card_id(cardId);
                    if (actionTypeId != 0){
                        bean.setActivity_type_id(actionTypeId);
                        switch (actionTypeId){
                            case 1://线上活动
                                bean.setPosition("");
                                break;
                            case 2://线下活动
                                if (StringXutil.isEmpty(position)){
                                    ToastXutil.show("线下活动地址不能为空");
                                }else{
                                    bean.setPosition(position);
                                }
                                break;
                        }
                        if (StringXutil.isEmpty(start_time)){
                            ToastXutil.show("活动开始时间不能为空");
                        }else{
                            bean.setBeg_time(start_time);
                            if (StringXutil.isEmpty(end_time)){
                                ToastXutil.show("结束时间不能为空");
                            }else{
                                bean.setEnd_time(end_time);
                                switch (enListtType){
                                    case 1:
                                        if (listTicket.size() != 0){
                                            bean.setTickets(gson.toJson(listTicket));
                                            for (int b = 0;b<listTicket.size();b++){
                                                if (listTicket.get(b).getPrice() != 0){
                                                    bean.setCharge(true);
                                                    break;
                                                }else {
                                                    bean.setCharge(false);
                                                }
                                            }
                                            bean.setPurchase_ticket(true);
                                            if (listInfo != null){
                                                bean.setFill_infos(ListtoString(listInfo));
                                            }
                                            if (selectList.size() != 0){
                                                bean.setLabels(ListtoString(selectList));
                                                if (!StringXutil.isEmpty(detail)){
                                                    LoadDialog.show(this);
                                                    bean.setDetails(detail);
                                                    Map<String,Object> map = new HashMap<>();
                                                    String account_token = SharePreferenceXutil.getAccountToken();
                                                    map.put("account_token", account_token);
                                                    String time_stamp = SystemUtil.getCurrentDate2();
                                                    map.put("time_stamp",time_stamp);
                                                    map.put("charge",bean.isCharge());
                                                    map.put("activity_type_id",bean.getActivity_type_id());
                                                    map.put("beg_time",bean.getBeg_time());
                                                    map.put("end_time",bean.getEnd_time());
                                                    map.put("labels",bean.getLabels());
                                                    map.put("purchase_ticket",bean.isPurchase_ticket());
                                                    map.put("title",bean.getTitle());
                                                    map.put("details",bean.getDetails());
                                                    map.put("user_card_id",bean.getUser_card_id());
                                                    map.put("position",bean.getPosition());
                                                    map.put("tickets",bean.getTickets());
                                                    map.put("fill_infos",bean.getFill_infos());
                                                    String sign = SignUtil.sign(map);
                                                    bean.setSign(sign);
                                                    bean.setAccount_token(account_token);
                                                    bean.setTime_stamp(time_stamp);
                                                    HomeHttp.releaseCityCafe(bean, listImg, new AsyncHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                                            try {
                                                                if (i == 200) {
                                                                    LoadDialog.dismiss(SendActionActivity.this);
                                                                    JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                                                                    int code = jsonObject.getInt("code");
                                                                    switch (code){
                                                                        case 0:
                                                                            ToastXutil.show("发布成功");
                                                                            SendActionActivity.this.finish();
                                                                            break;
                                                                        case 485:
                                                                            ToastXutil.show("发布的同城咖已上线(3条)");
                                                                            break;
                                                                        default:
                                                                            ToastXutil.show(jsonObject.getString("msg"));
                                                                            break;
                                                                    }
                                                                }
                                                            }catch (Exception e){
                                                                Log.e("456",e.getMessage()+"报错");
                                                            }

                                                        }

                                                        @Override
                                                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                                            LoadDialog.dismiss(SendActionActivity.this);
                                                        }
                                                    });
                                                }else{
                                                    ToastXutil.show("请添加活动详情");
                                                }
                                            }else{
                                                ToastXutil.show("请选择活动标签");
                                            }
                                        }else{
                                            ToastXutil.show("请添加线下活动信息");
                                        }
                                        break;
                                    case 2:
                                        bean.setPurchase_ticket(false);
                                        switch (isCharge){
                                            case 0:
                                                bean.setCharge(false);
                                                break;
                                            case 1:
                                                bean.setCharge(true);
                                                break;
                                        }
                                        bean.setFill_infos("");
                                        if (selectList.size() != 0){
                                            bean.setLabels(ListtoString(selectList));
                                            if (!StringXutil.isEmpty(detail)){
                                                LoadDialog.show(this);
                                                bean.setDetails(detail);
                                                Map<String,Object> map = new HashMap<>();
                                                String account_token = SharePreferenceXutil.getAccountToken();
                                                map.put("account_token", account_token);
                                                String time_stamp = SystemUtil.getCurrentDate2();
                                                map.put("time_stamp",time_stamp);
                                                map.put("charge",bean.isCharge());
                                                map.put("activity_type_id",bean.getActivity_type_id());
                                                map.put("beg_time",bean.getBeg_time());
                                                map.put("end_time",bean.getEnd_time());
                                                map.put("labels",bean.getLabels());
                                                map.put("purchase_ticket",bean.isPurchase_ticket());
                                                map.put("title",bean.getTitle());
                                                map.put("details",bean.getDetails());
                                                map.put("user_card_id",bean.getUser_card_id());
                                                map.put("position",bean.getPosition());
//                                                map.put("tickets",bean.getTickets());
                                                map.put("fill_infos",bean.getFill_infos());
                                                String sign = SignUtil.sign(map);
                                                bean.setSign(sign);
                                                bean.setAccount_token(account_token);
                                                bean.setTime_stamp(time_stamp);
                                                HomeHttp.releaseCityCafe(bean, listImg, new AsyncHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                                        try {
                                                            if (i == 200) {
                                                                LoadDialog.dismiss(SendActionActivity.this);
                                                                JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                                                                int code = jsonObject.getInt("code");
                                                                switch (code){
                                                                    case 0:
                                                                        ToastXutil.show("发布成功");
                                                                        SendActionActivity.this.finish();
                                                                        break;
                                                                    case 485:
                                                                        ToastXutil.show("发布的同城咖已上线(3条)");
                                                                        break;
                                                                    default:
                                                                        ToastXutil.show("发布失败");
                                                                        break;
                                                                }
                                                                Log.e("123", jsonObject.toString() + "--");
                                                            }
                                                        }catch (Exception e){
                                                            Log.e("456",e.getMessage()+"报错");
                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                                        LoadDialog.dismiss(SendActionActivity.this);
                                                    }
                                                });
                                            }else{
                                                ToastXutil.show("请添加活动详情");
                                            }
                                        }else{
                                            ToastXutil.show("请选择活动标签");
                                        }
                                        break;
                                }
                            }
                        }
                    }else{
                        ToastXutil.show("请选择活动形式");
                    }
                }else{
                    ToastXutil.show("请选择一张名片");
                }
            }else{
                ToastXutil.show("请选择活动海报");
            }
        }
        Log.e("456",bean.toString());

    }
}

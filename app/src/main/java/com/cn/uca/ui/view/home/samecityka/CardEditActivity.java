package com.cn.uca.ui.view.home.samecityka;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.AddCardBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.ItemClick;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.MyEditText;
import com.cn.uca.view.dialog.LoadDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardEditActivity extends BaseBackActivity implements View.OnClickListener,ItemClick{

    private TextView back,delete,name,type,typename,type_name,phone,finish,name_type;
    private LinearLayout typeLayout;
    private EditText nameet,phoneet,typenameet,introduction;
    private CircleImageView pic;
    private RelativeLayout layout;
    private int id;
    private String mold;
    private  List<String> list;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private Uri imageUri;
    private byte[] photodata = null;
    private ByteArrayInputStream bais;
    private String cardName,companyName,cardContent,phoneNumber;
    private int cardType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        getInfo();
        initView();

    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
            mold = intent.getStringExtra("type");
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        delete = (TextView)findViewById(R.id.delete);
        name = (TextView)findViewById(R.id.name);
        type = (TextView)findViewById(R.id.type);
        type_name = (TextView)findViewById(R.id.type_name);
        phone = (TextView)findViewById(R.id.phone);
        nameet = (EditText)findViewById(R.id.nameet);
        phoneet = (EditText)findViewById(R.id.phoneet);
        typenameet = (EditText)findViewById(R.id.typenameet);
        introduction = (EditText)findViewById(R.id.introduction);
        pic = (CircleImageView)findViewById(R.id.pic);
        layout = (RelativeLayout)findViewById(R.id.layout);
        finish  =(TextView)findViewById(R.id.finish);
        typeLayout = (LinearLayout)findViewById(R.id.typeLayout);
        name_type = (TextView)findViewById(R.id.name_type);
        typename = (TextView)findViewById(R.id.typename);

        switch (mold){
            case "add":
                delete.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);
                typename.setVisibility(View.GONE);
                break;
            case "edit":

                break;
        }
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
        layout.setOnClickListener(this);
        pic.setOnClickListener(this);
        finish.setOnClickListener(this);

        SpannableString ss = new SpannableString("最后添加自己的简介吧");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        introduction.setHint(new SpannedString(ss));

        if (id == 0){
            delete.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
        }

        list = new ArrayList<>();
        list.add("个人");
        list.add("学校");
        list.add("企业");

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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.delete:
                ToastXutil.show("删除");
                break;
            case R.id.layout:
                ShowPopupWindow.show(this,list,layout,this);
                break;
            case R.id.pic:
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(this, arrayString, title, onDialogClick);
                dialog.show();
                break;
            case R.id.finish:
                cardName = nameet.getText().toString();
                if (StringXutil.isEmpty(cardName)){
                    ToastXutil.show("称呼不能为空");
                }else{
                    if (photodata != null){
                        bais = new ByteArrayInputStream(photodata);
                        if (bais != null){
                            phoneNumber = phoneet.getText().toString();
                            if (StringXutil.isEmpty(phoneNumber)){
                                ToastXutil.show("手机号不能为空");
                            }else{
                                cardContent = introduction.getText().toString();
                                if (StringXutil.isEmpty(cardContent)){
                                    ToastXutil.show("简介不能为空");
                                }else{
                                    if (cardType == 1){
                                        AddCardBean bean = new AddCardBean();
                                        bean.setAccount_token(SharePreferenceXutil.getAccountToken());
                                        bean.setTime_stamp(SystemUtil.getCurrentDate2());
                                        bean.setHand_phone(phoneNumber);
                                        bean.setIntroduce(cardContent);
                                        bean.setLearning_name("");
                                        bean.setCorporate_name("");
                                        bean.setUser_card_name(cardName);
                                        bean.setUser_card_type_id(cardType);
                                        bean.setFile(bais);
                                        bean.setWeixin("");
                                        bean.setSign(signUp(bean));
                                        addUserCard(bean);
                                    }else if(cardType == 2){
                                        companyName = typenameet.getText().toString();
                                        if (StringXutil.isEmpty(companyName)){
                                            ToastXutil.show("学校/企业名称不能为空");
                                        }else{
                                            AddCardBean bean = new AddCardBean();
                                            bean.setAccount_token(SharePreferenceXutil.getAccountToken());
                                            bean.setTime_stamp(SystemUtil.getCurrentDate2());
                                            bean.setHand_phone(phoneNumber);
                                            bean.setIntroduce(cardContent);
                                            bean.setCorporate_name("");
                                            bean.setUser_card_name(cardName);
                                            bean.setUser_card_type_id(cardType);
                                            bean.setFile(bais);
                                            bean.setLearning_name(companyName);
                                            bean.setWeixin("");
                                            bean.setSign(signUp(bean));
                                            addUserCard(bean);
                                        }
                                    }else if(cardType == 3){ companyName = typenameet.getText().toString();
                                        if (StringXutil.isEmpty(companyName)){
                                            ToastXutil.show("学校/企业名称不能为空");
                                        }else {
                                            AddCardBean bean = new AddCardBean();
                                            bean.setAccount_token(SharePreferenceXutil.getAccountToken());
                                            bean.setTime_stamp(SystemUtil.getCurrentDate2());
                                            bean.setHand_phone(phoneNumber);
                                            bean.setIntroduce(cardContent);
                                            bean.setLearning_name("");
                                            bean.setUser_card_name(cardName);
                                            bean.setUser_card_type_id(cardType);
                                            bean.setFile(bais);
                                            bean.setCorporate_name(companyName);
                                            bean.setWeixin("");
                                            bean.setSign(signUp(bean));
                                            addUserCard(bean);
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        ToastXutil.show("头像不能为空");
                    }
                }
                break;
        }
    }

    @Override
    public void click(int i) {
        switch (list.get(i)){
            case "个人":
                cardType = 1;
                type.setBackgroundResource(R.mipmap.personal_type);
                type_name.setText("个人");
                typeLayout.setVisibility(View.GONE);
                break;
            case "学校":
                cardType = 2;
                type.setBackgroundResource(R.mipmap.college_type);
                type_name.setText("学校");
                typeLayout.setVisibility(View.VISIBLE);
                name_type.setText("学校名称");
                break;
            case "企业":
                cardType = 3;
                type.setBackgroundResource(R.mipmap.enterprise_type);
                type_name.setText("企业");
                typeLayout.setVisibility(View.VISIBLE);
                name_type.setText("企业名称");
                break;
        }
    }

    private String signUp(AddCardBean bean){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",bean.getAccount_token());
        map.put("time_stamp",bean.getTime_stamp());
        map.put("corporate_name",bean.getCorporate_name());
        map.put("hand_phone",bean.getHand_phone());
        map.put("introduce",bean.getIntroduce());
        map.put("learning_name",bean.getLearning_name());
        map.put("user_card_name",bean.getUser_card_name());
        map.put("user_card_type_id",bean.getUser_card_type_id());
        map.put("weixin",bean.getWeixin());
        String sign = SignUtil.sign(map);
        Log.i("123",sign+"签名");
        return sign;
    }
    private void addUserCard(AddCardBean bean){
        LoadDialog.show(this);
        HomeHttp.addUserCard(bean, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200){
                        LoadDialog.dismiss(getApplicationContext());
                        JSONObject jsonObject =new JSONObject(new String(bytes,"UTF-8"));
                        Log.i("123",jsonObject.toString()+"--");
                        int code  = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                ToastXutil.show("添加成功");
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("456",e.getMessage()+"报错");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                ToastXutil.show("您已经拒绝过一次");
//            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.CAMERA_PERMISSIONS_REQUEST_CODE);
            takePicture();
        } else {//有权限直接调用系统相机拍照
            if (SystemUtil.hasSDCard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(this, "com.cn.uca.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
            } else {
                ToastXutil.show("设备没有SD卡！");
            }
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
    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            final Bitmap photo = bundle.getParcelable("data");
            new Thread() {
                @Override
                public void run() {
                    if (photo != null) {
                        handler.obtainMessage(0, photo).sendToTarget();
                        //将bitmap转换成File类型
                    }else {
                        handler.obtainMessage(-1, null).sendToTarget();
                    }
                }
            }.start();

        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                Drawable drawable = new BitmapDrawable((Bitmap) msg.obj);
                pic.setImageDrawable(drawable);
                photodata = GraphicsBitmapUtils.Bitmap2Bytes((Bitmap) msg.obj);
                ByteArrayInputStream bais = new ByteArrayInputStream(photodata);
            }
        }
    };
    /**
     * 剪裁图片
     */
    public  void cropImageUri(Uri orgUri,int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        intent.putExtra("uri",orgUri);

        startActivityForResult(intent, Constant.PHOTO_REQUEST_CUT);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.PHOTO_REQUEST_TAKEPHOTO:
                    cropImageUri(imageUri, 480);
                    break;
                case Constant.PHOTO_REQUEST_GALLERY:
                    if (SystemUtil.hasSDCard()) {
                        Uri newUri = Uri.parse(OpenPhoto.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.cn.uca.fileprovider", new File(newUri.getPath()));
                        cropImageUri( newUri,480);
                    } else {
                        ToastXutil.show("设备没有SD卡！");
                    }
                    break;
                case Constant.PHOTO_REQUEST_CUT:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
            }
        }else{
            Log.i("123","456789");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

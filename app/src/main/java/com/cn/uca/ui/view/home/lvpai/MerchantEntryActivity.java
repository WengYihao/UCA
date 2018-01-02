package com.cn.uca.ui.view.home.lvpai;

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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.getAddressBean;
import com.cn.uca.bean.home.lvpai.BecomeMerchantBean;
import com.cn.uca.config.Constant;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.samecityka.MapChoiceActivity;
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
import com.cn.uca.view.dialog.LoadDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 商家入驻
 */
public class MerchantEntryActivity extends BaseBackActivity implements OnClickListener{

    private CircleImageView pic;
    private TextView confirm,back,address;
    private EditText name,introduce,contacts,contacts_number;
    private CheckBox abroad,domestic;
    private LinearLayout address_layout;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private Uri imageUri;
    private String addressStr;
    private ByteArrayInputStream bais;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_entry);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        pic = (CircleImageView)findViewById(R.id.pic);
        name = (EditText)findViewById(R.id.name);
        introduce = (EditText)findViewById(R.id.introduce);
        abroad = (CheckBox)findViewById(R.id.abroad);
        domestic = (CheckBox)findViewById(R.id.domestic);
        address = (TextView)findViewById(R.id.address);
        address_layout = (LinearLayout)findViewById(R.id.address_layout);
        contacts = (EditText)findViewById(R.id.contacts);
        contacts_number = (EditText)findViewById(R.id.contacts_number);
        confirm = (TextView)findViewById(R.id.confirm);

        back.setOnClickListener(this);
        pic.setOnClickListener(this);
        address_layout.setOnClickListener(this);
        confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.address_layout:
                Intent intent = new Intent();
                intent.setClass(MerchantEntryActivity.this,MapChoiceActivity.class);
                intent.putExtra("type","lvpai");
                startActivityForResult(intent,0);
                break;
            case R.id.pic:
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(this, arrayString, title, onDialogClick);
                dialog.show();
                break;
            case R.id.confirm:
                becomeMerchant();
                break;
        }
    }

    private void becomeMerchant(){
        String account_token = SharePreferenceXutil.getAccountToken();
        String time_stamp = SystemUtil.getCurrentDate2();
        BecomeMerchantBean bean = new BecomeMerchantBean();
        bean.setWeixin("");
        bean.setPictures("");
        bean.setAccount_token(account_token);
        bean.setTime_stamp(time_stamp);
        if (bais != null){
            if (StringXutil.isEmpty(name.getText().toString())){
                ToastXutil.show("商家名称不能为空");
            }else{
                bean.setMerchant_name(name.getText().toString());
                if (StringXutil.isEmpty(introduce.getText().toString())){
                    ToastXutil.show("商家介绍不能为空");
                }else{
                    bean.setIntroduce(introduce.getText().toString());
                    if (abroad.isChecked()){
                        bean.setOverseas_tour("true");
                    }else{
                        bean.setOverseas_tour("false");
                    }
                    if (domestic.isChecked()){
                        bean.setDomestic_travel("true");
                    }else{
                        bean.setDomestic_travel("false");
                    }
                    if (StringXutil.isEmpty(addressStr)){
                        ToastXutil.show("商家地址不能为空");
                    }else{
                        bean.setPosition(addressStr);
                        if (StringXutil.isEmpty(contacts.getText().toString())){
                            ToastXutil.show("商家联系人不能为空");
                        }else{
                            bean.setContacts(contacts.getText().toString());
                            if (StringXutil.isEmpty(contacts_number.getText().toString())){
                                ToastXutil.show("商家联系方式不能为空");
                            }else{
                                bean.setPhone(contacts_number.getText().toString());
                                LoadDialog.show(this);
                                Map<String,Object> map = new HashMap<>();
                                map.put("time_stamp",bean.getTime_stamp());
                                map.put("account_token",bean.getAccount_token());
                                map.put("contacts",bean.getContacts());
                                map.put("domestic_travel",bean.getDomestic_travel());
                                map.put("introduce",bean.getIntroduce());
                                map.put("merchant_name",bean.getMerchant_name());
                                map.put("overseas_tour",bean.getOverseas_tour());
                                map.put("weixin",bean.getWeixin());
                                map.put("position",bean.getPosition());
                                map.put("pictures",bean.getPictures());
                                map.put("phone",bean.getPhone());
                                String sign = SignUtil.sign(map);
                                bean.setSign(sign);
                                HomeHttp.becomeMerchant(bean, bais, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        LoadDialog.dismiss(MerchantEntryActivity.this);
                                        try {
                                            if (i == 200) {
                                                JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                                                int code  = jsonObject.getInt("code");
                                                switch (code){
                                                    case 0:
                                                        ToastXutil.show("商家入驻成功！");
                                                        break;
                                                }
                                            }
                                        }catch (Exception e){
                                            Log.e("456",e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                        LoadDialog.dismiss(MerchantEntryActivity.this);
                                        try {
                                            Log.e("456",i+"--");
                                            JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                                            Log.e("123", jsonObject.toString() + "--");
                                        }catch (Exception e){
                                            Log.e("456",e.getMessage());
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }else{
            ToastXutil.show("商家头像不能为空");
        }
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

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.obj != null) {
                Drawable drawable = new BitmapDrawable((Bitmap) msg.obj);
                pic.setImageDrawable(drawable);
                bais = new ByteArrayInputStream(GraphicsBitmapUtils.Bitmap2Bytes((Bitmap)msg.obj));
            }
        };
    };

    /**
     * 权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        cropImageUri(newUri, 480);
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
        }else if (resultCode == 0){
            if (data != null){
                getAddressBean bean = data.getParcelableExtra("address");
                Gson gson = new Gson();
                addressStr = gson.toJson(bean);
                address.setText(bean.getAddress());
            }
        }
    }
}

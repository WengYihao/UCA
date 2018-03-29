package com.cn.uca.ui.view.home.lvpai;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.bean.home.lvpai.SendImgBean;
import com.cn.uca.bean.home.lvpai.getAddressBean;
import com.cn.uca.bean.home.lvpai.BecomeMerchantBean;
import com.cn.uca.bean.home.samecityka.SendImgFileBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBackValue;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.fragment.home.lvpai.PhotoFragment;
import com.cn.uca.ui.view.home.samecityka.CardEditActivity;
import com.cn.uca.ui.view.home.samecityka.MapChoiceActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.Bimp;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.PageControlView;
import com.cn.uca.view.dialog.LoadDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家入驻
 */
public class MerchantEntryActivity extends BaseBackActivity implements OnClickListener,CallBackValue{

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
    private Bitmap bais;
    private PhotoFragment fragment;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private PageControlView pageControl;
    private List<SendImgFileBean> listPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_entry);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        mPager = (ViewPager)findViewById(R.id.mPager);
        pageControl = (PageControlView)findViewById(R.id.pageControl);
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

        listPhoto = new ArrayList<>();
        fragmentList = new ArrayList<>();
        for (int i = 1;i<=6;i++){
            fragment = PhotoFragment.newInstance(i);
            fragmentList.add(fragment);
        }
        mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);
        pageControl.count = 6;
        pageControl.generalPageControl(0);
        mPager .setOffscreenPageLimit(6);//viewpager缓存的界面数
        mPager.setOnPageChangeListener(onPageChangeListener);
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            pageControl.generalPageControl(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
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
                ToastXutil.show("正在获取数据...");
                becomeMerchant();
                break;
        }
    }

    private void becomeMerchant(){
        String account_token = SharePreferenceXutil.getAccountToken();
        String time_stamp = SystemUtil.getCurrentDate2();
        BecomeMerchantBean bean = new BecomeMerchantBean();
        bean.setWeixin("");
        if (listPhoto.size() > 0){
            StringBuilder builder = new StringBuilder();
            for (int i = 0;i<listPhoto.size();i++){
                if (i+1 == listPhoto.size()){
                    builder.append(listPhoto.get(i).getImgName());
                }else{
                    builder.append(listPhoto.get(i).getImgName()+",");
                }
            }
            bean.setPictures(builder.toString());
        }else{
            bean.setPictures("");
        }
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
                                File file = PhotoCompress.comp(bais);
                                HomeHttp.becomeMerchant(bean, file,listPhoto,new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        LoadDialog.dismiss(MerchantEntryActivity.this);
                                        try {
                                            if (i == 200) {
                                                JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                                                Log.e("456",jsonObject.toString());
                                                int code  = jsonObject.getInt("code");
                                                switch (code){
                                                    case 0:
                                                        MerchantEntryActivity.this.finish();
                                                        SharePreferenceXutil.setEnter(true);
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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                Drawable drawable = new BitmapDrawable((Bitmap) msg.obj);
                pic.setImageDrawable(drawable);
            }
        }
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
        //启动相册
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent,  Constant.PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = null;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.PHOTO_REQUEST_TAKEPHOTO:
                    setPicToView(fileUri);
                    break;
                case Constant.PHOTO_REQUEST_GALLERY:
                    if (data.getData() != null) {
                        try{
                            file = new File(SystemUtil.getRealPathFromURI(data.getData(),this));
                            setPicToView(file);
                        }catch (Exception e){
                            ToastXutil.show("无法获取照片");
                        }
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

    @Override
    public void sendMessage(int type, Object obj) {
        SendImgFileBean bean = (SendImgFileBean)obj;
        Log.e("456",type+"-"+bean.toString()+"activity");
        listPhoto.add(type-1,bean);
    }
}

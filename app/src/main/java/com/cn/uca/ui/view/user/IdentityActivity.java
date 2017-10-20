package com.cn.uca.ui.view.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;


public class IdentityActivity extends BaseBackActivity implements View.OnClickListener{

    private MyEditText name,identity;
    private TextView back,facePic,photoPic,submit;
    private ImageView pic1,pic2;
    private RelativeLayout picLayout1,piclayout2;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private AlertDialog.Builder dialog;
    private boolean isPic;
    private File bais1,bais2;
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(), SystemUtil.getPhotoFileName());
    private ProgressDialog progDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_identity);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        name = (MyEditText)findViewById(R.id.name);
        identity = (MyEditText)findViewById(R.id.identity);

        facePic = (TextView)findViewById(R.id.facePic);
        photoPic = (TextView)findViewById(R.id.photoPic);
        submit = (TextView)findViewById(R.id.submit);

        pic1 = (ImageView)findViewById(R.id.pic1);
        pic2 = (ImageView)findViewById(R.id.pic2);

        picLayout1 = (RelativeLayout)findViewById(R.id.picLayout1);
        piclayout2 = (RelativeLayout)findViewById(R.id.picLayout2);

        LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) picLayout1.getLayoutParams();
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) piclayout2.getLayoutParams();

        linearParams1.height = MyApplication.width/2;
        linearParams2.height = MyApplication.width/2;

        picLayout1.setLayoutParams(linearParams1);
        piclayout2.setLayoutParams(linearParams2);

        back.setOnClickListener(this);
        facePic.setOnClickListener(this);
        photoPic.setOnClickListener(this);
        submit.setOnClickListener(this);

        SpannableString phoneHint = new SpannableString("请填写您的真实姓名");//定义hint的值
        SpannableString identityHint = new SpannableString("请输入十八位居民身份证号码");//定义hint的值
        AbsoluteSizeSpan as = new AbsoluteSizeSpan(15,true);//设置字体大小 true表示单位是sp
        phoneHint.setSpan(as, 0, phoneHint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        identityHint.setSpan(as, 0, identityHint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        name.setHint(new SpannedString(phoneHint));
        identity.setHint(new SpannedString(identityHint));

        dialog = AndroidClass.getListDialogBuilder(this, arrayString, title, onDialogClick);
        progDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.facePic:
                dialog.show();
                isPic = true;
                break;
            case R.id.photoPic:
                dialog.show();
                isPic = false;
                break;
            case R.id.submit:
                sumbitIdentity();
                break;
        }
    }

    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("请稍后...");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    private void sumbitIdentity(){
        String userName = name.getText().toString();
        String identityNum = identity.getText().toString();
        if (!StringXutil.isEmpty(userName) && StringXutil.isIDCard(identityNum)){
            if(bais1 != null){
                if(bais2 != null){
                    showDialog();
                    UserHttp.submitIdentity(userName, identityNum, bais1, bais2, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            if (i == 200){
                                try {
                                    JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                                    int code = jsonObject.getInt("code");
                                    switch (code){
                                        case 0:
                                            dismissDialog();
                                            ToastXutil.show("上传成功,等待审核通过。");
                                            Intent intent = new Intent();
                                            intent.putExtra("state","待审核");
                                            setResult(5,intent);
                                            IdentityActivity.this.finish();
                                            break;
                                        case 63:
                                            dismissDialog();
                                            ToastXutil.show("已上传用户审批");
                                            break;
                                        case 66:
                                            dismissDialog();
                                            ToastXutil.show("提交审核失败");
                                            break;
                                        case 67:
                                            dismissDialog();
                                            ToastXutil.show("身份证或姓名错误");
                                            break;
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                        }
                    });
                }else{
                    ToastXutil.show("国徽像照片不能为空");
                }
            }else{
                ToastXutil.show("人面像照片不能为空");
            }
        }else{
            ToastXutil.show("姓名不能为空或身份证格式不正确");
        }
    }
    // 对话框
    android.content.DialogInterface.OnClickListener onDialogClick = new android.content.DialogInterface.OnClickListener() {
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
        if (isPic){
            bais1 = picdata;
        }else{
            bais2 = picdata;
        }
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
                if (isPic){
                    pic1.setVisibility(View.VISIBLE);
                    pic1.setImageDrawable(drawable);
                }else{
                    pic2.setVisibility(View.VISIBLE);
                    pic2.setImageDrawable(drawable);
                }
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
}
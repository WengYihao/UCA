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
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.AddContentAdapter;
import com.cn.uca.bean.home.lvpai.SendContentbean;
import com.cn.uca.bean.home.samecityka.SendContentBean;
import com.cn.uca.bean.home.samecityka.SendImgBean;
import com.cn.uca.bean.home.samecityka.SendImgFileBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.NoScrollListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendDetailActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,finish,add;
    private LinearLayout layout;
    private Dialog dialog;
    private LinearLayout content,pic;
    private List<Object>list;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private Uri imageUri;
    private ImageView imageView;
    private List<SendImgFileBean> listImg;
    private Map<Integer,String> listImgNAME;
    private List<ImageView> imageViewList = new ArrayList<>();
    private int type = 0;
    private int Tag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_detail);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        finish = (TextView)findViewById(R.id.finish);
        layout = (LinearLayout)findViewById(R.id.layout);
        add = (TextView)findViewById(R.id.add);

        back.setOnClickListener(this);
        finish.setOnClickListener(this);
        add.setOnClickListener(this);

        list = new ArrayList<>();
        listImg = new ArrayList<>();
        listImgNAME = new HashMap<>();

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.back:
               this.finish();
               break;
           case R.id.finish:
               for (int i = 0;i<layout.getChildCount();i++){
                   RelativeLayout layoutItem = (RelativeLayout) layout.getChildAt(i);// 获得子item的layout
                   EditText editText = (EditText)layoutItem.findViewById(R.id.content) ;
                   if (editText == null ){//图片
                       com.cn.uca.bean.home.lvpai.SendImgBean bean = new com.cn.uca.bean.home.lvpai.SendImgBean();
                       ImageView view = (ImageView)layoutItem.findViewById(R.id.pic);
                       bean.setImg_url(listImgNAME.get(view.getTag()));
                       bean.setParagraph_type("img");
                       list.add(bean);
                   }else{//文字
                       SendContentbean bean = new SendContentbean();
                       bean.setContent(editText.getText().toString());
                       bean.setParagraph_type("p");
                       list.add(bean);
                   }
               }
               SendActionActivity.getMessage(listImg,list);
               SendDetailActivity.this.finish();
               break;
           case R.id.add:
               showDialog();
               break;
           case R.id.content:
               addContentItem("");
               dialog.dismiss();
               break;
           case R.id.pic:
               addPicItem("");
               dialog.dismiss();
               break;
       }
    }
    private void addContentItem(String text){
        final View itemContent = View.inflate(this, R.layout.action_detail_content_item,null);
        final TextView delete = (TextView)itemContent.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(itemContent);
            }
        });
        if (text != ""){
            EditText content = (EditText) itemContent.findViewById(R.id.content);
            content.setText(text);
        }
        layout.addView(itemContent);
    }
    View itemPic = null;
    private void addPicItem(final String url){
        type = 0;
        itemPic = View.inflate(this, R.layout.action_detail_pic_item,null);
        final View view = itemPic;
        imageView = (ImageView) itemPic.findViewById(R.id.pic);
        imageView.setTag(imageViewList.size()+1);
        final TextView delete = (TextView)view.findViewById(R.id.delete);
        if (url != ""){
            layout.addView(itemPic);
            ImageLoader.getInstance().displayImage(url,imageView);
            imageViewList.add(imageView);
        }else{
            AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(SendDetailActivity.this, arrayString, title, onDialogClick);
            dialog.show();
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                Tag = (int)v.getTag();
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(SendDetailActivity.this, arrayString, title, onDialogClick);
                dialog.show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });
    }
    public void showDialog(){
        dialog = new Dialog(this,R.style.dialog_style);
        View view = LayoutInflater.from(this).inflate(R.layout.add_action_content_dialog,null);
        content = (LinearLayout)view.findViewById(R.id.content);
        pic = (LinearLayout)view.findViewById(R.id.pic);
        content.setOnClickListener(this);
        pic.setOnClickListener(this);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获取当前Activity所在的窗体
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width*2/3;
        params.height = MyApplication.width/3;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.CENTER);
        dialogWindow.setAttributes(params);
        dialog.show();//显示对话框
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
        //启动相册
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent,  Constant.PHOTO_REQUEST_GALLERY);
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
//                bais = picdata;
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
                File file = PhotoCompress.comp((Bitmap) msg.obj);
                Bitmap bitmap= BitmapFactory.decodeFile(file.toString());
                SendImgFileBean bean = null;
                switch (type){
                    case 0:
                        layout.addView(itemPic);
                        imageView.setImageBitmap(bitmap);
                        imageViewList.add(imageView);
                        String name1  = "图片_"+System.currentTimeMillis();
                        listImgNAME.put((int) imageView.getTag(),name1);
                        bean = new SendImgFileBean();
                        bean.setImgName(name1);
                        bean.setFile(bitmap);
                        listImg.add(bean);
                        break;
                    case 1:
                        for (ImageView view : imageViewList){
                            if ((int)view.getTag() == Tag){
                                view.setImageBitmap(bitmap);
                                String name2  = "图片_"+System.currentTimeMillis();
                                listImgNAME.put(Tag,name2);
                                bean = new SendImgFileBean();
                                bean.setImgName(name2);
                                bean.setFile(bitmap);
                                listImg.add(bean);
                            }
                        }
                        break;
                }
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
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

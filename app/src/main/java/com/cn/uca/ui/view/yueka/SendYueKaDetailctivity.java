package com.cn.uca.ui.view.yueka;

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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.ImgAndContentBean;
import com.cn.uca.bean.home.lvpai.SendContentbean;
import com.cn.uca.bean.home.lvpai.SendImgBean;
import com.cn.uca.bean.home.samecityka.ActionDescribeBean;
import com.cn.uca.bean.home.samecityka.SendImgFileBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.dialog.LoadDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendYueKaDetailctivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,finish,add;
    private LinearLayout layout;
    private Dialog dialog;
    private LinearLayout content,pic;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private Uri imageUri;
    private ImageView imageView;
    private ArrayList<Object> list;//图文混合
    private Map<Integer,String> listImgNAME;//图片名
    private ArrayList<SendImgFileBean> listImg;//图片file
    private int type = 0;
    private int Tag = 0;
    private List<ImageView> imageViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_yueka_detail);

        initView();
        getInfo();
    }

    private void getInfo() {
        Intent intent = getIntent();
        if (intent != null){
            List<ActionDescribeBean> list = intent.getParcelableArrayListExtra("list");
            if (list != null){
                for (ActionDescribeBean bean : list){
                    switch (bean.getParagraph_type()){
                        case "p":
                            addContentItem(bean.getContent());
                            break;
                        case "img":
                            addPicItem(bean.getImg_url());
                            break;
                    }
                }
            }
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        finish = (TextView)findViewById(R.id.finish);
        layout = (LinearLayout)findViewById(R.id.layout);
        add = (TextView)findViewById(R.id.add);

        list = new ArrayList<>();
        listImgNAME = new HashMap<>();
        listImg = new ArrayList<>();
        back.setOnClickListener(this);
        finish.setOnClickListener(this);
        add.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.finish:
              svaeDetail();
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
    private void svaeDetail(){
        LoadDialog.show(this);
        Log.e("456",layout.getChildCount()+"-----size");
        for (int i = 0;i<layout.getChildCount();i++){
            RelativeLayout layoutItem = (RelativeLayout) layout.getChildAt(i);// 获得子item的layout
            EditText editText = (EditText)layoutItem.findViewById(R.id.content) ;
            if (editText == null ){//图片
                SendImgBean bean = new SendImgBean();
                ImageView view = (ImageView)layoutItem.findViewById(R.id.pic);
                bean.setImg_url(listImgNAME.get(view.getTag()));
                bean.setParagraph_type("img");
                list.add(bean);
            }else{//文字
                Log.e("456",i+"********");
                SendContentbean bean = new SendContentbean();
                bean.setContent(editText.getText().toString());
                bean.setParagraph_type("p");
                list.add(bean);
            }
        }

        Log.e("456",listImg.toString()+"--");
        ImgAndContentBean bean = new ImgAndContentBean();
        bean.setBeen(listImg);
        Gson gson = new Gson();
        bean.setContent(gson.toJson(list));
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("details",bean.getContent());
        String sign = SignUtil.sign(map);
        YueKaHttp.saveDetails(account_token, time_stamp, sign, bean.getContent(), bean.getBeen(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200) {
                        LoadDialog.dismiss(SendYueKaDetailctivity.this);
                        JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                        Log.e("456", jsonObject.toString() + "--");
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                ToastXutil.show("保存成功");
                                JSONObject object = jsonObject.getJSONObject("data");
                                int id = object.getInt("escort_details_id");
                                Intent intent = new Intent();
                                intent.putExtra("id",id);
                                setResult(0,intent);
                                SendYueKaDetailctivity.this.finish();
                                break;
                            default:
                                ToastXutil.show(jsonObject.getString("msg"));
                                list.clear();
                                listImg.clear();
                                break;
                        }
                    }
                }catch (Exception e){
//                    LoadDialog.dismiss(AddMerchantActivity.this);
                    LoadDialog.dismiss(SendYueKaDetailctivity.this);
                    Log.e("789",e.getMessage());
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
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
            Log.e("456",(int)imageView.getTag()+"-");
        }else{
            AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(SendYueKaDetailctivity.this, arrayString, title, onDialogClick);
            dialog.show();
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                Tag = (int)v.getTag();
                Log.e("456",Tag+"**");
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(SendYueKaDetailctivity.this, arrayString, title, onDialogClick);
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
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case Constant.PHOTO_REQUEST_TAKEPHOTO:
                    setPicToView(fileUri);
                    break;
                case Constant.PHOTO_REQUEST_GALLERY:
                    if (data.getData() != null) {
                        setPicToView(new File(SystemUtil.getRealPathFromURI(data.getData(),this)));
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}


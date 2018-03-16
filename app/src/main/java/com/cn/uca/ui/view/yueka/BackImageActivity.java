package com.cn.uca.ui.view.yueka;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.yueka.BackImageAdapter;
import com.cn.uca.bean.yueka.BackImageBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.BackImageListener;
import com.cn.uca.popupwindows.LoadingPopupWindow;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.dialog.LoadDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackImageActivity extends BaseBackActivity implements OnClickListener,BackImageListener{
    private TextView add;
    private ListView listView;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private Uri imageUri;
    private List<BackImageBean> list;
    private BackImageAdapter adapter;
    private TextView back;
    private File bais;
    private boolean isSend,isUpdate;
    private int photoId;
    private LoadingPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_image);

        initView();
        getCoverPicture();
    }


    private void initView() {
        popupWindow = new LoadingPopupWindow(this);
        popupWindow.setText("正在加载...");
        popupWindow.show();
        list = new ArrayList<>();
        back = (TextView)findViewById(R.id.back);
        adapter = new BackImageAdapter(list,this,this);
        add = (TextView)findViewById(R.id.add);
        back.setOnClickListener(this);
        add.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }


    private void getCoverPicture(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        YueKaHttp.getCoverPicture(account_token, time_stamp, sign, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<BackImageBean> bean = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<BackImageBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.clear();
                                list.addAll(bean);
                                adapter.setList(list);
                                if (popupWindow != null && popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                }
                            }else{
                                list.clear();
                                list.addAll(bean);
                                adapter.setList(list);
                                if (popupWindow != null && popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                }
                            }
                            break;
                    }
                }catch (Exception e){
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
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
            case R.id.add:
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(this, arrayString, title, onDialogClick);
                dialog.show();
                isSend = true;
                isUpdate = false;
                break;
            case R.id.back:
                this.finish();
                break;
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

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.obj != null) {
                if (isSend){
                    sendPhoto(0);
                }
               if (isUpdate){
                   sendPhoto(photoId);
               }
            }
        };
    };

    private void sendPhoto(final int type){
        LoadDialog.show(this);
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        if (type != 0){
            map.put("cover_photo_id",type);
        }
        String sign = SignUtil.sign(map);
        YueKaHttp.uploadCoverPicture(bais, account_token, time_stamp, sign, type, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                ToastXutil.show("添加成功");
                                getCoverPicture();
                                LoadDialog.dismiss(getApplicationContext());
                                break;
                            default:
                                LoadDialog.dismiss(getApplicationContext());
                                break;
                        }
                    }catch (Exception e){
                        LoadDialog.dismiss(getApplicationContext());
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                    LoadDialog.dismiss(getApplicationContext());
                }catch (Exception e){
                    LoadDialog.dismiss(getApplicationContext());
                }
            }
        });
    }

    private void deletePhoto(int photoId){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("cover_photo_id",photoId);
        String sign = SignUtil.sign(map);
        YueKaHttp.deleteCoverPicture(account_token, time_stamp, sign, photoId, new CallBack() {
            @Override
            public void onResponse(Object response) {
                if ((int)response == 0){
                    ToastXutil.show("删除成功");
                    getCoverPicture();
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
    // 将图片显示到UI界面上
    private void setPicToView(final File picdata) {
        if (picdata != null){
            try{
                bais = picdata;
                new Thread() {
                    @Override
                    public void run() {
                        handler.obtainMessage(0, picdata).sendToTarget();
                    }
                }.start();
            }catch (Exception e){

            }
        }else{
            handler.obtainMessage(-1, null).sendToTarget();
        }
    }

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

    @Override
    public void editClick(View view) {
        isSend = false;
        isUpdate = true;
        LinearLayout layout = (LinearLayout) listView.getChildAt((int) view.getTag());// 获得子item的layout
        photoId = list.get((int)view.getTag()).getCover_photo_id();
        TextView edit = (TextView)layout.findViewById(R.id.edit);
        SimpleDraweeView pic = (SimpleDraweeView)layout.findViewById(R.id.pic);
        AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(this, arrayString, title, onDialogClick);
        dialog.show();

    }

    @Override
    public void deleteClick(View view) {
        LinearLayout layout = (LinearLayout)listView.getChildAt((int)view.getTag());
        photoId = list.get((int)view.getTag()).getCover_photo_id();
        deletePhoto(photoId);
    }
}

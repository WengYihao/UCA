package com.cn.uca.ui.view.home.lvpai;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.PhotoItemAdapter;
import com.cn.uca.bean.home.lvpai.MerchantPhotoBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.lvpai.AddAlbumBack;
import com.cn.uca.impl.lvpai.ServiceBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.server.util.LvPaiUtil;
import com.cn.uca.ui.view.home.footprint.AddFootActivity;
import com.cn.uca.ui.view.home.yusheng.YuShengActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.NoScrollGridView;
import com.cn.uca.view.dialog.LoadDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoManageActivity extends BaseBackActivity implements View.OnClickListener,AddAlbumBack,ServiceBack{

    private TextView back,name,more,edit,add,delete,rename,deleteAlbum,btn_cancel;
    private PopupWindow popupWindow;//更多
    private Dialog dialog;//编辑相册
    private NoScrollGridView gridView;
    private PhotoItemAdapter adapter;
    private List<MerchantPhotoBean> list;
    private int id;
    private List<String> listUrl;
    private Bitmap bais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_manage);

        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        name = (TextView)findViewById(R.id.name);
        more = (TextView)findViewById(R.id.more);
        gridView = (NoScrollGridView)findViewById(R.id.gridView);

        back.setOnClickListener(this);
        more.setOnClickListener(this);

        list = new ArrayList<>();
        listUrl = new ArrayList<>();
        adapter = new PhotoItemAdapter(list,this);
        gridView.setAdapter(adapter);
        getAlbumPicture();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPhoto.imageUrl(PhotoManageActivity.this,position,(ArrayList<String>) listUrl);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.more:
                show();
                break;
            case R.id.edit:
                showEdit();
                popupWindow.dismiss();
                break;
            case R.id.add:
                autoObtainStoragePermission();
                popupWindow.dismiss();
                break;
            case R.id.delete:
                ToastXutil.show("删除");
                break;
            case R.id.man://相册重命名
                dialog.dismiss();
                renameWindow(this);
                break;
            case R.id.woman://删除相册
                dialog.dismiss();
                ToastXutil.show("删除相册");
                break;
            case R.id.btn_cancel://取消
                dialog.dismiss();
                break;
        }
    }
    private void getAlbumPicture(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("merchant_album_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.getAlbumPicture(time_stamp, sign, account_token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            name.setText(jsonObject.getJSONObject("data").getString("album_name"));
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("albumPictures");
                            List<MerchantPhotoBean> bean = gson.fromJson(array.toString(),new TypeToken<List<MerchantPhotoBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);
                                adapter.setList(list);
                                for (int i = 0;i<list.size();i++){
                                    listUrl.add(list.get(i).getPicture_url());
                                }
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
    //更多弹窗
    private void show(){
        View show = LayoutInflater.from(this).inflate(R.layout.lvpai_edit_album_dialog, null);
        edit = (TextView)show.findViewById(R.id.edit);//编辑相册
        add = (TextView)show.findViewById(R.id.add);//添加作品
        delete = (TextView)show.findViewById(R.id.delete);//移除操作
        edit.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        popupWindow = new PopupWindow(show, MyApplication.width/4,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAsDropDown(more,20,20);
//        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT|Gravity.TOP,20,50);
    }
    //编辑相册
    private void showEdit(){
        dialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.choose_sex_dialog, null);
        //初始化控件
        rename = (TextView) inflate.findViewById(R.id.man);
        rename.setText("相册重命名");
        rename.setTextColor(getResources().getColor(R.color.grey));
        deleteAlbum = (TextView) inflate.findViewById(R.id.woman);
        deleteAlbum.setText("删除相册");
        deleteAlbum.setTextColor(getResources().getColor(R.color.grey));
        btn_cancel = (TextView)inflate.findViewById(R.id.btn_cancel);
        btn_cancel.setTextColor(getResources().getColor(R.color.grey));
        rename.setOnClickListener(this);
        deleteAlbum.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialogWindow.setAttributes(params);
        StatusMargin.setFrameLayoutBottom(PhotoManageActivity.this,inflate,0);
        dialog.show();//显示对话框
    }

    //相册重命名
    private void renameWindow(final AddAlbumBack back){
        View inflate = LayoutInflater.from(this).inflate(R.layout.lvpai_addpic_dialog, null);
        final EditText albumname = (EditText)inflate.findViewById(R.id.albumname);
        TextView positiveButton = (TextView)inflate.findViewById(R.id.positiveButton);//确定
        TextView negativeButton = (TextView)inflate.findViewById(R.id.negativeButton);//取消


        final PopupWindow popupWindow = new PopupWindow(inflate, MyApplication.width,
                MyApplication.height/3, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (albumname.getText().toString() != null){
                    back.determine(albumname.getText().toString());
                    popupWindow.dismiss();
                }else{
                    ToastXutil.show("相册名不能为空");
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void determine(String name) {
        LvPaiUtil.updateMerchantAlbum(this,"update",id,name);
    }

    @Override
    public void success(String name) {
        this.name.setText(name);
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
            case Constant.STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPic();
                } else {
                    ToastXutil.show("请允许打操作SDCard！！");
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = null;
        if (resultCode == RESULT_OK){
            switch (requestCode) {
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
                addAlbumPicture();
            }else{
                ToastXutil.show("选择照片出错");
            }
        };
    };

    private void addAlbumPicture(){
        LoadDialog.show(this);
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("merchant_album_id",id);
        String sign = SignUtil.sign(map);
        File file = PhotoCompress.comp(bais);
        HomeHttp.addAlbumPicture(time_stamp, sign, account_token, id, file, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200){
                    LoadDialog.dismiss(PhotoManageActivity.this);
                    try {
                        JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                        int code = jsonObject.getInt("code");
                        switch (code) {
                            case 0:
                                ToastXutil.show("添加成功");
                                list.clear();
                                getAlbumPicture();
                                break;
                            default:
                                ToastXutil.show("添加失败");
                                LoadDialog.dismiss(PhotoManageActivity.this);
                                break;
                        }
                    }catch (Exception e){
                        LoadDialog.dismiss(PhotoManageActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                LoadDialog.dismiss(PhotoManageActivity.this);
            }
        });
    }
}

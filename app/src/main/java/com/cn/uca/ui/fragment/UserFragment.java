package com.cn.uca.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.uca.R;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.view.CircleImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asus on 2017/8/2.
 */

public class UserFragment extends Fragment implements View.OnClickListener{

    private View view;
    private CircleImageView pic;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
    private byte[] photodata = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, null);

        initView();
        return view;
    }

    private void initView(){
        pic = (CircleImageView)view.findViewById(R.id.pic);
        pic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pic:
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(getActivity(), arrayString, title, onDialogClick);
                dialog.show();
                break;
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
    private void startCamearPicCut(DialogInterface dialog) {
        dialog.dismiss();
        // 调用系统的拍照功能
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("camerasensortype", 2);// 调用前置摄像头
            intent.putExtra("autofocus", true);// 自动对焦
            intent.putExtra("fullScreen", false);// 全屏
            intent.putExtra("showActionIcons", false);
            // 指定调用相机拍照后照片的储存路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
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
    private void startImageCaptrue(DialogInterface dialog) {
        dialog.dismiss();
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, Constant.PHOTO_REQUEST_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 照片命名
     * @return
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    /**
     * 剪裁图片
     *
     * @param uri
     * @param size
     */
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, Constant.PHOTO_REQUEST_CUT);
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
                photodata = GraphicsBitmapUtils.Bitmap2Bytes((Bitmap)msg.obj);
            } else {
            }
        };
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(tempFile), 300);
                break;
            case Constant.PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    startPhotoZoom(data.getData(), 300);
                }
                break;
            case Constant.PHOTO_REQUEST_CUT:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

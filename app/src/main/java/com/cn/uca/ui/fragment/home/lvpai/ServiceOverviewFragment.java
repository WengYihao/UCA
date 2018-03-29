package com.cn.uca.ui.fragment.home.lvpai;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.ImgAndContentBean;
import com.cn.uca.bean.home.lvpai.SendContentbean;
import com.cn.uca.bean.home.lvpai.SendImgBean;
import com.cn.uca.bean.home.samecityka.SendImgFileBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBackValue;
import com.cn.uca.ui.view.home.lvpai.LvPaiDetailActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/12/25.
 * 服务概述
 */

public class ServiceOverviewFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TextView add,next;
    private Dialog dialog;
    private LinearLayout content,pic;
    private LinearLayout layout;
    private CallBackValue callBackValue;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private Uri imageUri;
    private ImageView imageView;
    private LvPaiDetailActivity activity;
    private ArrayList<Object> list;//图文混合
    private Map<Integer,String> listImgNAME;//图片名
    private ArrayList<SendImgFileBean> listImg;//图片file
    private int type = 0;
    private int Tag = 0;
    private List<ImageView> imageViewList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_overview,null);
        initView();
        return view;
    }

    private void initView() {
        add = (TextView)view.findViewById(R.id.add);
        next = (TextView)view.findViewById(R.id.next);
        layout = (LinearLayout)view.findViewById(R.id.layout);
        add.setOnClickListener(this);
        next.setOnClickListener(this);

        activity = (LvPaiDetailActivity)getActivity();

        list = new ArrayList<>();
        listImgNAME = new HashMap<>();
        listImg = new ArrayList<>();
    }
    public void showDialog(){
        dialog = new Dialog(getActivity(),R.style.dialog_style);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_action_content_dialog,null);
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
    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }
    protected void onAttachToContext(Context context) {
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
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
                ImgAndContentBean bean = new ImgAndContentBean();
                bean.setBeen(listImg);
                Gson gson = new Gson();
                bean.setContent(gson.toJson(list));
                callBackValue.sendMessage(1,bean);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.hide(this);
                fr.add(R.id.container,new RecommendedTravelFragment());
                fr.addToBackStack(null);
                fr.commit();
                activity.setBack(1);
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
        final View itemContent = View.inflate(getActivity(), R.layout.action_detail_content_item,null);
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
        itemPic = View.inflate(getActivity(), R.layout.action_detail_pic_item,null);
        final View view = itemPic;
        imageView = (ImageView) itemPic.findViewById(R.id.pic);
        imageView.setTag(imageViewList.size()+1);
        final TextView delete = (TextView)view.findViewById(R.id.delete);
        if (url != ""){
            layout.addView(itemPic);
            ImageLoader.getInstance().displayImage(url,imageView);
            imageViewList.add(imageView);
        }else{
            AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(getActivity(), arrayString, title, onDialogClick);
            dialog.show();
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                Tag = (int)v.getTag();
                Log.e("456",Tag+"**");
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(getActivity(), arrayString, title, onDialogClick);
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

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                ToastXutil.show("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (SystemUtil.hasSDCard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(getActivity(), "com.cn.uca.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.STORAGE_PERMISSIONS_REQUEST_CODE);
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
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.cn.uca.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
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
        if (resultCode == getActivity().RESULT_OK){
            switch (requestCode) {
                case Constant.PHOTO_REQUEST_TAKEPHOTO:
                    setPicToView(fileUri);
                    break;
                case Constant.PHOTO_REQUEST_GALLERY:
                    if (data.getData() != null) {
                        setPicToView(new File(SystemUtil.getRealPathFromURI(data.getData(),getActivity())));
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

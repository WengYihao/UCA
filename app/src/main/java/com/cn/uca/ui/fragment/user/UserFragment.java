package com.cn.uca.ui.fragment.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.user.UserInfo;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.LoginActivity;
import com.cn.uca.ui.user.CollectionActivity;
import com.cn.uca.ui.user.IdentityActivity;
import com.cn.uca.ui.user.InformationActivity;
import com.cn.uca.ui.user.OrderActivity;
import com.cn.uca.ui.user.SettingActivity;
import com.cn.uca.ui.user.WalletActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by asus on 2017/8/2.
 */

public class UserFragment extends Fragment implements View.OnClickListener{

    private View view;
    private CircleImageView pic;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";

    private byte[] photodata = null;
    private TextView setting,nickName,sex,state;
    private RelativeLayout head,noneLayout,userInfo,loginLayout;
    private LinearLayout llTitle,myOrder,myCollection;
    private RelativeLayout layout1,layout2,layout3,layout4,layout5;
    private String userName,userAge,userSex;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, null);
        initView();
        getUserInfo();
        return view;
    }

    private void initView(){
        pic = (CircleImageView)view.findViewById(R.id.pic);
        pic.setOnClickListener(this);

        setting = (TextView)view.findViewById(R.id.setting);
        setting.setOnClickListener(this);

        head = (RelativeLayout)view.findViewById(R.id.head);
        head.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyApplication.height*2/5));

        noneLayout = (RelativeLayout)view.findViewById(R.id.noneLayout);
        noneLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (MyApplication.height*2/5)*5/8));

        llTitle = (LinearLayout)view.findViewById(R.id.llTitle);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (MyApplication.height*2/5)*3/8);
        params.addRule(RelativeLayout.BELOW,R.id.noneLayout);
        llTitle.setLayoutParams(params);
        RelativeLayout.LayoutParams paramsPic = new RelativeLayout.LayoutParams(SystemUtil.dip2px(80), SystemUtil.dip2px(80));
        paramsPic.setMargins(SystemUtil.dip2px(10),((MyApplication.height*2/5)*5/8)-(SystemUtil.dip2px(40)),0,0);
        pic.setLayoutParams(paramsPic);

        loginLayout = (RelativeLayout)view.findViewById(R.id.loginLayout);

        userInfo = (RelativeLayout)view.findViewById(R.id.userInfo);
        userInfo.setOnClickListener(this);

        myOrder = (LinearLayout)view.findViewById(R.id.myOrder);
        myOrder.setOnClickListener(this);

        myCollection = (LinearLayout)view.findViewById(R.id.myCollection);
        myCollection.setOnClickListener(this);

        nickName = (TextView)view.findViewById(R.id.nickName);
        sex = (TextView)view.findViewById(R.id.sex);
        state = (TextView)view.findViewById(R.id.state);

        layout1 = (RelativeLayout)view.findViewById(R.id.layout1);
        layout2 = (RelativeLayout)view.findViewById(R.id.layout2);
        layout3 = (RelativeLayout)view.findViewById(R.id.layout3);
        layout4 = (RelativeLayout)view.findViewById(R.id.layout4);
        layout5 = (RelativeLayout)view.findViewById(R.id.layout5);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        loginLayout.setOnClickListener(this);
        if (!SharePreferenceXutil.isSuccess()){
            loginLayout.setVisibility(View.VISIBLE);
            userInfo.setVisibility(View.GONE);
        }else{
            loginLayout.setVisibility(View.GONE);
            userInfo.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pic:
                if (SharePreferenceXutil.isSuccess()){
                    AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(getActivity(), arrayString, title, onDialogClick);
                    dialog.show();
                }else{
                    ToastXutil.show("请先登录");
                }
                break;
            case R.id.setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.userInfo:
                Intent intent = new Intent();
                intent.setClass(getActivity(),InformationActivity.class);
                intent.putExtra("userName",userName);
                intent.putExtra("userAge",userAge);
                intent.putExtra("userSex",userSex);
                startActivityForResult(intent,0);
                break;
            case R.id.loginLayout:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.myOrder:
                if (SharePreferenceXutil.isSuccess()){
                    startActivity(new Intent(getActivity(), OrderActivity.class));
                }else{
                    ToastXutil.show("请先登录");
                }
                break;
            case R.id.myCollection:
                if (SharePreferenceXutil.isSuccess()){
                    startActivity(new Intent(getActivity(), CollectionActivity.class));
                }else {
                    ToastXutil.show("请先登录");
                }
                break;
            case R.id.layout1:
                if (SharePreferenceXutil.isSuccess()){
                    startActivity(new Intent(getActivity(), WalletActivity.class));
                }else {
                    ToastXutil.show("请先登录");
                }
                break;
            case R.id.layout2:
                if (SharePreferenceXutil.isSuccess()){
                    startActivity(new Intent(getActivity(), IdentityActivity.class));
                }else {
                    ToastXutil.show("请先登录");
                }
                break;
            case R.id.layout3:

                break;
            case R.id.layout4:

                break;
            case R.id.layout5:

                break;
//            case R.id.open:
//                startActivity(new Intent(getActivity(), LocationActivity.class));
//                break;
//
//            case R.id.set:
//                boolean success = ShortcutBadger.applyCount(getActivity(), 3);
//                Toast.makeText(getActivity()," success=" + success, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.clean:
//                boolean success2 = ShortcutBadger.removeCount(getActivity());
//                break;
        }
    }

    private void getUserInfo(){
        UserHttp.getUserBriefInfo(new CallBack() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                UserInfo info = gson.fromJson(response.toString(),new TypeToken<UserInfo>(){}.getType());
                try{
                    ImageLoader.getInstance().displayImage(info.getUser_head_portrait(), pic);
                    userName = info.getUser_nick_name();
                    nickName.setText(userName);
                    userAge = info.getUser_birth_date();
                    switch (info.getSex_id()){
                        case 1://男
                            sex.setBackgroundResource(R.mipmap.man);
                            userSex = "男";
                            break;
                        case 2://女
                            sex.setBackgroundResource(R.mipmap.woman);
                            userSex = "女";
                            break;
                        case 3://保密
                            sex.setVisibility(View.GONE);
                            userSex = "保密";
                            break;
                    }
                    switch (info.getBind_identity_state_id()){
                        case 1:
                            state.setText("已认证");
                            break;
                        case 2:
                            state.setText("待审核");
                            break;
                        case 3:
                            state.setText("未认证");
                            break;
                    }

                }catch (Exception e){
                    Log.i("456",e.getMessage()+"报错");
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {
                Log.i("456",error.getMessage());

            }
        });
    }

    // 对话框
    DialogInterface.OnClickListener onDialogClick = new DialogInterface.OnClickListener() {
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
        intent.putExtra("uri",uri);

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
                photodata = GraphicsBitmapUtils.Bitmap2Bytes((Bitmap)msg.obj);
                ByteArrayInputStream bais = new ByteArrayInputStream(photodata);
                UserHttp.uploadPic(bais, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        if (i == 200){
                            try {
                                JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                                int code = jsonObject.getInt("code");
                                if (code == 0){
                                    ToastXutil.show("上传成功");
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
                        ToastXutil.show("上传失败");
                    }
                });
            }
        };
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(MyApplication.tempFile), 400);
                break;
            case Constant.PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    startPhotoZoom(data.getData(), 400);
                }
                break;
            case Constant.PHOTO_REQUEST_CUT:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            case 0:
                try{
                    if (data != null){
                        userName = data.getStringExtra("userName");
                        nickName.setText(userName);
                        userAge = data.getStringExtra("userAge");
                        Date date = SystemUtil.StringToUtilDate(userAge);
                        userSex = data.getStringExtra("userSex");
                        switch (userSex){
                            case "男":
                                sex.setBackgroundResource(R.mipmap.man);
                                break;
                            case "女":
                                sex.setBackgroundResource(R.mipmap.woman);
                                break;
                            case "保密":
                                sex.setVisibility(View.GONE);
                                break;
                        }

                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

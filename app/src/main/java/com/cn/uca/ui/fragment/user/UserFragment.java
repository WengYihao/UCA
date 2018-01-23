package com.cn.uca.ui.fragment.user;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.cn.uca.ui.view.LoginActivity;
import com.cn.uca.ui.view.user.CollectionActivity;
import com.cn.uca.ui.view.user.InformationActivity;
import com.cn.uca.ui.view.user.message.MessageActivity;
import com.cn.uca.ui.view.user.OrderActivity;
import com.cn.uca.ui.view.user.SettingActivity;
import com.cn.uca.ui.view.user.WalletActivity;
import com.cn.uca.util.AndroidClass;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.DragPointView;
import com.cn.uca.view.dialog.LoadDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by asus on 2017/8/2.
 */

public class UserFragment extends Fragment implements View.OnClickListener{

    private View view;
    private CircleImageView pic;
    private String[] arrayString = { "拍照", "相册" };
    private String title = "上传照片";
    private TextView setting,nickName,sex,state;
    private RelativeLayout head,noneLayout,userInfo,loginLayout;
    private LinearLayout llTitle,myOrder,myCollection;
    private RelativeLayout layout1,layout2,layout3;
    private String userName,userAge,userSex;
    private Bitmap bais;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private Uri imageUri;
    private DragPointView message_num;
    final Conversation.ConversationType[] conversationTypes = {
            Conversation.ConversationType.PRIVATE,
            Conversation.ConversationType.SYSTEM,
    };
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
        StatusMargin.setRelativeLayout(getActivity(),setting,20);

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
        message_num = (DragPointView)view.findViewById(R.id.message_num);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        loginLayout.setOnClickListener(this);
        if (!SharePreferenceXutil.isSuccess()){
            loginLayout.setVisibility(View.VISIBLE);
            userInfo.setVisibility(View.GONE);
        }else{
            loginLayout.setVisibility(View.GONE);
            userInfo.setVisibility(View.VISIBLE);
        }

        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                if (i == 0) {
                    message_num.setVisibility(View.GONE);

                } else if (i > 0 && i < 100) {
                    message_num.setVisibility(View.VISIBLE);
                    message_num.setText(String.valueOf(i));
                } else {
                    message_num.setVisibility(View.VISIBLE);
                    message_num.setText(R.string.no_read_message);
                }
            }
        }, conversationTypes);//未读会话消息

        message_num.setDragListencer(new DragPointView.OnDragListencer() {
            @Override
            public void onDragOut() {
                message_num.setVisibility(View.GONE);
                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        if (conversations != null && conversations.size() > 0) {
                            for (Conversation c : conversations) {
                                RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                            }
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {

                    }
                }, conversationTypes);
            }
        });
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
                    getIdCardUrl();
                }else {
                    ToastXutil.show("请先登录");
                }
                break;
            case R.id.layout3:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
        }
    }

    private void getUserInfo(){
        UserHttp.getUserBriefInfo(new CallBack() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                UserInfo info = gson.fromJson(response.toString(),new TypeToken<UserInfo>(){}.getType());
                Log.e("456",info.getUser_head_portrait());
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

            }
        });
    }

    private void getIdCardUrl(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.getIdCardUrl(sign, time_stamp, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            String url = jsonObject.getJSONObject("data").getString("url");
                           doVerify(url);
                            break;
                        case 63:
                            ToastXutil.show("已认证");
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
    /**
     * 启动支付宝进行认证
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            // 这里使用固定appid 20000067
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
            builder.append(URLEncoder.encode(url));
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
        } else {
            // 处理没有安装支付宝的情况
            new AlertDialog.Builder(getActivity())
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     * @return true 为已经安装
     */
    private  boolean hasApplication() {
        PackageManager manager = getActivity().getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //刷新数据
//            if (!SharePreferenceXutil.isSuccess()){
//                loginLayout.setVisibility(View.VISIBLE);
//                userInfo.setVisibility(View.GONE);
//            }else{
//                loginLayout.setVisibility(View.GONE);
//                userInfo.setVisibility(View.VISIBLE);
//            }
        }
    }

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

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            try{
                if (msg.obj != null) {
                    Drawable drawable = new BitmapDrawable((Bitmap) msg.obj);
                    pic.setImageDrawable(drawable);
                    LoadDialog.show(getActivity());
                    File file = PhotoCompress.comp(bais);
                    UserHttp.uploadPic(file, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            if (i == 200){
                                try {
                                    JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                                    Log.e("456",jsonObject.toString());
                                    int code = jsonObject.getInt("code");
                                    switch (code){
                                        case 0:
                                            LoadDialog.dismiss(getActivity());
                                            ToastXutil.show("上传成功");
                                            break;
                                        default:
                                            LoadDialog.dismiss(getActivity());
                                            ToastXutil.show("上传失败");
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
                            ToastXutil.show("上传失败--");
                            LoadDialog.dismiss(getActivity());
                        }
                    });
                }
            }catch (Exception e){
                Log.e("789",e.getMessage());
                LoadDialog.dismiss(getActivity());
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = null;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.PHOTO_REQUEST_TAKEPHOTO:
                    setPicToView(fileUri);
                    break;
                case Constant.PHOTO_REQUEST_GALLERY:
                    if (data.getData() != null) {
                        file = new File(SystemUtil.getRealPathFromURI(data.getData(),getActivity()));
                        setPicToView(file);
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
                case 5:
                    if (data != null){
                        String state = data.getStringExtra("state");
                        this.state.setText(state);
                    }
                    break;
            }
        }else{
            Log.i("123","456789");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

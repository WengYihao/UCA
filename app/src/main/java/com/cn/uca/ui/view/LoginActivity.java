package com.cn.uca.ui.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.secretkey.MD5;
import com.cn.uca.secretkey.RSAUtils;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.ui.view.register.ForgetPasswordActivity;
import com.cn.uca.ui.view.register.RegisterActivity;
import com.cn.uca.ui.view.util.BaseHideActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StatusBarUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;
import com.cn.uca.view.dialog.LoadDialog;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;
import java.security.PublicKey;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends BaseHideActivity implements View.OnClickListener{

    private MyEditText username,password;
    private TextView look,login,register,alipayLogin,weChatLogin,forgetPassword;
    private String name,code;
    public static boolean isLook = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this);
        setContentView(R.layout.activity_login);
        ActivityCollector.loginActivity(this);
        ActivityCollector.registerActivity(this);
        ActivityCollector.forgetActivity(this);
        initView();
    }

    private void initView() {
        username = (MyEditText)findViewById(R.id.username);
        password = (MyEditText)findViewById(R.id.password);
        look = (TextView)findViewById(R.id.look);
        login = (TextView)findViewById(R.id.login);
        register = (TextView)findViewById(R.id.register);
        alipayLogin = (TextView)findViewById(R.id.alipayLogin);
        weChatLogin = (TextView)findViewById(R.id.weChatLogin);
        forgetPassword = (TextView)findViewById(R.id.forgetPassword);

        look.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        alipayLogin.setOnClickListener(this);
        weChatLogin.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

        if (!StringXutil.isEmpty(SharePreferenceXutil.getUserName()) && !StringXutil.isEmpty(SharePreferenceXutil.getPassword())){
            username.setText(SharePreferenceXutil.getUserName());
            password.setText(SharePreferenceXutil.getPassword());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.look:
                if (!StringXutil.isEmpty(password.getText().toString())){
                    if (isLook){
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        look.setBackgroundResource(R.mipmap.notsee);
                        isLook = false;
                    }else {
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        look.setBackgroundResource(R.mipmap.cansee);
                        isLook = true;
                    }
                }else{
                    ToastXutil.show("密码为空，不能查看");
                }
                break;
            case R.id.login:
                phoneLogin();
                break;
            case R.id.register:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.alipayLogin:
                ToastXutil.show("该功能暂未开放");
                break;
            case R.id.weChatLogin:
                SharePreferenceXutil.saveAccessToken("");
                sendWeChatAuthRequest();
                break;
            case R.id.forgetPassword:
                Intent intent2 = new Intent();
                intent2.setClass(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 手机号登录
     */
    private void phoneLogin(){
        LoadDialog.show(this);
        try {
            final String phoneNumber = username.getText().toString().trim();
            final String passwordText = password.getText().toString().trim();
            PublicKey publicKey = RSAUtils.loadPublicKey(Constant.PUBLIC_KEY);
            byte[] encryptByte = RSAUtils.encryptData(MD5.getMD5(passwordText).getBytes(), publicKey);
            String afterencrypt = Base64.encode(encryptByte);
            QueryHttp.phoneLogin(phoneNumber,afterencrypt, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        if (i == 200){
                            JSONObject jsonObject =new JSONObject(new String(bytes,"UTF-8"));
                            Log.i("123",jsonObject.toString()+"--");
                            int code  = jsonObject.getInt("code");
                            switch (code){
                                case 0:
                                    ToastXutil.show("登录成功");
                                    SharePreferenceXutil.setSuccess(true);
                                    SharePreferenceXutil.saveAccountToken(jsonObject.getJSONObject("data").getString("account_token"));
                                    SharePreferenceXutil.saveRongToken(jsonObject.getJSONObject("data").getString("rongyun_token"));
                                    MyApplication.connectRongServer(SharePreferenceXutil.getRongToken());//验证融云
                                    SharePreferenceXutil.savePhoneNumber(phoneNumber);//保存手机号
                                    SharePreferenceXutil.saveUserName(phoneNumber);//保存用户名
                                    SharePreferenceXutil.savePassword(passwordText);//保存密码
                                    SharePreferenceXutil.setExit(false);
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    LoadDialog.dismiss(getApplicationContext());
                                    LoginActivity.this.finish();
                                    break;
                                case 74:
                                    LoadDialog.dismiss(getApplicationContext());
                                    ToastXutil.show("账号不正确");
                                    break;
                                case 75:
                                    LoadDialog.dismiss(getApplicationContext());
                                    ToastXutil.show("密码不正确");
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("456",e.getMessage()+"报错");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.i("456",i+"--");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 微信登录
     */
    private void sendWeChatAuthRequest() {
        if (WeChatManager.instance().isWXAppInstalled()) {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo";
            //拉起微信授权，授权结果在WXEntryActivity中接收处理
            WeChatManager.instance().sendReq(req);
        } else {
            ToastXutil.show(R.string.wechat_not_installed);
        }
    }
}
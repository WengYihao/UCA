package com.cn.uca.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.user.RegisterBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.secretkey.MD5;
import com.cn.uca.secretkey.RSAUtils;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.ui.util.BaseBackActivity;
import com.cn.uca.ui.MainActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.view.MyEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;

public class SettingPasswordActivity extends BaseBackActivity implements View.OnClickListener{

    private MyEditText password;
    private TextView finish;
    private String phoneNumber,codeNumber,passwordNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_setting_password);
        ActivityCollector.registerActivity(this);
        initView();
        getInfo();
    }

    private void initView() {
        password = (MyEditText)findViewById(R.id.password);
        finish = (TextView)findViewById(R.id.finish);

        finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                phoneRegister();
                break;
        }
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            phoneNumber = intent.getStringExtra("phone");
            codeNumber = intent.getStringExtra("code");
        }
    }

    private void phoneRegister(){
        try{
            final String passwordText = password.getText().toString().trim();
            PublicKey publicKey = RSAUtils.loadPublicKey(Constant.PUBLIC_KEY);
            byte[] encryptByte = RSAUtils.encryptData(MD5.getMD5(passwordText).getBytes(), publicKey);
            passwordNumber = Base64.encode(encryptByte);
            RegisterBean bean = new RegisterBean();
            bean.setRegistration_id(SharePreferenceXutil.getChannelId());
            bean.setCode(codeNumber);
            bean.setEncryption_password(passwordNumber);
            bean.setPhone_number(phoneNumber);
            QueryHttp.phoneRegister(bean, new CallBack() {
                @Override
                public void onResponse(Object response) {
                    Log.i("123",response.toString()+"*****");
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        SharePreferenceXutil.saveAccountToken(jsonObject.getString("account_token"));
                        SharePreferenceXutil.setSuccess(true);
                        SharePreferenceXutil.savePhoneNumber(phoneNumber);
                        SharePreferenceXutil.setExit(false);
                        SharePreferenceXutil.saveUserName(phoneNumber);
                        SharePreferenceXutil.savePassword(passwordText);
                        Intent intent = new Intent();
                        intent.setClass(SettingPasswordActivity.this,MainActivity.class);
                        startActivity(intent);
                        ActivityCollector.finishRegister();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onErrorMsg(String errorMsg) {

                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        }catch (Exception e){

        }

    }
}

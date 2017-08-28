package com.cn.uca.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username,password;
    private TextView send,login,otherLogin,alipayLogin,weChatLogin;
    private String name,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        send = (TextView)findViewById(R.id.send);
        login = (TextView)findViewById(R.id.login);
        otherLogin = (TextView)findViewById(R.id.otherLogin);
        alipayLogin = (TextView)findViewById(R.id.alipayLogin);
        weChatLogin = (TextView)findViewById(R.id.weChatLogin);

        send.setOnClickListener(this);
        login.setOnClickListener(this);
        otherLogin.setOnClickListener(this);
        alipayLogin.setOnClickListener(this);
        weChatLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:
                sendCode();
                break;
            case R.id.login:

                break;
            case R.id.otherLogin:

                break;
            case R.id.alipayLogin:

                break;
            case R.id.weChatLogin:

                break;
        }
    }

    private void sendCode(){
        code = username.getText().toString();
        if (code.equals("") || code == null){
            ToastXutil.show("手机号不能为空");
        }else{
            if (StringXutil.isPhoneNumberValid(code)){
                MyApplication.getServer().sendCode(code, new CallBack() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("123",response.toString());
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

            }else{
                ToastXutil.show("手机号不合法");
            }
        }

    }
}
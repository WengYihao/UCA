package com.cn.uca.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private MyEditText username,password;
    private TextView login;

    private String name,pad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        username = (MyEditText)findViewById(R.id.username_et);
        password = (MyEditText)findViewById(R.id.password_et);

        login = (TextView)findViewById(R.id.login);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                name = username.getText().toString().trim();
                pad = password.getText().toString().trim();
                if (name.equals("123")&&pad.equals("123")){
                    ToastXutil.show("登录成功");
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    this.finish();
                }else{
                    ToastXutil.show("账号密码错误");
                }
                break;
        }
    }
}
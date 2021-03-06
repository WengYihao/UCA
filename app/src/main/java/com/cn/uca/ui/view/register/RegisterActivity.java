package com.cn.uca.ui.view.register;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.view.WebViewActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;

public class RegisterActivity extends BaseBackActivity implements View.OnClickListener,TextWatcher{

    private TextView back;
    private MyEditText phone,code;
    private TextView getCode,register,link;
    private CheckBox isCheck;
    private String phoneNumber,codeNumber;
    private TimeCount time;
    private boolean isClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.registerActivity(this);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        phone = (MyEditText)findViewById(R.id.phone);
        code = (MyEditText)findViewById(R.id.code);
        getCode  = (TextView)findViewById(R.id.getCode);
        register = (TextView)findViewById(R.id.register);
        link = (TextView)findViewById(R.id.link);
        isCheck = (CheckBox)findViewById(R.id.isCheck);

        back.setOnClickListener(this);
        getCode.setOnClickListener(this);
        register.setOnClickListener(this);
        link.setOnClickListener(this);

        phone.addTextChangedListener(this);
        code.addTextChangedListener(this);

        isCheck.setChecked(true);
        isCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    register.setEnabled(true);
                }else{
                    register.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.getCode:
                phoneNumber = phone.getText().toString().trim();
                if (StringXutil.isPhoneNumberValid(phoneNumber)){
                    MyApplication.sendCode(phoneNumber);
                    time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
                    time.start();
                }else{
                    ToastXutil.show("请输入正确的手机号");
                }
                break;
            case R.id.register:
                sendInfo();
                break;
            case R.id.link:
//                startActivity(new Intent(RegisterActivity.this,RegisterWebActivity.class));
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, WebViewActivity.class);
                intent.putExtra("type","register");
                intent.putExtra("url","http://www.szyouka.com:8080/youkatravel/agreement/appUseProtocol.html");
                startActivity(intent);
                break;
        }
    }

    private void sendInfo(){
        codeNumber = code.getText().toString().trim();
        if (StringXutil.rexCheckPassword(codeNumber)){
            Intent intent = new Intent();
            intent.setClass(RegisterActivity.this,SettingPasswordActivity.class);
            intent.putExtra("phone",phoneNumber);
            intent.putExtra("code",codeNumber);
            startActivity(intent);
        }else{
            ToastXutil.show("请输入6位验证码");
        }
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (phone.getText().toString().trim().length() == 11 && code.getText().toString().trim().length() == 6 && isCheck.isChecked()){
            register.setEnabled(true);
            isClick = true;
        }else{
            register.setEnabled(false);
            isClick = false;
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            getCode.setEnabled(true);
            getCode.setText("重新获取");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            getCode.setText(millisUntilFinished / 1000 + "s后重新获取");
            getCode.setEnabled(false);
        }
    }
}

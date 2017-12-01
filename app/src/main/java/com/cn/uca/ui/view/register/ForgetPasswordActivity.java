package com.cn.uca.ui.view.register;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.secretkey.MD5;
import com.cn.uca.secretkey.RSAUtils;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.ui.view.MainActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;

import java.security.PublicKey;

public class ForgetPasswordActivity extends BaseBackActivity implements View.OnClickListener{

    private MyEditText phone,password,code;
    private TextView back,getCode,finish;
    private String phoneNumber,passwordNumber,codeNumber;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ActivityCollector.forgetActivity.add(this);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        phone = (MyEditText)findViewById(R.id.phone);
        password = (MyEditText)findViewById(R.id.password);
        code = (MyEditText)findViewById(R.id.code);

        getCode = (TextView)findViewById(R.id.getCode);
        finish = (TextView)findViewById(R.id.finish);

        back.setOnClickListener(this);
        getCode.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.getCode:
                phoneNumber = phone.getText().toString().trim();
                MyApplication.sendCode(phoneNumber);
                time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
                time.start();
                break;
            case R.id.finish:
                forgetPassword();
                break;
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

    private void forgetPassword(){
        try {
            if (StringXutil.isPhoneNumberValid(phone.getText().toString())){
                if (!StringXutil.isEmpty(password.getText().toString())){
                    if(StringXutil.rexCheckPassword(password.getText().toString())){
                        if (!StringXutil.isEmpty(code.getText().toString())&& code.getText().toString().length() == 6){
                            final String passwordText = password.getText().toString().trim();
                            PublicKey publicKey = RSAUtils.loadPublicKey(Constant.PUBLIC_KEY);
                            byte[] encryptByte = RSAUtils.encryptData(MD5.getMD5(passwordText).getBytes(), publicKey);
                            passwordNumber = Base64.encode(encryptByte);
                            codeNumber = code.getText().toString().trim();
                            QueryHttp.forgetPassword(phoneNumber, passwordNumber, codeNumber, new CallBack() {
                                @Override
                                public void onResponse(Object response) {
                                    switch ((int) response){
                                        case 0:
                                            ToastXutil.show("重置成功");
                                            SharePreferenceXutil.saveUserName(phoneNumber);
                                            SharePreferenceXutil.savePassword(passwordText);
                                            SharePreferenceXutil.setExit(false);
                                            Intent intent = new Intent();
                                            intent.setClass(ForgetPasswordActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            ActivityCollector.finishForget();
                                            break;
                                        case 36:
                                            ToastXutil.show("验证码已失效");
                                            break;
                                        case 37:
                                            ToastXutil.show("验证码错误");
                                            break;
                                        case 57:
                                            ToastXutil.show("没有找到此手机号");
                                            break;
                                        default:
                                            ToastXutil.show("找回密码失败");
                                            break;
                                    }
                                }

                                @Override
                                public void onErrorMsg(String errorMsg) {

                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });
                        }else{
                            ToastXutil.show("请输入6位验证码");
                        }
                    }else{
                        ToastXutil.show("6-20密码暴扣非法字符");
                    }
                }else{
                    ToastXutil.show("新密码不能为空");
                }
            }else{
                ToastXutil.show("请输入正确的手机号");
            }

        }catch (Exception e){

        }

    }
}

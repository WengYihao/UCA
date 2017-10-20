package com.cn.uca.ui.view.user;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.secretkey.MD5;
import com.cn.uca.secretkey.RSAUtils;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;

import java.security.PublicKey;

public class UpdatePasswordActivity extends BaseBackActivity implements View.OnClickListener{

    private MyEditText oldPassword,newPassword;
    private TextView back,update;
    private String passwordOld,passwordNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_update_password);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        oldPassword = (MyEditText) findViewById(R.id.oldPassword);
        newPassword = (MyEditText)findViewById(R.id.newPassword);
        update = (TextView)findViewById(R.id.update);
        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:

                break;
            case R.id.update:
                update();
                break;
        }
    }

    private void update(){
        passwordOld = oldPassword.getText().toString().trim();
        passwordNew = newPassword.getText().toString().trim();
        try {
            if (passwordOld.length() < 6 && passwordOld.length() < 6 ){
                ToastXutil.show("密码长大于6位小于20位");
            }else{
                PublicKey publicKey = RSAUtils.loadPublicKey(Constant.PUBLIC_KEY);
                byte[] encryptByteOld = RSAUtils.encryptData(MD5.getMD5(passwordOld).getBytes(), publicKey);
                String afterencryptOld = Base64.encode(encryptByteOld);

                byte[] encryptByteNew = RSAUtils.encryptData(MD5.getMD5(passwordNew).getBytes(), publicKey);
                String afterencryptNew = Base64.encode(encryptByteNew);
                UserHttp.updatePassword(afterencryptOld, afterencryptNew, new CallBack() {
                    @Override
                    public void onResponse(Object response) {
                        if ((int)response == 0){
                            ToastXutil.show("修改成功！");
                            SharePreferenceXutil.savePassword(passwordNew);
                            UpdatePasswordActivity.this.finish();
                        }
                    }

                    @Override
                    public void onErrorMsg(String errorMsg) {
                        if (errorMsg != null){
                            ToastXutil.show(errorMsg);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            }


        }catch (Exception e){

        }

    }
}

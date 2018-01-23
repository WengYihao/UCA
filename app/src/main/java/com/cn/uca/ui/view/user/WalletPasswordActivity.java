package com.cn.uca.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.PasswordInputView;
import com.cn.uca.view.dialog.LoadDialog;

import org.json.JSONObject;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

//钱包密码-修改/设置
public class WalletPasswordActivity extends BaseBackActivity implements View.OnClickListener,PasswordInputView.OnFinishListener{

    private TextView back,title,text;
    private PasswordInputView passwordView;
    private String firstPwd,secondPwd;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_password);

        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            type = intent.getIntExtra("type",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        text = (TextView)findViewById(R.id.text);
        passwordView = (PasswordInputView)findViewById(R.id.passwordView);

        switch (type){
            case 1:
                title.setText("修改支付密码");
                text.setText("请输入原密码");
                break;
            case 2:
                title.setText("设置支付密码");
                break;
        }
        back.setOnClickListener(this);
        passwordView.setOnFinishListener(this);
    }

    @Override
    public void setOnPasswordFinished() {
        switch (type){
            case 1:
                if (passwordView.getOriginText().length() == passwordView.getMaxPasswordLength()) {
                    if (firstPwd == null) {
                        firstPwd = passwordView.getOriginText();
                        text.setText("请新的支付密码");
                        passwordView.setText("");
                    }else{
                        if (secondPwd == null){
                            secondPwd = passwordView.getOriginText();
                            text.setText("请再次确认支付密码");
                            passwordView.setText("");
                        }else{
                            if (secondPwd.equals(passwordView.getOriginText())){
                                LoadDialog.show(this);
                                //--请求修改
                                try{
                                    PublicKey publicKey = RSAUtils.loadPublicKey(Constant.PUBLIC_KEY);
                                    byte[] oldPaw = RSAUtils.encryptData(MD5.getMD5(firstPwd).getBytes(), publicKey);
                                    String old = Base64.encode(oldPaw);
                                    byte[] newPaw = RSAUtils.encryptData(MD5.getMD5(secondPwd).getBytes(), publicKey);
                                    String newPad = Base64.encode(newPaw);
                                    updatePayPwd(old,newPad);
                                }catch (Exception e){

                                }
                            }else{
                                ToastXutil.show("两次输入不一致，请重新输入");
                                text.setText("请新的支付密码");
                                passwordView.setText("");
                                secondPwd = null;
                            }
                        }
                    }
                }
                break;
            case 2:
                if (passwordView.getOriginText().length() == passwordView.getMaxPasswordLength()) {
                    if (firstPwd == null){
                        firstPwd = passwordView.getOriginText();
                        text.setText("请确认支付密码");
                        passwordView.setText("");
                    }else{
                        if (secondPwd == null){
                            secondPwd = passwordView.getOriginText();
                            if (firstPwd.equals(secondPwd)){
                                LoadDialog.show(this);
                                try{
                                    PublicKey publicKey = RSAUtils.loadPublicKey(Constant.PUBLIC_KEY);
                                    byte[] encryptByte = RSAUtils.encryptData(MD5.getMD5(firstPwd).getBytes(), publicKey);
                                    String afterencrypt = Base64.encode(encryptByte);
                                    setPayPwd(afterencrypt);
                                }catch (Exception e){
                                    Log.e("456",e.getMessage());
                                }
                            }else{
                                ToastXutil.show("两次密码不一致，请重新输入");
                                text.setText("请输入6位支付密码");
                                passwordView.setText("");
                                firstPwd = null;
                                secondPwd = null;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }
    //设置支付密码
    private void setPayPwd(String pay_pwd){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("pay_pwd",pay_pwd);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.setPayPwd(pay_pwd, sign, time_stamp, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            ToastXutil.show("设置成功");
                            LoadDialog.dismiss(WalletPasswordActivity.this);
                            WalletPasswordActivity.this.finish();
                            break;
                        case 738:
                            ToastXutil.show("请先绑定手机号");
                            break;
                        default:
                            LoadDialog.dismiss(WalletPasswordActivity.this);
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
    //修改支付密码
    private void updatePayPwd(String pay_pwd,String new_pay_pwd){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("pay_pwd",pay_pwd);
        map.put("new_pay_pwd",new_pay_pwd);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.updatePayPwd(new_pay_pwd, pay_pwd, sign, time_stamp, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            LoadDialog.dismiss(WalletPasswordActivity.this);
                            ToastXutil.show("修改成功");
                            WalletPasswordActivity.this.finish();
                            break;
                        case 743:
                            ToastXutil.show("密码不正确");
                            text.setText("请输入原密码");
                            firstPwd = null;
                            secondPwd = null;
                            passwordView.setText("");
                            break;
                        default:
                            LoadDialog.dismiss(WalletPasswordActivity.this);
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
}

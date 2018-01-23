package com.cn.uca.ui.view.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.alipay.AliPayUtil;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.ui.view.LoginActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.ToastXutil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.json.JSONObject;

//账号关联
public class AccountAssociationActivity extends BaseBackActivity implements View.OnClickListener{

    private static TextView back,state1,state2;
    private RelativeLayout layout1,layout2;
    private static boolean bingWeChat,bingPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_association);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        state1 = (TextView)findViewById(R.id.state1);
        state2 = (TextView)findViewById(R.id.state2);
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);

        back.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);

        bingWeChat = SharePreferenceXutil.isBindWeCaht();
        bingPay = SharePreferenceXutil.isBindPay();
        if (bingWeChat){
            state1.setText("已关联");
        }

        if (bingPay){
            state2.setText("已关联");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.layout1:
                if (!bingWeChat){
                    SharePreferenceXutil.setWeChatLogin(false);
                    sendWeChatAuthRequest();
                }
                break;
            case R.id.layout2:
                if (!bingPay){
                    aliPayRelation();
                }
                break;
        }
    }

    public static void  getUserState(){
        QueryHttp.getUserState(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            int weixin_state_id = object.getInt("bind_weixin_state_id");
                            if (weixin_state_id == 1){
                                SharePreferenceXutil.setBindWeCaht(true);
                                bingWeChat = true;
                                state1.setText("已关联");
                                state1.setText("立即关联");
                            }else{
                                SharePreferenceXutil.setBindWeCaht(false);
                            }
                            int zhifubao_state_id = object.getInt("bind_zhifubao_state_id");
                            if (zhifubao_state_id == 1){
                                bingPay = true;
                                SharePreferenceXutil.setBindPay(true);
                                state2.setText("已关联");
                            }else{
                                SharePreferenceXutil.setBindPay(false);
                                state2.setText("立即关联");
                            }
                            break;
                        case 17:
                            ToastXutil.show("登录已过期，请重新登录！");
                            SharePreferenceXutil.setExit(true);
                            SharePreferenceXutil.setSuccess(false);
                            SharePreferenceXutil.saveAccountToken("");
                            SharePreferenceXutil.saveAccessToken("");
                            SharePreferenceXutil.setOpenYS(false);
                            SharePreferenceXutil.setAuthentication(false);
                            break;
                    }
                }catch (Exception e){
                    Log.i("123",e.getMessage());
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                Log.i("789",errorMsg.toString()+"--");
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    //关联支付宝
    private void aliPayRelation(){
        QueryHttp.getAuthInfo(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            byte [] bytes = Base64.decode(jsonObject.getJSONObject("data").getString("auth_info"));
                            String str = new String(bytes,"UTF-8");
                            AliPayUtil payUtil = new AliPayUtil();
                            payUtil.toAliRelation(AccountAssociationActivity.this,str);
                            break;
                    }
                }catch (Exception E){

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
    //关联微信
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

package com.cn.uca.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.wechat.WeChatAccessToken;
import com.cn.uca.bean.wechat.WeChatLogin;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.MainActivity;
import com.cn.uca.ui.view.user.AccountAssociationActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.json.JSONObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler,CallBack{

    // IWXAPI 是第三方app和微信通信的openapi接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeChatManager.instance().handleIntent(getIntent(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WeChatManager.instance().handleIntent(getIntent(), this);
    }

    //微信请求相应
    @Override
    public void onReq(BaseReq baseReq) {

    }
    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        Log.i("123",resp.errCode+"-"+resp.errStr);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                if(resp instanceof SendAuth.Resp){
                    SendAuth.Resp newResp = (SendAuth.Resp) resp;
                    String code = newResp.code;
                    //获取微信传回的code--------
                    Log.e("456",code+"---返回code");
                    QueryHttp.getWeChatAccessToken(code,this) ;
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //发送取消
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //发送被拒绝
                result = R.string.errcode_deny;
                break;
            default:
                //发送返回
                result = R.string.errcode_unknown;
                break;
        }
        finish();
        ToastXutil.show(result);
    }

    @Override
    public void onResponse(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            Log.e("456",jsonObject.toString()+"授权返回值");
            Gson gson = new Gson();
            WeChatAccessToken token = gson.fromJson(jsonObject.toString(),new TypeToken<WeChatAccessToken>(){}.getType());
            MyApplication.getInstance().setAccessToken(token);
            SharePreferenceXutil.saveAccessToken(token.getAccess_token());
            SharePreferenceXutil.saveOpenId(token.getOpenId());
            if (MyApplication.getAccessToken().getOpenId() != null){
                if (SharePreferenceXutil.isWeChatLogin()){
                    startLogin();//微信登录
                }else{
                    //关联微信
                    bindWeixin();
                }
            }
        }catch (Exception e){
            Log.e("456",e.getMessage()+"---请求报错");
            finish();
        }
    }

    @Override
    public void onErrorMsg(String errorMsg) {
        ToastXutil.show(errorMsg);

    }

    @Override
    public void onError(VolleyError error) {
        Log.e("456",error.getMessage()+"授权登录报错");
    }

    //微信登录
    private void startLogin() {
        WeChatLogin weChatLogin = new WeChatLogin();
        weChatLogin.setRegistration_id(SharePreferenceXutil.getChannelId());
        weChatLogin.setAccess_token(MyApplication.getAccessToken().getAccess_token());
        weChatLogin.setOpenid(MyApplication.getAccessToken().getOpenId());
        QueryHttp.WeChatLogin(weChatLogin, new CallBack() {
            @Override
            public void onResponse(Object response) {
                if (response != null){
                    Gson gson = new Gson();
                    WeChatLogin token = gson.fromJson(response.toString(),new TypeToken<WeChatLogin>(){}.getType());
                    ToastXutil.show(R.string.errcode_login_success);
                    Log.e("123",token.getAccount_token());
                    SharePreferenceXutil.saveAccountToken(token.getAccount_token());
                    SharePreferenceXutil.setSuccess(true);
                    SharePreferenceXutil.setExit(false);
                    Intent intent = new Intent();
                    intent.setClass(WXEntryActivity.this,MainActivity.class);
                    startActivity(intent);
                    ActivityCollector.finishLogin();
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                ToastXutil.show(errorMsg);
            }

            @Override
            public void onError(VolleyError error) {
                Log.i("456",error.getMessage()+"---");
                ToastXutil.show("报错了");
            }
        });
    }
    //微信关联
    private void bindWeixin(){
        String ac = SharePreferenceXutil.getAccessToken();
        String openId = SharePreferenceXutil.getOpenId();
        UserHttp.bindWeixin(ac, openId, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            ToastXutil.show("账号关联成功");
                            AccountAssociationActivity.getUserState();
                            break;
                        case 42:
                            ToastXutil.show("微信账号已被绑定");
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

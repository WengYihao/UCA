package com.cn.uca.server;

import android.telecom.Call;
import android.util.Log;

import com.cn.uca.bean.user.RegisterBean;
import com.cn.uca.bean.user.UserInfo;
import com.cn.uca.bean.wechat.WeChatLogin;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyConfig;
import com.cn.uca.config.base.BaseServer;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.SharePreferenceXutil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2017/8/24.
 */

public class QueryHttp extends BaseServer{
    public void register(String leader_account_name,CallBack callback){
        Map<String, String> map = new HashMap<>();
        map.put("leader_account_name", leader_account_name);
        post3(MyConfig.text, map, callback);
    }

    /**
     * 发送短信验证码
     * @param phone_number
     * @param callBack
     */
    public void sendCode(String phone_number,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("phone_number",phone_number);
        post3(MyConfig.sendCode,map,callBack);
    }

    /**
     * 获取微信token
     * @param code
     * @param callBack
     */
    public void getWeChatAccessToken(String code,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("appid", Constant.WeChat_APP_ID);
        map.put("secret",Constant.WeChat_APP_SECRET);
        map.put("code",code);
        map.put("grant_type","authorization_code");
        get(MyConfig.getWeChatToken,map,callBack);
    }

    /**
     * 微信登录
     * @param weChatLogin
     * @param callBack
     */
    public void WeChatLogin(WeChatLogin weChatLogin,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("registration_id",weChatLogin.getRegistration_id());
        map.put("access_token",weChatLogin.getAccess_token());
        map.put("openid",weChatLogin.getOpenid());
        post3(MyConfig.weChatLogin,map,callBack);
    }


    /**
     * 测试接口
     * @param list
     * @param map
     * @param callBack
     */
    public void test(Map<String,File> list,Map<String,String> map,CallBack callBack){
        sendFiles("http://192.168.1.103/youkatravel/api/abnormal/test.do",list,map,callBack);
    }

    /**
     * 上传用户头像
     * @param inputStream
     * @param handler
     */
    public void uploadPic(InputStream inputStream, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_head_portrait",inputStream);
        params.put("account_token", SharePreferenceXutil.getAccountToken());
        client.post(MyConfig.uplodePic,params,handler);
    }

    /**
     * 获取用户信息
     * @param callBack
     */
    public void getUserInfo(CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.getUerInfo,map,callBack);
    }

    /**
     * 修改用户信息
     * @param handler
     */
    public void updateUserInfo(UserInfo userInfo, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",SharePreferenceXutil.getAccountToken());
        params.put("user_nick_name",userInfo.getUser_nick_name());
        params.put("sex_id",userInfo.getSex_id());
        params.put("user_birth_date",userInfo.getUser_birth_date());
        params.put("position_id",1);
        client.post(MyConfig.setUserInfo,params,handler);
    }

    /**
     * 用户手机号登录
     * @param password
     * @param handler
     */
    public void phoneLogin(String password,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone_number","15875536869");
        params.put("encryption_password",password);
        client.post(MyConfig.phoneLogin,params,handler);
    }

    /**
     * 手机号注册
     * @param bean
     * @param callBack
     */
    public void phoneRegister(RegisterBean bean,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("phone_number",bean.getPhone_number());
        map.put("code",bean.getCode());
        map.put("encryption_password",bean.getEncryption_password());
        map.put("registration_id","123456789");
        post3(MyConfig.phoneRegister,map,callBack);
    }
}

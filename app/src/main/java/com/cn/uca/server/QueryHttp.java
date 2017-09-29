package com.cn.uca.server;

import com.cn.uca.bean.yueka.GetEscortBean;
import com.cn.uca.bean.user.RegisterBean;
import com.cn.uca.bean.user.UserInfo;
import com.cn.uca.bean.wechat.WeChatLogin;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyConfig;
import com.cn.uca.config.base.BaseServer;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StringXutil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/8/24.
 */

public class QueryHttp extends BaseServer{
    /**
     * 发送短信验证码
     * @param phone_number
     * @param callBack
     */
    public static void sendCode(String phone_number,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("phone_number",phone_number);
        post3(MyConfig.sendCode,map,callBack);
    }
    /**
     * 获取微信token
     * @param code
     * @param callBack
     */
    public static void getWeChatAccessToken(String code,CallBack callBack){
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
    public static void WeChatLogin(WeChatLogin weChatLogin,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("registration_id",weChatLogin.getRegistration_id());
        map.put("access_token",weChatLogin.getAccess_token());
        map.put("openid",weChatLogin.getOpenid());
        post3(MyConfig.weChatLogin,map,callBack);
    }
    /**
     * 用户手机号登录
     * @param password
     * @param handler
     */
    public static void phoneLogin(String phoneNumber,String password,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone_number",phoneNumber);
        params.put("encryption_password",password);
        params.put("registration_id",SharePreferenceXutil.getChannelId());
        client.post(MyConfig.phoneLogin,params,handler);
    }
    /**
     * 手机号注册
     * @param bean
     * @param callBack
     */
    public static void phoneRegister(RegisterBean bean,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("phone_number",bean.getPhone_number());
        map.put("code",bean.getCode());
        map.put("encryption_password",bean.getEncryption_password());
        map.put("registration_id",bean.getRegistration_id());
        post3(MyConfig.phoneRegister,map,callBack);
    }
    /**
     * 忘记密码
     * @param phone_number
     * @param encryption_new_password
     * @param code
     * @param callBack
     */
    public static void forgetPassword(String phone_number,String encryption_new_password,String code,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("phone_number",phone_number);
        map.put("encryption_new_password",encryption_new_password);
        map.put("code",code);
        post4(MyConfig.forgetPassword,map,callBack);
    }

    /**
     * 获取版本号
     * @param callBack
     */
    public static void getLineVersion(CallBack callBack){
        get(MyConfig.newAppVersionAndroid,null,callBack);
    }

    /**
     * 快速登录
     * @param callBack
     */
    public static void fastLogin(CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("registration_id",SharePreferenceXutil.getRegistrationId());
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.fastLogin,map,callBack);
    }
    /**
     * 获取用户状态
     * @param callBack
     */
    public static void getUserState(CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token", SharePreferenceXutil.getAccountToken());
        get(MyConfig.getUserState,map,callBack);
    }
}

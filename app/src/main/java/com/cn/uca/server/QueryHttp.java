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
import java.io.FileNotFoundException;
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
     * 获取j简单用户信息
     * @param callBack
     */
    public void getUserBriefInfo(CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.getUserBriefInfo,map,callBack);
    }


    /**
     * 获取用户信息
     * @param callBack
     */
    public void getUserInfo(CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.getUserInfo,map,callBack);
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
    public void phoneLogin(String phoneNumber,String password,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone_number",phoneNumber);
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

    /**
     * 获取用户推送
     * @param callBack
     */
    public void getUserPush(CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        get(MyConfig.getUserPush,map,callBack);
    }

    /**
     * 设置用户是否推送
     * @param open_push
     * @param callBack
     */
    public void setUserPush(boolean open_push,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("open_push",open_push+"");
        post4(MyConfig.setUserPush,map,callBack);
    }

    /**
     * 修改密码
     * @param encryption_password
     * @param encryption_new_password
     * @param callBack
     */
    public void updatePassword(String encryption_password,String encryption_new_password,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("encryption_password",encryption_password);
        map.put("encryption_new_password",encryption_new_password);
        post4(MyConfig.updatePassword,map,callBack);
    }

    /**
     * 忘记密码
     * @param phone_number
     * @param encryption_new_password
     * @param code
     * @param callBack
     */
    public void forgetPassword(String phone_number,String encryption_new_password,String code,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("phone_number",phone_number);
        map.put("encryption_new_password",encryption_new_password);
        map.put("code",code);
        post4(MyConfig.forgetPassword,map,callBack);
    }

    /**
     * 身份认证
     * @param user_identiry_name
     * @param user_identity_number
     * @param user_identiry_photo
     * @param user_identiry_photo_back
     * @param handler
     */
    public void submitIdentity(String user_identiry_name,String user_identity_number,File user_identiry_photo,File user_identiry_photo_back,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",SharePreferenceXutil.getAccountToken());
        params.put("user_identiry_name",user_identiry_name);
        params.put("user_identity_number",user_identity_number);
        try {
            params.put("user_identiry_photo",user_identiry_photo);
            params.put("user_identiry_photo_back",user_identiry_photo_back);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(MyConfig.submitIdentity,params,handler);
    }
}

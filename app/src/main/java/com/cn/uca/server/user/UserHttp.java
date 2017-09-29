package com.cn.uca.server.user;

import com.cn.uca.bean.user.UserInfo;
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
 * Created by asus on 2017/9/29.
 */

public class UserHttp extends BaseServer {
    /**
     * 上传用户头像
     * @param inputStream
     * @param handler
     */
    public static void uploadPic(InputStream inputStream, AsyncHttpResponseHandler handler){
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
    public static void getUserBriefInfo(CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.getUserBriefInfo,map,callBack);
    }

    /**
     * 获取用户信息
     * @param callBack
     */
    public static void getUserInfo(CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.getUserInfo,map,callBack);
    }

    /**
     * 修改用户信息
     * @param handler
     */
    public static void updateUserInfo(UserInfo userInfo, AsyncHttpResponseHandler handler){
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
     * 获取用户推送
     * @param callBack
     */
    public static void getUserPush(CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        get(MyConfig.getUserPush,map,callBack);
    }

    /**
     * 设置用户是否推送
     * @param open_push
     * @param callBack
     */
    public static void setUserPush(boolean open_push,CallBack callBack){
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
    public static void updatePassword(String encryption_password,String encryption_new_password,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("encryption_password",encryption_password);
        map.put("encryption_new_password",encryption_new_password);
        post4(MyConfig.updatePassword,map,callBack);
    }

    /**
     * 身份认证
     * @param user_identiry_name
     * @param user_identity_number
     * @param user_identiry_photo
     * @param user_identiry_photo_back
     * @param handler
     */
    public static void submitIdentity(String user_identiry_name, String user_identity_number, File user_identiry_photo, File user_identiry_photo_back, AsyncHttpResponseHandler handler){
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

    /**
     * 获取用户余额
     * @param callBack
     */
    public static void getWallet(CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.getWallet,map,callBack);
    }
}

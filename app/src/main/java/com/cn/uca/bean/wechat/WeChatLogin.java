package com.cn.uca.bean.wechat;

/**
 * Created by asus on 2017/8/29.
 */

public class WeChatLogin {
    private String registration_id;//用户的极光推送id
    private String access_token;   //微信token
    private String openid;          //微信openid
    private String account_token;  //用户token值
    private long valid_time;       //有效时间

    public String getRegistration_id() {
        return registration_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getOpenid() {
        return openid;
    }

    public String getAccount_token() {
        return account_token;
    }

    public long getValid_time() {
        return valid_time;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setAccount_token(String account_token) {
        this.account_token = account_token;
    }

    public void setValid_time(long valid_time) {
        this.valid_time = valid_time;
    }
}

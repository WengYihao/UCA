package com.cn.uca.bean.wechat;

import java.io.Serializable;

/**
 * Created by asus on 2017/8/29.
 */

public class WeChatAccessToken implements Serializable{

    private static final long serialVersionUID = 1L;

    public String access_token;//接口调用凭证
    public int expires_in;//access_token接口调用凭证超时时间，单位（秒
    public String refresh_token;//用户刷新access_token
    public String openid;//授权用户唯一标识
    public String scope;//用户授权的作用域，使用逗号（,）分隔
    public String unionid;//用户授权的作用域，使用逗号（,）分隔
    public int errcode;// 错误码
    public String errmsg;//错误信息

    public String getAccess_token() {
        return access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getOpenId() {
        return openid;
    }

    public String getScope() {
        return scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setOpenId(String openid) {
        this.openid = openid;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "WeChatAccessToken{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}

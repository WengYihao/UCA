package com.cn.uca.bean.home.lvpai;

/**
 * Created by asus on 2017/12/20.
 * 商家入驻
 */

public class BecomeMerchantBean {
    private String time_stamp;
    private String sign;
    private String account_token;
    private String contacts;
    private String domestic_travel;
    private String introduce;
    private String merchant_name;
    private String overseas_tour;
    private String weixin;
    private String position;
    private String pictures;
    private String phone;

    public String getTime_stamp() {
        return time_stamp;
    }

    public String getSign() {
        return sign;
    }

    public String getAccount_token() {
        return account_token;
    }

    public String getContacts() {
        return contacts;
    }

    public String getDomestic_travel() {
        return domestic_travel;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getOverseas_tour() {
        return overseas_tour;
    }

    public String getWeixin() {
        return weixin;
    }

    public String getPosition() {
        return position;
    }

    public String getPictures() {
        return pictures;
    }

    public String getPhone() {
        return phone;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setAccount_token(String account_token) {
        this.account_token = account_token;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setDomestic_travel(String domestic_travel) {
        this.domestic_travel = domestic_travel;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public void setOverseas_tour(String overseas_tour) {
        this.overseas_tour = overseas_tour;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "BecomeMerchantBean{" +
                "time_stamp='" + time_stamp + '\'' +
                ", sign='" + sign + '\'' +
                ", account_token='" + account_token + '\'' +
                ", contacts='" + contacts + '\'' +
                ", domestic_travel='" + domestic_travel + '\'' +
                ", introduce='" + introduce + '\'' +
                ", merchant_name='" + merchant_name + '\'' +
                ", overseas_tour='" + overseas_tour + '\'' +
                ", weixin='" + weixin + '\'' +
                ", position='" + position + '\'' +
                ", pictures='" + pictures + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

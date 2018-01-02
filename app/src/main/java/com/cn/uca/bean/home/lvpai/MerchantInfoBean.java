package com.cn.uca.bean.home.lvpai;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/21.
 * 商家信息
 */

public class MerchantInfoBean implements Parcelable{
    private int trip_shoot_merchant_id;
    private String head_portrait_url;
    private double score;
    private String weixin;
    private boolean domestic_travel;
    private String phone;
    private String introduce;
    private boolean personal_tailor;
    private String merchant_name;
    private String contacts;
    private boolean overseas_tour;

    public int getTrip_shoot_merchant_id() {
        return trip_shoot_merchant_id;
    }

    public String getHead_portrait_url() {
        return head_portrait_url;
    }

    public double getScore() {
        return score;
    }

    public String getWeixin() {
        return weixin;
    }

    public boolean isDomestic_travel() {
        return domestic_travel;
    }

    public String getPhone() {
        return phone;
    }

    public String getIntroduce() {
        return introduce;
    }

    public boolean isPersonal_tailor() {
        return personal_tailor;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getContacts() {
        return contacts;
    }

    public boolean isOverseas_tour() {
        return overseas_tour;
    }

    public void setTrip_shoot_merchant_id(int trip_shoot_merchant_id) {
        this.trip_shoot_merchant_id = trip_shoot_merchant_id;
    }

    public void setHead_portrait_url(String head_portrait_url) {
        this.head_portrait_url = head_portrait_url;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public void setDomestic_travel(boolean domestic_travel) {
        this.domestic_travel = domestic_travel;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setPersonal_tailor(boolean personal_tailor) {
        this.personal_tailor = personal_tailor;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setOverseas_tour(boolean overseas_tour) {
        this.overseas_tour = overseas_tour;
    }

    public static final Parcelable.Creator<MerchantInfoBean> CREATOR = new Creator<MerchantInfoBean>() {
        @Override
        public MerchantInfoBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            MerchantInfoBean bean = new MerchantInfoBean();
            bean.setTrip_shoot_merchant_id(source.readInt());
            bean.setHead_portrait_url(source.readString());
            bean.setScore(source.readDouble());
            bean.setWeixin(source.readString());
            bean.domestic_travel = (source.readByte()!= 0);
            bean.setPhone(source.readString());
            bean.setIntroduce(source.readString());
//            bean.personal_tailor(source.readByte() != 0);
            bean.personal_tailor = (source.readByte()!= 0);
            bean.setMerchant_name(source.readString());
            bean.setContacts(source.readString());
//            bean.overseas_tour(source.readByte() != 0);
            bean.overseas_tour = (source.readByte()!= 0);
            return bean;
        }
        @Override
        public MerchantInfoBean[] newArray(int size) {
            return new MerchantInfoBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(trip_shoot_merchant_id);
        dest.writeString(head_portrait_url);
        dest.writeDouble(score);
        dest.writeString(weixin);
        dest.writeByte((byte)(domestic_travel ?1:0));
        dest.writeString(phone);
        dest.writeString(introduce);
        dest.writeByte((byte)(personal_tailor ?1:0));
        dest.writeString(merchant_name);
        dest.writeString(contacts);
        dest.writeByte((byte)(overseas_tour ?1:0));
    }
}

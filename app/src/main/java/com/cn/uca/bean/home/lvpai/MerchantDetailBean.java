package com.cn.uca.bean.home.lvpai;

import java.util.List;

/**
 * Created by asus on 2017/12/27.
 */

public class MerchantDetailBean {
    private int account_number_id;
    private List<setAddressBean> address;
    private String contacts;
    private boolean domestic_travel;
    private boolean follow;
    private String head_portrait_url;
    private String introduce;
    private String merchant_name;
    private boolean overseas_tour;
    private boolean personal_tailor;
    private String phone;
    private int picture_number;
    private List<String> pictures;
    private double score;
    private List<TeamBean> teams;
    private List<TripShootsBean> tripShoots;
    private int trip_shoot_merchant_id;
    private String weixin;

    public int getAccount_number_id() {
        return account_number_id;
    }

    public List<setAddressBean> getAddress() {
        return address;
    }

    public String getContacts() {
        return contacts;
    }

    public boolean isDomestic_travel() {
        return domestic_travel;
    }

    public boolean isFollow() {
        return follow;
    }

    public String getHead_portrait_url() {
        return head_portrait_url;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public boolean isOverseas_tour() {
        return overseas_tour;
    }

    public boolean isPersonal_tailor() {
        return personal_tailor;
    }

    public String getPhone() {
        return phone;
    }

    public int getPicture_number() {
        return picture_number;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public double getScore() {
        return score;
    }

    public List<TeamBean> getTeams() {
        return teams;
    }

    public List<TripShootsBean> getTripShoots() {
        return tripShoots;
    }

    public int getTrip_shoot_merchant_id() {
        return trip_shoot_merchant_id;
    }

    public String getWeixin() {
        return weixin;
    }
}

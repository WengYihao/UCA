package com.cn.uca.bean.home.lvpai;

import java.util.List;

/**
 * Created by asus on 2017/12/27.
 */

public class CommodityDetailBean {
    private int account_number_id;
    private boolean collection;
    private int comment_number;
    private String destination;
    private String head_portrait_url;
    private List<String> lable_names;
    private String merchant_name;
    private List<DescriptionBean> notices;
    private String phone;
    private List<PhotoBean> photos;
    private List<String> pictures;
    private double price;
    private int purchase_number;
    private double score;
    private List<ServiceBean> summarys;
    private List<TeamBean > teams;
    private String title;
    private int trip_shoot_id;
    private int trip_shoot_merchant_id;
    private int trip_shoot_type_id;
    private List<ServiceBean> trips;
    private AddressBean tsmAddress;

    public int getAccount_number_id() {
        return account_number_id;
    }

    public boolean isCollection() {
        return collection;
    }

    public int getComment_number() {
        return comment_number;
    }

    public String getDestination() {
        return destination;
    }

    public String getHead_portrait_url() {
        return head_portrait_url;
    }

    public List<String> getLable_names() {
        return lable_names;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public List<DescriptionBean> getNotices() {
        return notices;
    }

    public String getPhone() {
        return phone;
    }

    public List<PhotoBean> getPhotos() {
        return photos;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public double getPrice() {
        return price;
    }

    public int getPurchase_number() {
        return purchase_number;
    }

    public double getScore() {
        return score;
    }

    public List<ServiceBean> getSummarys() {
        return summarys;
    }

    public List<TeamBean> getTeams() {
        return teams;
    }

    public String getTitle() {
        return title;
    }

    public int getTrip_shoot_id() {
        return trip_shoot_id;
    }

    public int getTrip_shoot_merchant_id() {
        return trip_shoot_merchant_id;
    }

    public int getTrip_shoot_type_id() {
        return trip_shoot_type_id;
    }

    public List<ServiceBean> getTrips() {
        return trips;
    }

    public AddressBean getTsmAddress() {
        return tsmAddress;
    }
}

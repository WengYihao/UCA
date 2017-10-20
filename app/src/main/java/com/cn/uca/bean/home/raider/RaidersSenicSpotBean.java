package com.cn.uca.bean.home.raider;

/**
 * Created by asus on 2017/10/17.
 * 攻略-景点
 */

public class RaidersSenicSpotBean {
    private String address;
    private double lng;
    private String picture_url;
    private String introduce;
    private int raiders_scenic_spot_id;
    private double lat;
    private String scenic_spot_name;
    private int order;

    public String getAddress() {
        return address;
    }

    public double getLng() {
        return lng;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public String getIntroduce() {
        return introduce;
    }

    public int getRaiders_scenic_spot_id() {
        return raiders_scenic_spot_id;
    }

    public double getLat() {
        return lat;
    }

    public String getScenic_spot_name() {
        return scenic_spot_name;
    }

    public int getOrder() {
        return order;
    }
}

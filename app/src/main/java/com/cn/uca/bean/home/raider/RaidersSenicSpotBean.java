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
    private boolean isStart;
    private boolean isEnd;

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

    public boolean isStart() {
        return isStart;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setRaiders_scenic_spot_id(int raiders_scenic_spot_id) {
        this.raiders_scenic_spot_id = raiders_scenic_spot_id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setScenic_spot_name(String scenic_spot_name) {
        this.scenic_spot_name = scenic_spot_name;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}

package com.cn.uca.bean.home.samecityka;

/**
 * Created by asus on 2018/1/8.
 */

public class PositionBean {
    private double lng;
    private double lat;
    private String gaode_code;
    private String address;
    private String remarks;

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String getGaode_code() {
        return gaode_code;
    }

    public String getAddress() {
        return address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setGaode_code(String gaode_code) {
        this.gaode_code = gaode_code;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "{" +
                "lng=" + lng +
                ", lat=" + lat +
                ", gaode_code='" + gaode_code + '\'' +
                ", address='" + address + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

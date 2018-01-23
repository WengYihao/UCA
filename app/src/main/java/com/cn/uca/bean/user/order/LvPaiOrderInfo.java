package com.cn.uca.bean.user.order;

import java.util.List;

/**
 * Created by asus on 2018/1/18.
 */

public class LvPaiOrderInfo extends OrderInfo{
    private int trip_shoot_id;// 旅拍id
    private String picture;// 图片
    private String title;// 旅拍标题
    private int trip_shoot_type_id;// 类型(1:国内,2:国外)
    private int country_id;// 国家id
    private String country_name;// 国家名称
    private int city_id;// 城市id
    private String city_name;// 城市名称
    private String beg_date;// 开始时间
    private String end_date;// 结束时间
    private List<String> style_lable;// 风格标签数组

    public int getTrip_shoot_id() {
        return trip_shoot_id;
    }

    public void setTrip_shoot_id(int trip_shoot_id) {
        this.trip_shoot_id = trip_shoot_id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTrip_shoot_type_id() {
        return trip_shoot_type_id;
    }

    public void setTrip_shoot_type_id(int trip_shoot_type_id) {
        this.trip_shoot_type_id = trip_shoot_type_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getBeg_date() {
        return beg_date;
    }

    public void setBeg_date(String beg_date) {
        this.beg_date = beg_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public List<String> getStyle_lable() {
        return style_lable;
    }

    public void setStyle_lable(List<String> style_lable) {
        this.style_lable = style_lable;
    }
}


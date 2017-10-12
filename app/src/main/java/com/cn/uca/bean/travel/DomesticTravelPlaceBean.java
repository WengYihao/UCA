package com.cn.uca.bean.travel;

/**
 * Created by asus on 2017/10/9.
 */

public class DomesticTravelPlaceBean {
    private int city_id;
    private String city_name;
    private String city_picture_url;

    public int getCity_id() {
        return city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getCity_picture_url() {
        return city_picture_url;
    }

    @Override
    public String toString() {
        return "DomesticTravelPlaceBean{" +
                "city_id=" + city_id +
                ", city_name='" + city_name + '\'' +
                ", city_picture_url='" + city_picture_url + '\'' +
                '}';
    }
}

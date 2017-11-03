package com.cn.uca.bean.home.footprint;

/**
 * Created by asus on 2017/11/1.
 */

public class CityNameBean {

    private String city_name;
    private String gaode_code;
    private int city_id;

    public String getCity_name() {
        return city_name;
    }

    public String getGaode_code() {
        return gaode_code;
    }

    public int getCity_id() {
        return city_id;
    }

    @Override
    public String toString() {
        return "CityNameBean{" +
                "city_name='" + city_name + '\'' +
                ", gaode_code='" + gaode_code + '\'' +
                ", city_id=" + city_id +
                '}';
    }
}

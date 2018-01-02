package com.cn.uca.bean.home.lvpai;

import java.util.List;

/**
 * Created by asus on 2017/12/27.
 */

public class TripShootsBean {
    private List<String> lable_names;
    private int m_city_id;
    private String picture_url;
    private double price;
    private String title;
    private int trip_shoot_id;

    public List<String> getLable_names() {
        return lable_names;
    }

    public int getM_city_id() {
        return m_city_id;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public int getTrip_shoot_id() {
        return trip_shoot_id;
    }
}

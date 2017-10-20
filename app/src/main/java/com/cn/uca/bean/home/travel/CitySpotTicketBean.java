package com.cn.uca.bean.home.travel;

/**
 * Created by asus on 2017/10/16.
 * 城市门票信息实体类
 */

public class CitySpotTicketBean {
    private int evaluation_quantity;
    private double score;
    private String address;
    private double min_price;
    private String picture_url;
    private int scenic_spot_id;
    private int product_quantity;
    private String scenic_spot_name;
    private int city_id;

    public int getEvaluation_quantity() {
        return evaluation_quantity;
    }

    public double getScore() {
        return score;
    }

    public String getAddress() {
        return address;
    }

    public double getMin_price() {
        return min_price;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public int getScenic_spot_id() {
        return scenic_spot_id;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public String getScenic_spot_name() {
        return scenic_spot_name;
    }

    public int getCity_id() {
        return city_id;
    }
}

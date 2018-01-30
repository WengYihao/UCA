package com.cn.uca.bean.home.sign;

/**
 * Created by asus on 2018/1/24.
 */

public class IntegralPoolBean {
    private String commodity_name;
    private String commodity_picture_path;
    private int commodity_type_id;
    private int exchange_count;
    private int integral_pool_id;
    private int need_integral;
    private double price;

    public String getCommodity_name() {
        return commodity_name;
    }

    public String getCommodity_picture_path() {
        return commodity_picture_path;
    }

    public int getCommodity_type_id() {
        return commodity_type_id;
    }

    public int getExchange_count() {
        return exchange_count;
    }

    public int getIntegral_pool_id() {
        return integral_pool_id;
    }

    public int getNeed_integral() {
        return need_integral;
    }

    public double getPrice() {
        return price;
    }
}

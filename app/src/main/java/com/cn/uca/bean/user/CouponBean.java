package com.cn.uca.bean.user;

/**
 * Created by asus on 2018/3/14.
 * 优惠券
 */

public class CouponBean {
    private String coupon_existence_time;
    private double coupon_price;
    private int coupon_type_id;
    private String setting_coupon_introduce;
    private String setting_coupon_name;
    private String start_date;
    private int user_coupon_id;

    public String getCoupon_existence_time() {
        return coupon_existence_time;
    }

    public double getCoupon_price() {
        return coupon_price;
    }

    public int getCoupon_type_id() {
        return coupon_type_id;
    }

    public String getSetting_coupon_introduce() {
        return setting_coupon_introduce;
    }

    public String getSetting_coupon_name() {
        return setting_coupon_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public int getUser_coupon_id() {
        return user_coupon_id;
    }
}

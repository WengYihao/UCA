package com.cn.uca.bean.home.lvpai;

/**
 * Created by asus on 2018/1/19.
 */

public class MerchantAllOrderBean {
    private int account_number_id;
    private String beg_date;
    private String end_date;
    private String pictures;
    private double price;
    private String title;
    private int trip_shoot_id;
    private int trip_shoot_order_id;
    private String user_head_portrait;
    private String user_nick_name;

    public int getAccount_number_id() {
        return account_number_id;
    }

    public String getBeg_date() {
        return beg_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getPictures() {
        return pictures;
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

    public int getTrip_shoot_order_id() {
        return trip_shoot_order_id;
    }

    public String getUser_head_portrait() {
        return user_head_portrait;
    }

    public String getUser_nick_name() {
        return user_nick_name;
    }
}

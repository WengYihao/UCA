package com.cn.uca.bean.user;

/**
 * Created by asus on 2017/11/16.
 * 订单
 */

public class OrderBean {
    private int commodity_id;
    private String merchandise_order_title;
    private String publisher_name;
    private int user_order_id;
    private int user_order_state_id;
    private String order_number;
    private String create_order_time;
    private String commodity_time;
    private double order_price;

    public int getCommodity_id() {
        return commodity_id;
    }

    public String getMerchandise_order_title() {
        return merchandise_order_title;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public int getUser_order_id() {
        return user_order_id;
    }

    public int getUser_order_state_id() {
        return user_order_state_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public String getCreate_order_time() {
        return create_order_time;
    }

    public String getCommodity_time() {
        return commodity_time;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setCommodity_id(int commodity_id) {
        this.commodity_id = commodity_id;
    }

    public void setMerchandise_order_title(String merchandise_order_title) {
        this.merchandise_order_title = merchandise_order_title;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public void setUser_order_id(int user_order_id) {
        this.user_order_id = user_order_id;
    }

    public void setUser_order_state_id(int user_order_state_id) {
        this.user_order_state_id = user_order_state_id;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public void setCreate_order_time(String create_order_time) {
        this.create_order_time = create_order_time;
    }

    public void setCommodity_time(String commodity_time) {
        this.commodity_time = commodity_time;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }
}

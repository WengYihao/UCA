package com.cn.uca.bean.user.order;

import java.util.List;

/**
 * Created by asus on 2018/1/18.
 */

public class OrderInfo {
    private int user_order_id;
    private int user_order_state_id;
    private double payables;
    private double actual_payment;
    private String order_number;
    private int order_coupon_use_state_id;
    private String order_create_time;
    private OrderCoupon orderCoupon;
    private List<OrderCoupon> coupons;// 此订单可用的优惠券

    public int getUser_order_id() {
        return user_order_id;
    }

    public int getUser_order_state_id() {
        return user_order_state_id;
    }

    public double getPayables() {
        return payables;
    }

    public double getActual_payment() {
        return actual_payment;
    }

    public String getOrder_number() {
        return order_number;
    }

    public int getOrder_coupon_use_state_id() {
        return order_coupon_use_state_id;
    }

    public String getOrder_create_time() {
        return order_create_time;
    }

    public OrderCoupon getOrderCoupon() {
        return orderCoupon;
    }

    public List<OrderCoupon> getCoupons() {
        return coupons;
    }
}

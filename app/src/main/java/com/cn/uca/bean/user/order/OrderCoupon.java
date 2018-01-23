package com.cn.uca.bean.user.order;

public class OrderCoupon {
	private int user_coupon_id;// 用户优惠券id
	private int setting_coupon_id;// 系统优惠券id
	private String setting_coupon_name;// 优惠券名称
	private double coupon_price;// 优惠券价格

	public int getUser_coupon_id() {
		return user_coupon_id;
	}

	public void setUser_coupon_id(int user_coupon_id) {
		this.user_coupon_id = user_coupon_id;
	}

	public int getSetting_coupon_id() {
		return setting_coupon_id;
	}

	public void setSetting_coupon_id(int setting_coupon_id) {
		this.setting_coupon_id = setting_coupon_id;
	}

	public String getSetting_coupon_name() {
		return setting_coupon_name;
	}

	public void setSetting_coupon_name(String setting_coupon_name) {
		this.setting_coupon_name = setting_coupon_name;
	}

	public double getCoupon_price() {
		return coupon_price;
	}

	public void setCoupon_price(double coupon_price) {
		this.coupon_price = coupon_price;
	}
}

package com.cn.uca.bean.user.order;

import com.cn.uca.bean.home.samecityka.MyTicketCodeBean;

import java.util.List;

/**
 * Created by asus on 2018/1/18.
 */

public class SameCityKaOrderInfo extends OrderInfo {
    private int city_cafe_id;// 同城咖id
    private String picture;// 图片
    private String title;// 同城咖标题
    private int activity_type_id;// 获取类型(线上,线下)
    private int city_id;// 城市id
    private String city_name;// 城市名称

    private String beg_date;// 开始时间
    private String end_date;// 结束时间
    private String user_card_name;// 主办方名称(名片名称)
    private List<MyTicketCodeBean> tickets;// 同城咖票数组

    public int getCity_cafe_id() {
        return city_cafe_id;
    }

    public void setCity_cafe_id(int city_cafe_id) {
        this.city_cafe_id = city_cafe_id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getActivity_type_id() {
        return activity_type_id;
    }

    public void setActivity_type_id(int activity_type_id) {
        this.activity_type_id = activity_type_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getBeg_date() {
        return beg_date;
    }

    public void setBeg_date(String beg_date) {
        this.beg_date = beg_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getUser_card_name() {
        return user_card_name;
    }

    public void setUser_card_name(String user_card_name) {
        this.user_card_name = user_card_name;
    }

    public List<MyTicketCodeBean> getTickets() {
        return tickets;
    }

    public void setTickets(List<MyTicketCodeBean> tickets) {
        this.tickets = tickets;
    }
}

package com.cn.uca.bean.home.samecityka;

import java.util.List;

/**
 * Created by asus on 2017/12/18.
 */

public class MyTicketBean {
    private int city_cafe_id;
    private int city_cafe_order_id;
    private int city_cafe_state_id;
    private String state;
    private String title;
    private List<MyTicketCodeBean> tickets;

    public int getCity_cafe_id() {
        return city_cafe_id;
    }

    public int getCity_cafe_order_id() {
        return city_cafe_order_id;
    }

    public int getCity_cafe_state_id() {
        return city_cafe_state_id;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public List<MyTicketCodeBean> getTickets() {
        return tickets;
    }
}

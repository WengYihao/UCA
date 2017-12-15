package com.cn.uca.bean.home.samecityka;

import java.util.List;

/**
 * Created by asus on 2017/12/14.
 */

public class SameCityKaOrderBean {
    private String order_number;
    private List<TicketsRets> ticketsRets;
    private double price;

    public String getOrder_number() {
        return order_number;
    }

    public List<TicketsRets> getTicketsRets() {
        return ticketsRets;
    }

    public double getPrice() {
        return price;
    }
}

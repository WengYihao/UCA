package com.cn.uca.bean.home.samecityka;

/**
 * Created by asus on 2017/12/13.
 */

public class TicketBean {
    private int city_cafe_ticket_id;
    private int surplus;
    private String ticket_name;
    private int limit_ticket;
    private boolean open_examine;
    private double price;

    public int getCity_cafe_ticket_id() {
        return city_cafe_ticket_id;
    }

    public int getSurplus() {
        return surplus;
    }

    public String getTicket_name() {
        return ticket_name;
    }

    public int getLimit_ticket() {
        return limit_ticket;
    }

    public boolean isOpen_examine() {
        return open_examine;
    }

    public double getPrice() {
        return price;
    }
}

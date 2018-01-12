package com.cn.uca.bean.home.samecityka;

/**
 * Created by asus on 2018/1/5.
 */

public class SettlementTicketBean {
    private int check_ticket_number;
    private int city_cafe_ticket_id;
    private double price;
    private int refund_ticket_number;
    private int sell_out;
    private String ticket_name;

    public int getCheck_ticket_number() {
        return check_ticket_number;
    }

    public int getCity_cafe_ticket_id() {
        return city_cafe_ticket_id;
    }

    public double getPrice() {
        return price;
    }

    public int getRefund_ticket_number() {
        return refund_ticket_number;
    }

    public int getSell_out() {
        return sell_out;
    }

    public String getTicket_name() {
        return ticket_name;
    }
}

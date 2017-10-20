package com.cn.uca.bean.home.travel;

/**
 * Created by asus on 2017/10/13.
 * 门票信息
 */

public class TicketBean {
    private String source_merchant;
    private double price_original;
    private String scenic_name;
    private int admission_ticket_id;
    private boolean idcard_required;
    private boolean can_direct_revocation;
    private String scheduled_time;
    private int min_order_len;
    private int ticket_source_id;
    private int payment_method_id;
    private int max_order_len;
    private String order_notice_url;
    private double price_customer;

    public String getSource_merchant() {
        return source_merchant;
    }

    public double getPrice_original() {
        return price_original;
    }

    public String getScenic_name() {
        return scenic_name;
    }

    public int getAdmission_ticket_id() {
        return admission_ticket_id;
    }

    public boolean isIdcard_required() {
        return idcard_required;
    }

    public boolean isCan_direct_revocation() {
        return can_direct_revocation;
    }

    public String getScheduled_time() {
        return scheduled_time;
    }

    public int getMin_order_len() {
        return min_order_len;
    }

    public int getTicket_source_id() {
        return ticket_source_id;
    }

    public int getPayment_method_id() {
        return payment_method_id;
    }

    public int getMax_order_len() {
        return max_order_len;
    }

    public String getOrder_notice_url() {
        return order_notice_url;
    }

    public double getPrice_customer() {
        return price_customer;
    }
}

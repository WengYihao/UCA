package com.cn.uca.bean.home.samecityka;

import java.util.List;

/**
 * Created by asus on 2017/12/13.
 */

public class BuyTicketInfoBean {
    private List<TicketBean> tickets;
    private List<FillInfoBean> fillUserInfos;
    private boolean purchase_ticket;
    private int user_info_fill;
    private int ticket_number;

    public List<TicketBean> getTickets() {
        return tickets;
    }

    public List<FillInfoBean> getFillUserInfos() {
        return fillUserInfos;
    }

    public boolean isPurchase_ticket() {
        return purchase_ticket;
    }

    public int getUser_info_fill() {
        return user_info_fill;
    }

    public int getTicket_number() {
        return ticket_number;
    }
}

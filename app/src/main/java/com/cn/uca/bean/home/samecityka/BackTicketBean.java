package com.cn.uca.bean.home.samecityka;

import java.util.List;

/**
 * Created by asus on 2018/1/5.
 */

public class BackTicketBean {
    private int city_cafe_id;
    private String create_time;
    private String reason;
    private int refund_ticket_id;
    private List<MyTicketCodeBean> tickets;
    private String title;
    private String user_head_portrait;
    private String user_nick_name;

    public int getCity_cafe_id() {
        return city_cafe_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getReason() {
        return reason;
    }

    public int getRefund_ticket_id() {
        return refund_ticket_id;
    }

    public List<MyTicketCodeBean> getTickets() {
        return tickets;
    }

    public String getTitle() {
        return title;
    }

    public String getUser_head_portrait() {
        return user_head_portrait;
    }

    public String getUser_nick_name() {
        return user_nick_name;
    }
}

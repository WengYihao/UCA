package com.cn.uca.bean.home.samecityka;

import java.util.List;

/**
 * Created by asus on 2018/1/4.
 */

public class SignUpBean {
    private int city_cafe_id;
    private String create_time;
    private List<SignUpInfoBean> fillInfos;
    private int ticket_approval_content_id;
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

    public List<SignUpInfoBean> getFillInfos() {
        return fillInfos;
    }

    public int getTicket_approval_content_id() {
        return ticket_approval_content_id;
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

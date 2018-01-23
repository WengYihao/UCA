package com.cn.uca.bean.home.samecityka;

import java.util.List;

/**
 * Created by asus on 2017/12/13.
 * 活动详情
 */

public class ActionDetailBean {
    private int account_number_id;
    private int city_cafe_id;
    private String cover_url;
    private boolean charge;
    private String end_time;
    private int city_cafe_state_id;
    private boolean collection;
    private boolean purchase_ticket;
    private List<ActionDescribeBean> paragraphs;
    private String title;
    private String user_card_name;
    private int activity_type_id;
    private String beg_time;
    private AddressBean position;

    public int getAccount_number_id() {
        return account_number_id;
    }

    public int getCity_cafe_id() {
        return city_cafe_id;
    }

    public String getCover_url() {
        return cover_url;
    }

    public boolean isCharge() {
        return charge;
    }

    public String getEnd_time() {
        return end_time;
    }

    public int getCity_cafe_state_id() {
        return city_cafe_state_id;
    }

    public boolean isCollection() {
        return collection;
    }

    public boolean isPurchase_ticket() {
        return purchase_ticket;
    }

    public List<ActionDescribeBean> getParagraphs() {
        return paragraphs;
    }

    public String getTitle() {
        return title;
    }

    public String getUser_card_name() {
        return user_card_name;
    }

    public int getActivity_type_id() {
        return activity_type_id;
    }

    public String getBeg_time() {
        return beg_time;
    }

    public AddressBean getPosition() {
        return position;
    }
}

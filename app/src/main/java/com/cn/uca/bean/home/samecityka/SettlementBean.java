package com.cn.uca.bean.home.samecityka;

import java.util.List;

/**
 * Created by asus on 2018/1/5.
 */

public class SettlementBean {
    private int activity_type_id;
    private String beg_time;
    private boolean charge;
    private String cityCafeState;
    private List<SettlementTicketBean> cityCafeTickets;
    private int city_cafe_id;
    private int city_cafe_state_id;
    private String cover_url;
    private String end_time;
    private AddressBean position;
    private boolean purchase_ticket;
    private String title;
    private double total;

    public int getActivity_type_id() {
        return activity_type_id;
    }

    public String getBeg_time() {
        return beg_time;
    }

    public boolean isCharge() {
        return charge;
    }

    public String getCityCafeState() {
        return cityCafeState;
    }

    public List<SettlementTicketBean> getCityCafeTickets() {
        return cityCafeTickets;
    }

    public int getCity_cafe_id() {
        return city_cafe_id;
    }

    public int getCity_cafe_state_id() {
        return city_cafe_state_id;
    }

    public String getCover_url() {
        return cover_url;
    }

    public String getEnd_time() {
        return end_time;
    }

    public AddressBean getPosition() {
        return position;
    }

    public boolean isPurchase_ticket() {
        return purchase_ticket;
    }

    public String getTitle() {
        return title;
    }

    public double getTotal() {
        return total;
    }
}

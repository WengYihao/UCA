package com.cn.uca.bean.home.travel;

import java.util.List;

/**
 * Created by asus on 2017/10/13.
 * 景点门票详情
 */

public class SpotTicketBean {
   private String address;
    private int evaluation_quantity;
    private String introduce_url;
    private double lat;
    private double lng;
    private String picture_url;
    private int scenic_spot_id;
    private String scenic_spot_name;
    private double score;
    private List<TicketBean> ticketRets;

    public String getAddress() {
        return address;
    }

    public int getEvaluation_quantity() {
        return evaluation_quantity;
    }

    public String getIntroduce_url() {
        return introduce_url;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public int getScenic_spot_id() {
        return scenic_spot_id;
    }

    public String getScenic_spot_name() {
        return scenic_spot_name;
    }

    public double getScore() {
        return score;
    }

    public List<TicketBean> getTicketRets() {
        return ticketRets;
    }
}

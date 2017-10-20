package com.cn.uca.bean.home.travel;

import java.util.List;

/**
 * Created by asus on 2017/10/13.
 * 景点门票详情
 */

public class SpotTicketBean {
    private int evaluation_quantity;
    private String introduce_url;
    private double score;
    private String address;
    private String picture_url;
    private List<TicketBean> ticketRets;
    private int scenic_spot_id;
    private List<SpotTicketCommentBean> scenicSpotComments;
    private String scenic_spot_name;

    public int getEvaluation_quantity() {
        return evaluation_quantity;
    }

    public String getIntroduce_url() {
        return introduce_url;
    }

    public double getScore() {
        return score;
    }

    public String getAddress() {
        return address;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public List<TicketBean> getTicketRets() {
        return ticketRets;
    }

    public int getScenic_spot_id() {
        return scenic_spot_id;
    }

    public List<SpotTicketCommentBean> getScenicSpotComments() {
        return scenicSpotComments;
    }

    public String getScenic_spot_name() {
        return scenic_spot_name;
    }
}

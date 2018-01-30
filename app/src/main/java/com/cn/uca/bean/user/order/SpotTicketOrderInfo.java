package com.cn.uca.bean.user.order;

/**
 * Created by asus on 2018/1/26.
 */

public class SpotTicketOrderInfo extends OrderInfo {
    private int commodity_id;
    private String id_card;
    private String mobile;
    private int number;
    private String picture;
    private String play_date;
    private int scenic_spot_id;
    private String scenic_spot_name;
    private String user_name;

    public int getCommodity_id() {
        return commodity_id;
    }

    public String getId_card() {
        return id_card;
    }

    public String getMobile() {
        return mobile;
    }

    public int getNumber() {
        return number;
    }

    public String getPicture() {
        return picture;
    }

    public String getPlay_date() {
        return play_date;
    }

    public int getScenic_spot_id() {
        return scenic_spot_id;
    }

    public String getScenic_spot_name() {
        return scenic_spot_name;
    }

    public String getUser_name() {
        return user_name;
    }
}

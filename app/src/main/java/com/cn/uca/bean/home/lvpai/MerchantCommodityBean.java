package com.cn.uca.bean.home.lvpai;

import java.util.List;

/**
 * Created by asus on 2018/1/2.
 */

public class MerchantCommodityBean {
    private int collection_number;
    private int comment_number;
    private String destination;
    private boolean hotspot;
    private List<String> lable_names;
    private int m_city_id;
    private int msg_index;
    private String picture_url;
    private double price;
    private int purchase_number;
    private String title;
    private int trip_shoot_id;
    private int trip_shoot_state_id;

    public void setHotspot(boolean hotspot) {
        this.hotspot = hotspot;
    }

    public int getCollection_number() {
        return collection_number;
    }

    public int getComment_number() {
        return comment_number;
    }

    public String getDestination() {
        return destination;
    }

    public boolean isHotspot() {
        return hotspot;
    }

    public List<String> getLable_names() {
        return lable_names;
    }

    public int getM_city_id() {
        return m_city_id;
    }

    public int getMsg_index() {
        return msg_index;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public double getPrice() {
        return price;
    }

    public int getPurchase_number() {
        return purchase_number;
    }

    public String getTitle() {
        return title;
    }

    public int getTrip_shoot_id() {
        return trip_shoot_id;
    }

    public int getTrip_shoot_state_id() {
        return trip_shoot_state_id;
    }
}

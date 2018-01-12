package com.cn.uca.bean.home.samecityka;

/**
 * Created by asus on 2018/1/4.
 */

public class CollectionBean {
    private int activity_type_id;
    private String beg_time;
    private boolean charge;
    private int city_cafe_id;
    private int city_cafe_state_id;
    private String cover_url;
    private String end_time;
    private AddressBean position;
    private String state;
    private String title;

    public int getActivity_type_id() {
        return activity_type_id;
    }

    public String getBeg_time() {
        return beg_time;
    }

    public boolean isCharge() {
        return charge;
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

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }
}

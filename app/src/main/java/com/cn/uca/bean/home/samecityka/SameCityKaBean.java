package com.cn.uca.bean.home.samecityka;

import java.util.List;

/**
 * Created by asus on 2017/12/13.
 */

public class SameCityKaBean {
    private int activity_type_id;
    private int city_cafe_id;
    private String cover_url;
    private boolean charge;
    private int account_number_id;
    private String beg_time;
    private String end_time;
    private AddressBean position;
    private String title;
    private List<LableBean> labels;

    public int getActivity_type_id() {
        return activity_type_id;
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

    public int getAccount_number_id() {
        return account_number_id;
    }

    public String getBeg_time() {
        return beg_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public AddressBean getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public List<LableBean> getLabels() {
        return labels;
    }
}

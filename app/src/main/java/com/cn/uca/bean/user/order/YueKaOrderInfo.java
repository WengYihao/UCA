package com.cn.uca.bean.user.order;

import java.util.List;

/**
 * Created by asus on 2018/1/18.
 */

public class YueKaOrderInfo extends OrderInfo {
    private int escort_record_id;
    private String picture;
    private String escort_record_name;
    private int city_id;
    private String city_name;
    private String beg_date;
    private String end_date;
    private int actual_number;
    private List<ServiceFunction> serviceFunctionPrices;
    private int escort_user_state_id;// 约咖状态id

    public int getEscort_record_id() {
        return escort_record_id;
    }

    public String getPicture() {
        return picture;
    }

    public String getEscort_record_name() {
        return escort_record_name;
    }

    public int getCity_id() {
        return city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getBeg_date() {
        return beg_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public int getActual_number() {
        return actual_number;
    }

    public List<ServiceFunction> getServiceFunctionPrices() {
        return serviceFunctionPrices;
    }

    public int getEscort_user_state_id() {
        return escort_user_state_id;
    }
}

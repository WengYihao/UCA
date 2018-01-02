package com.cn.uca.bean.home.lvpai;

import java.util.List;

/**
 * Created by asus on 2017/12/26.
 */

public class MerchantBean {
    private boolean domestic_travel;
    private int m_city_id;
    private String m_city_name;
    private String merchant_name;
    private boolean overseas_tour;
    private List<String> pictures;
    private double score;
    private int trip_shoot_merchant_id;

    public boolean isDomestic_travel() {
        return domestic_travel;
    }

    public int getM_city_id() {
        return m_city_id;
    }

    public String getM_city_name() {
        return m_city_name;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public boolean isOverseas_tour() {
        return overseas_tour;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public double getScore() {
        return score;
    }

    public int getTrip_shoot_merchant_id() {
        return trip_shoot_merchant_id;
    }
}

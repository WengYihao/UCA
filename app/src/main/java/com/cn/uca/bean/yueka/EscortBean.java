package com.cn.uca.bean.yueka;

import java.util.List;

/**
 * Created by asus on 2017/11/21.
 */

public class EscortBean {
    private List<EscortDayBean> escortDayDiscount;
    private double star_price;
    private List<EscortPeopleBean> escortPeopleNumberDiscount;

    public List<EscortDayBean> getEscortDayDiscount() {
        return escortDayDiscount;
    }

    public double getStar_price() {
        return star_price;
    }

    public List<EscortPeopleBean> getEscortPeopleNumberDiscount() {
        return escortPeopleNumberDiscount;
    }
}

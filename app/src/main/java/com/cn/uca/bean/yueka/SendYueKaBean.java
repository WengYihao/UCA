package com.cn.uca.bean.yueka;

import java.util.List;

/**
 * Created by asus on 2017/9/28.
 */

public class SendYueKaBean {
    private double average_score_price;
    private double average_score;
    private String average_score_content;
    private String dayDiscountContent;
    private String numberDiscountContent;
    private List<RoutesBean> escortAllRoutes;

    public void setAverage_score_price(double average_score_price) {
        this.average_score_price = average_score_price;
    }

    public void setAverage_score(double average_score) {
        this.average_score = average_score;
    }

    public void setAverage_score_content(String average_score_content) {
        this.average_score_content = average_score_content;
    }

    public void setDayDiscountContent(String dayDiscountContent) {
        this.dayDiscountContent = dayDiscountContent;
    }

    public void setNumberDiscountContent(String numberDiscountContent) {
        this.numberDiscountContent = numberDiscountContent;
    }

    public void setEscortAllRoutes(List<RoutesBean> escortAllRoutes) {
        this.escortAllRoutes = escortAllRoutes;
    }

    public double getAverage_score_price() {
        return average_score_price;
    }

    public double getAverage_score() {
        return average_score;
    }

    public String getAverage_score_content() {
        return average_score_content;
    }

    public String getDayDiscountContent() {
        return dayDiscountContent;
    }

    public String getNumberDiscountContent() {
        return numberDiscountContent;
    }

    public List<RoutesBean> getEscortAllRoutes() {
        return escortAllRoutes;
    }

    @Override
    public String toString() {
        return "SendYueKaBean{" +
                "average_score_price=" + average_score_price +
                ", average_score=" + average_score +
                ", average_score_content='" + average_score_content + '\'' +
                ", dayDiscountContent='" + dayDiscountContent + '\'' +
                ", numberDiscountContent='" + numberDiscountContent + '\'' +
                ", escortAllRoutes=" + escortAllRoutes +
                '}';
    }
}

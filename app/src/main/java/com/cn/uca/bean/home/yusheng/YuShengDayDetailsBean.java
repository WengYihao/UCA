package com.cn.uca.bean.home.yusheng;

import java.util.List;

/**
 * Created by asus on 2017/10/25.
 */

public class YuShengDayDetailsBean {
    private List<DayMonthsBean> lifeMonths;
    private int sum_days;
    private List<LifeDaysBean> lifeDays;
    private int fill;
    private int now_days;

    public List<DayMonthsBean> getLifeMonths() {
        return lifeMonths;
    }

    public int getSum_days() {
        return sum_days;
    }

    public List<LifeDaysBean> getLifeDays() {
        return lifeDays;
    }

    public int getFill() {
        return fill;
    }

    public int getNow_days() {
        return now_days;
    }

    public void setLifeMonths(List<DayMonthsBean> lifeMonths) {
        this.lifeMonths = lifeMonths;
    }

    public void setSum_days(int sum_days) {
        this.sum_days = sum_days;
    }

    public void setLifeDays(List<LifeDaysBean> lifeDays) {
        this.lifeDays = lifeDays;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }

    public void setNow_days(int now_days) {
        this.now_days = now_days;
    }

    @Override
    public String toString() {
        return "YuShengDayDetailsBean{" +
                "lifeMonths=" + lifeMonths +
                ", sum_days=" + sum_days +
                ", lifeDays=" + lifeDays +
                ", fill=" + fill +
                ", now_days=" + now_days +
                '}';
    }
}

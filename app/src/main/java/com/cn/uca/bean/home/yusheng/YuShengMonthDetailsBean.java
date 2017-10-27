package com.cn.uca.bean.home.yusheng;

import java.util.List;

/**
 * Created by asus on 2017/10/26.
 */

public class YuShengMonthDetailsBean {
    private List<LifeMonthsBean> lifeMonthsRet;
    private int sum_month;
    private int now_month;
    private int fill;

    public List<LifeMonthsBean> getLifeMonthsRet() {
        return lifeMonthsRet;
    }

    public int getSum_month() {
        return sum_month;
    }

    public int getNow_month() {
        return now_month;
    }

    public int getFill() {
        return fill;
    }

    public void setLifeMonthsRet(List<LifeMonthsBean> lifeMonthsRet) {
        this.lifeMonthsRet = lifeMonthsRet;
    }

    public void setSum_month(int sum_month) {
        this.sum_month = sum_month;
    }

    public void setNow_month(int now_month) {
        this.now_month = now_month;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }
}

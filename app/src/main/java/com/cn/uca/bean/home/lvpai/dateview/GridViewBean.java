package com.cn.uca.bean.home.lvpai.dateview;

import java.util.List;

/**
 * Created by asus on 2017/12/28.
 */

public class GridViewBean {
    private int weekId;//星期几
    private List<DateBean> viewBean;//日期
    private List<SourceDateBean> sourceBeen;//商家日期

    public int getWeekId() {
        return weekId;
    }

    public List<DateBean> getViewBean() {
        return viewBean;
    }

    public List<SourceDateBean> getSourceBeen() {
        return sourceBeen;
    }

    public void setWeekId(int weekId) {
        this.weekId = weekId;
    }

    public void setViewBean(List<DateBean> viewBean) {
        this.viewBean = viewBean;
    }

    public void setSourceBeen(List<SourceDateBean> sourceBeen) {
        this.sourceBeen = sourceBeen;
    }

    @Override
    public String toString() {
        return "GridViewBean{" +
                "weekId=" + weekId +
                ", viewBean=" + viewBean +
                ", sourceBeen=" + sourceBeen +
                '}';
    }
}

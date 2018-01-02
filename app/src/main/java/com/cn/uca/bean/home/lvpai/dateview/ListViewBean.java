package com.cn.uca.bean.home.lvpai.dateview;

/**
 * Created by asus on 2017/12/28.
 */

public class ListViewBean {
    private String monthName;
    private GridViewBean bean;

    public String getMonthName() {
        return monthName;
    }

    public GridViewBean getBean() {
        return bean;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setBean(GridViewBean bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "ListViewBean{" +
                "monthName='" + monthName + '\'' +
                ", bean=" + bean +
                '}';
    }
}

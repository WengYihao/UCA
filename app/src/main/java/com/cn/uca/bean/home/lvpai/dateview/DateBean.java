package com.cn.uca.bean.home.lvpai.dateview;

/**
 * Created by asus on 2017/12/28.
 */

public class DateBean {
    private String date;//日期
    private String day;//几号
    private String select;//选的是不是今天true:是ob:在服务日期内false:不在服务范围

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getSelect() {
        return select;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "DateBean{" +
                "date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", select='" + select + '\'' +
                '}';
    }
}

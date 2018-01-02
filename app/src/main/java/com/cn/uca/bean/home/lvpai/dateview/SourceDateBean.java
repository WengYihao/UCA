package com.cn.uca.bean.home.lvpai.dateview;

/**
 * Created by asus on 2017/12/28.
 */

public class SourceDateBean {
    private String date;
    private int type;

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SourceDateBean{" +
                "date='" + date + '\'' +
                ", type=" + type +
                '}';
    }
}

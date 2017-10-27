package com.cn.uca.bean.home.yusheng;

/**
 * Created by asus on 2017/10/26.
 */

public class LifeMonthsBean {
    private String date;
    private int month;
    private int month_event_count;
    private String content;

    public String getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public int getMonth_event_count() {
        return month_event_count;
    }

    public String getContent() {
        return content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setMonth_event_count(int month_event_count) {
        this.month_event_count = month_event_count;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

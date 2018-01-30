package com.cn.uca.bean.home.yusheng;

import java.util.List;

/**
 * Created by asus on 2017/10/25.
 */

public class LifeDaysBean {
    private String date;
    private int life_month_id;
    private int system_event_count;
    private String user_content;
    private List<SystemEventsBean> systemEvents;
    private int user_event_count;
    private int day;
    public String getDate() {
        return date;
    }

    public int getLife_month_id() {
        return life_month_id;
    }

    public int getSystem_event_count() {
        return system_event_count;
    }

    public String getUser_content() {
        return user_content;
    }

    public List<SystemEventsBean> getSystemEvents() {
        return systemEvents;
    }

    public int getUser_event_count() {
        return user_event_count;
    }

    public int getDay() {
        return day;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLife_month_id(int life_month_id) {
        this.life_month_id = life_month_id;
    }

    public void setSystem_event_count(int system_event_count) {
        this.system_event_count = system_event_count;
    }

    public void setUser_content(String user_content) {
        this.user_content = user_content;
    }

    public void setSystemEvents(List<SystemEventsBean> systemEvents) {
        this.systemEvents = systemEvents;
    }

    public void setUser_event_count(int user_event_count) {
        this.user_event_count = user_event_count;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "LifeDaysBean{" +
                "date='" + date + '\'' +
                ", life_month_id=" + life_month_id +
                ", system_event_count=" + system_event_count +
                ", user_content='" + user_content + '\'' +
                ", systemEvents=" + systemEvents +
                ", user_event_count=" + user_event_count +
                ", day=" + day +
                '}';
    }
}

package com.cn.uca.bean;

import java.util.List;

/**
 * Created by asus on 2017/9/7.
 */

public class CourseEscortBean {
    private List<String> picList;
    private String type;
    private String time;
    private String evaluate;

    public List<String> getPicList() {
        return picList;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }
}

package com.cn.uca.bean.yueka;

import java.util.List;

/**
 * Created by asus on 2017/11/21.
 */

public class SubmitYuekaBean {
    private double min_consumption;
    private String beg_time;
    private String user_head_portrait;
    private String end_time;
    private int requirement_people_number;
    private int escort_record_id;
    private EscortBean escortDiscount;

    public double getMin_consumption() {
        return min_consumption;
    }

    public String getBeg_time() {
        return beg_time;
    }

    public String getUser_head_portrait() {
        return user_head_portrait;
    }

    public String getEnd_time() {
        return end_time;
    }

    public int getRequirement_people_number() {
        return requirement_people_number;
    }

    public int getEscort_record_id() {
        return escort_record_id;
    }

    public EscortBean getEscortDiscount() {
        return escortDiscount;
    }
}

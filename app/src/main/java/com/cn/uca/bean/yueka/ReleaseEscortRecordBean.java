package com.cn.uca.bean.yueka;

import java.util.List;

/**
 * Created by asus on 2017/9/28.
 * 发布伴游
 */

public class ReleaseEscortRecordBean {
    private String escort_record_name;
    private String account_token;
    private String beg_time;
    private String end_time;
    private int escort_details_id;
    private int route_id;
    private int requirement_people_number;
    private String releaseServices;

    public String getEscort_record_name() {
        return escort_record_name;
    }

    public String getAccount_token() {
        return account_token;
    }

    public String getBeg_time() {
        return beg_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public int getEscort_details_id() {
        return escort_details_id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public int getRequirement_people_number() {
        return requirement_people_number;
    }


    public void setEscort_record_name(String escort_record_name) {
        this.escort_record_name = escort_record_name;
    }

    public void setAccount_token(String account_token) {
        this.account_token = account_token;
    }

    public void setBeg_time(String beg_time) {
        this.beg_time = beg_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setEscort_details_id(int escort_details_id) {
        this.escort_details_id = escort_details_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public void setRequirement_people_number(int requirement_people_number) {
        this.requirement_people_number = requirement_people_number;
    }

    public String getReleaseServices() {
        return releaseServices;
    }

    public void setReleaseServices(String releaseServices) {
        this.releaseServices = releaseServices;
    }

    @Override
    public String toString() {
        return "ReleaseEscortRecordBean{" +
                "escort_record_name='" + escort_record_name + '\'' +
                ", account_token='" + account_token + '\'' +
                ", beg_time='" + beg_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", escort_details_id=" + escort_details_id +
                ", route_id=" + route_id +
                ", requirement_people_number=" + requirement_people_number +
                ", releaseServices=" + releaseServices +
                '}';
    }
}

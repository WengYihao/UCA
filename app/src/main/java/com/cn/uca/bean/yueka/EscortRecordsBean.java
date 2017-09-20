package com.cn.uca.bean.yueka;

import java.util.List;

/**
 * Created by asus on 2017/9/12.
 */

public class EscortRecordsBean {
    private double min_consumption;
    private double average_score;
    private int comment_number;
    private int route_id;
    private int account_number_id;
    private List<String> escort_record_picture_urls;
    private List<LableBean> escortLabels;
    private int escort_id;
    private String user_head_portrait_url;
    private boolean collection;
    private String user_nick_name;
    private int escort_record_id;
    private int sex_id;
    private int escort_record_state_id;
    private int escort_details_id;
    private String comment_content;
    private int browse_times;
    private String user_birth_date;
    private double max_consumption;

    public double getMin_consumption() {
        return min_consumption;
    }

    public double getAverage_score() {
        return average_score;
    }

    public int getComment_number() {
        return comment_number;
    }

    public int getRoute_id() {
        return route_id;
    }

    public int getAccount_number_id() {
        return account_number_id;
    }

    public List<String> getEscort_record_picture_urls() {
        return escort_record_picture_urls;
    }

    public List<LableBean> getEscortLabels() {
        return escortLabels;
    }

    public int getEscort_id() {
        return escort_id;
    }

    public String getUser_head_portrait_url() {
        return user_head_portrait_url;
    }

    public boolean isCollection() {
        return collection;
    }

    public String getUser_nick_name() {
        return user_nick_name;
    }

    public int getEscort_record_id() {
        return escort_record_id;
    }

    public int getSex_id() {
        return sex_id;
    }

    public int getEscort_record_state_id() {
        return escort_record_state_id;
    }

    public int getEscort_details_id() {
        return escort_details_id;
    }

    public String getComment_content() {
        return comment_content;
    }

    public int getBrowse_times() {
        return browse_times;
    }

    public String getUser_birth_date() {
        return user_birth_date;
    }

    public double getMax_consumption() {
        return max_consumption;
    }

}

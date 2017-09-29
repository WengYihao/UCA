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

    public void setMin_consumption(double min_consumption) {
        this.min_consumption = min_consumption;
    }

    public void setAverage_score(double average_score) {
        this.average_score = average_score;
    }

    public void setComment_number(int comment_number) {
        this.comment_number = comment_number;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public void setAccount_number_id(int account_number_id) {
        this.account_number_id = account_number_id;
    }

    public void setEscort_record_picture_urls(List<String> escort_record_picture_urls) {
        this.escort_record_picture_urls = escort_record_picture_urls;
    }

    public void setEscortLabels(List<LableBean> escortLabels) {
        this.escortLabels = escortLabels;
    }

    public void setEscort_id(int escort_id) {
        this.escort_id = escort_id;
    }

    public void setUser_head_portrait_url(String user_head_portrait_url) {
        this.user_head_portrait_url = user_head_portrait_url;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public void setUser_nick_name(String user_nick_name) {
        this.user_nick_name = user_nick_name;
    }

    public void setEscort_record_id(int escort_record_id) {
        this.escort_record_id = escort_record_id;
    }

    public void setSex_id(int sex_id) {
        this.sex_id = sex_id;
    }

    public void setEscort_record_state_id(int escort_record_state_id) {
        this.escort_record_state_id = escort_record_state_id;
    }

    public void setEscort_details_id(int escort_details_id) {
        this.escort_details_id = escort_details_id;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public void setBrowse_times(int browse_times) {
        this.browse_times = browse_times;
    }

    public void setUser_birth_date(String user_birth_date) {
        this.user_birth_date = user_birth_date;
    }

    public void setMax_consumption(double max_consumption) {
        this.max_consumption = max_consumption;
    }
}

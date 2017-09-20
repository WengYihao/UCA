package com.cn.uca.bean.yueka;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/9/13.
 */

public class YueKaDetailsBean {
    private double min_consumption;
    private double average_score;
    private int account_number_id;
    private List<LableBean> escortLabels;
    private int escort_id;
    private String user_head_portrait_url;
    private boolean collection;
    private String user_nick_name;
    private int sex_id;
    private ArrayList<PlacesBean> places;
    private String escort_details_url;
    private int requirement_people_number;
    private int history_escort_count;
    private int browse_times;
    private String user_birth_date;
    private double max_consumption;

    public double getMin_consumption() {
        return min_consumption;
    }

    public double getAverage_score() {
        return average_score;
    }

    public int getAccount_number_id() {
        return account_number_id;
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

    public int getSex_id() {
        return sex_id;
    }

    public ArrayList<PlacesBean> getPlaces() {
        return places;
    }

    public String getEscort_details_url() {
        return escort_details_url;
    }

    public int getRequirement_people_number() {
        return requirement_people_number;
    }

    public int getHistory_escort_count() {
        return history_escort_count;
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

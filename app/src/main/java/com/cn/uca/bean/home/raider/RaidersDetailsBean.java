package com.cn.uca.bean.home.raider;

import java.util.List;

/**
 * Created by asus on 2017/10/17.
 * 城市攻略详情
 */

public class RaidersDetailsBean {
    private String introduce_url;
    private List<RaidersTrainStationBean> trainStationRets;
    private double left_lower_lat;
    private List<RaidersSenicSpotBean> scenicSpotRets;
    private double upper_right_lng;
    private List<RaidersFoodBean> foodRets;
    private double left_lower_lng;
    private double upper_right_lat;
    private List<RaidersAirportBean> airportRets;
    private String map_picture_url;
    private double price;
    private long file_resources_size;
    private int city_raiders_id;
    private int city_id;

    public String getIntroduce_url() {
        return introduce_url;
    }

    public List<RaidersTrainStationBean> getTrainStationRets() {
        return trainStationRets;
    }

    public double getLeft_lower_lat() {
        return left_lower_lat;
    }

    public List<RaidersSenicSpotBean> getScenicSpotRets() {
        return scenicSpotRets;
    }

    public double getUpper_right_lng() {
        return upper_right_lng;
    }

    public List<RaidersFoodBean> getFoodRets() {
        return foodRets;
    }

    public double getLeft_lower_lng() {
        return left_lower_lng;
    }

    public double getUpper_right_lat() {
        return upper_right_lat;
    }

    public List<RaidersAirportBean> getAirportRets() {
        return airportRets;
    }

    public String getMap_picture_url() {
        return map_picture_url;
    }

    public double getPrice() {
        return price;
    }

    public long getFile_resources_size() {
        return file_resources_size;
    }

    public int getCity_raiders_id() {
        return city_raiders_id;
    }

    public int getCity_id() {
        return city_id;
    }
}

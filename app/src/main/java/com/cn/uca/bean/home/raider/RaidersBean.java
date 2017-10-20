package com.cn.uca.bean.home.raider;

/**
 * Created by asus on 2017/9/30.
 * 一元攻略
 */

public class RaidersBean {
    private String pacture_url;
    private String city_name;
    private double price;
    private long file_resources_size;
    private boolean lock;
    private int city_raiders_id;
    private boolean collection;
    private int city_id;

    public String getPacture_url() {
        return pacture_url;
    }

    public String getCity_name() {
        return city_name;
    }

    public double getPrice() {
        return price;
    }

    public long getFile_resources_size() {
        return file_resources_size;
    }

    public boolean isLock() {
        return lock;
    }

    public int getCity_raiders_id() {
        return city_raiders_id;
    }

    public boolean isCollection() {
        return collection;
    }

    public int getCity_id() {
        return city_id;
    }
}

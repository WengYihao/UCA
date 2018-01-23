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

    public void setPacture_url(String pacture_url) {
        this.pacture_url = pacture_url;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setFile_resources_size(long file_resources_size) {
        this.file_resources_size = file_resources_size;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public void setCity_raiders_id(int city_raiders_id) {
        this.city_raiders_id = city_raiders_id;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    @Override
    public String toString() {
        return "RaidersBean{" +
                "pacture_url='" + pacture_url + '\'' +
                ", city_name='" + city_name + '\'' +
                ", price=" + price +
                ", file_resources_size=" + file_resources_size +
                ", lock=" + lock +
                ", city_raiders_id=" + city_raiders_id +
                ", collection=" + collection +
                ", city_id=" + city_id +
                '}';
    }
}

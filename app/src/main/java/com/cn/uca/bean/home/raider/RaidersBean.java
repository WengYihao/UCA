package com.cn.uca.bean.home.raider;

/**
 * Created by asus on 2017/9/30.
 * 一元攻略
 */

public class RaidersBean {
    private int city_id;
    private String city_name;
    private int city_raiders_id;
    private boolean collection;
    private boolean lock;
    private String pacture_url;
    private double price;
    private long file_resources_size;
    private int select;

//    public RaidersBean(int city_id,String city_name,int city_raiders_id,boolean collection,boolean lock,String pacture_url,double price){
//        this.city_id = city_id;
//        this.city_name = city_name;
//        this.city_raiders_id = city_raiders_id;
//        this.collection = collection;
//        this.lock = lock;
//        this.pacture_url = pacture_url;
//        this.price = price;
//    }

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

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "RaidersBean{" +
                "city_id=" + city_id +
                ", city_name='" + city_name + '\'' +
                ", city_raiders_id=" + city_raiders_id +
                ", collection=" + collection +
                ", lock=" + lock +
                ", pacture_url='" + pacture_url + '\'' +
                ", price=" + price +
                ", file_resources_size=" + file_resources_size +
                '}';
    }
}

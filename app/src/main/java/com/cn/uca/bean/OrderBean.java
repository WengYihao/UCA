package com.cn.uca.bean;

public class OrderBean {
    private String type;
    private String name;
    private String state;
    private String place;
    private String time;
    private int price;

    public void setType(String type){
        this.type = type;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType(){
        return type;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }
}

package com.cn.uca.bean;

import java.util.List;

/**
 * Created by asus on 2017/8/25.
 */

public class YueKaBean {

    private String img;
    private String name;
    private int age;
    private int start;
    private String price;
    private int sum;
    private int count;
    private String evaluate;
    private List<String> lable;
    private List<String> imgList;

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getStart() {
        return start;
    }

    public String getPrice() {
        return price;
    }

    public int getSum() {
        return sum;
    }

    public int getCount() {
        return count;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public List<String> getLable() {
        return lable;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public void setLable(List<String> lable) {
        this.lable = lable;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }
}

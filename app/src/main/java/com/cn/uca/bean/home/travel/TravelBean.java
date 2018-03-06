package com.cn.uca.bean.home.travel;

/**
 * Created by asus on 2018/3/2.
 * 所有旅游实体类
 */

public class TravelBean {
    private int check_in_days;//住几晚
    private int journey_days;//玩几天
    private String product_name;//名称
    private String product_type_name;//项目类型
    private String purchase_quantity;//订购数
    private int tourism_id;//旅游id
    private String tourism_type_name;//旅游类型名称
    private String traffic_type_name;//交通类型
    private String compress_picture;//缩略图
    private String picture;//图片
    private double min_price;//最低价格
    private double score;//评分
    private int sign_up_end_days;

    public int getCheck_in_days() {
        return check_in_days;
    }

    public int getJourney_days() {
        return journey_days;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_type_name() {
        return product_type_name;
    }

    public String getPurchase_quantity() {
        return purchase_quantity;
    }

    public int getTourism_id() {
        return tourism_id;
    }

    public String getTourism_type_name() {
        return tourism_type_name;
    }

    public String getTraffic_type_name() {
        return traffic_type_name;
    }

    public String getCompress_picture() {
        return compress_picture;
    }

    public String getPicture() {
        return picture;
    }

    public double getMin_price() {
        return min_price;
    }

    public double getScore() {
        return score;
    }

    public int getSign_up_end_days() {
        return sign_up_end_days;
    }
}

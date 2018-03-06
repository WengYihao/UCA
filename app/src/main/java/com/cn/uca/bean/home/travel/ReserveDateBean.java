package com.cn.uca.bean.home.travel;

/**
 * Created by asus on 2018/3/5.
 */

public class ReserveDateBean {
    private String date;//日期
    private String day;//几号
    private double price;//售价
    private int count;//库存
    private String select;//选的是不是今天true:是ob:在服务日期内false:不在服务范围

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getSelect() {
        return select;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public double getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ReserveDateBean{" +
                "date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", select='" + select + '\'' +
                '}';
    }
}

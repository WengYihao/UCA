package com.cn.uca.bean.home.travel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2018/3/5.
 * 团期
 */

public class ScheduleBean implements Parcelable{
    private String schedule_id;// size:32 班期ID √ 24
    private String schedule_date;// yyyy-MM-dd 班期日期 √ 25
    private double person_price;// 成人售价（邮轮产品此字段值无效，具体价格在下方 √ 26
    private double person_peer_price;// 成人同行价（邮轮产品此字段值无效，具体价格在下方 √ 27
    private double child_price;// 儿童售价（邮轮产品此字段值无效，具体价格在下方 √ 28
    private double child_peer_price;// 儿童同行价（邮轮产品此字段值无效，具体价格在下方 √ 29
    private int stock_count;// 库存数（如果没有库存权限，则为0；邮轮产品此字段值无效，具体库存在下方 √ 30
    private String visa_end_date;// yyyy-MM-dd HH:mm:ss 出发日期对应的签证截止日期，格式：YYYY-MM-dd HH:mm:ss，没有签证信息的产品字段为空 × 31
    private int isCheck;

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public double getPerson_price() {
        return person_price;
    }

    public void setPerson_price(double person_price) {
        this.person_price = person_price;
    }

    public double getPerson_peer_price() {
        return person_peer_price;
    }

    public void setPerson_peer_price(double person_peer_price) {
        this.person_peer_price = person_peer_price;
    }

    public double getChild_price() {
        return child_price;
    }

    public void setChild_price(double child_price) {
        this.child_price = child_price;
    }

    public double getChild_peer_price() {
        return child_peer_price;
    }

    public void setChild_peer_price(double child_peer_price) {
        this.child_peer_price = child_peer_price;
    }

    public int getStock_count() {
        return stock_count;
    }

    public void setStock_count(int stock_count) {
        this.stock_count = stock_count;
    }

    public String getVisa_end_date() {
        return visa_end_date;
    }

    public void setVisa_end_date(String visa_end_date) {
        this.visa_end_date = visa_end_date;
    }

    public int isCheck() {
        return isCheck;
    }

    public void setCheck(int check) {
        isCheck = check;
    }

    public static final Parcelable.Creator<ScheduleBean> CREATOR = new Creator<ScheduleBean>() {
        @Override
        public ScheduleBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            ScheduleBean bean = new ScheduleBean();
            bean.setSchedule_id(source.readString());
            bean.setSchedule_date(source.readString());
            bean.setPerson_price(source.readDouble());
            bean.setPerson_peer_price(source.readDouble());
            bean.setChild_price(source.readDouble());
            bean.setChild_peer_price(source.readDouble());
            bean.setStock_count(source.readInt());
            bean.setVisa_end_date(source.readString());
            bean.setCheck(source.readInt());
            return bean;
        }
        @Override
        public ScheduleBean[] newArray(int size) {
            return new ScheduleBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(schedule_id);
        dest.writeString(schedule_date);
        dest.writeDouble(person_price);
        dest.writeDouble(person_peer_price);
        dest.writeDouble(child_price);
        dest.writeDouble(child_peer_price);
        dest.writeInt(stock_count);
        dest.writeString(visa_end_date);
        dest.writeInt(isCheck);
    }

    @Override
    public String toString() {
        return "ScheduleBean{" +
                "schedule_id='" + schedule_id + '\'' +
                ", schedule_date='" + schedule_date + '\'' +
                ", person_price=" + person_price +
                ", person_peer_price=" + person_peer_price +
                ", child_price=" + child_price +
                ", child_peer_price=" + child_peer_price +
                ", stock_count=" + stock_count +
                ", visa_end_date='" + visa_end_date + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}

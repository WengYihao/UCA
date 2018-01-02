package com.cn.uca.bean.home.samecityka;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/18.
 */

public class MyTicketCodeBean implements Parcelable{
    private int city_cafe_ticket_id;
    private int number;
    private String ticket_name;

    public int getCity_cafe_ticket_id() {
        return city_cafe_ticket_id;
    }

    public int getNumber() {
        return number;
    }

    public String getTicket_name() {
        return ticket_name;
    }

    public void setCity_cafe_ticket_id(int city_cafe_ticket_id) {
        this.city_cafe_ticket_id = city_cafe_ticket_id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTicket_name(String ticket_name) {
        this.ticket_name = ticket_name;
    }

    public static final Parcelable.Creator<MyTicketCodeBean> CREATOR = new Creator<MyTicketCodeBean>() {
        @Override
        public MyTicketCodeBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            MyTicketCodeBean bean = new MyTicketCodeBean();
            bean.setCity_cafe_ticket_id(source.readInt());
            bean.setNumber(source.readInt());
            bean.setTicket_name(source.readString());
            return bean;
        }
        @Override
        public MyTicketCodeBean[] newArray(int size) {
            return new MyTicketCodeBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(city_cafe_ticket_id);
        dest.writeInt(number);
        dest.writeString(ticket_name);
    }
}

package com.cn.uca.bean.home.samecityka;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/13.
 */

public class TicketBean implements Parcelable{
    private int city_cafe_ticket_id;
    private int surplus;
    private String ticket_name;
    private int limit_ticket;
    private String open_examine;
    private double price;

    public int getCity_cafe_ticket_id() {
        return city_cafe_ticket_id;
    }

    public int getSurplus() {
        return surplus;
    }

    public String getTicket_name() {
        return ticket_name;
    }

    public int getLimit_ticket() {
        return limit_ticket;
    }

    public String getOpen_examine() {
        return open_examine;
    }

    public double getPrice() {
        return price;
    }

    public void setCity_cafe_ticket_id(int city_cafe_ticket_id) {
        this.city_cafe_ticket_id = city_cafe_ticket_id;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    public void setTicket_name(String ticket_name) {
        this.ticket_name = ticket_name;
    }

    public void setLimit_ticket(int limit_ticket) {
        this.limit_ticket = limit_ticket;
    }

    public void setOpen_examine(String open_examine) {
        this.open_examine = open_examine;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TicketBean{" +
                "city_cafe_ticket_id=" + city_cafe_ticket_id +
                ", surplus=" + surplus +
                ", ticket_name='" + ticket_name + '\'' +
                ", limit_ticket=" + limit_ticket +
                ", open_examine='" + open_examine + '\'' +
                ", price=" + price +
                '}';
    }

    public static final Parcelable.Creator<TicketBean> CREATOR = new Creator<TicketBean>() {
        @Override
        public TicketBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            TicketBean bean = new TicketBean();
            bean.setCity_cafe_ticket_id(source.readInt());
            bean.setSurplus(source.readInt());
            bean.setTicket_name(source.readString());
            bean.setLimit_ticket(source.readInt());
            bean.setOpen_examine(source.readString());
            bean.setPrice(source.readDouble());
            return bean;
        }
        @Override
        public TicketBean[] newArray(int size) {
            return new TicketBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(city_cafe_ticket_id);
        dest.writeInt(surplus);
        dest.writeString(ticket_name);
        dest.writeInt(limit_ticket);
        dest.writeString(open_examine);
        dest.writeDouble(price);
    }
}

package com.cn.uca.bean.home.samecityka;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/14.
 */

public class SetTicketBean implements Parcelable{
    private String ticket_name;
    private String open_examine;
    private int sum_ticket;
    private int limit_ticket;
    private double price;

    public String getTicket_name() {
        return ticket_name;
    }

    public String getOpen_examine() {
        return open_examine;
    }

    public int getSum_ticket() {
        return sum_ticket;
    }

    public int getLimit_ticket() {
        return limit_ticket;
    }

    public double getPrice() {
        return price;
    }

    public void setTicket_name(String ticket_name) {
        this.ticket_name = ticket_name;
    }

    public void setOpen_examine(String open_examine) {
        this.open_examine = open_examine;
    }

    public void setSum_ticket(int sum_ticket) {
        this.sum_ticket = sum_ticket;
    }

    public void setLimit_ticket(int limit_ticket) {
        this.limit_ticket = limit_ticket;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static final Parcelable.Creator<SetTicketBean> CREATOR = new Creator<SetTicketBean>() {
        @Override
        public SetTicketBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            SetTicketBean bean = new SetTicketBean();
            bean.setTicket_name(source.readString());
            bean.setOpen_examine(source.readString());
            bean.setSum_ticket(source.readInt());
            bean.setLimit_ticket(source.readInt());
            bean.setPrice(source.readDouble());
            return bean;
        }
        @Override
        public SetTicketBean[] newArray(int size) {
            return new SetTicketBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ticket_name);
        dest.writeString(open_examine);
        dest.writeInt(sum_ticket);
        dest.writeInt(limit_ticket);
        dest.writeDouble(price);
    }
}

package com.cn.uca.bean.home.samecityka;

import android.os.Parcel;
import android.os.Parcelable;

import com.cn.uca.bean.yueka.PlacesBean;

/**
 * Created by asus on 2017/12/14.
 */

public class AddTicketBean implements Parcelable {
    private int city_cafe_ticket_id;
    private int number;

    public int getCity_cafe_ticket_id() {
        return city_cafe_ticket_id;
    }

    public int getNumber() {
        return number;
    }

    public void setCity_cafe_ticket_id(int city_cafe_ticket_id) {
        this.city_cafe_ticket_id = city_cafe_ticket_id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "AddTicketBean{" +
                "user_cafe_ticket_id=" + city_cafe_ticket_id +
                ", number=" + number +
                '}';
    }

    public static final Parcelable.Creator<AddTicketBean> CREATOR = new Creator<AddTicketBean>() {
        @Override
        public AddTicketBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            AddTicketBean bean = new AddTicketBean();
            bean.setCity_cafe_ticket_id(source.readInt());
            bean.setNumber(source.readInt());
            return bean;
        }
        @Override
        public AddTicketBean[] newArray(int size) {
            return new AddTicketBean[size];
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
    }
}

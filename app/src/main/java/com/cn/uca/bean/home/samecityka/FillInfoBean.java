package com.cn.uca.bean.home.samecityka;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/13.
 */

public class FillInfoBean implements Parcelable{
    private int cafe_fill_user_info_id;
    private String info_name;

    public int getCafe_fill_user_info_id() {
        return cafe_fill_user_info_id;
    }

    public String getInfo_name() {
        return info_name;
    }

    public void setCafe_fill_user_info_id(int cafe_fill_user_info_id) {
        this.cafe_fill_user_info_id = cafe_fill_user_info_id;
    }

    public void setInfo_name(String info_name) {
        this.info_name = info_name;
    }

    @Override
    public String toString() {
        return "FillInfoBean{" +
                "cafe_fill_user_info_id=" + cafe_fill_user_info_id +
                ", info_name='" + info_name + '\'' +
                '}';
    }

    public static final Parcelable.Creator<FillInfoBean> CREATOR = new Creator<FillInfoBean>() {
        @Override
        public FillInfoBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            FillInfoBean bean = new FillInfoBean();
            bean.setCafe_fill_user_info_id(source.readInt());
            bean.setInfo_name(source.readString());
            return bean;
        }
        @Override
        public FillInfoBean[] newArray(int size) {
            return new FillInfoBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cafe_fill_user_info_id);
        dest.writeString(info_name);
    }
}

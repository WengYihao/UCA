package com.cn.uca.bean.home.lvpai;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/20.
 */

public class getAddressBean implements Parcelable {
    private String gaode_code;
    private String address;

    public String getGaode_code() {
        return gaode_code;
    }

    public String getAddress() {
        return address;
    }

    public void setGaode_code(String gaode_code) {
        this.gaode_code = gaode_code;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static final Parcelable.Creator<getAddressBean> CREATOR = new Creator<getAddressBean>() {
        @Override
        public getAddressBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            getAddressBean bean = new getAddressBean();
            bean.setGaode_code(source.readString());
            bean.setAddress(source.readString());
            return bean;
        }
        @Override
        public getAddressBean[] newArray(int size) {
            return new getAddressBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gaode_code);
        dest.writeString(address);
    }
}

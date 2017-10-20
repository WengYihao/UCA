package com.cn.uca.bean.yueka;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/9/13.
 * 路线点
 */

public class PlacesBean implements Parcelable{
    private String departure_address;
    private String place_name;
    private int route_id;
    private double lng;
    private double lat;
    private int place_id;
    private int city_id;
    private int order;

    public String getDeparture_address() {
        return departure_address;
    }

    public String getPlace_name() {
        return place_name;
    }

    public int getRoute_id() {
        return route_id;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public int getPlace_id() {
        return place_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public int getOrder() {
        return order;
    }

    public void setDeparture_address(String departure_address) {
        this.departure_address = departure_address;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "PlacesBean{" +
                "departure_address='" + departure_address + '\'' +
                ", place_name='" + place_name + '\'' +
                ", route_id=" + route_id +
                ", lng=" + lng +
                ", lat=" + lat +
                ", place_id=" + place_id +
                ", city_id=" + city_id +
                ", order=" + order +
                '}';
    }

    /**
     * 必须实现Parcelable.Creator接口，否则在获取该类数据的时候会报错
     *
     * android.os.BadParcelableException:Parcelable protocol requires a
     * Parcelable.Creator object called CREATOR on class
     *
     * Parcelable.Creator接口实现了从Parcel容器读取本对象（Teacher）数据，并返回该对象给逻辑层使用
     *
     * Parcelable.Creator接口对象名必须为CREATOR,否则同样会报上述同样的错误；
     *
     * 在读取Parcel容器中的数据事，必须按成员变量声明的顺序来读取数据，不然会出现获取数据错误；
     *
     * 反序列化对象
     */
    public static final Parcelable.Creator<PlacesBean> CREATOR = new Creator<PlacesBean>() {

        @Override
        public PlacesBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            PlacesBean bean = new PlacesBean();
            bean.setDeparture_address(source.readString());
            bean.setPlace_name(source.readString());
            bean.setRoute_id(source.readInt());
            bean.setPlace_id(source.readInt());
            bean.setCity_id(source.readInt());
            bean.setOrder(source.readInt());
            bean.setLng(source.readDouble());
            bean.setLat(source.readDouble());

            return bean;
        }

        @Override
        public PlacesBean[] newArray(int size) {
            // TODO Auto-generated method stub
            return new PlacesBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(departure_address);
        parcel.writeString(place_name);
        parcel.writeInt(route_id);
        parcel.writeInt(place_id);
        parcel.writeInt(city_id);
        parcel.writeInt(order);
        parcel.writeDouble(lng);
        parcel.writeDouble(lat);

    }
}

package com.cn.uca.bean.yueka;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/9/18.
 */

public class YueKaLineBean {
    private int route_id;
    private int escort_id;
    private String route_name;
    private List<PlacesBean> places;

    public int getRoute_id() {
        return route_id;
    }

    public int getEscort_id() {
        return escort_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public List<PlacesBean> getPlaces() {
        return places;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public void setEscort_id(int escort_id) {
        this.escort_id = escort_id;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public void setPlaces(List<PlacesBean> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "YueKaLineBean{" +
                "route_id=" + route_id +
                ", escort_id=" + escort_id +
                ", route_name='" + route_name + '\'' +
                ", places=" + places +
                '}';
    }
}

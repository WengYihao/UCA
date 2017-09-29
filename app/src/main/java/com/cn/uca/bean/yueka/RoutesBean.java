package com.cn.uca.bean.yueka;

import java.util.List;

/**
 * Created by asus on 2017/9/28.
 */

public class RoutesBean {
    private List<PlacesBean> places;
    private int route_id;
    private String route_name;
    private int escort_id;

    public List<PlacesBean> getPlaces() {
        return places;
    }

    public int getRoute_id() {
        return route_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public int getEscort_id() {
        return escort_id;
    }
}

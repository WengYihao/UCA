package com.cn.uca.bean.home.footprint;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class CityDetailsBean {
    private int travel_age;
    private int pageCount;
    private int how_page;
    private int city_number;
    private List<ProvinceBean> footprintProvinces;
    private List<CityBean> footprintChinaCityRets;
    private int page;

    public int getTravel_age() {
        return travel_age;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getHow_page() {
        return how_page;
    }

    public int getCity_number() {
        return city_number;
    }

    public List<ProvinceBean> getFootprintProvinces() {
        return footprintProvinces;
    }

    public List<CityBean> getFootprintChinaCityRets() {
        return footprintChinaCityRets;
    }

    public int getPage() {
        return page;
    }
}

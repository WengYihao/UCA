package com.cn.uca.bean.home.footprint;

import java.util.List;

/**
 * Created by asus on 2017/11/9.
 */

public class CountryDetailsBean {
    private int travel_age;
    private int pageCount;
    private int country_number;
    private int how_page;
    private List<WorldCountryBean> footprintWorldCountrys;
    private int page;
    private List<CountryBean> travelCountrys;

    public int getTravel_age() {
        return travel_age;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getCountry_number() {
        return country_number;
    }

    public int getHow_page() {
        return how_page;
    }

    public List<WorldCountryBean> getFootprintWorldCountrys() {
        return footprintWorldCountrys;
    }

    public int getPage() {
        return page;
    }

    public List<CountryBean> getTravelCountrys() {
        return travelCountrys;
    }
}

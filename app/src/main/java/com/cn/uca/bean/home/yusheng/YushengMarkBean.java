package com.cn.uca.bean.home.yusheng;

import java.util.List;

/**
 * Created by asus on 2017/10/27.
 */

public class YushengMarkBean {
    private List<LifeHistoricalsBean> lifeHistoricals;
    private int pageCount;
    private int how_page;
    private int page;

    public List<LifeHistoricalsBean> getLifeHistoricals() {
        return lifeHistoricals;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getHow_page() {
        return how_page;
    }

    public int getPage() {
        return page;
    }
}

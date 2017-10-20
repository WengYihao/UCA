package com.cn.uca.bean.yueka;

import java.util.List;

/**
 * Created by asus on 2017/9/12.
 * 约咖
 */

public class YueKaBean {
    private int pageCount;
    private int page;
    private int how_page;
    private List<EscortRecordsBean> escortRecords;

    public int getPageCount() {
        return pageCount;
    }

    public int getPage() {
        return page;
    }

    public int getHow_page() {
        return how_page;
    }

    public List<EscortRecordsBean> getEscortRecords() {
        return escortRecords;
    }
}

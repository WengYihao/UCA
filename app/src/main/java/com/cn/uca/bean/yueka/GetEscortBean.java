package com.cn.uca.bean.yueka;

/**
 * Created by asus on 2017/9/12.
 */

public class GetEscortBean {
    private String account_token;
    private String gaode_code;
    private int page;
    private int pageCount;
    private String beg_time;
    private String end_time;

    public String getAccount_token() {
        return account_token;
    }

    public String getGaode_code() {
        return gaode_code;
    }

    public int getPage() {
        return page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getBeg_time() {
        return beg_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setAccount_token(String account_token) {
        this.account_token = account_token;
    }

    public void setGaode_code(String gaode_code) {
        this.gaode_code = gaode_code;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setBeg_time(String beg_time) {
        this.beg_time = beg_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}

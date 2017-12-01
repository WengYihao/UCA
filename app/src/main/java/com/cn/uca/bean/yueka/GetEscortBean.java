package com.cn.uca.bean.yueka;

/**
 * Created by asus on 2017/9/12.
 * 获取伴游
 */

public class GetEscortBean {
    private String account_token;
    private String gaode_code;
    private int page;
    private int pageCount;
    private String beg_time;
    private String end_time;
    private int sex_id;
    private String beg_age;
    private String end_age;
    private String label;

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

    public int getSex_id() {
        return sex_id;
    }

    public String getBeg_age() {
        return beg_age;
    }

    public String getEnd_age() {
        return end_age;
    }

    public String getLabel() {
        return label;
    }

    public void setSex_id(int sex_id) {
        this.sex_id = sex_id;
    }

    public void setBeg_age(String beg_age) {
        this.beg_age = beg_age;
    }

    public void setEnd_age(String end_age) {
        this.end_age = end_age;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

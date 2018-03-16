package com.cn.uca.bean.home.translate;

/**
 * Created by asus on 2018/3/12.
 */

public class HistoryBean {
    private String fromType;
    private String toType;
    private String src;
    private String dst;

    public String getFromType() {
        return fromType;
    }

    public String getToType() {
        return toType;
    }

    public String getSrc() {
        return src;
    }

    public String getDst() {
        return dst;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
}

package com.cn.uca.bean.home.lvpai;

/**
 * Created by asus on 2017/12/27.
 * 说明须知
 */

public class DescriptionBean {
    private String content;
    private int order;

    public String getContent() {
        return content;
    }

    public int getOrder() {
        return order;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "DescriptionBean{" +
                "content='" + content + '\'' +
                ", order=" + order +
                '}';
    }
}

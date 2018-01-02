package com.cn.uca.bean.home.lvpai;

/**
 * Created by asus on 2017/12/27.
 */

public class ServiceBean {
    private String content;
    private int order;
    private String img_url;

    public String getContent() {
        return content;
    }

    public int getOrder() {
        return order;
    }

    public String getImg_url() {
        return img_url;
    }

    @Override
    public String toString() {
        return "ServiceBean{" +
                "content='" + content + '\'' +
                ", order=" + order +
                ", img_url='" + img_url + '\'' +
                '}';
    }
}

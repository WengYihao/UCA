package com.cn.uca.bean.home.lvpai;

/**
 * Created by asus on 2017/12/27.
 */

public class PhotoBean {
    private String img_url;
    private int order;

    public String getImg_url() {
        return img_url;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "PhotoBean{" +
                "img_url='" + img_url + '\'' +
                ", order=" + order +
                '}';
    }
}

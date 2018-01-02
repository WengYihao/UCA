package com.cn.uca.bean.home.lvpai;

/**
 * Created by asus on 2017/12/27.
 */

public class DetailContentBean {
    private String type;
    private ServiceBean bean;

    public String getType() {
        return type;
    }

    public ServiceBean getBean() {
        return bean;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBean(ServiceBean bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "DetailContentBean{" +
                "type='" + type + '\'' +
                ", bean=" + bean +
                '}';
    }
}

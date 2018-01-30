package com.cn.uca.bean.user.order;

/**
 * Created by asus on 2018/1/24.
 */

public class OrderTypeBean {
    private String name;
    private boolean isCheck;

    public String getName() {
        return name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}

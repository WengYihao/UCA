package com.cn.uca.bean.user;

/**
 * Created by asus on 2017/8/31.
 * 身份认证状态
 */

public class UserIdentityState {

    private int bind_identity_state_id;
    private String bind_identity_state_name;

    public int getBind_identity_state_id() {
        return bind_identity_state_id;
    }

    public String getBind_identity_state_name() {
        return bind_identity_state_name;
    }

    public void setBind_identity_state_id(int bind_identity_state_id) {
        this.bind_identity_state_id = bind_identity_state_id;
    }

    public void setBind_identity_state_name(String bind_identity_state_name) {
        this.bind_identity_state_name = bind_identity_state_name;
    }
}

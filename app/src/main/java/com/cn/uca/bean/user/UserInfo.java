package com.cn.uca.bean.user;

/**
 * Created by asus on 2017/8/31.
 * 用户信息
 */

public class UserInfo {
    private Integer account_number_id;
    private int bind_identity_state_id;
    private String user_nick_name;
    private String user_head_portrait;
    private Integer sex_id;
    private String user_birth_date;

    public Integer getAccount_number_id() {
        return account_number_id;
    }

    public int getBind_identity_state_id() {
        return bind_identity_state_id;
    }

    public String getUser_nick_name() {
        return user_nick_name;
    }

    public String getUser_head_portrait() {
        return user_head_portrait;
    }

    public Integer getSex_id() {
        return sex_id;
    }

    public String getUser_birth_date() {
        return user_birth_date;
    }

    public void setAccount_number_id(Integer account_number_id) {
        this.account_number_id = account_number_id;
    }

    public void setBind_identity_state_id(int bind_identity_state_id) {
        this.bind_identity_state_id = bind_identity_state_id;
    }

    public void setUser_nick_name(String user_nick_name) {
        this.user_nick_name = user_nick_name;
    }

    public void setUser_head_portrait(String user_head_portrait) {
        this.user_head_portrait = user_head_portrait;
    }

    public void setSex_id(Integer sex_id) {
        this.sex_id = sex_id;
    }

    public void setUser_birth_date(String user_birth_date) {
        this.user_birth_date = user_birth_date;
    }
}

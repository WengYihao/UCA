package com.cn.uca.bean.user;

/**
 * Created by asus on 2017/9/4.
 */

public class RegisterBean {
    private String phone_number;
    private String code;
    private String encryption_password;
    private String registration_id;

    public String getPhone_number() {
        return phone_number;
    }

    public String getCode() {
        return code;
    }

    public String getEncryption_password() {
        return encryption_password;
    }

    public String getRegistration_id() {
        return registration_id;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setEncryption_password(String encryption_password) {
        this.encryption_password = encryption_password;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    @Override
    public String toString() {
        return "RegisterBean{" +
                "phone_number='" + phone_number + '\'' +
                ", code='" + code + '\'' +
                ", encryption_password='" + encryption_password + '\'' +
                ", registration_id='" + registration_id + '\'' +
                '}';
    }
}

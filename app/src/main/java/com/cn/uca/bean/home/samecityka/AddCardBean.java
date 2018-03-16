package com.cn.uca.bean.home.samecityka;

import java.io.File;
import java.io.InputStream;

/**
 * Created by asus on 2017/12/12.
 */

public class AddCardBean {
    private String account_token;
    private String time_stamp;
    private String sign;
    private String corporate_name;
    private String hand_phone;
    private  String introduce;
    private String learning_name;
    private String user_card_name;
    private int user_card_type_id;
    private String weixin;
    private File file;

    public String getAccount_token() {
        return account_token;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public String getSign() {
        return sign;
    }

    public String getCorporate_name() {
        return corporate_name;
    }

    public String getHand_phone() {
        return hand_phone;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getLearning_name() {
        return learning_name;
    }

    public String getUser_card_name() {
        return user_card_name;
    }

    public int getUser_card_type_id() {
        return user_card_type_id;
    }

    public String getWeixin() {
        return weixin;
    }

    public File getFile() {
        return file;
    }

    public void setAccount_token(String account_token) {
        this.account_token = account_token;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setCorporate_name(String corporate_name) {
        this.corporate_name = corporate_name;
    }

    public void setHand_phone(String hand_phone) {
        this.hand_phone = hand_phone;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setLearning_name(String learning_name) {
        this.learning_name = learning_name;
    }

    public void setUser_card_name(String user_card_name) {
        this.user_card_name = user_card_name;
    }

    public void setUser_card_type_id(int user_card_type_id) {
        this.user_card_type_id = user_card_type_id;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "AddCardBean{" +
                "account_token='" + account_token + '\'' +
                ", time_stamp='" + time_stamp + '\'' +
                ", sign='" + sign + '\'' +
                ", corporate_name='" + corporate_name + '\'' +
                ", hand_phone='" + hand_phone + '\'' +
                ", introduce='" + introduce + '\'' +
                ", learning_name='" + learning_name + '\'' +
                ", user_card_name='" + user_card_name + '\'' +
                ", user_card_type_id=" + user_card_type_id +
                ", weixin='" + weixin + '\'' +
                ", file=" + file +
                '}';
    }
}

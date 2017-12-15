package com.cn.uca.bean.home.samecityka;

/**
 * Created by asus on 2017/12/12.
 */

public class CardBean {
    private String hand_phone;
    private String weixin;
    private int user_card_id;
    private String introduce;
    private String user_head_portrait;
    private String learning_name;
    private String corporate_name;
    private String user_card_type_name;
    private int user_card_type_id;
    private String user_card_name;
    private boolean isCheck;

    public String getHand_phone() {
        return hand_phone;
    }

    public String getWeixin() {
        return weixin;
    }

    public int getUser_card_id() {
        return user_card_id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getUser_head_portrait() {
        return user_head_portrait;
    }

    public String getLearning_name() {
        return learning_name;
    }

    public String getCorporate_name() {
        return corporate_name;
    }

    public String getUser_card_type_name() {
        return user_card_type_name;
    }

    public int getUser_card_type_id() {
        return user_card_type_id;
    }

    public String getUser_card_name() {
        return user_card_name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}

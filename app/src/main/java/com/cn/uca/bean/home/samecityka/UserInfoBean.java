package com.cn.uca.bean.home.samecityka;

import java.util.List;

/**
 * Created by asus on 2018/1/9.
 */

public class UserInfoBean {
    private List<CollectionBean> cityCafes;
    private String corporate_name;
    private String hand_phone;
    private String head_portrait_url;
    private String introduce;
    private String user_card_name;
    private int user_card_type_id;
    private String user_card_type_name;
    private String weixin;

    public List<CollectionBean> getCityCafes() {
        return cityCafes;
    }

    public String getCorporate_name() {
        return corporate_name;
    }

    public String getHand_phone() {
        return hand_phone;
    }

    public String getHead_portrait_url() {
        return head_portrait_url;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getUser_card_name() {
        return user_card_name;
    }

    public int getUser_card_type_id() {
        return user_card_type_id;
    }

    public String getUser_card_type_name() {
        return user_card_type_name;
    }

    public String getWeixin() {
        return weixin;
    }
}

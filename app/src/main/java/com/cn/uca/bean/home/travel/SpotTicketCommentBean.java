package com.cn.uca.bean.home.travel;

import java.util.List;

/**
 * Created by asus on 2017/10/13.
 * 景点门票评论信息
 */

public class SpotTicketCommentBean {
    private String comment_time;
    private double score;
    private int account_number_id;
    private int scenic_spot_evaluate_id;
    private String comment;
    private String user_nick_name;
    private List<String> pictures;

    public String getComment_time() {
        return comment_time;
    }

    public double getScore() {
        return score;
    }

    public int getAccount_number_id() {
        return account_number_id;
    }

    public int getScenic_spot_evaluate_id() {
        return scenic_spot_evaluate_id;
    }

    public String getComment() {
        return comment;
    }

    public String getUser_nick_name() {
        return user_nick_name;
    }

    public List<String> getPictures() {
        return pictures;
    }
}

package com.cn.uca.bean.home.samecityka;

import java.io.InputStream;

/**
 * Created by asus on 2017/12/15.
 */

public class SendActionBean {
    private String account_token;
    private String time_stamp;
    private String sign;
    private boolean charge;
    private int activity_type_id;
    private String beg_time;
    private String end_time;
    private String labels;
    private boolean purchase_ticket;
    private String title;
    private String details;
    private int user_card_id;
    private InputStream cover;
    private String position;
    private String tickets;
    private String fill_infos;

    public String getAccount_token() {
        return account_token;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public String getSign() {
        return sign;
    }

    public boolean isCharge() {
        return charge;
    }

    public int getActivity_type_id() {
        return activity_type_id;
    }

    public String getBeg_time() {
        return beg_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getLabels() {
        return labels;
    }

    public boolean isPurchase_ticket() {
        return purchase_ticket;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public int getUser_card_id() {
        return user_card_id;
    }

    public InputStream getCover() {
        return cover;
    }

    public String getPosition() {
        return position;
    }

    public String getTickets() {
        return tickets;
    }

    public String getFill_infos() {
        return fill_infos;
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

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

    public void setActivity_type_id(int activity_type_id) {
        this.activity_type_id = activity_type_id;
    }

    public void setBeg_time(String beg_time) {
        this.beg_time = beg_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setPurchase_ticket(boolean purchase_ticket) {
        this.purchase_ticket = purchase_ticket;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setUser_card_id(int user_card_id) {
        this.user_card_id = user_card_id;
    }

    public void setCover(InputStream cover) {
        this.cover = cover;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public void setFill_infos(String fill_infos) {
        this.fill_infos = fill_infos;
    }

    @Override
    public String toString() {
        return "SendActionBean{" +
                "account_token='" + account_token + '\'' +
                ", time_stamp='" + time_stamp + '\'' +
                ", sign='" + sign + '\'' +
                ", charge=" + charge +
                ", activity_type_id=" + activity_type_id +
                ", beg_time='" + beg_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", labels='" + labels + '\'' +
                ", purchase_ticket=" + purchase_ticket +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", user_card_id=" + user_card_id +
                ", cover=" + cover +
                ", position='" + position + '\'' +
                ", tickets='" + tickets + '\'' +
                ", fill_infos='" + fill_infos + '\'' +
                '}';
    }
}

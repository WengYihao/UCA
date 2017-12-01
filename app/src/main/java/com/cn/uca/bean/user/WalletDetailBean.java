package com.cn.uca.bean.user;

/**
 * Created by asus on 2017/11/20.
 */

public class WalletDetailBean {
    private double price;
    private int gain_loss;
    private String transaction_time;
    private int purse_record_id;
    private String remarks;

    public double getPrice() {
        return price;
    }

    public int getGain_loss() {
        return gain_loss;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public int getPurse_record_id() {
        return purse_record_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setGain_loss(int gain_loss) {
        this.gain_loss = gain_loss;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public void setPurse_record_id(int purse_record_id) {
        this.purse_record_id = purse_record_id;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

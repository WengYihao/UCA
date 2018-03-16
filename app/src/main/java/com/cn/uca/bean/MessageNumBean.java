package com.cn.uca.bean;

/**
 * Created by asus on 2018/3/13.
 * 消息未读数
 */

public class MessageNumBean {

    private static MessageNumBean bean;

    private int cc_refund_ticket_examine;//同城咖退票审核
    private int cc_settlement;//同城咖结算
    private int cc_sign_examine;//同城咖报名审核
    private int e_agree_back;//游咖购买约咖
    private int e_back_request;//游咖已支付
    private int e_disagree_back;//游咖同意退单
    private int e_payment;//游咖不同意退单
    private int e_purchase;//游咖退单请求
    private int e_settlement;//结算
    private int eu_agree_back;//领咖同意购买
    private int eu_agree_purchase;//领咖不同意购买
    private int eu_back_request;//领咖退单请求
    private int eu_disagree_back;//领咖同意退单
    private int eu_disagree_purchase;//领咖不同意退单

    public static MessageNumBean getInstens(){
        if (bean == null){
            bean = new MessageNumBean();
        }
        return bean;
    }

    @Override
    public String toString() {
        return "MessageNumBean{" +
                "cc_refund_ticket_examine=" + cc_refund_ticket_examine +
                ", cc_settlement=" + cc_settlement +
                ", cc_sign_examine=" + cc_sign_examine +
                ", e_agree_back=" + e_agree_back +
                ", e_back_request=" + e_back_request +
                ", e_disagree_back=" + e_disagree_back +
                ", e_payment=" + e_payment +
                ", e_purchase=" + e_purchase +
                ", e_settlement=" + e_settlement +
                ", eu_agree_back=" + eu_agree_back +
                ", eu_agree_purchase=" + eu_agree_purchase +
                ", eu_back_request=" + eu_back_request +
                ", eu_disagree_back=" + eu_disagree_back +
                ", eu_disagree_purchase=" + eu_disagree_purchase +
                '}';
    }

    public int getCc_refund_ticket_examine() {
        return cc_refund_ticket_examine;
    }

    public int getCc_settlement() {
        return cc_settlement;
    }

    public int getCc_sign_examine() {
        return cc_sign_examine;
    }

    public int getE_agree_back() {
        return e_agree_back;
    }

    public int getE_back_request() {
        return e_back_request;
    }

    public int getE_disagree_back() {
        return e_disagree_back;
    }

    public int getE_payment() {
        return e_payment;
    }

    public int getE_purchase() {
        return e_purchase;
    }

    public int getE_settlement() {
        return e_settlement;
    }

    public int getEu_agree_back() {
        return eu_agree_back;
    }

    public int getEu_agree_purchase() {
        return eu_agree_purchase;
    }

    public int getEu_back_request() {
        return eu_back_request;
    }

    public int getEu_disagree_back() {
        return eu_disagree_back;
    }

    public int getEu_disagree_purchase() {
        return eu_disagree_purchase;
    }

    public void setCc_refund_ticket_examine(int cc_refund_ticket_examine) {
        this.cc_refund_ticket_examine = cc_refund_ticket_examine;
    }

    public void setCc_settlement(int cc_settlement) {
        this.cc_settlement = cc_settlement;
    }

    public void setCc_sign_examine(int cc_sign_examine) {
        this.cc_sign_examine = cc_sign_examine;
    }

    public void setE_agree_back(int e_agree_back) {
        this.e_agree_back = e_agree_back;
    }

    public void setE_back_request(int e_back_request) {
        this.e_back_request = e_back_request;
    }

    public void setE_disagree_back(int e_disagree_back) {
        this.e_disagree_back = e_disagree_back;
    }

    public void setE_payment(int e_payment) {
        this.e_payment = e_payment;
    }

    public void setE_purchase(int e_purchase) {
        this.e_purchase = e_purchase;
    }

    public void setE_settlement(int e_settlement) {
        this.e_settlement = e_settlement;
    }

    public void setEu_agree_back(int eu_agree_back) {
        this.eu_agree_back = eu_agree_back;
    }

    public void setEu_agree_purchase(int eu_agree_purchase) {
        this.eu_agree_purchase = eu_agree_purchase;
    }

    public void setEu_back_request(int eu_back_request) {
        this.eu_back_request = eu_back_request;
    }

    public void setEu_disagree_back(int eu_disagree_back) {
        this.eu_disagree_back = eu_disagree_back;
    }

    public void setEu_disagree_purchase(int eu_disagree_purchase) {
        this.eu_disagree_purchase = eu_disagree_purchase;
    }
}

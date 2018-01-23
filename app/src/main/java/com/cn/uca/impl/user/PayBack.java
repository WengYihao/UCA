package com.cn.uca.impl.user;

import android.view.View;

import com.cn.uca.view.PasswordInputView;

/**
 * Created by asus on 2018/1/18.
 */

public interface PayBack {
    void walletPay(String password);//钱包支付
    void otherPay(int payType);//微信、支付宝支付
}

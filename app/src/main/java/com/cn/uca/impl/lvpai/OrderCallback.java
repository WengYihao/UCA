package com.cn.uca.impl.lvpai;

import android.view.View;

/**
 * Created by asus on 2018/1/3.
 */

public interface OrderCallback {
    void backOrder(View v);//退单
    void settlement(View v);//结算
    void chat(View v);//聊天
}

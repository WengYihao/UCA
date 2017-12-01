package com.cn.uca.popupwindows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.impl.raider.SubmitClickListener;
import com.cn.uca.util.SystemUtil;

import java.util.Date;

/**
 * Created by asus on 2017/11/15.
 */

public class BuyRaiderPopupWindow {
    public PopupWindow popupWindow;
    private Context context;
    private View view;
    private SubmitClickListener listener;
    private String name,order;
    private double price;

    public BuyRaiderPopupWindow(Context context,View view,String name,String order,double price,SubmitClickListener listener){
        this.context = context;
        this.view = view;
        this.name = name;
        this.order = order;
        this.price = price;
        this.listener = listener;
        initWindow();
    }

    public void initWindow(){
        View window = LayoutInflater.from(context).inflate(R.layout.raider_dialog,null);
        TextView close = (TextView)window.findViewById(R.id.close);
        TextView name = (TextView)window.findViewById(R.id.name);
        TextView orderNumber = (TextView)window.findViewById(R.id.order_number);
        TextView price = (TextView)window.findViewById(R.id.price);
        TextView submit = (TextView)window.findViewById(R.id.submit);

        name.setText(this.name+"攻略");
        orderNumber.setText("订单编号:"+order);
        price.setText("￥"+(int) this.price);

        popupWindow = new PopupWindow(window, MyApplication.width- SystemUtil.dip2px(60),MyApplication.height/3);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickCall(popupWindow);
            }
        });
    }

    public void close(){
        popupWindow.dismiss();
    }
}

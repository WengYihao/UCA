package com.cn.uca.popupwindows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.TicketNameAdapter;
import com.cn.uca.bean.home.samecityka.SameCityKaOrderBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.raider.SubmitClickListener;
import com.cn.uca.util.SystemUtil;

/**
 * Created by asus on 2017/12/14.
 */

public class BuySameCityKaPopupWindow {
    public PopupWindow popupWindow;
    private Context context;
    private View view;
    private SubmitClickListener listener;
    private SameCityKaOrderBean bean;

    public BuySameCityKaPopupWindow(Context context,View view,SameCityKaOrderBean bean,SubmitClickListener listener){
        this.context = context;
        this.view = view;
        this.bean = bean;
        this.listener = listener;
        initWindow();
    }
    public void initWindow(){
        final View window = LayoutInflater.from(context).inflate(R.layout.samecityka_order_dialog,null);
        final ListView listView = (ListView)window.findViewById(R.id.listView);
        TicketNameAdapter adapter = new TicketNameAdapter(bean.getTicketsRets(),context);
        listView.setAdapter(adapter);
        TextView order = (TextView)window.findViewById(R.id.order);
        TextView price = (TextView)window.findViewById(R.id.price);
        TextView submit = (TextView)window.findViewById(R.id.submit);

        order.setText("订单编号"+bean.getOrder_number());
        price.setText("￥"+(int) bean.getPrice());
        popupWindow = new PopupWindow(window, MyApplication.width- SystemUtil.dip2px(60),MyApplication.height/3);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickCall(popupWindow);
            }
        });
    }
}

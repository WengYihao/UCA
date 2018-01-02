package com.cn.uca.popupwindows;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.TicketNameAdapter;
import com.cn.uca.bean.home.samecityka.SameCityKaOrderBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.raider.SubmitClickListener;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SystemUtil;

/**
 * Created by asus on 2017/12/14.
 */

public class BuySameCityKaPopupWindow {
    public PopupWindow popupWindow;
    private Activity activity;
    private View view;
    private SubmitClickListener listener;
    private SameCityKaOrderBean bean;

    public BuySameCityKaPopupWindow(Activity activity, View view, SameCityKaOrderBean bean, SubmitClickListener listener){
        this.activity = activity;
        this.view = view;
        this.bean = bean;
        this.listener = listener;
        initWindow();
    }
    public void initWindow(){
        final View window = LayoutInflater.from(activity).inflate(R.layout.samecityka_order_dialog,null);
        final ListView listView = (ListView)window.findViewById(R.id.listView);
        TicketNameAdapter adapter = new TicketNameAdapter(bean.getTicketsRets(),activity);
        listView.setAdapter(adapter);
        SetListView.setListViewHeightBasedOnChildren(listView);
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
        backgroundAlpha(0.5f);
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                backgroundAlpha(1f);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickCall(popupWindow);
            }
        });
    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}

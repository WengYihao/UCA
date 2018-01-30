package com.cn.uca.popupwindows;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.yueka.SearchCallBack;
import com.cn.uca.ui.view.util.CityActivity;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.PickerScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/13.
 */

public class LvpaiSearchPopupWindow {
    private PopupWindow popupWindow;
    private Context context;
    private View view;
    private SearchCallBack searchCallBack;
    private int i = 1;

    public LvpaiSearchPopupWindow(Context context, View view, SearchCallBack searchCallBack){
        this.context = context;
        this.view = view;
        this.searchCallBack = searchCallBack;
        lvpaiSearchPopupWindow();
    }

    public  void lvpaiSearchPopupWindow(){
        View show = LayoutInflater.from(context).inflate(R.layout.lvpai_search_popupwindow,null);
        LinearLayout layout = (LinearLayout)show.findViewById(R.id.layout);
        final TextView text = (TextView)show.findViewById(R.id.text);
        TextView close = (TextView)show.findViewById(R.id.close);
        final TextView icon = (TextView)show.findViewById(R.id.icon);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i){
                    case 1:
                        text.setTextColor(context.getResources().getColor(R.color.ori));
                        icon.setBackgroundResource(R.mipmap.lvpai_ascending_back);
                        i = 2;
                        break;
                    case 2:
                        text.setTextColor(context.getResources().getColor(R.color.ori));
                        icon.setBackgroundResource(R.mipmap.lvpai_descending_back);
                        i = 3;
                        break;
                    case 3:
                        text.setTextColor(context.getResources().getColor(R.color.white));
                        icon.setBackgroundResource(R.mipmap.lvpai_default_back);
                        i = 1;
                        break;
                }
            }
        });
        TextView location = (TextView)show.findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CityActivity.class));
            }
        });
        TextView config = (TextView)show.findViewById(R.id.config);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastXutil.show("搜索完成");
                popupWindow.dismiss();
            }
        });
        layout.getBackground().setAlpha(220);
        popupWindow = new PopupWindow(show, MyApplication.width*5/7,MyApplication.height- SystemUtil.dip2px(65));
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
        popupWindow.showAtLocation(view, Gravity.RIGHT|Gravity.BOTTOM,0,0);
//        popupWindow.showAsDropDown(view);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
}

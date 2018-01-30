package com.cn.uca.popupwindows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.yueka.SearchCallBack;
import com.cn.uca.view.PickerScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/13.
 */

public class SameCityKaSearchPopupWindow {
    private PopupWindow popupWindow;
    private Context context;
    private View view;
    private SearchCallBack searchCallBack;

    public SameCityKaSearchPopupWindow(Context context, View view, SearchCallBack searchCallBack){
        this.context = context;
        this.view = view;
        this.searchCallBack = searchCallBack;
        yuekaSearchPopupwindow();
    }

    public  void yuekaSearchPopupwindow(){
        View show = LayoutInflater.from(context).inflate(R.layout.samecityka_search_popupwindow,null);

        final TextView all = (TextView)show.findViewById(R.id.all);
        final TextView domestic = (TextView)show.findViewById(R.id.domestic);
        final TextView abroad = (TextView)show.findViewById(R.id.abroad);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.lvpai_search_lable_bg2);
                all.setTextColor(context.getResources().getColor(R.color.ori));
                domestic.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
                domestic.setTextColor(context.getResources().getColor(R.color.white));
                abroad.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
                abroad.setTextColor(context.getResources().getColor(R.color.white));
            }
        });
        domestic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                domestic.setBackgroundResource(R.drawable.lvpai_search_lable_bg2);
                domestic.setTextColor(context.getResources().getColor(R.color.ori));
                all.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
                all.setTextColor(context.getResources().getColor(R.color.white));
                abroad.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
                abroad.setTextColor(context.getResources().getColor(R.color.white));
            }
        });
        abroad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abroad.setBackgroundResource(R.drawable.lvpai_search_lable_bg2);
                abroad.setTextColor(context.getResources().getColor(R.color.ori));
                domestic.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
                domestic.setTextColor(context.getResources().getColor(R.color.white));
                all.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
                all.setTextColor(context.getResources().getColor(R.color.white));
            }
        });
        popupWindow = new PopupWindow(show, MyApplication.width,MyApplication.height/5);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
        popupWindow.showAsDropDown(view);
    }
}

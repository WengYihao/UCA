package com.cn.uca.popupwindows;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.LableBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.SearchCallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.CityActivity;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.FluidLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/11/13.
 */

public class LvpaiSearchPopupWindow2 {
    private PopupWindow popupWindow;
    private Context context;
    private View view;
    private SearchCallBack searchCallBack;
    private int pc = 1;
    private int ti = 1;
    private List<LableBean> listLable;
    private List<String> selectLable;

    public LvpaiSearchPopupWindow2(Context context, View view, List<LableBean> list,SearchCallBack searchCallBack){
        this.context = context;
        this.view = view;
        this.listLable = list;
        this.searchCallBack = searchCallBack;
        lvpaiSearchPopupWindow();
    }

    public  void lvpaiSearchPopupWindow(){
        selectLable = new ArrayList<>();
        View show = LayoutInflater.from(context).inflate(R.layout.lvpai_search_popupwindow2,null);
        LinearLayout layout = (LinearLayout)show.findViewById(R.id.layout);
        FluidLayout lable = (FluidLayout)show.findViewById(R.id.lable);
        genTag(listLable,lable);
        final TextView all = (TextView)show.findViewById(R.id.all);
        final TextView domestic = (TextView)show.findViewById(R.id.domestic);
        final TextView abroad = (TextView)show.findViewById(R.id.abroad);
        final TextView price = (TextView)show.findViewById(R.id.price);
        final TextView icon1 = (TextView)show.findViewById(R.id.icon1);
        final TextView time = (TextView)show.findViewById(R.id.time);
        final TextView icon2 = (TextView)show.findViewById(R.id.icon2);

        TextView close = (TextView)show.findViewById(R.id.close);
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
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pc){
                    case 1:
                        price.setTextColor(context.getResources().getColor(R.color.ori));
                        icon1.setBackgroundResource(R.mipmap.lvpai_ascending_back);
                        pc = 2;

                        time.setTextColor(context.getResources().getColor(R.color.white));
                        icon2.setBackgroundResource(R.mipmap.lvpai_default_back);
                        ti = 1;
                        break;
                    case 2:
                        price.setTextColor(context.getResources().getColor(R.color.ori));
                        icon1.setBackgroundResource(R.mipmap.lvpai_descending_back);
                        pc = 3;

                        time.setTextColor(context.getResources().getColor(R.color.white));
                        icon2.setBackgroundResource(R.mipmap.lvpai_default_back);
                        ti = 1;
                        break;
                    case 3:
                        price.setTextColor(context.getResources().getColor(R.color.white));
                        icon1.setBackgroundResource(R.mipmap.lvpai_default_back);
                        pc = 1;

                        time.setTextColor(context.getResources().getColor(R.color.white));
                        icon2.setBackgroundResource(R.mipmap.lvpai_default_back);
                        ti = 1;
                        break;
                }
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ti){
                    case 1:
                        time.setTextColor(context.getResources().getColor(R.color.ori));
                        icon2.setBackgroundResource(R.mipmap.lvpai_ascending_back);
                        ti = 2;

                        price.setTextColor(context.getResources().getColor(R.color.white));
                        icon1.setBackgroundResource(R.mipmap.lvpai_default_back);
                        pc = 1;
                        break;
                    case 2:
                        time.setTextColor(context.getResources().getColor(R.color.ori));
                        icon2.setBackgroundResource(R.mipmap.lvpai_descending_back);
                        ti = 3;

                        price.setTextColor(context.getResources().getColor(R.color.white));
                        icon1.setBackgroundResource(R.mipmap.lvpai_default_back);
                        pc = 1;
                        break;
                    case 3:
                        time.setTextColor(context.getResources().getColor(R.color.white));
                        icon2.setBackgroundResource(R.mipmap.lvpai_default_back);
                        ti = 1;

                        price.setTextColor(context.getResources().getColor(R.color.white));
                        icon1.setBackgroundResource(R.mipmap.lvpai_default_back);
                        pc = 1;
                        break;
                }
            }
        });
//        TextView location = (TextView)show.findViewById(R.id.location);
//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                context.startActivity(new Intent(context, CityActivity.class));
//            }
//        });
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
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void genTag(final List<LableBean> list, FluidLayout fluidLayout) {
        fluidLayout.removeAllViews();
        fluidLayout.setGravity(Gravity.TOP);
        for (int i = 0; i < list.size(); i++) {
            final TextView tv = new TextView(context);
            tv.setText(list.get(i).getStyle_label_name());
            tv.setTextSize(12);
            tv.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
            tv.setTextColor(context.getResources().getColor(R.color.white));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv.setBackgroundResource(R.drawable.lvpai_search_lable_bg2);
                    tv.setTextColor(context.getResources().getColor(R.color.ori));
                    if (selectLable.size() > 0) {
                        Log.e("456", "1111" + "--" + selectLable.size());
                        for (int i = 0; i < selectLable.size(); i++) {
                            Log.e("456", "5555555555555");
                            if (tv.getText().toString().equals(selectLable.get(i))) {
                                Log.e("456", "2222" + tv.getText() + "--" + selectLable.get(i) + "---" + i);
                                tv.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
                                tv.setTextColor(context.getResources().getColor(R.color.white));
                                selectLable.remove(i);
                            } else {
                                selectLable.add(tv.getText().toString());
                            }
                        }
                    }else{
                        selectLable.add(tv.getText().toString());
                    }

                }
            });
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 12, 20, 12);
            fluidLayout.addView(tv,params);
        }
    }
}

package com.cn.uca.popupwindows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.LableBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.yueka.SearchCallBack;
import com.cn.uca.view.FluidLayout;

import java.util.List;

/**
 * Created by asus on 2017/11/13.
 */

public class SameCityKaSearchPopupWindow2 {
    private PopupWindow popupWindow;
    private Context context;
    private View view;
    private SearchCallBack searchCallBack;
    private List<String> listLable;
    private List<String> selectLable;

    public SameCityKaSearchPopupWindow2(Context context, View view,  List<String> list,SearchCallBack searchCallBack){
        this.context = context;
        this.view = view;
        this.searchCallBack = searchCallBack;
        this.listLable = list;
        yuekaSearchPopupwindow();
    }

    public  void yuekaSearchPopupwindow(){
        View show = LayoutInflater.from(context).inflate(R.layout.samecityka_search_popupwindow2,null);

        FluidLayout lable = (FluidLayout)show.findViewById(R.id.lable);
        genTag(listLable,lable);
        popupWindow = new PopupWindow(show, MyApplication.width,MyApplication.height/5);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
        popupWindow.showAsDropDown(view);
    }

    private void genTag(final List<String> list, FluidLayout fluidLayout) {
        fluidLayout.removeAllViews();
        fluidLayout.setGravity(Gravity.TOP);
        for (int i = 0; i < list.size(); i++) {
            final TextView tv = new TextView(context);
            tv.setText(list.get(i));
            tv.setTextSize(12);
            tv.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
            tv.setTextColor(context.getResources().getColor(R.color.white));
//            tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tv.setBackgroundResource(R.drawable.lvpai_search_lable_bg2);
//                    tv.setTextColor(context.getResources().getColor(R.color.ori));
//                    if (selectLable.size() > 0) {
//                        Log.e("456", "1111" + "--" + selectLable.size());
//                        for (int i = 0; i < selectLable.size(); i++) {
//                            Log.e("456", "5555555555555");
//                            if (tv.getText().toString().equals(selectLable.get(i))) {
//                                Log.e("456", "2222" + tv.getText() + "--" + selectLable.get(i) + "---" + i);
//                                tv.setBackgroundResource(R.drawable.lvpai_search_lable_bg);
//                                tv.setTextColor(context.getResources().getColor(R.color.white));
//                                selectLable.remove(i);
//                            } else {
//                                selectLable.add(tv.getText().toString());
//                            }
//                        }
//                    }else{
//                        selectLable.add(tv.getText().toString());
//                    }
//
//                }
//            });
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 12, 20, 12);
            fluidLayout.addView(tv,params);
        }
    }
}

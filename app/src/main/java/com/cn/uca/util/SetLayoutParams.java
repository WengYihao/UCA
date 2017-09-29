package com.cn.uca.util;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cn.uca.config.MyApplication;

/**
 * Created by asus on 2017/9/27.
 */

public class SetLayoutParams {
    /**
     * 设置LinearLayout布局中控件的宽高
     * @param view
     * @param width
     * @param height
     */
    public static void setLinearLayout (View view,int width, int height){
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams();
        linearParams.height = height;
        linearParams.width = width;
       view.setLayoutParams(linearParams);
    }

    /**
     * 设置RelativeLayout布局中控件的宽高
     * @param view
     * @param width
     * @param height
     */
    public static void setRelativeLayout (View view,int width, int height){
        RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) view.getLayoutParams();
        linearParams.height = height;
        linearParams.width = width;
        view.setLayoutParams(linearParams);
    }
}

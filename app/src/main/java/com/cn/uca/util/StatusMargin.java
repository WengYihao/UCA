package com.cn.uca.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by asus on 2017/9/12.
 * 错开状态栏高度
 */

public class StatusMargin {
    //设置RelativeLayout的布局距离
    public static void setRelativeLayout(Activity activity, View view){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, SystemUtil.getStatusHeight(activity), 0, 0);
        view.setLayoutParams(params);
    }
    //设置LinearLayout布局顶部距离
    public static void setLinearLayout(Activity activity, View view){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, SystemUtil.getStatusHeight(activity), 0, 0);
        view.setLayoutParams(params);
    }
    //设置RelativeLayout布局顶部距离
    public static void setRelativeLayoutTop(Activity activity, View view,int num){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, num, 0, 0);
        view.setLayoutParams(params);
    }
    //设置RelativeLayout布局顶部、左边距离
    public static void setRelativeLayout(Activity activity, View view,int num){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, SystemUtil.getStatusHeight(activity), SystemUtil.dip2px(num), 0);
        view.setLayoutParams(params);
    }
    //设置RelativeLayout的布局距离
//    public static void setLinearLayoutBottom(Activity activity, View view,int num){
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
//        params.setMargins(0, 0, 0, num);
//        view.setLayoutParams(params);
//    }

    //设置FrameLayout的布局距离
    public static void setFrameLayoutBottom(Activity activity, View view,int num){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, 0, 0, num);
        view.setLayoutParams(params);
    }

    //设置FrameLayout的布局距离
    public static void setRelativeLayoutBottom(Activity activity, View view,int num){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, 0, 0, num);
        view.setLayoutParams(params);
    }
    /**
     * 设置顶部距离
     * @param view
     * @param num
     */
    public static void setTop(View view,int num){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, num, 0, 0);
        view.setLayoutParams(params);
    }
}

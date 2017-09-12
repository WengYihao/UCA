package com.cn.uca.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by asus on 2017/9/12.
 */

public class StatusMargin {
    //设置RelativeLayout的布局距离
    public static void setRelativeLayout(Activity activity, View view){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, SystemUtil.getStatusHeight(activity), 0, 0);
        view.setLayoutParams(params);
    }
    //设置LinearLayout的布局距离
    public static void setLinearLayout(Activity activity, View view){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, SystemUtil.getStatusHeight(activity), 0, 0);
        view.setLayoutParams(params);
    }
}

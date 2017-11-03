package com.cn.uca.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
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
        if (width == 0){
            linearParams.width = linearParams.WRAP_CONTENT;
        }else{
            linearParams.width = width;
        }
        if (height == 0){
            linearParams.height = linearParams.WRAP_CONTENT;
        }else {
            linearParams.height = height;
        }
       view.setLayoutParams(linearParams);
    }
    public static void setAbsListView (View view,int width, int height){
        AbsListView.LayoutParams linearParams =(AbsListView.LayoutParams) view.getLayoutParams();
        if (width == 0){
            linearParams.width = linearParams.WRAP_CONTENT;
        }else{
            linearParams.width = width;
        }
        if (height == 0){
            linearParams.height = linearParams.WRAP_CONTENT;
        }else {
            linearParams.height = height;
        }
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
        if (width == 0){
            linearParams.width = linearParams.WRAP_CONTENT;
        }else{
            linearParams.width = width;
        }
        if (height == 0){
            linearParams.height = linearParams.WRAP_CONTENT;
        }else {
            linearParams.height = height;
        }
        view.setLayoutParams(linearParams);
    }
    /**
     * 设置FrameLayout布局中控件的宽高
     * @param view
     * @param width
     * @param height
     */
    public static void setFrameLayout (View view,int width, int height){
        FrameLayout.LayoutParams linearParams =(FrameLayout.LayoutParams) view.getLayoutParams();
        if (width == 0){
            linearParams.width = linearParams.WRAP_CONTENT;
        }else{
            linearParams.width = width;
        }
        if (height == 0){
            linearParams.height = linearParams.WRAP_CONTENT;
        }else {
            linearParams.height = height;
        }
        view.setLayoutParams(linearParams);
    }
}

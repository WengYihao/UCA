package com.cn.uca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by asus on 2017/9/8.
 */

public class TouchWebView extends WebView {
    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TouchWebView(Context context) {
        super(context);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }

}

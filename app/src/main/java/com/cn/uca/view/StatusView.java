package com.cn.uca.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.util.SystemUtil;

/**
 * Created by asus on 2017/9/12.
 * 添加状态栏的自定义布局
 */

public class StatusView extends LinearLayout {

    private TextView stateTitle;

    public StatusView(Context context) {
        super(context);
    }

    public StatusView(Context context,AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.status_view,this);
        stateTitle = (TextView)findViewById(R.id.stateTitle);
        stateTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.getStatusHeight(context)));
    }
}

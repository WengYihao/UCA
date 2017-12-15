package com.cn.uca.view;
/**
 * 同城咖底部标题栏
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;

public class BottomBar extends LinearLayout {

    private Context mContext;

    private RelativeLayout mFirst_bottom, mSecond_bottom;
    private FrameLayout mCenter_bottom;
    private TextView tab_text1,tab_text2;
    private OnBottonbarClick mOnBottonbarClick;

    public BottomBar(Context context) {
        super(context);
        init(context);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_bottom, this, true);
        //获取控件id
        initId();
        onBottomBarClick();
    }



    private void initId() {

        mFirst_bottom = (RelativeLayout) findViewById(R.id.first);
        mSecond_bottom = (RelativeLayout) findViewById(R.id.second);
        mCenter_bottom = (FrameLayout) findViewById(R.id.center);

        tab_text1 = (TextView)findViewById(R.id.tab_text1);
        tab_text2 = (TextView)findViewById(R.id.tab_text2);

    }

    /**
     * 底部按钮点击监听器
     */
    private void onBottomBarClick() {

        mFirst_bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBottonbarClick != null) {
                    mOnBottonbarClick.onFirstClick();
                    tab_text1.setTextColor(getResources().getColor(R.color.ori));
                    tab_text2.setTextColor(getResources().getColor(R.color.grey3));
                }
            }
        });
        mSecond_bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBottonbarClick != null) {
                    mOnBottonbarClick.onSecondClick();
                    tab_text1.setTextColor(getResources().getColor(R.color.grey3));
                    tab_text2.setTextColor(getResources().getColor(R.color.ori));
                }
            }
        });
        mCenter_bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBottonbarClick != null) {
                    mOnBottonbarClick.onCenterClick();
                }
            }
        });

    }
    public void setOnBottombarOnclick(OnBottonbarClick onBottonbarClick) {

        mOnBottonbarClick = onBottonbarClick;
    }

    public interface OnBottonbarClick {
        void onFirstClick();

        void onSecondClick();

        void onCenterClick();
    }
}

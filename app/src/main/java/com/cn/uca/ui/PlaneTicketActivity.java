package com.cn.uca.ui;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.OneWayFragment;
import com.cn.uca.ui.fragment.TwoWayFragment;
import com.jaeger.library.StatusBarUtil;

public class PlaneTicketActivity extends FragmentActivity implements View.OnClickListener{

    private TextView view;
    private ImageView img;//广告图片
    private RadioButton oneWay,twoWay;
    private OneWayFragment oneWayFragment;
    private TwoWayFragment twoWayFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_ticket);

        StatusBarUtil.setTranslucentForImageView(this, 0, view);//设置状态栏透明

        initView();
    }

    private void initView() {
        img = (ImageView)findViewById(R.id.img);
        view = (TextView)findViewById(R.id.view);
        oneWay = (RadioButton)findViewById(R.id.oneWay);
        twoWay = (RadioButton)findViewById(R.id.twoWay);

        oneWay.setOnClickListener(this);
        twoWay.setOnClickListener(this);

        oneWay.setChecked(true);
        show(0);
    }
    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (index) {
            case 0:
                if (oneWayFragment == null) {
                    oneWayFragment = new OneWayFragment();
                    fragmentTransaction.add(R.id.container, oneWayFragment);
                }
                fragmentTransaction.show(oneWayFragment);
                oneWay.setBackgroundResource(R.drawable.shape_white_bg);
                twoWay.setBackgroundResource(R.drawable.shape_gray_bg);
                break;
            case 1:
                if (twoWayFragment == null) {
                    twoWayFragment = new TwoWayFragment();
                    fragmentTransaction.add(R.id.container, twoWayFragment);
                }
                fragmentTransaction.show(twoWayFragment);
                oneWay.setBackgroundResource(R.drawable.shape_gray_bg);
                twoWay.setBackgroundResource(R.drawable.shape_white_bg);
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(oneWayFragment);
                break;
            case 1:
                fragmentTransaction.hide(twoWayFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.oneWay:
                show(0);
                break;
            case R.id.twoWay:
                show(1);
                break;
        }
    }
}

package com.cn.uca.ui.view.user;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.ui.fragment.user.OrderFragment;

public class OrderActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,title01,title02,title03,title04,title05,title06;
    private OrderFragment orderFragment01,orderFragment02,orderFragment03,orderFragment04,orderFragment05,orderFragment06;
    private FragmentTransaction  fragmentTransaction;
    private int currentIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        title03 = (TextView)findViewById(R.id.title03);
        title04 = (TextView)findViewById(R.id.title04);
        title05 = (TextView)findViewById(R.id.title05);
        title06 = (TextView)findViewById(R.id.title06);

        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
        title04.setOnClickListener(this);
        title05.setOnClickListener(this);
        title06.setOnClickListener(this);

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
                if (orderFragment01 == null) {
                    orderFragment01 = OrderFragment.newInstance("0");
                    fragmentTransaction.add(R.id.container, orderFragment01);
                }

                fragmentTransaction.show(orderFragment01);

                title01.setTextColor(getResources().getColor(R.color.ori));
                title01.setBackgroundResource(R.color.white);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.ori);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.ori);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.ori);

                title05.setTextColor(getResources().getColor(R.color.white));
                title05.setBackgroundResource(R.color.ori);

                title06.setTextColor(getResources().getColor(R.color.white));
                title06.setBackgroundResource(R.color.ori);

                break;
            case 1:
                if (orderFragment02 == null) {
                    orderFragment02 = OrderFragment.newInstance("1");
                    fragmentTransaction.add(R.id.container, orderFragment02);
                }

                fragmentTransaction.show(orderFragment02);

                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.ori);

                title02.setTextColor(getResources().getColor(R.color.ori));
                title02.setBackgroundResource(R.color.white);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.ori);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.ori);

                title05.setTextColor(getResources().getColor(R.color.white));
                title05.setBackgroundResource(R.color.ori);

                title06.setTextColor(getResources().getColor(R.color.white));
                title06.setBackgroundResource(R.color.ori);

                break;
            case 2:
                if (orderFragment03 == null) {
                    orderFragment03 = OrderFragment.newInstance("2");
                    fragmentTransaction.add(R.id.container, orderFragment03);
                }

                fragmentTransaction.show(orderFragment03);

                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.ori);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.ori);

                title03.setTextColor(getResources().getColor(R.color.ori));
                title03.setBackgroundResource(R.color.white);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.ori);

                title05.setTextColor(getResources().getColor(R.color.white));
                title05.setBackgroundResource(R.color.ori);

                title06.setTextColor(getResources().getColor(R.color.white));
                title06.setBackgroundResource(R.color.ori);

                break;
            case 3:
                if (orderFragment04 == null) {
                    orderFragment04 = OrderFragment.newInstance("3");
                    fragmentTransaction.add(R.id.container, orderFragment04);
                }

                fragmentTransaction.show(orderFragment04);
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.ori);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.ori);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.ori);

                title04.setTextColor(getResources().getColor(R.color.ori));
                title04.setBackgroundResource(R.color.white);

                title05.setTextColor(getResources().getColor(R.color.white));
                title05.setBackgroundResource(R.color.ori);

                title06.setTextColor(getResources().getColor(R.color.white));
                title06.setBackgroundResource(R.color.ori);

                break;

            case 4:
                if (orderFragment05 == null) {
                    orderFragment05 = OrderFragment.newInstance("4");
                    fragmentTransaction.add(R.id.container, orderFragment05);
                }

                fragmentTransaction.show(orderFragment05);
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.ori);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.ori);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.ori);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.ori);

                title05.setTextColor(getResources().getColor(R.color.ori));
                title05.setBackgroundResource(R.color.white);

                title06.setTextColor(getResources().getColor(R.color.white));
                title06.setBackgroundResource(R.color.ori);

                break;
            case 5:
                if (orderFragment06 == null) {
                    orderFragment06 = OrderFragment.newInstance("5");
                    fragmentTransaction.add(R.id.container, orderFragment06);
                }

                fragmentTransaction.show(orderFragment06);
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.ori);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.ori);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.ori);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.ori);

                title05.setTextColor(getResources().getColor(R.color.white));
                title05.setBackgroundResource(R.color.ori);

                title06.setTextColor(getResources().getColor(R.color.ori));
                title06.setBackgroundResource(R.color.white);

                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(orderFragment01);
                break;
            case 1:
                fragmentTransaction.hide(orderFragment02);
                break;
            case 2:
                fragmentTransaction.hide(orderFragment03);
                break;
            case 3:
                fragmentTransaction.hide(orderFragment04);
                break;
            case 4:
                fragmentTransaction.hide(orderFragment05);
                break;
            case 5:
                fragmentTransaction.hide(orderFragment06);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.title01:
                show(0);
                break;
            case R.id.title02:
                show(1);
                break;
            case R.id.title03:
                show(2);
                break;
            case R.id.title04:
                show(3);
                break;
            case R.id.title05:
                show(4);
                break;
            case R.id.title06:
                show(5);
                break;
        }
    }
}

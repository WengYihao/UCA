package com.cn.uca.ui.view.user;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.ui.fragment.user.OrderFragment;
import com.cn.uca.util.FitStateUI;

public class OrderActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,title01,title02,title03,title04;
    private OrderFragment orderFragment01,orderFragment02,orderFragment03,orderFragment04;
    private FragmentTransaction  fragmentTransaction;
    private int currentIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_order);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        title03 = (TextView)findViewById(R.id.title03);
        title04 = (TextView)findViewById(R.id.title04);

        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
        title04.setOnClickListener(this);

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
                    orderFragment01 = OrderFragment.newInstance("1");
                    fragmentTransaction.add(R.id.container, orderFragment01);
                }

                fragmentTransaction.show(orderFragment01);

                title01.setTextColor(getResources().getColor(R.color.black));
                title01.setBackgroundResource(R.color.white);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.grey2);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.grey2);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.grey2);
                break;
            case 1:
                if (orderFragment02 == null) {
                    orderFragment02 = OrderFragment.newInstance("2");
                    fragmentTransaction.add(R.id.container, orderFragment02);
                }

                fragmentTransaction.show(orderFragment02);

                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.grey2);

                title02.setTextColor(getResources().getColor(R.color.black));
                title02.setBackgroundResource(R.color.white);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.grey2);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.grey2);
                break;
            case 2:
                if (orderFragment03 == null) {
                    orderFragment03 = OrderFragment.newInstance("3");
                    fragmentTransaction.add(R.id.container, orderFragment03);
                }

                fragmentTransaction.show(orderFragment03);

                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.grey2);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.grey2);

                title03.setTextColor(getResources().getColor(R.color.black));
                title03.setBackgroundResource(R.color.white);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.grey2);
                break;
            case 3:
                if (orderFragment04 == null) {
                    orderFragment04 = OrderFragment.newInstance("4");
                    fragmentTransaction.add(R.id.container, orderFragment04);
                }

                fragmentTransaction.show(orderFragment04);
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.grey2);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.grey2);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.grey2);

                title04.setTextColor(getResources().getColor(R.color.black));
                title04.setBackgroundResource(R.color.white);
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
        }
    }
}

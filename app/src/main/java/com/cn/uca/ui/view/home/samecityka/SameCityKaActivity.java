package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.impl.ServiceBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.ui.fragment.home.samecityka.ActionSquareFragment;
import com.cn.uca.ui.fragment.home.samecityka.MyActionFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.BottomBar;

public class SameCityKaActivity extends BaseBackActivity implements ServiceBack{

    private ActionSquareFragment squareFragment;
    private MyActionFragment actionFragment;
    private BottomBar mBottomBar;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_city_ka);

        initView();
    }

    private void initView() {
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.setOnBottombarOnclick(new BottomBar.OnBottonbarClick() {
            @Override
            public void onFirstClick() {
              show(0);
            }

            @Override
            public void onSecondClick() {
                show(1);
            }

            @Override
            public void onCenterClick() {
                String str = "http://www.szyouka.com:8080/youkatravel/agreement/CityCafeProtocol.html";
                ShowPopupWindow.seviceWindow(getWindow().getDecorView(),SameCityKaActivity.this,str,SameCityKaActivity.this);
            }


        });
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
                if (squareFragment == null) {
                    squareFragment = new ActionSquareFragment();
                    fragmentTransaction.add(R.id.container, squareFragment);
                }
                fragmentTransaction.show(squareFragment);
                break;
            case 1:
                if (actionFragment == null) {
                    actionFragment = new MyActionFragment();
                    fragmentTransaction.add(R.id.container, actionFragment);
                }
                fragmentTransaction.show(actionFragment);
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(squareFragment);
                break;
            case 1:
                fragmentTransaction.hide(actionFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }

    @Override
    public void sure() {
        startActivity(new Intent(SameCityKaActivity.this ,SendActionActivity.class));
    }
}

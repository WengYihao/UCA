package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.home.samecityka.ActionSquareFragment;
import com.cn.uca.ui.fragment.home.samecityka.MyActionFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.BottomBar;

public class SameCityKaActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,search;
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
        back = (TextView)findViewById(R.id.back);
        search = (TextView)findViewById(R.id.search);

        back.setOnClickListener(this);
        search.setOnClickListener(this);
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
                startActivity(new Intent(SameCityKaActivity.this ,SendActionActivity.class));
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.search:
                startActivity(new Intent(SameCityKaActivity.this,ActionSearchActivity.class));
                break;
        }
    }
}

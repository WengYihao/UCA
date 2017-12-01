package com.cn.uca.ui.view.home.footprint;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.home.footprint.ChinaFragment;
import com.cn.uca.ui.fragment.home.footprint.WorldFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.StatusMargin;

public class FootPrintActivity extends BaseBackActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private RelativeLayout llTitle;
    private TextView back;
    private Switch select;
    private ChinaFragment chinaFragment;
    private WorldFragment worldFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print);

        initView();
    }

    private void initView() {
        llTitle = (RelativeLayout)findViewById(R.id.llTitle);
        back = (TextView)findViewById(R.id.back);
        select = (Switch)findViewById(R.id.select);
        StatusMargin.setLinearLayout(this,llTitle);
        back.setOnClickListener(this);
        select.setOnCheckedChangeListener(this);

        show(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            select.setText("世界");
            show(1);
        }else{
            select.setText("中国");
            show(0);
        }
    }

    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (index) {
            case 0:
                if (chinaFragment == null) {
                    chinaFragment = new ChinaFragment();
                    fragmentTransaction.add(R.id.container, chinaFragment);
                }
                fragmentTransaction.show(chinaFragment);
                break;
            case 1:
                if (worldFragment == null) {
                    worldFragment = new WorldFragment();
                    fragmentTransaction.add(R.id.container, worldFragment);
                }
                fragmentTransaction.show(worldFragment);
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(chinaFragment);
                break;
            case 1:
                fragmentTransaction.hide(worldFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }
}

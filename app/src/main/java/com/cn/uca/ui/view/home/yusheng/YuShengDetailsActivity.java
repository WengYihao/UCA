package com.cn.uca.ui.view.home.yusheng;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.home.yusheng.YuShengDayFragment;
import com.cn.uca.ui.fragment.home.yusheng.YuShengMarkFragment;
import com.cn.uca.ui.fragment.home.yusheng.YuShengMonthFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;

public class YuShengDetailsActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,day,month,mark;
    private TextView label,label1,label2;
    private YuShengDayFragment dayFragment;
    private YuShengMonthFragment monthFragment;
    private YuShengMarkFragment markFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_yu_sheng_details);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        day = (TextView)findViewById(R.id.day);
        month = (TextView)findViewById(R.id.month);
        mark = (TextView)findViewById(R.id.mark);

        label = (TextView)findViewById(R.id.lable);
        label1 = (TextView)findViewById(R.id.lable2);
        label2 = (TextView)findViewById(R.id.lable3);

        back.setOnClickListener(this);
        day.setOnClickListener(this);
        month.setOnClickListener(this);
        mark.setOnClickListener(this);

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
            if (dayFragment == null) {
                dayFragment = new YuShengDayFragment();
                fragmentTransaction.add(R.id.container, dayFragment);
            }
            fragmentTransaction.show(dayFragment);
            label.setVisibility(View.VISIBLE);
            label1.setVisibility(View.GONE);
            label2.setVisibility(View.GONE);
            day.setTextColor(getResources().getColor(R.color.ori));
            month.setTextColor(getResources().getColor(R.color.white));
            mark.setTextColor(getResources().getColor(R.color.white));
            break;
        case 1:
            if (monthFragment == null) {
                monthFragment = new YuShengMonthFragment();
                fragmentTransaction.add(R.id.container, monthFragment);
            }
            fragmentTransaction.show(monthFragment);
            label.setVisibility(View.GONE);
            label1.setVisibility(View.VISIBLE);
            label2.setVisibility(View.GONE);
            day.setTextColor(getResources().getColor(R.color.white));
            month.setTextColor(getResources().getColor(R.color.ori));
            mark.setTextColor(getResources().getColor(R.color.white));
            break;
        case 2:
            if (markFragment == null) {
                markFragment = new YuShengMarkFragment();
                fragmentTransaction.add(R.id.container, markFragment);
            }
            fragmentTransaction.show(markFragment);
            label.setVisibility(View.GONE);
            label1.setVisibility(View.GONE);
            label2.setVisibility(View.VISIBLE);
            day.setTextColor(getResources().getColor(R.color.white));
            month.setTextColor(getResources().getColor(R.color.white));
            mark.setTextColor(getResources().getColor(R.color.ori));
            break;
    }
    switch (currentIndex) {
        case 0:
            fragmentTransaction.hide(dayFragment);
            break;
        case 1:
            fragmentTransaction.hide(monthFragment);
            break;
        case 2:
            fragmentTransaction.hide(markFragment);
            break;
    }
    fragmentTransaction.commit();
    currentIndex = index;
}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.day:
                show(0);
                break;
            case R.id.month:
                show(1);
                break;
            case R.id.mark:
                show(2);
                break;
        }
    }

}

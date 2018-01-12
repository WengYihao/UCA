package com.cn.uca.ui.view.home.samecityka;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.ui.fragment.home.samecityka.ActionEnrolmentFragment;
import com.cn.uca.ui.fragment.home.samecityka.ActionFinishedFragment;
import com.cn.uca.ui.fragment.home.samecityka.ActionFragment;
import com.cn.uca.ui.fragment.home.samecityka.ActionHavingFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;

import java.util.ArrayList;

public class MyActionActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,title01,title02,title03,title04;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private ActionFragment actionFragment;
    private ActionFinishedFragment finishedFragment;
    private ActionHavingFragment havingFragment;
    private ActionEnrolmentFragment enrolmentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_action);

        initView();
        initFragment();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        title03 = (TextView)findViewById(R.id.title03);
        title04 = (TextView)findViewById(R.id.title04);
        mPager = (ViewPager)findViewById(R.id.viewPager) ;
        back.setOnClickListener(this);
        title01.setOnClickListener(new MyOnClickListener(0));
        title02.setOnClickListener(new MyOnClickListener(1));
        title03.setOnClickListener(new MyOnClickListener(2));
        title04.setOnClickListener(new MyOnClickListener(3));
        mPager .setOffscreenPageLimit(4);//viewpager缓存的界面数
        fragmentList = new ArrayList<>();
        setTabSelection(0);//设置默认显示的界面

    }

    private void initFragment() {
        actionFragment = new ActionFragment();
        enrolmentFragment = new ActionEnrolmentFragment();
        havingFragment = new ActionHavingFragment();
        finishedFragment = new ActionFinishedFragment();
        fragmentList.add(actionFragment);
        fragmentList.add(enrolmentFragment);
        fragmentList.add(havingFragment);
        fragmentList.add(finishedFragment);

        mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(onPageChangeListener);

    }
    public class MyOnClickListener implements View.OnClickListener{
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View v) {
            //通过ViewPager的方法setCurrentItem(positon), c此时ViewPager会切换到position所对应item
            mPager.setCurrentItem(index);
        }
    }
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            setTabSelection(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        switch (index) {
            case 0:
                title01.setTextColor(getResources().getColor(R.color.ori));
                break;
            case 1:
                title02.setTextColor(getResources().getColor(R.color.ori));
                break;
            case 2:
                title03.setTextColor(getResources().getColor(R.color.ori));
                break;
            case 3:
                title04.setTextColor(getResources().getColor(R.color.ori));
                break;
        }
    }
    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        title01.setTextColor(getResources().getColor(R.color.gray2));
        title02.setTextColor(getResources().getColor(R.color.gray2));
        title03.setTextColor(getResources().getColor(R.color.gray2));
        title04.setTextColor(getResources().getColor(R.color.gray2));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.title01:
                title01.setTextColor(getResources().getColor(R.color.ori));
                title02.setTextColor(getResources().getColor(R.color.grey));
                title03.setTextColor(getResources().getColor(R.color.grey));
                title04.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.title02:
                title01.setTextColor(getResources().getColor(R.color.grey));
                title02.setTextColor(getResources().getColor(R.color.ori));
                title03.setTextColor(getResources().getColor(R.color.grey));
                title04.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.title03:
                title01.setTextColor(getResources().getColor(R.color.grey));
                title02.setTextColor(getResources().getColor(R.color.grey));
                title03.setTextColor(getResources().getColor(R.color.ori));
                title04.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.title04:
                title01.setTextColor(getResources().getColor(R.color.grey));
                title02.setTextColor(getResources().getColor(R.color.grey));
                title03.setTextColor(getResources().getColor(R.color.grey));
                title04.setTextColor(getResources().getColor(R.color.ori));
                break;
        }
    }
}

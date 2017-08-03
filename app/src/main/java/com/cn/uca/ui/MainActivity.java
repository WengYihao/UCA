package com.cn.uca.ui;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.ui.fragment.HomeFragment;
import com.cn.uca.ui.fragment.UserFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private HomeFragment homeFragment;
    private UserFragment userFragment;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private View homeLayout;
    private View userLayout;

    private ImageView homeImage;
    private ImageView userImage;

    private TextView homeText;
    private TextView userText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        homeLayout = findViewById(R.id.home_layout);
        userLayout = findViewById(R.id.user_layout);

        homeImage = (ImageView) findViewById(R.id.home_image);
        userImage = (ImageView) findViewById(R.id.user_image);

        homeText = (TextView) findViewById(R.id.home_text);
        userText = (TextView) findViewById(R.id.user_text);

        mPager = (ViewPager) findViewById(R.id.content);

        homeLayout.setOnClickListener(this);
        userLayout.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        setTabSelection(0);//设置默认显示的界面
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(userFragment);

        mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList) {
        });
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(onPageChangeListener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_layout:
                setTabSelection(0);
                mPager.setCurrentItem(0);
                break;
            case R.id.user_layout:
                setTabSelection(1);
                mPager.setCurrentItem(1);
                break;
        }
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
                // 当点击了主页tab时，改变控件的图片和文字颜色
                homeImage.setImageResource(R.mipmap.home_select);
                homeText.setTextColor(Color.parseColor("#fa753c"));
                break;
            case 1:
                // 当点击了语言设置tab时，改变控件的图片和文字颜色
                userImage.setImageResource(R.mipmap.user_select);
                userText.setTextColor(Color.parseColor("#fa753c"));
                break;
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        homeImage.setImageResource(R.mipmap.home_normal);
        homeText.setTextColor(Color.parseColor("#82858b"));
        userImage.setImageResource(R.mipmap.user_normal);
        userText.setTextColor(Color.parseColor("#82858b"));
    }
}

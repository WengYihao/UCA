package com.cn.uca.ui.view.home.yusheng;

import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.fragment.home.yusheng.YuShengDayFragment;
import com.cn.uca.ui.fragment.home.yusheng.YuShengMarkFragment;
import com.cn.uca.ui.fragment.home.yusheng.YuShengMonthFragment;
import com.cn.uca.ui.view.MainActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class YuShengDetailsActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,day,month,mark;
    private TextView label;
    private YuShengDayFragment dayFragment;
    private YuShengMonthFragment monthFragment;
    private YuShengMarkFragment markFragment;
    private int lastIndex = 0;//声明蓝线滑动的距离
    private int position_one;
    private int position_two;
    private ViewPager mPager;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_yu_sheng_details);

        initView();
        initFragment();
        initWidth();
    }



    private void initView() {
        back = (TextView)findViewById(R.id.back);
        day = (TextView)findViewById(R.id.day);
        month = (TextView)findViewById(R.id.month);
        mark = (TextView)findViewById(R.id.mark);
        label = (TextView)findViewById(R.id.lable);

        mPager = (ViewPager) findViewById(R.id.container);

        back.setOnClickListener(this);
        day.setOnClickListener(this);
        month.setOnClickListener(this);
        mark.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        day.setOnClickListener(new MyOnClickListener(0));
        month.setOnClickListener(new MyOnClickListener(1));
        mark.setOnClickListener(new MyOnClickListener(2));

    }
    private void initFragment() {
        dayFragment = new YuShengDayFragment();
        monthFragment = new YuShengMonthFragment();
        markFragment = new YuShengMarkFragment();
        fragmentList.add(dayFragment);
        fragmentList.add(monthFragment);
        fragmentList.add(markFragment);

        mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(onPageChangeListener);
    }
    private void initWidth() {
        position_one = (MyApplication.width- SystemUtil.dip2px(10)) / 3;// 设置 横线 滑动 一个距离
        position_two = position_one * 2;// 滑动2个距离
        ViewGroup.LayoutParams para = label.getLayoutParams();// 得到一个组件 的属性 对象
        para.width = position_one;// 为这个组件设置宽度
        para.height = SystemUtil.dip2px(40);// 为这个组件设置高度
        label.setLayoutParams(para);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
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
            Animation animation = null;
            switch (position) {
                case 0:
                    if (lastIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        month.setTextColor(getResources().getColor(R.color.ori));
                        month.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    } else if (lastIndex == 2) {
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                        mark.setTextColor(getResources().getColor(R.color.ori));
                        mark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    day.setTextColor(getResources().getColor(R.color.white));
                    day.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    label.setText("天");
                    break;
                case 1:
                    if (lastIndex == 0) {
                        animation = new TranslateAnimation(0, position_one, 0, 0);
                        day.setTextColor(getResources().getColor(R.color.ori));
                        day.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    } else if (lastIndex == 2) {
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        mark.setTextColor(getResources().getColor(R.color.ori));
                        mark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    month.setTextColor(getResources().getColor(R.color.white));
                    month.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    label.setText("月");
                    break;
                case 2:
                    if (lastIndex == 0) {
                        animation = new TranslateAnimation(0, position_two, 0, 0);
                        day.setTextColor(getResources().getColor(R.color.ori));
                        day.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    } else if (lastIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        month.setTextColor(getResources().getColor(R.color.ori));
                        month.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    mark.setTextColor(getResources().getColor(R.color.white));
                    mark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    label.setText("痕迹");
                    break;
            }
            lastIndex = position;
            animation.setFillAfter(true);// 让绑定动画效果的组件 停留在 动画结束的位置
            animation.setDuration(200);
            label.startAnimation(animation);// 为组件绑定动画效果
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

}

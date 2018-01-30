package com.cn.uca.ui.view.home.raider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.adapter.LetterAdapter;
import com.cn.uca.adapter.home.raiders.RaidersAdapter;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.loading.LoadingLayout;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.fragment.home.raider.CityFragment;
import com.cn.uca.ui.fragment.home.raider.ProvinceFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaidersActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,province,city,collection;
    private ImageView iv_bottom_line;
    private ViewPager viewPager;
    private ProvinceFragment provinceFragment;
    private CityFragment cityFragment;
    private List<Fragment> fragmentList;
    private int position_one;////声明白线滑动的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raiders);

        initView();
        initFragment();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        province = (TextView)findViewById(R.id.province);
        city = (TextView)findViewById(R.id.city);
        collection = (TextView)findViewById(R.id.collection);

        back.setOnClickListener(this);
        province.setOnClickListener(new MyOnClickListener(0));
        city.setOnClickListener(new MyOnClickListener(1));
        collection.setOnClickListener(this);

        iv_bottom_line = (ImageView)findViewById(R.id.iv_bottom_line);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        position_one = SystemUtil.dip2px(60);
    }


    private void initFragment(){
        fragmentList = new ArrayList<>();
        provinceFragment = new ProvinceFragment();
        cityFragment = new CityFragment();
        fragmentList.add(provinceFragment);
        fragmentList.add(cityFragment);

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.collection:
                startActivity(new Intent(RaidersActivity.this,RaiderCollectionActivity.class));
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
            viewPager.setCurrentItem(index);
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                case 0:
                    animation = new TranslateAnimation(position_one, 0, 0, 0);
                    province.setTextColor(getResources().getColor(R.color.white));
                    city.setTextColor(getResources().getColor(R.color.ori2));
                    break;
                case 1:
                    animation = new TranslateAnimation(0, position_one, 0, 0);
                    province.setTextColor(getResources().getColor(R.color.ori2));
                    city.setTextColor(getResources().getColor(R.color.white));
                    break;
            }
            animation.setFillAfter(true);// 让绑定动画效果的组件 停留在 动画结束的位置
            animation.setDuration(200);
            iv_bottom_line.startAnimation(animation);// 为组件绑定动画效果
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}

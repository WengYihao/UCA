package com.cn.uca.ui.view.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.alipay.AliPayUtil;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.ui.fragment.user.order.HotelFragment;
import com.cn.uca.ui.fragment.user.order.LvPaiFragment;
import com.cn.uca.ui.fragment.user.order.PlantTicketFragment;
import com.cn.uca.ui.fragment.user.order.SameCityKaFragment;
import com.cn.uca.ui.fragment.user.order.TravelFragment;
import com.cn.uca.ui.fragment.user.order.YueKaFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,title01,title02,title03,title04,title05,title06;
    private LinearLayout layout;
    private ArrayList<Fragment> fragmentList;
    private PlantTicketFragment ticketFragment;
    private HotelFragment hotelFragment;
    private TravelFragment travelFragment;
    private SameCityKaFragment sameCityKaFragment;
    private YueKaFragment yueKaFragment;
    private LvPaiFragment lvPaiFragment;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
        initFragment();
        setTabSelection(0);//设置默认显示的界面
    }


    private void initFragment() {
        ticketFragment = new PlantTicketFragment();
        hotelFragment = new HotelFragment();
        travelFragment = new TravelFragment();
        sameCityKaFragment = new SameCityKaFragment();
        yueKaFragment =  new YueKaFragment();
        lvPaiFragment = new LvPaiFragment();
        fragmentList.add(ticketFragment);
        fragmentList.add(hotelFragment);
        fragmentList.add(travelFragment);
        fragmentList.add(sameCityKaFragment);
        fragmentList.add(yueKaFragment);
        fragmentList.add(lvPaiFragment);

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(onPageChangeListener);
    }

    private void initView() {
        fragmentList = new ArrayList<>();
        back = (TextView)findViewById(R.id.back);
        layout = (LinearLayout)findViewById(R.id.layout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        title03 = (TextView)findViewById(R.id.title03);
        title04 = (TextView)findViewById(R.id.title04);
        title05 = (TextView)findViewById(R.id.title05);
        title06 = (TextView)findViewById(R.id.title06);

//        layout.setBackgroundResource(R.drawable.gradient);

        back.setOnClickListener(this);
        title01.setOnClickListener(new MyOnClickListener(0));
        title02.setOnClickListener(new MyOnClickListener(1));
        title03.setOnClickListener(new MyOnClickListener(2));
        title04.setOnClickListener(new MyOnClickListener(3));
        title05.setOnClickListener(new MyOnClickListener(4));
        title06.setOnClickListener(new MyOnClickListener(5));
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
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        switch (index) {
            case 0:
                title01.setTextColor(getResources().getColor(R.color.ori));
                title01.setBackgroundResource(R.color.white);
                break;
            case 1:
                title02.setTextColor(getResources().getColor(R.color.ori));
                title02.setBackgroundResource(R.color.white);
                break;
            case 2:
                title03.setTextColor(getResources().getColor(R.color.ori));
                title03.setBackgroundResource(R.color.white);
                break;
            case 3:
                title04.setTextColor(getResources().getColor(R.color.ori));
                title04.setBackgroundResource(R.color.white);
                break;
            case 4:
                title05.setTextColor(getResources().getColor(R.color.ori));
                title05.setBackgroundResource(R.color.white);
                break;
            case 5:
                title06.setTextColor(getResources().getColor(R.color.ori));
                title06.setBackgroundResource(R.color.white);
                break;
        }
    }
    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        layout.setBackgroundResource(R.drawable.gradient);
        title01.setBackgroundResource(0);
        title02.setBackgroundResource(0);
        title03.setBackgroundResource(0);
        title04.setBackgroundResource(0);
        title05.setBackgroundResource(0);
        title06.setBackgroundResource(0);
        title01.setTextColor(getResources().getColor(R.color.white));
        title02.setTextColor(getResources().getColor(R.color.white));
        title03.setTextColor(getResources().getColor(R.color.white));
        title04.setTextColor(getResources().getColor(R.color.white));
        title05.setTextColor(getResources().getColor(R.color.white));
        title06.setTextColor(getResources().getColor(R.color.white));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
        }
    }
}

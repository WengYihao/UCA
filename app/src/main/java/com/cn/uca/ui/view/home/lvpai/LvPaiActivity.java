package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.ui.fragment.home.lvpai.CollectionFragment;
import com.cn.uca.ui.fragment.home.lvpai.LvPaiFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class LvPaiActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,merchant_entry,lvpai,collection;
    private ImageView iv_bottom_line;
    private ViewPager viewPager;
    private LvPaiFragment lvPaiFragment;
    private CollectionFragment collectionFragment;
    private List<Fragment> fragmentList;
    private int position_one;////声明白线滑动的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv_pai);

        initView();
        initFragment();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        merchant_entry = (TextView)findViewById(R.id.merchant_entry);
        lvpai = (TextView)findViewById(R.id.lvpai);
        collection = (TextView)findViewById(R.id.collection);
        iv_bottom_line = (ImageView)findViewById(R.id.iv_bottom_line);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        back.setOnClickListener(this);
        merchant_entry.setOnClickListener(this);
        lvpai.setOnClickListener(new MyOnClickListener(0));
        collection.setOnClickListener(new MyOnClickListener(1));


        position_one = SystemUtil.dip2px(60);
    }

    private void initFragment(){
        fragmentList = new ArrayList<>();
        lvPaiFragment = new LvPaiFragment();
        collectionFragment = new CollectionFragment();
        fragmentList.add(lvPaiFragment);
        fragmentList.add(collectionFragment);

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(onPageChangeListener);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.merchant_entry://商家入驻
                if (SharePreferenceXutil.isEnter()){
                    startActivity(new Intent(LvPaiActivity.this, MerchantManageActivity.class));
                }else{
                    startActivity(new Intent(LvPaiActivity.this,MerchantEntryActivity.class));
                }
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
                    lvpai.setTextColor(getResources().getColor(R.color.white));
                    collection.setTextColor(getResources().getColor(R.color.ori2));
                    break;
                case 1:
                    animation = new TranslateAnimation(0, position_one, 0, 0);
                    lvpai.setTextColor(getResources().getColor(R.color.ori2));
                    collection.setTextColor(getResources().getColor(R.color.white));
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

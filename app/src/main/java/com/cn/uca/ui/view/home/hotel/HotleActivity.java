package com.cn.uca.ui.view.home.hotel;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.home.FarmFragment;
import com.cn.uca.ui.fragment.home.HotleFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.StatusMargin;

/**
 * 周边游
 */
public class HotleActivity extends BaseBackActivity implements View.OnClickListener{

        private TextView view;
        private ImageView back,img;//广告图片
        private RadioButton hotel,farm;
        private HotleFragment hotleFragment;
        private FarmFragment farmFragment;
        private FragmentTransaction fragmentTransaction;
        private int currentIndex = -1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_hotel);

            initView();
        }

        private void initView() {
            back = (ImageView)findViewById(R.id.back);
            img = (ImageView)findViewById(R.id.img);
            view = (TextView)findViewById(R.id.view);
            hotel = (RadioButton)findViewById(R.id.hotel);
            farm = (RadioButton)findViewById(R.id.farm);

            back.setOnClickListener(this);
            hotel.setOnClickListener(this);
            farm.setOnClickListener(this);

            hotel.setChecked(true);
            show(0);
            StatusMargin.setRelativeLayout(this,back);
        }
        private void show(int index) {
            if (currentIndex == index) {
                return;
            }
            fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            switch (index) {
                case 0:
                    if (hotleFragment == null) {
                        hotleFragment = new HotleFragment();
                        fragmentTransaction.add(R.id.container, hotleFragment);
                    }
                    fragmentTransaction.show(hotleFragment);
                    hotel.setBackgroundResource(R.drawable.shape_white_left_bg);
                    hotel.setTextColor(getResources().getColor(R.color.ori));
                    farm.setBackgroundResource(R.drawable.shape_ori_right_bg);
                    farm.setTextColor(getResources().getColor(R.color.white));
                    farm.getBackground().setAlpha(120);
                    break;
                case 1:
                    if (farmFragment == null) {
                        farmFragment = new FarmFragment();
                        fragmentTransaction.add(R.id.container, farmFragment);
                    }
                    fragmentTransaction.show(farmFragment);
                    hotel.setBackgroundResource(R.drawable.shape_ori_left_bg);
                    hotel.setTextColor(getResources().getColor(R.color.white));
                    hotel.getBackground().setAlpha(120);
                    farm.setBackgroundResource(R.drawable.shape_white_right_bg);
                    farm.setTextColor(getResources().getColor(R.color.ori));
                    break;
            }
            switch (currentIndex) {
                case 0:
                    fragmentTransaction.hide(hotleFragment);
                    break;
                case 1:
                    fragmentTransaction.hide(farmFragment);
                    break;
            }
            fragmentTransaction.commit();
            currentIndex = index;
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    this.finish();
                    break;
                case R.id.hotel:
                    show(0);
                    break;
                case R.id.farm:
                    show(1);
                    break;
            }
        }
    }
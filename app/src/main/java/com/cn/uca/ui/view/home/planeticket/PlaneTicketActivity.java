package com.cn.uca.ui.view.home.planeticket;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.home.OneWayFragment;
import com.cn.uca.ui.fragment.home.TwoWayFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.StatusMargin;

/**
 *
 */
public  class PlaneTicketActivity extends BaseBackActivity implements View.OnClickListener{

        private TextView view;
        private ImageView img,back;//广告图片
        private RadioButton oneWay,twoWay;
        private OneWayFragment oneWayFragment;
        private TwoWayFragment twoWayFragment;
        private FragmentTransaction fragmentTransaction;
        private int currentIndex = -1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_plane_ticket);

            initView();
        }

        private void initView() {
            img = (ImageView)findViewById(R.id.img);
            back = (ImageView)findViewById(R.id.back);
            view = (TextView)findViewById(R.id.view);
            oneWay = (RadioButton)findViewById(R.id.oneWay);
            twoWay = (RadioButton)findViewById(R.id.twoWay);

            back.setOnClickListener(this);
            oneWay.setOnClickListener(this);
            twoWay.setOnClickListener(this);

            oneWay.setChecked(true);
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
                    if (oneWayFragment == null) {
                        oneWayFragment = new OneWayFragment();
                        fragmentTransaction.add(R.id.container, oneWayFragment);
                    }
                    fragmentTransaction.show(oneWayFragment);
                    oneWay.setBackgroundResource(R.drawable.shape_white_left_bg);
                    oneWay.setTextColor(getResources().getColor(R.color.ori));
                    twoWay.setBackgroundResource(R.drawable.shape_ori_right_bg);
                    twoWay.setTextColor(getResources().getColor(R.color.white));
                    twoWay.getBackground().setAlpha(100);
                    break;
                case 1:
                    if (twoWayFragment == null) {
                        twoWayFragment = new TwoWayFragment();
                        fragmentTransaction.add(R.id.container, twoWayFragment);
                    }
                    fragmentTransaction.show(twoWayFragment);
                    oneWay.setBackgroundResource(R.drawable.shape_ori_left_bg);
                    oneWay.setTextColor(getResources().getColor(R.color.white));
                    oneWay.getBackground().setAlpha(120);
                    twoWay.setBackgroundResource(R.drawable.shape_white_right_bg);
                    twoWay.setTextColor(getResources().getColor(R.color.ori));
                    break;
            }
            switch (currentIndex) {
                case 0:
                    fragmentTransaction.hide(oneWayFragment);
                    break;
                case 1:
                    fragmentTransaction.hide(twoWayFragment);
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
                case R.id.oneWay:
                    show(0);
                    break;
                case R.id.twoWay:
                    show(1);
                    break;
            }
        }
}
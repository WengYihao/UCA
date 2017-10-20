package com.cn.uca.ui.view.home.travel;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.home.PeripheryFragment;
import com.cn.uca.ui.fragment.home.PeripheryHotelFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;


/**
 * 周边游
 */
public class PeripheryTravelActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private RadioButton hot,hotel;
    private PeripheryFragment peripheryFragment;
    private PeripheryHotelFragment peripheryHotelFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_periphery_travel);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        hot = (RadioButton) findViewById(R.id.hot);
        hotel = (RadioButton)findViewById(R.id.hotel);

        back.setOnClickListener(this);
        hot.setOnClickListener(this);
        hotel.setOnClickListener(this);

        hot.setChecked(true);
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
                if (peripheryFragment == null) {
                    peripheryFragment = new PeripheryFragment();
                    fragmentTransaction.add(R.id.container, peripheryFragment);
                }
                fragmentTransaction.show(peripheryFragment);
                hot.setBackgroundResource(R.color.white);
                hot.setTextColor(getResources().getColor(R.color.ori));
                hotel.setBackgroundResource(R.color.ori);
                hotel.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                if (peripheryHotelFragment == null) {
                    peripheryHotelFragment = new PeripheryHotelFragment();
                    fragmentTransaction.add(R.id.container, peripheryHotelFragment);
                }
                fragmentTransaction.show(peripheryHotelFragment);
                hot.setBackgroundResource(R.color.ori);
                hot.setTextColor(getResources().getColor(R.color.white));
                hotel.setBackgroundResource(R.color.white);
                hotel.setTextColor(getResources().getColor(R.color.ori));
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(peripheryFragment);
                break;
            case 1:
                fragmentTransaction.hide(peripheryHotelFragment);
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
            case R.id.hot:
                show(0);
                break;
            case R.id.hotel:
                show(1);
                break;
        }
    }
}

package com.cn.uca.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.IndianaFragment;
import com.cn.uca.ui.fragment.LotteryFragment;
import com.cn.uca.ui.fragment.PopularityFragment;
import com.cn.uca.view.AutoVerticalScrollTextView;

import java.util.ArrayList;
import java.util.List;

public class IndianaActivity extends FragmentActivity implements View.OnClickListener{

    private TextView title01,title02,title03;
    private PopularityFragment popularityFragment;
    private LotteryFragment lotteryFragment;
    private IndianaFragment indianaFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;
    private AutoVerticalScrollTextView view;
    private List<String> data;
    private int number = 0;
    private ImageView play;
    private AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indiana);

        initView();
    }

    private void initView() {
        title01 = (TextView) findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        title03 = (TextView)findViewById(R.id.title03);

        play = (ImageView)findViewById(R.id.play);
        play.setImageResource(R.drawable.animation);
        animationDrawable = (AnimationDrawable) play.getDrawable();
        animationDrawable.start();

        view = new AutoVerticalScrollTextView(this);
        view = (AutoVerticalScrollTextView) findViewById(R.id.view);

        data = new ArrayList<>();
        data.add("嘻嘻");
        data.add("哈哈");
        data.add("哦哦");
        data.add("啊啊");
        view.setText(data.get(0));
        new Thread(){
            @Override
            public void run() {
                while (true){
                    SystemClock.sleep(3000);
                    handler.sendEmptyMessage(199);
                }
            }
        }.start();

        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
        show(0);
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 199) {
                view.next();
                number++;
//                view.setText(data.get[number%strings.length]);
                view.setText(data.get(number%data.size()));
            }
        }
    };
    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (index) {
            case 0:
                if (popularityFragment == null) {
                    popularityFragment = new PopularityFragment();
                    fragmentTransaction.add(R.id.container, popularityFragment);
                }
                fragmentTransaction.show(popularityFragment);
                title01.setTextColor(getResources().getColor(R.color.black));
                title01.setBackgroundResource(R.color.white);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.grey2);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.grey2);
                break;
            case 1:
                if (indianaFragment == null) {
                    indianaFragment = new IndianaFragment();
                    fragmentTransaction.add(R.id.container, indianaFragment);
                }
                fragmentTransaction.show(indianaFragment);
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.grey2);

                title02.setTextColor(getResources().getColor(R.color.black));
                title02.setBackgroundResource(R.color.white);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.grey2);
                break;
            case 2:
                if (lotteryFragment == null) {
                    lotteryFragment = new LotteryFragment();
                    fragmentTransaction.add(R.id.container, lotteryFragment);
                }
                fragmentTransaction.show(lotteryFragment);
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.grey2);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.grey2);

                title03.setTextColor(getResources().getColor(R.color.black));
                title03.setBackgroundResource(R.color.white);
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(popularityFragment);
                break;
            case 1:
                fragmentTransaction.hide(indianaFragment);
                break;
            case 2:
                fragmentTransaction.hide(lotteryFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title01:
                show(0);
                break;
            case R.id.title02:
                show(1);
                break;
            case R.id.title03:
                show(2);
                break;
        }
    }
}

package com.cn.uca.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.util.SharePreferenceXutil;

public class LoadActivity extends AppCompatActivity {

    private TimeCount time;
    private TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        initView();
        initTime();
    }

    private void initView(){
        timeTextView = (TextView)findViewById(R.id.time);
    }

    private void initTime(){
        time = new TimeCount(5000, 1000);// 构造CountDownTimer对象
        time.start();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (SharePreferenceXutil.isSuccess()){
                if (SharePreferenceXutil.isExit()) {
                    Intent intent = new Intent();
                    intent.setClass(LoadActivity.this, LoginActivity.class);
                    startActivity(intent);
                    LoadActivity.this.finish();
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(LoadActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoadActivity.this.finish();
                }
            }else{
                Intent intent = new Intent();
                intent.setClass(LoadActivity.this, LoginActivity.class);
                startActivity(intent);
                LoadActivity.this.finish();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            timeTextView.setText(millisUntilFinished / 1000 + "s");
        }
    }
}

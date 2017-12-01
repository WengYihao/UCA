package com.cn.uca.ui.view;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StatusBarUtil;

import org.json.JSONObject;

/**
 * 加载过度
 */
public class LoadActivity extends AppCompatActivity {

    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this);
        setContentView(R.layout.activity_load);
        initTime();
        judgeState();
    }

    private void judgeState(){
        if (SharePreferenceXutil.isSuccess()){//登录过
            QueryHttp.fastLogin(new CallBack() {
                @Override
                public void onResponse(Object response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        SharePreferenceXutil.saveAccountToken(jsonObject.getString("account_token"));
                    }catch (Exception e){

                    }
                }

                @Override
                public void onErrorMsg(String errorMsg) {
                    Log.i("789",errorMsg+"***");
                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        }else {//没有
            Log.i("123","*****");
        }
    }
    private void initTime(){
        time = new TimeCount(3000, 1000);// 构造CountDownTimer对象
        time.start();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            Intent intent = new Intent();
            intent.setClass(LoadActivity.this, MainActivity.class);
            startActivity(intent);
            LoadActivity.this.finish();
            overridePendingTransition(R.anim.activity_right_in,R.anim.activity_right_out);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
        }
    }

}

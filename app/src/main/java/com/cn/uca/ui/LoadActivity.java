package com.cn.uca.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.secretkey.MD5;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoadActivity extends AppCompatActivity {

    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_load);
        initTime();
        judgeState();
        aa();
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

    /**
     * 获取当前版本号
     * @return
     */
    private String getVersion()
    {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
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

    public void aa(){
        try {
            Log.i("123", MD5.getMD5("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6hV/6c+dPehRoP2xOGF2dVORBvwRLxhdSgi/JytqBFpvi6NGN8MpOokxlYiNL2nS2s9UT28bBXbUCc/vPxIejWogYPtuaYToK5Si6tcymaAmEp58pjbIwUT2AF/U53Rm9qKjB+Ag9jSYlRw5iC1gx7woShe61awt+oBIVk3Tu1QIDAQAB").toUpperCase());
        }catch (Exception e){

        }
        Map<String,Object> map = new HashMap<>();
        map.put("scenic_spot_id",1);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getTicket(1, sign, time_stamp, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString()+"---");
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
        SystemUtil.getCurrentDate2();
    }
}

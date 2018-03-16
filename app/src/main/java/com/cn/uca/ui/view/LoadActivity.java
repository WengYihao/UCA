package com.cn.uca.ui.view;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.MessageNumBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusBarUtil;
import com.cn.uca.util.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        msgReminding();
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


    //获取用户标记红点
    private void msgReminding(){
        Map<String ,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.msgReminding(account_token, time_stamp, sign, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            MessageNumBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<MessageNumBean>() {
                            }.getType());
                            MessageNumBean.getInstens().setE_purchase(bean.getE_purchase());
                            MessageNumBean.getInstens().setE_payment(bean.getE_payment());
                            MessageNumBean.getInstens().setE_agree_back(bean.getE_agree_back());
                            MessageNumBean.getInstens().setE_disagree_back(bean.getE_disagree_back());
                            MessageNumBean.getInstens().setE_back_request(bean.getE_back_request());
                            MessageNumBean.getInstens().setE_settlement(bean.getE_settlement());
                            MessageNumBean.getInstens().setEu_agree_purchase(bean.getEu_agree_purchase());
                            MessageNumBean.getInstens().setEu_disagree_purchase(bean.getEu_disagree_purchase());
                            MessageNumBean.getInstens().setEu_back_request(bean.getEu_back_request());
                            MessageNumBean.getInstens().setEu_disagree_back(bean.getEu_disagree_back());
                            MessageNumBean.getInstens().setEu_agree_back(bean.getEu_agree_back());
                            MessageNumBean.getInstens().setCc_sign_examine(bean.getCc_sign_examine());
                            MessageNumBean.getInstens().setCc_refund_ticket_examine(bean.getCc_refund_ticket_examine());
                            MessageNumBean.getInstens().setCc_settlement(bean.getCc_settlement());
                            Log.e("456",SystemUtil.getCurrentDate()+"******");
                            Log.e("456",MessageNumBean.getInstens().toString()+"******");
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage()+"******");
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

}

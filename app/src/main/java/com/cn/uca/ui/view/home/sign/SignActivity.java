package com.cn.uca.ui.view.home.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.home.sign.SignBean;
import com.cn.uca.bean.home.sign.SignDayBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,sign_icon,sign_mark,sign_state,sign_jifen,sign_day;
    private LinearLayout sign_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
        getUserClock();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        sign_icon = (TextView)findViewById(R.id.sign_icon);
        sign_mark = (TextView) findViewById(R.id.sign_mark);
        sign_state = (TextView)findViewById(R.id.sign_state);
        sign_jifen = (TextView)findViewById(R.id.sign_jifen);
        sign_day = (TextView)findViewById(R.id.sign_day);

        sign_btn = (LinearLayout)findViewById(R.id.sign_btn);
        back.setOnClickListener(this);
        sign_btn.setOnClickListener(this);
        sign_jifen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.sign_btn:
                userClock();
                break;
            case R.id.sign_jifen:
                startActivity(new Intent(SignActivity.this,IntegralRecordActivity.class));
                break;
        }
    }

    private void userClock(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.userClock(account_token, sign, time_stamp, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString());
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            ToastXutil.show("签到成功");
                            sign_btn.setClickable(false);
                            getUserClock();
                            break;
                    }
                }catch (Exception e){

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

    private void getUserClock(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getUserClock(account_token, time_stamp, sign, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            SignBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<SignBean>() {
                            }.getType());
                            sign_jifen.setText(bean.getIntegral()+"积分>");
                            sign_day.setText("连签"+bean.getContinuity_clock()+"天");
                            List<SignDayBean> list = bean.getClockDays();
                            for (int i=0;i<list.size();i++){
                                if (list.get(i).getLocation() == 0){
                                    if (!list.get(i).isClock()){
                                        sign_state.setText("开始签到");
                                        sign_mark.setVisibility(View.GONE);
                                    }else {
                                        sign_state.setText("签到成功");
                                        sign_mark.setText("+2积分");
                                        sign_mark.setVisibility(View.VISIBLE);
                                        sign_icon.setBackgroundResource(R.mipmap.success_sign);
                                    }
                                }
                            }
                            break;
                    }
                }catch (Exception e){

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

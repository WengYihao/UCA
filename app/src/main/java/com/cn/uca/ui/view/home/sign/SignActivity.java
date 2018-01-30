package com.cn.uca.ui.view.home.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.sign.IntegralAdapter;
import com.cn.uca.bean.home.sign.IntegralPoolBean;
import com.cn.uca.bean.home.sign.SignBean;
import com.cn.uca.bean.home.sign.SignDayBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
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
    private TextView day1,day2,day3,day4,day5,day6,day7;
    private LinearLayout sign_btn;
    private ImageView backlayout;
    private ListView listView;
    private List<IntegralPoolBean> list;
    private IntegralAdapter adapter;
    private List<String> lockDay;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
        getUserClock();
        getIntegralPool();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        backlayout = (ImageView)findViewById(R.id.backlayout);
        sign_icon = (TextView)findViewById(R.id.sign_icon);
        sign_mark = (TextView) findViewById(R.id.sign_mark);
        sign_state = (TextView)findViewById(R.id.sign_state);
        sign_jifen = (TextView)findViewById(R.id.sign_jifen);
        sign_day = (TextView)findViewById(R.id.sign_day);

        day1 = (TextView)findViewById(R.id.day1);
        day2 = (TextView)findViewById(R.id.day2);
        day3 = (TextView)findViewById(R.id.day3);
        day4 = (TextView)findViewById(R.id.day4);
        day5 = (TextView)findViewById(R.id.day5);
        day6 = (TextView)findViewById(R.id.day6);
        day7 = (TextView)findViewById(R.id.day7);
        SetLayoutParams.setRelativeLayout(backlayout, MyApplication.width,MyApplication.height*4/7);

        listView = (ListView)findViewById(R.id.listView);
        sign_btn = (LinearLayout)findViewById(R.id.sign_btn);
        back.setOnClickListener(this);
        sign_btn.setOnClickListener(this);
        sign_jifen.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new IntegralAdapter(list,this,1);
        listView.setAdapter(adapter);
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

    //签到
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
    //获取用户签到信息
    private void getUserClock(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        final String sign = SignUtil.sign(map);
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
                            if (bean.getContinuity_clock() == 0){
                                sign_day.setText("暂无连签");
                            }else{
                                sign_day.setText("连签"+bean.getContinuity_clock()+"天");
                            }
                            List<SignDayBean> list = bean.getClockDays();
                            List<String> list1 = new ArrayList<String>();
                            for (int i=0;i<list.size();i++){
                                list1.add(list.get(i).getClock_date());
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
                                    switch (i){
                                        case 0:
                                            day1.setBackgroundResource(R.drawable.sign_back);
                                            break;
                                        case 1:
                                            day2.setBackgroundResource(R.drawable.sign_back);
                                            break;
                                        case 2:
                                            day3.setBackgroundResource(R.drawable.sign_back);
                                            break;
                                        case 3:
                                            day4.setBackgroundResource(R.drawable.sign_back);
                                            break;
                                        case 4:
                                            day5.setBackgroundResource(R.drawable.sign_back);
                                            break;
                                        case 5:
                                            day6.setBackgroundResource(R.drawable.sign_back);
                                            break;
                                        case 6:
                                            day7.setBackgroundResource(R.drawable.sign_back);
                                            break;
                                    }
                                }
                            }
                            lockDay = StringXutil.DateToDay(list1);
                            day1.setText(lockDay.get(0));
                            day2.setText(lockDay.get(1));
                            day3.setText(lockDay.get(2));
                            day4.setText(lockDay.get(3));
                            day5.setText(lockDay.get(4));
                            day6.setText(lockDay.get(5));
                            day7.setText(lockDay.get(6));
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
    //获取积分税换物品
    private void getIntegralPool(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getIntegralPool(account_token, time_stamp, sign, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<IntegralPoolBean> been = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("integralPools").toString(),new TypeToken<List<IntegralPoolBean>>() {
                            }.getType());
                            if (been.size() > 0){
                                list.addAll(been);
                                adapter.setList(list);
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

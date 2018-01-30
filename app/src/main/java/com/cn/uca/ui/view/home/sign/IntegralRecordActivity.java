package com.cn.uca.ui.view.home.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.sign.IntegralDetailAdapter;
import com.cn.uca.bean.home.sign.IntegralDetailedBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分记录
 */
public class IntegralRecordActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,exchange,title01,title02;
    private ListView listView;
    private List<IntegralDetailedBean> list;
    private IntegralDetailAdapter adapter;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_record);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        listView = (ListView)findViewById(R.id.listView);

        list = new ArrayList<>();
        adapter = new IntegralDetailAdapter(list,this);
        listView.setAdapter(adapter);
        exchange =(TextView)findViewById(R.id.exchange);
        exchange.setOnClickListener(this);

        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        exchange.setOnClickListener(this);
        getIntegralDetailed(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.exchange:
                startActivity(new Intent(IntegralRecordActivity.this,IntegralExchangeActivity.class));
                break;
            case R.id.title01:
                list.clear();
                adapter.setList(list);
                title01.setTextColor(getResources().getColor(R.color.ori));
                title02.setTextColor(getResources().getColor(R.color.gray2));
                getIntegralDetailed(0);
                break;
            case R.id.title02:
                list.clear();
                adapter.setList(list);
                title01.setTextColor(getResources().getColor(R.color.gray2));
                title02.setTextColor(getResources().getColor(R.color.ori));
                getIntegralDetailed(1);
                break;
        }
    }

    private void getIntegralDetailed(int gain_loss){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("gain_loss",gain_loss);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getIntegralDetailed(account_token, time_stamp, sign, gain_loss, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<IntegralDetailedBean> been = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("integralDetaileds").toString(),new TypeToken<List<IntegralDetailedBean>>() {
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

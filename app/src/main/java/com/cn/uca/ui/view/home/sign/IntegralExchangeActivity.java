package com.cn.uca.ui.view.home.sign;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.sign.IntegralAdapter;
import com.cn.uca.bean.home.sign.IntegralPoolBean;
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
 *积分兑换
 */
public class IntegralExchangeActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;
    private List<IntegralPoolBean> list;
    private IntegralAdapter adapter;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_exchange);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);

        back.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new IntegralAdapter(list,this,2);
        listView.setAdapter(adapter);

        getIntegralPool();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
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

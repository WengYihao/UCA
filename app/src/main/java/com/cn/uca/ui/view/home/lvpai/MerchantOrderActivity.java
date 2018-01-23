package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.MerchantOrderAdapter;
import com.cn.uca.bean.home.lvpai.MerchantOrderBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.lvpai.OrderCallback;
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
 * 商家订单
 */
public class MerchantOrderActivity extends BaseBackActivity implements View.OnClickListener,OrderCallback{

    private TextView back;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private int id = 0;
    private ListView listView;
    private MerchantOrderAdapter adapter;
    private List<MerchantOrderBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_order);

        getInfo();
        initView();
        getTsOrders(id);
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new MerchantOrderAdapter(list,this,this);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getTsOrders(int id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("trip_shoot_id",id);
        map.put("trip_shoot_state","orders");
        String sign = SignUtil.sign(map);
        HomeHttp.getTsOrders(time_stamp, sign, account_token, page, pageCount, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<MerchantOrderBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("tsOrders").toString(),new TypeToken<List<MerchantOrderBean>>() {
                            }.getType());
                            if (bean.size()>0){
                                list.addAll(bean);
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

    @Override
    public void backOrder(View v) {
        //退单
    }

    @Override
    public void settlement(View v) {
        //结算
    }

    @Override
    public void chat(View v) {
        //聊天
    }
}

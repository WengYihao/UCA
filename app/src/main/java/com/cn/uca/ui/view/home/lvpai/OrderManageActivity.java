package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.MerchantAllOrderAdapter;
import com.cn.uca.bean.home.lvpai.MerchantAllOrderBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.lvpai.OrderItemBack;
import com.cn.uca.loading.LoadingLayout;
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

//订单管理
public class OrderManageActivity extends BaseBackActivity implements View.OnClickListener,OrderItemBack{

    private TextView back;
    private RadioButton title01,title02,title03;
    private ListView listView;
    private MerchantAllOrderAdapter adapter;
    private List<MerchantAllOrderBean> list;
    private LoadingLayout loading;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manage);

        initView();
        getTsOrder("orders");
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (RadioButton)findViewById(R.id.title01);
        title02 = (RadioButton)findViewById(R.id.title02);
        title03 = (RadioButton)findViewById(R.id.title03);
        listView = (ListView)findViewById(R.id.listView);
        loading = (LoadingLayout)findViewById(R.id.loading);
        list = new ArrayList<>();
        adapter = new MerchantAllOrderAdapter(list,this,this);
        listView.setAdapter(adapter);

        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.title01:
                list.clear();
                getTsOrder("orders");
                break;
            case R.id.title02:
                list.clear();
                getTsOrder("success");
                break;
            case R.id.title03:
                list.clear();
                getTsOrder("back");
                break;
        }
    }

//    orders 待执行
//    success 完成
//    back 退单
    private void getTsOrder(String state){
        loading.setStatus(LoadingLayout.Loading);
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("trip_shoot_state",state);
        map.put("page", page);
        map.put("pageCount", pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getTsOrder(time_stamp, sign, account_token, state, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            loading.setStatus(LoadingLayout.Success);
                            Gson gson = new Gson();
                            List<MerchantAllOrderBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("tsOrders").toString(),new TypeToken<List<MerchantAllOrderBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.clear();
                                loading.setStatus(LoadingLayout.Success);
                                list.addAll(bean);
                                adapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    list.clear();
                                    loading.setStatus(LoadingLayout.Success);
                                    adapter.setList(list);
                                    ToastXutil.show("没有更多数据了");
                                }else {
                                    //没有数据
                                    loading.setStatus(LoadingLayout.Empty);
                                }
                            }
                            break;
                        default:
                            loading.setStatus(LoadingLayout.Error);
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
    public void back(View v) {
        int id = list.get((int)v.getTag()).getTrip_shoot_id();
        Intent intent = new Intent();
        intent.setClass(OrderManageActivity.this,MerchantOrderActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}

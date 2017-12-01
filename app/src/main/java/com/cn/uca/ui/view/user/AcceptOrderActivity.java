package com.cn.uca.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.user.AcceptOrderAdapter;
import com.cn.uca.bean.yueka.AcceptOrderBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.BackOrderCallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcceptOrderActivity extends BaseBackActivity implements View.OnClickListener,BackOrderCallBack{

    private TextView back;
    private ListView listView;
    private TextView title01,title02,title03,title04;
    private int page = 1;
    private int pageCount = 5;
    private AcceptOrderAdapter adapter;
    private List<AcceptOrderBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        title03 = (TextView)findViewById(R.id.title03);
        title04 = (TextView)findViewById(R.id.title04);
        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
        title04.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new AcceptOrderAdapter(list,getApplicationContext(),this);
        listView.setAdapter(adapter);

        show(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.title01:
                show(0);
                break;
            case R.id.title02:
                show(1);
                break;
            case R.id.title03:
                show(2);
                break;
            case R.id.title04:
                show(3);
                break;
        }
    }

    private void show(int index){
        switch (index){
            case 0:
                title01.setTextColor(getResources().getColor(R.color.ori));
                title01.setBackgroundResource(R.color.white);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.ori);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.ori);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.ori);
                getMyOrder("",page,pageCount);
                break;
            case 1:
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.ori);

                title02.setTextColor(getResources().getColor(R.color.ori));
                title02.setBackgroundResource(R.color.white);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.ori);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.ori);
                getMyOrder("3",page,pageCount);
                break;
            case 2:
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.ori);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.ori);

                title03.setTextColor(getResources().getColor(R.color.ori));
                title03.setBackgroundResource(R.color.white);

                title04.setTextColor(getResources().getColor(R.color.white));
                title04.setBackgroundResource(R.color.ori);
                getMyOrder("2",page,pageCount);
                break;
            case 3:
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.ori);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.ori);

                title03.setTextColor(getResources().getColor(R.color.white));
                title03.setBackgroundResource(R.color.ori);

                title04.setTextColor(getResources().getColor(R.color.ori));
                title04.setBackgroundResource(R.color.white);
                getMyOrder("4",page,pageCount);
                break;
        }
    }

    private void getMyOrder(String id,int page,int pageCount){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("escort_record_state_id",id);
        map.put("pageCount",pageCount);
        map.put("page",page);
        String sign = SignUtil.sign(map);
        UserHttp.getMyEscortOrder(account_token, page, pageCount, sign, time_stamp, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString());
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("myEscortOrders");
                            List<AcceptOrderBean> bean = gson.fromJson(array.toString(),new TypeToken<List<AcceptOrderBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.clear();
                                list.addAll(bean);
                                adapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    list.clear();
                                    adapter.setList(list);
                                    ToastXutil.show("没有更多数据了");
                                }else {
                                    list.clear();
                                    adapter.setList(list);
                                    ToastXutil.show("暂无数据");
                                }
                            }
                            break;
                        case 17:
                            ToastXutil.show("登录已过期");
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
    public void onBack(View v) {
        Intent intent = new Intent();
        switch (list.get((int)v.getTag()).getEscort_record_state_id()){
            case 6:
                ToastXutil.show("自己要退单");
                break;
            case 8:
                intent.setClass(AcceptOrderActivity.this,BackOrderApplyActivity.class);
                intent.putExtra("userId",list.get((int)v.getTag()).getEscort_user_id());
                startActivity(intent);
                break;
        }

    }
}

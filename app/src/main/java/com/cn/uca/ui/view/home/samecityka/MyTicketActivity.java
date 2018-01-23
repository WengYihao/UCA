package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.MyTicketAdapter;
import com.cn.uca.bean.home.samecityka.MyTicketBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.samecityka.TicketItemListener;
import com.cn.uca.server.home.HomeHttp;
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

public class MyTicketActivity extends BaseBackActivity implements View.OnClickListener,TicketItemListener{

    private TextView back;
    private int page = 1;
    private int pageCount = 5;
    private ListView listView;
    private MyTicketAdapter adapter;
    private List<MyTicketBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket);

        initView();
        getMyTicket();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new MyTicketAdapter(list,this,this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getMyTicket(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getMyTicket(account_token, time_stamp, sign, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("tickets");
                            List<MyTicketBean> bean = gson.fromJson(array.toString(),new TypeToken<List<MyTicketBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);
                                adapter.setList(list);
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage());
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
    public void onActionLayout(View v) {
        Intent intent = new Intent();
        intent.setClass(this, ActionDetailActivity.class);
        intent.putExtra("id",list.get((int)v.getTag()).getCity_cafe_id());
        startActivity(intent);
    }

    @Override
    public void onCodeLayout(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TicketCodeActivity.class);
        intent.putExtra("id",list.get((int) view.getTag()).getCity_cafe_order_id());
        intent.putParcelableArrayListExtra("listTicket",(ArrayList<? extends Parcelable>) list.get((int)view.getTag()).getTickets());
        startActivity(intent);
    }
}

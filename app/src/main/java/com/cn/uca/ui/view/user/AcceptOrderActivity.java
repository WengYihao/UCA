package com.cn.uca.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.user.AcceptOrderAdapter;
import com.cn.uca.adapter.user.OrderTypeAdapter;
import com.cn.uca.bean.user.order.OrderTypeBean;
import com.cn.uca.bean.yueka.AcceptOrderBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.BackOrderCallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.HorizontalListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcceptOrderActivity extends BaseBackActivity implements View.OnClickListener,BackOrderCallBack{

    private TextView back;
    private ListView listView;
    private int count = 0;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private AcceptOrderAdapter adapter;
    private OrderTypeAdapter adapterType;
    private List<AcceptOrderBean> list;
    private List<OrderTypeBean> listType;
    private HorizontalListView type;
    private String [] data;
    private RefreshLayout refreshLayout;
    private LinearLayout layout;
    private RelativeLayout no_layout;//没有数据布局
    private String state = "all";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);

        type = (HorizontalListView)findViewById(R.id.typeName);
        data = getResources().getStringArray(R.array.yueka_order_type);

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        listType = new ArrayList<>();
        OrderTypeBean bean = null;
        for (int i = 0;i<data.length;i++){
            bean = new OrderTypeBean();
            if (i==0){
                bean.setCheck(true);
                bean.setName(data[i]);
            }else{
                bean.setCheck(false);
                bean.setName(data[i]);
            }
            listType.add(bean);
        }
        adapterType = new OrderTypeAdapter(listType,this);
        type.setAdapter(adapterType);

        type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                layout.setVisibility(View.VISIBLE);
                no_layout.setVisibility(View.GONE);
                for (OrderTypeBean typeBean :listType){
                    typeBean.setCheck(false);
                }
                listType.get(position).setCheck(true);
                adapterType.notifyDataSetChanged();
                switch (listType.get(position).getName()){
                    case "全部":
                        state = "all";
                        break;
                    case "审核":
                        state = "examine";
                        break;
                    case "进行中":
                        state = "running";
                        break;
                    case "完成":
                        state = "complete";
                        break;
                    case "退单":
                        state = "back";
                        break;
                    case "失效":
                        state = "invalid";
                        break;
                }
                refreshLayout.autoRefresh();
            }
        });

        layout = (LinearLayout)findViewById(R.id.layout);
        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        layout = (LinearLayout)findViewById(R.id.layout);
        no_layout = (RelativeLayout)findViewById(R.id.no_layout);
        adapter = new AcceptOrderAdapter(list,getApplicationContext(),this);
        listView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMyOrder(1,state,page,pageCount);
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 200);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        getMyOrder(2,state,page+count,pageCount);
                        refreshlayout.finishLoadmore();
                    }
                }, 200);
            }
        });
        refreshLayout.autoRefresh();
//        show(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }
    private void getMyOrder(final int type,String state,int page,int pageCount){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("escort_record_state",state);
        map.put("pageCount",pageCount);
        map.put("page",page);
        String sign = SignUtil.sign(map);
        UserHttp.getMyEscortOrder(account_token, page, pageCount, sign, time_stamp, state, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("myEscortOrders");
                            List<AcceptOrderBean> bean = gson.fromJson(array.toString(),new TypeToken<List<AcceptOrderBean>>() {
                            }.getType());
                            switch (type){
                                case 1://刷新
                                    if (bean.size() > 0){
                                        layout.setVisibility(View.VISIBLE);
                                        no_layout.setVisibility(View.GONE);
                                        list.clear();
                                        list.addAll(bean);
                                        adapter.setList(list);
                                    }else{
                                        layout.setVisibility(View.GONE);
                                        no_layout.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 2://加载
                                    if (bean.size() > 0){
                                        list.addAll(bean);
                                        adapter.setList(list);
                                    }
                                    break;
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
                ToastXutil.show(errorMsg);
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

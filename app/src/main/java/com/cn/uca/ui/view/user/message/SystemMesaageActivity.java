package com.cn.uca.ui.view.user.message;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.user.SystemMessageAdapter;
import com.cn.uca.bean.user.message.MessageBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemMesaageActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;
    private SystemMessageAdapter adapter;
    private List<MessageBean> list;
    private int count = 0;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private RefreshLayout refreshLayout;
    private LinearLayout layout;
    private RelativeLayout no_layout;//没有数据布局
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_mesaage);

        initView();

    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);

        listView = (ListView)findViewById(R.id.listView);

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        layout = (LinearLayout)findViewById(R.id.layout);
        no_layout = (RelativeLayout)findViewById(R.id.no_layout);
        back.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new SystemMessageAdapter(list,this);
        listView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count = 0;
                        getPushUser(1,page,pageCount);
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
                        getPushUser(2,count+page,pageCount);
                        refreshlayout.finishLoadmore();
                    }
                }, 200);
            }
        });
        refreshLayout.autoRefresh();
    }

    private void getPushUser(final int type,int page,int pageCount){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("push_type","system");
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.getPushUser(sign, time_stamp, account_token, page, pageCount, "system", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<MessageBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("pushUsers").toString(),new TypeToken<List<MessageBean>>() {
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }
}

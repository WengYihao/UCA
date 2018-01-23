package com.cn.uca.ui.view.user.message;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.user.SystemMessageAdapter;
import com.cn.uca.bean.user.message.MessageBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.loading.LoadingLayout;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
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
    private LoadingLayout loading;
    private ListView listView;
    private SystemMessageAdapter adapter;
    private List<MessageBean> list;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_mesaage);

        initView();
        getPushUser();

    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        loading = (LoadingLayout)findViewById(R.id.loading);
        listView = (ListView)findViewById(R.id.listView);

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
                        getPushUser();
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getPushUser();
                        refreshlayout.finishLoadmore();
                    }
                }, 1000);
            }
        });
    }

    private void getPushUser(){
        loading.setStatus(LoadingLayout.Loading);
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
                            loading.setStatus(LoadingLayout.Success);
                            Gson gson = new Gson();
                            List<MessageBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("pushUsers").toString(),new TypeToken<List<MessageBean>>() {
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
                                    //没有数据
                                    loading.setStatus(LoadingLayout.Empty);
                                }
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

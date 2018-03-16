package com.cn.uca.ui.view.user;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.user.WalletDetailAdapter;
import com.cn.uca.bean.user.WalletDetailBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalletDetailActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;
    private int count = 0;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private List<WalletDetailBean> list;
    private WalletDetailAdapter adapter;
    private RefreshLayout refreshLayout;
    private LinearLayout layout;
    private RelativeLayout no_layout;//没有数据布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);

        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);
        layout = (LinearLayout)findViewById(R.id.layout);
        no_layout = (RelativeLayout)findViewById(R.id.no_layout);
        back.setOnClickListener(this);
        back.setOnClickListener(this);

        adapter = new WalletDetailAdapter(list,this);
        listView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count = 0;
                        getPusrsRecord(1,page,pageCount);
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
                        getPusrsRecord(2,count+page,pageCount);
                        refreshlayout.finishLoadmore();
                    }
                }, 200);
            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getPusrsRecord(final int type,int page,int pageCount){
        String token = SharePreferenceXutil.getAccountToken();
        UserHttp.getPurseRecords(token, page, pageCount, "", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("purseRecordDatas");
                            List<WalletDetailBean> bean  = gson.fromJson(array.toString(),new TypeToken<List<WalletDetailBean>>(){
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

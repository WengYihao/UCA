package com.cn.uca.ui.view.yueka;

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
import com.cn.uca.adapter.yueka.YuekaCollectionAdapter;
import com.cn.uca.bean.yueka.CollectionBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.server.yueka.YueKaHttp;
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

//领咖收藏
public class YueKaCollectionActivity extends BaseBackActivity implements View.OnClickListener,CollectionClickListener {

    private TextView back;
    private ListView listView;
    private int count = 0;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private List<CollectionBean> list;
    private YuekaCollectionAdapter adapter;
    private RefreshLayout refreshLayout;
    private LinearLayout layout;
    private RelativeLayout no_layout;//没有数据布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_ka_collection);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        listView = (ListView)findViewById(R.id.listView);
        layout = (LinearLayout)findViewById(R.id.layout);
        no_layout = (RelativeLayout)findViewById(R.id.no_layout);
        list = new ArrayList<>();
        adapter = new YuekaCollectionAdapter(list,this,this);
        listView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getEscortCollection(1,page,pageCount);
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
                        getEscortCollection(2,page+count,pageCount);
                        refreshlayout.finishLoadmore();
                    }
                }, 200);
            }
        });
        refreshLayout.autoRefresh();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(YueKaCollectionActivity.this, YueChatActivity.class);
                intent.putExtra("id", list.get(position).getEscort_record_id());
                startActivity(intent);
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

    private void getEscortCollection(final int type,int page,int pageCount){
        Map<String ,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        YueKaHttp.getEscortCollection(account_token,time_stamp, sign, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<CollectionBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("escortCollections").toString(),new TypeToken<List<CollectionBean>>() {
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

    @Override
    public void onCollectionClick(View v) {
        cancelCollection(list.get((int) v.getTag()).getEscort_collection_id());
    }

    public void cancelCollection(int id){
        Map<String ,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("escort_collection_id",id);
        String sign = SignUtil.sign(map);
        YueKaHttp.cancelCollection(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                if ((int)response == 0){
                    ToastXutil.show("取消成功");
                    refreshLayout.autoRefresh();
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

package com.cn.uca.ui.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.PeripheryTravelAdapter;
import com.cn.uca.bean.home.travel.PeripheryTraverBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 当季热门
 */
public class PeripheryFragment extends Fragment {
    private View view;
    private ListView listView;
    private List<PeripheryTraverBean> list;
    private PeripheryTravelAdapter adapter;
    private RefreshLayout refreshLayout;
    private int page = 1;
    private int pageCount = 5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_periphery,null);

        initView();
        getZhouBianTravel(page,pageCount);
        return view;
    }

    private void initView() {
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        listView = (ListView)view.findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new PeripheryTravelAdapter(list,getActivity());
        listView.setAdapter(adapter);

//        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getZhouBianTravel(page,pageCount);
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getZhouBianTravel(page,pageCount);
                        refreshlayout.finishLoadmore();
                    }
                }, 2000);
            }
        });

//        refreshLayout.autoRefresh();
    }

    private void getZhouBianTravel(int page,int pageCount){
        HomeHttp.getZhouBianTravel(page,pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("zhouBianTravelRets");
                            Gson gson = new Gson();
                            List<PeripheryTraverBean> bean = gson.fromJson(array.toString(),new TypeToken<List<PeripheryTraverBean>>() {
                            }.getType());
                            if (bean.size() > 0) {
                                list.clear();
                                list.addAll(bean);
                                adapter.setList(list);
                            } else {
                                if (list.size() != 0) {
                                    ToastXutil.show("没有更多数据了");
                                } else {
                                    adapter.setList(list);
                                }
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

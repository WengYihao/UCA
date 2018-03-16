package com.cn.uca.ui.fragment.home.raider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.raiders.RaidersCollectionAdapter;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.fragment.home.BaseFragment;
import com.cn.uca.ui.view.home.raider.RaidersDetailActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
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

/**
 * Created by asus on 2018/3/8.
 * 攻略-收藏
 */

public class GLCollectionFragment extends BaseFragment implements CollectionClickListener{

    private View view;
    private GridView gridView;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private List<RaidersBean> list;
    private RaidersCollectionAdapter adapter;
    private RefreshLayout refreshLayout;
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collection_gl,null);

        isPrepared = true;
        setlazyLoad();
        return view;
    }

    @Override
    protected void setlazyLoad() {
        super.setlazyLoad();
        if(!isPrepared || !isVisible) {
            return;
        }
        initView();
    }

    private void initView() {
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        gridView = (GridView)view.findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new RaidersCollectionAdapter(list,getActivity(),this);
        gridView.setAdapter(adapter);

        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cpyGetCityRaiders(1);
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 500);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        cpyGetCityRaiders(page);
                        refreshlayout.finishLoadmore();
                    }
                }, 500);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),RaidersDetailActivity.class);
                intent.putExtra("id",list.get(position).getCity_raiders_id());
                intent.putExtra("cityName",list.get(position).getCity_name());
                startActivity(intent);
            }
        });
    }

    private void cpyGetCityRaiders(int page){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        String timr_temp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",timr_temp);
        map.put("city_pinyin","");
        String token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", token);
        String sign = SignUtil.sign(map);
        HomeHttp.cpyGetCityRaiders(page,pageCount, "",0, sign, timr_temp, token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("cityRaidersRets");
                            Gson gson = new Gson();
                            List<RaidersBean> bean = gson.fromJson(array.toString(),new TypeToken<List<RaidersBean>>() {
                            }.getType());
                            list.clear();
                            for (RaidersBean bean1 : bean){
                                if (bean1.isCollection()){
                                    list.add(bean1);
                                }
                            }
                            if (list.size() > 0){
                                adapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
                                }else{
                                    adapter.setList(list);
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
    public void onCollectionClick(final View v) {
        RelativeLayout layout = (RelativeLayout) gridView.getChildAt((int) v.getTag());// 获得子item的layout
        final TextView tv = (TextView) layout.findViewById(R.id.collection);
        if (list.get((int) v.getTag()).isCollection()){
            //--发送请求
            Map<String,Object> map = new HashMap<>();
            String account_token = SharePreferenceXutil.getAccountToken();
            map.put("account_token",account_token);
            map.put("city_raiders_id",list.get((int) v.getTag()).getCity_raiders_id());
            String time_stamp = SystemUtil.getCurrentDate2();
            map.put("time_stamp",time_stamp);
            final String sign = SignUtil.sign(map);
            HomeHttp.collectionRaiders(account_token,list.get((int) v.getTag()).getCity_raiders_id(),sign,time_stamp, new CallBack() {
                @Override
                public void onResponse(Object response) {
                    if ((int) response == 0){
                        ToastXutil.show("取消收藏");
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
        }else{
            //--发送请求
            Map<String,Object> map = new HashMap<>();
            String account_token = SharePreferenceXutil.getAccountToken();
            map.put("account_token",account_token);
            map.put("city_raiders_id",list.get((int) v.getTag()).getCity_raiders_id());
            String time_stamp = SystemUtil.getCurrentDate2();
            map.put("time_stamp",time_stamp);
            final String sign = SignUtil.sign(map);
            HomeHttp.collectionRaiders(account_token,list.get((int) v.getTag()).getCity_raiders_id(),sign,time_stamp, new CallBack() {
                @Override
                public void onResponse(Object response) {
                    if ((int) response == 0){
                        ToastXutil.show("收藏成功");
                        tv.setBackgroundResource(R.mipmap.collection_white);
                        list.get((int) v.getTag()).setCollection(true);
                        adapter.setList(list);
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
}

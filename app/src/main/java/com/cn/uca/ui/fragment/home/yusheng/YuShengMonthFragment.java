package com.cn.uca.ui.fragment.home.yusheng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.yusheng.YuShengDayAdapter;
import com.cn.uca.adapter.home.yusheng.YuShengMonthAdapter;
import com.cn.uca.bean.home.yusheng.LifeDaysBean;
import com.cn.uca.bean.home.yusheng.LifeMonthsBean;
import com.cn.uca.bean.home.yusheng.SystemEventsBean;
import com.cn.uca.bean.home.yusheng.YuShengDayDetailsBean;
import com.cn.uca.bean.home.yusheng.YuShengMonthDetailsBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yusheng.YuShengMonthImpl;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.util.yusheng.YuShengUtil;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/10/25.
 */

public class YuShengMonthFragment extends Fragment implements AsymmetricGridView.OnItemClickListener,View.OnClickListener{
    private View view;
    private SmartRefreshLayout refreshLayout;
    private AsymmetricGridView listView;
    private int loadPage = 1;
    private int refreshPage = 1;
    private List<LifeMonthsBean> monthBean,setBean;
    private YuShengMonthImpl adapter;
    private YuShengUtil itemBeen;
    private YuShengMonthDetailsBean monthDetailsBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_month_yusheng,null);
        initView();
        return view;
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        listView = (AsymmetricGridView)view.findViewById(R.id.listView);
        monthDetailsBean = new YuShengMonthDetailsBean();
        monthBean = new ArrayList<>();
        setBean = new ArrayList<>();
        itemBeen = new YuShengUtil();
        adapter = new YuShengMonthAdapter(getActivity(),itemBeen.monthItems(monthBean.size(),monthDetailsBean));
        listView.setRequestedColumnCount(4);
        listView.setRequestedHorizontalSpacing(SystemUtil.dip2px(3));
        listView.setAdapter(getNewAdapter());
        listView.setDebugging(true);
        listView.setOnItemClickListener(this);
        getLifeMonths(1);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshPage++;
                        getLifeMonths(refreshPage);
                        refreshlayout.finishRefresh();
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
                        loadPage++;
                        loadLifeMonths(loadPage);
                        refreshlayout.finishLoadmore();
                    }
                }, 1000);
            }
        });
    }

    private AsymmetricGridViewAdapter getNewAdapter() {
        return new AsymmetricGridViewAdapter(getActivity(), listView, (ListAdapter) adapter);
    }
    private void getLifeMonths(int page){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("direction","u");
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String sign = SignUtil.sign(map);
        HomeHttp.getLifeMonths(sign, time_stamp, account_token, page, "u", new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            int fill = jsonObject.getJSONObject("data").getInt("fill");
                            Gson gson = new Gson();
                            YuShengMonthDetailsBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YuShengMonthDetailsBean>() {
                            }.getType());
                            monthBean.clear();
                            monthBean = bean.getLifeMonthsRet();
                            if (monthBean.size() > 0) {
                                if (setBean.size() > 0){
                                    if (fill > 0){
                                        for (int a = 0;a<fill;a++){
                                            LifeMonthsBean monthsBean = new LifeMonthsBean();
                                            monthsBean.setContent("");
                                            monthsBean.setDate("");
                                            monthsBean.setMonth(0);
                                            monthsBean.setMonth_event_count(0);
                                            setBean.add(a,monthsBean);
                                        }
                                        for (int i = 0;i <monthBean.size();i++){
                                            setBean.add(i+fill,monthBean.get(i));
                                        }
                                        bean.setLifeMonthsRet(setBean);
                                        adapter.setItems(itemBeen.monthItems(setBean.size(),bean));
                                    }else{
                                        for (int i = 0;i <monthBean.size();i++){
                                            setBean.add(i,monthBean.get(i));
                                        }
                                        bean.setLifeMonthsRet(setBean);
                                        adapter.setItems(itemBeen.monthItems(setBean.size(),bean));
                                    }
                                }else{
                                    setBean.addAll(monthBean);
                                    bean.setLifeMonthsRet(setBean);
                                    adapter.setItems(itemBeen.monthItems(setBean.size(),bean));
                                }
                            } else {
                                if (monthBean.size() != 0) {
                                    ToastXutil.show("没有更多数据了");
                                } else {
                                    //
                                    Log.i("123","456");
                                }
                            }
                            break;
                        case 321:
                            ToastXutil.show("已全部加载！");
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
    private void loadLifeMonths(int page){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("direction","l");
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String sign = SignUtil.sign(map);
        HomeHttp.getLifeMonths(sign, time_stamp, account_token, loadPage, "l", new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            YuShengMonthDetailsBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YuShengMonthDetailsBean>() {
                            }.getType());
                            monthBean.clear();
                            monthBean = bean.getLifeMonthsRet();
                            if (monthBean.size() > 0) {
                                setBean.addAll(monthBean);
                                bean.setLifeMonthsRet(setBean);
                                adapter.setItems(itemBeen.monthItems(setBean.size(),bean));
                            } else {
                                ToastXutil.show("没有更多数据了");
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
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}

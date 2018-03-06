package com.cn.uca.ui.fragment.home.yusheng;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.yusheng.YuShengMonthAdapter;
import com.cn.uca.bean.home.yusheng.LifeMonthsBean;
import com.cn.uca.bean.home.yusheng.YuShengMonthDetailsBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.NoScrollGridView;
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

public class YuShengMonthFragment extends Fragment implements View.OnClickListener{
    private View view;
    private RelativeLayout layout;
    private TextView month,add;
    private NoScrollGridView pastMonth,futureMonth;
    private SmartRefreshLayout refreshLayout;
    private int loadPage = 1;
    private int refreshPage = 1;
    private List<LifeMonthsBean> listPast,listPastMonth,listFuture,listFutureMonth;
    private YuShengMonthAdapter adapterPast,adapterFuture;
    private int nowMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_month_yusheng,null);
        initView();
        return view;
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        layout = (RelativeLayout)view.findViewById(R.id.layout);//当前月
        month = (TextView)view.findViewById(R.id.month);
        add = (TextView)view.findViewById(R.id.add);
        pastMonth = (NoScrollGridView)view.findViewById(R.id.pastMonth);//过去月份view
        futureMonth = (NoScrollGridView)view.findViewById(R.id.futureMonth);//未来月份view

        add.setOnClickListener(this);
        listPast = new ArrayList<>();
        listPastMonth = new ArrayList<>();
        listFuture = new ArrayList<>();
        listFutureMonth = new ArrayList<>();
        adapterPast = new YuShengMonthAdapter(getActivity(),listPast,0);
        adapterFuture = new YuShengMonthAdapter(getActivity(),listFuture,0);
        pastMonth.setAdapter(adapterPast);
        futureMonth.setAdapter(adapterFuture);
        SetLayoutParams.setLinearLayout(layout, MyApplication.width,MyApplication.height*11/25);
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

        futureMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ShowPopupWindow.dayPopupwindow(view,getActivity(),listFuture.get(position).getMonth(),"month",1,"");
            }
        });
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
                            Gson gson = new Gson();
                            YuShengMonthDetailsBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YuShengMonthDetailsBean>() {
                            }.getType());
                            nowMonth = bean.getNow_month();
                            AssetManager mgr = getActivity().getAssets();//得到AssetManager
                            Typeface tf=Typeface.createFromAsset(mgr, "fonts/ttf.ttf");//根据路径得到Typeface
                            month.setText("余"+bean.getNow_month()+"月");
                            month.setTypeface(tf);//设置字体
                            int fill = bean.getFill();
                            listPastMonth.clear();
                            listPastMonth = bean.getLifeMonthsRet();
                            if (listPastMonth.size() > 0) {
                                if (listPast.size() > 0){
                                    List<LifeMonthsBean> list = new ArrayList<>();
                                    list.addAll(listPast);
                                    listPast.clear();
                                    if (fill > 0){
                                        LifeMonthsBean monthsBean = null;
                                        for (int a = 0;a<fill;a++){
                                            monthsBean = new LifeMonthsBean();
                                            monthsBean.setContent("");
                                            monthsBean.setDate("");
                                            monthsBean.setMonth(0);
                                            monthsBean.setMonth_event_count(0);
                                            listPast.add(a,monthsBean);
                                        }
                                        for (int i = 0;i <listPastMonth.size();i++){
                                            listPast.add(i+fill,listPastMonth.get(i));
                                        }
                                        listPast.addAll(list);
                                        adapterPast.setList(listPast,bean.getNow_month());
                                        SetListView.setGridViewHeightBasedOnChildren(pastMonth);
                                    }else{
                                        for (int i = 0;i <listPastMonth.size();i++){
                                            listPast.add(i,listPastMonth.get(i));
                                        }
                                        listPast.addAll(list);
                                        adapterPast.setList(listPast,bean.getNow_month());
                                        SetListView.setGridViewHeightBasedOnChildren(pastMonth);
                                    }
                                }else{
                                    if (fill > 0){
                                        LifeMonthsBean monthsBean = null;
                                        for (int a = 0;a<fill;a++){
                                            monthsBean = new LifeMonthsBean();
                                            monthsBean.setContent("");
                                            monthsBean.setDate("");
                                            monthsBean.setMonth(0);
                                            monthsBean.setMonth_event_count(0);
                                            listPast.add(a,monthsBean);
                                        }
                                        for (int i = 0;i <listPast.size();i++){
                                            if (listPastMonth.get(i).getMonth() > bean.getNow_month()){
                                                listPast.add(i+fill,listPastMonth.get(i));
                                            }else if(listPastMonth.get(i).getMonth() < bean.getNow_month()){
                                                listFuture.add(listPastMonth.get(i));
                                            }
                                        }
                                        adapterPast.setList(listPast,bean.getNow_month());
                                        adapterFuture.setList(listFuture,bean.getNow_month());
                                        SetListView.setGridViewHeightBasedOnChildren(pastMonth);
                                        SetListView.setGridViewHeightBasedOnChildren(futureMonth);
                                    }else{
                                        for (int i = 0;i <listPastMonth.size();i++){
                                            if (listPastMonth.get(i).getMonth() > bean.getNow_month()){
                                                listPast.add(listPastMonth.get(i));
                                            }else if(listPastMonth.get(i).getMonth() < bean.getNow_month()){
                                                listFuture.add(listPastMonth.get(i));
                                            }
                                        }
                                        adapterPast.setList(listPast,bean.getNow_month());
                                        adapterFuture.setList(listFuture,bean.getNow_month());
                                        SetListView.setGridViewHeightBasedOnChildren(pastMonth);
                                        SetListView.setGridViewHeightBasedOnChildren(futureMonth);
                                    }

                                }
                            } else {
                                if (listPastMonth.size() != 0) {
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
                    Log.i("456",e.getMessage());
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
                            listFutureMonth.clear();
                            listFutureMonth = bean.getLifeMonthsRet();
                            if (listFutureMonth.size() > 0) {
                                listFuture.addAll(listFutureMonth);
                                adapterFuture.setList(listFuture,bean.getNow_month());
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
        switch (view.getId()){
            case R.id.add:
                ShowPopupWindow.dayPopupwindow(this.view,getActivity(),nowMonth,"month",1,"");
                break;
        }
    }
}

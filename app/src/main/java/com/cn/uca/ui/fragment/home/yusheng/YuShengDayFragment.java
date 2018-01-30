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
import com.cn.uca.adapter.home.yusheng.YuShengDayAdapter;
import com.cn.uca.bean.home.yusheng.LifeDaysBean;
import com.cn.uca.bean.home.yusheng.SystemEventsBean;
import com.cn.uca.bean.home.yusheng.YuShengDayDetailsBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.popupwindows.LoadingPopupWindow;
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

public class YuShengDayFragment extends Fragment implements View.OnClickListener{
    private View view;
    private RelativeLayout layout;
    private TextView day,add;
    private SmartRefreshLayout refreshLayout;
    private NoScrollGridView pastTime,futureTime;
    private YuShengDayAdapter adapterPast,adapterFuture;
    private List<LifeDaysBean> listPast,listPastDay,listFuture,listFutureDay;
    private int loadPage = 1;
    private int refreshPage = 1;
    private int nowDay;
    private LoadingPopupWindow popupWindow;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_yusheng,null);

        initView();
        return view;
    }

    private void initView() {
        popupWindow = new LoadingPopupWindow(getActivity());
        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        layout = (RelativeLayout)view.findViewById(R.id.layout);
        day = (TextView)view.findViewById(R.id.day);
        add = (TextView)view.findViewById(R.id.add);
        pastTime = (NoScrollGridView)view.findViewById(R.id.pastTime);
        futureTime = (NoScrollGridView)view.findViewById(R.id.futureTime);
        listPast = new ArrayList<>();
        listPastDay = new ArrayList<>();
        listFuture = new ArrayList<>();
        listFutureDay = new ArrayList<>();
        adapterPast = new YuShengDayAdapter(getActivity(),listPast,0);
        adapterFuture = new YuShengDayAdapter(getActivity(),listFuture,0);
        pastTime.setAdapter(adapterPast);
        futureTime.setAdapter(adapterFuture);
        add.setOnClickListener(this);
        SetLayoutParams.setLinearLayout(layout, MyApplication.width,MyApplication.height*11/25);
        getLifeDays(1);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshPage++;
                        getLifeDays(refreshPage);
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
                        loadPage++;
                        loadLifeDays(loadPage);
                        refreshlayout.finishLoadmore();
                    }
                }, 1000);
            }
        });

        futureTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ShowPopupWindow.dayPopupwindow(view,getActivity(),listFuture.get(position).getDay(),"day");
            }
        });
    }

    private void getLifeDays(int page){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("direction","u");
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String sign = SignUtil.sign(map);
        HomeHttp.getLifeDays(sign, time_stamp, account_token, page, "u", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            YuShengDayDetailsBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YuShengDayDetailsBean>() {
                            }.getType());
                            nowDay = bean.getNow_days();
                            AssetManager mgr = getActivity().getAssets();//得到AssetManager
                            Typeface tf=Typeface.createFromAsset(mgr, "fonts/ttf.ttf");//根据路径得到Typeface
                            day.setText("余"+bean.getNow_days()+"天");
                            day.setTypeface(tf);//设置字体
                            int fill = bean.getFill();
                            listPastDay.clear();
                            listPastDay = bean.getLifeDays();
                            if (listPastDay.size() > 0){
                                if (listPast.size() > 0){
                                    List<LifeDaysBean> list = new ArrayList<>();
                                    list.addAll(listPast);
                                    listPast.clear();
                                    if (fill > 0){
                                        LifeDaysBean daysBean = null;
                                        for (int a = 0;a<fill;a++){
                                            daysBean = new LifeDaysBean();
                                            daysBean.setDate("");
                                            daysBean.setDay(0);
                                            daysBean.setSystem_event_count(0);
                                            daysBean.setLife_month_id(0);
                                            daysBean.setSystemEvents(new ArrayList<SystemEventsBean>());
                                            daysBean.setUser_content("");
                                            daysBean.setUser_event_count(0);
                                            listPast.add(a,daysBean);
                                        }
                                        for (int i = 0;i <listPastDay.size();i++){
                                            listPast.add(i+fill,listPastDay.get(i));
                                        }
                                        listPast.addAll(list);
                                        adapterPast.setList(listPast,bean.getNow_days());
                                        SetListView.setGridViewHeightBasedOnChildren(pastTime);
                                    }else{
                                        for (int i = 0;i <listPastDay.size();i++){
                                            listPast.add(i,listPastDay.get(i));
                                        }
                                        listPast.addAll(list);
                                        adapterPast.setList(listPast,bean.getNow_days());
                                        SetListView.setGridViewHeightBasedOnChildren(pastTime);
                                    }
                                }else{
                                    if (fill > 0){
                                        LifeDaysBean daysBean = null;
                                        for (int a = 0;a<fill;a++){
                                            daysBean = new LifeDaysBean();
                                            daysBean.setDate("");
                                            daysBean.setDay(0);
                                            daysBean.setSystem_event_count(0);
                                            daysBean.setLife_month_id(0);
                                            daysBean.setSystemEvents(new ArrayList<SystemEventsBean>());
                                            daysBean.setUser_content("");
                                            daysBean.setUser_event_count(0);
                                            listPast.add(a,daysBean);
                                        }
                                        for (int i = 0;i <listPastDay.size();i++){
                                            if (listPastDay.get(i).getDay() > bean.getNow_days()){
                                                listPast.add(i+fill,listPastDay.get(i));
                                            }else if(listPastDay.get(i).getDay() < bean.getNow_days()){
                                                listFuture.add(listPastDay.get(i));
                                            }
                                        }
                                        adapterPast.setList(listPast,bean.getNow_days());
                                        adapterFuture.setList(listFuture,bean.getNow_days());
                                        SetListView.setGridViewHeightBasedOnChildren(pastTime);
                                        SetListView.setGridViewHeightBasedOnChildren(futureTime);
                                        popupWindow.dismiss();
                                    }else{
                                        for (int i = 0;i <listPastDay.size();i++){
                                            if (listPastDay.get(i).getDay() > bean.getNow_days()){
                                                listPast.add(listPastDay.get(i));
                                            }else if(listPastDay.get(i).getDay() < bean.getNow_days()){
                                                listFuture.add(listPastDay.get(i));
                                            }
                                        }
                                        adapterPast.setList(listPast,bean.getNow_days());
                                        adapterFuture.setList(listFuture,bean.getNow_days());
                                        SetListView.setGridViewHeightBasedOnChildren(pastTime);
                                        SetListView.setGridViewHeightBasedOnChildren(futureTime);
                                        popupWindow.dismiss();
                                    }
                                }
                            }else{
                                popupWindow.dismiss();
                                Log.e("456","----");
                            }
                            break;
                        case 321:
                            ToastXutil.show("已全部加载！");
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage()+"----");
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
    private void loadLifeDays(int page){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("direction","l");
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String sign = SignUtil.sign(map);
        HomeHttp.getLifeDays(sign, time_stamp, account_token, loadPage, "l", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            YuShengDayDetailsBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YuShengDayDetailsBean>() {
                            }.getType());
                            listFutureDay.clear();
                            listFutureDay = bean.getLifeDays();
                            if (listFutureDay.size() > 0) {
                                listFuture.addAll(listFutureDay);
                                adapterFuture.setList(listFuture,bean.getNow_days());
                            } else {
                                ToastXutil.show("没有更多数据了");
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                ShowPopupWindow.dayPopupwindow(this.view,getActivity(),nowDay,"day");
                break;
        }
    }
}

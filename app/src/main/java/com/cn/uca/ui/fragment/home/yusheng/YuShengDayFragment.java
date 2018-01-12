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
import com.cn.uca.bean.home.yusheng.LifeDaysBean;
import com.cn.uca.bean.home.yusheng.SystemEventsBean;
import com.cn.uca.bean.home.yusheng.YuShengDayDetailsBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yusheng.EditItemClick;
import com.cn.uca.impl.yusheng.YuShengDayImpl;
import com.cn.uca.popupwindows.LoadingPopupWindow;
import com.cn.uca.popupwindows.ShowPopupWindow;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by asus on 2017/10/25.
 */

public class YuShengDayFragment extends Fragment implements AsymmetricGridView.OnItemClickListener,View.OnClickListener,EditItemClick{
    private View view;
    private SmartRefreshLayout refreshLayout;
    private AsymmetricGridView listView;
    private int loadPage = 1;
    private int refreshPage = 1;
    private List<LifeDaysBean> dayBean,setBean;
    private YuShengDayImpl adapter;
    private YuShengUtil itemBeen;
    private YuShengDayDetailsBean yuShengDayBean;
    private LoadingPopupWindow popupWindow;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_yusheng,null);

        initView();
        return view;
    }

    private void initView() {
        popupWindow = new LoadingPopupWindow(getActivity());
        popupWindow.show();
        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        listView = (AsymmetricGridView)view.findViewById(R.id.listView);
        yuShengDayBean = new YuShengDayDetailsBean();
        dayBean = new ArrayList<>();
        setBean = new ArrayList<>();
        itemBeen = new YuShengUtil();
        adapter = new YuShengDayAdapter(getActivity(),itemBeen.dayItems(dayBean.size(),yuShengDayBean),this);
        listView.setRequestedColumnCount(7);
        listView.setRequestedHorizontalSpacing(SystemUtil.dip2px(3));
        listView.setAdapter(getNewAdapter());
        listView.setDebugging(true);
        listView.setOnItemClickListener(this);

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
//        refreshLayout.autoRefresh();
    }

    private AsymmetricGridViewAdapter getNewAdapter() {
        return new AsymmetricGridViewAdapter(getActivity(), listView, (ListAdapter) adapter);
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
                Log.e("123",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            int fill = jsonObject.getJSONObject("data").getInt("fill");
                            Gson gson = new Gson();
                            YuShengDayDetailsBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YuShengDayDetailsBean>() {
                            }.getType());
                            dayBean.clear();
                            dayBean = bean.getLifeDays();
                            if (dayBean.size() > 0) {
                                if (setBean.size() > 0){
                                    if (fill > 0){
                                        for (int a = 0;a<fill;a++){
                                            LifeDaysBean daysBean = new LifeDaysBean();
                                            daysBean.setDate("");
                                            daysBean.setDay(0);
                                            daysBean.setSystem_event_count(0);
                                            daysBean.setLife_month_id(0);
                                            daysBean.setSystemEvents(new ArrayList<SystemEventsBean>());
                                            daysBean.setUser_content("");
                                            daysBean.setUser_event_count(0);
                                            setBean.add(a,daysBean);
                                        }
                                        for (int i = 0;i <dayBean.size();i++){
                                            setBean.add(i+fill,dayBean.get(i));
                                        }
                                        bean.setLifeDays(setBean);
                                        adapter.setItems(itemBeen.dayItems(setBean.size(),bean));
                                        popupWindow.dismiss();
                                    }else{
                                        for (int i = 0;i <dayBean.size();i++){
                                            setBean.add(i,dayBean.get(i));
                                        }
                                        bean.setLifeDays(setBean);
                                        adapter.setItems(itemBeen.dayItems(setBean.size(),bean));
                                        popupWindow.dismiss();
                                    }
                                }else{
                                    if (fill > 0){
                                        for (int a = 0;a<fill;a++){
                                            LifeDaysBean daysBean = new LifeDaysBean();
                                            daysBean.setDate("");
                                            daysBean.setDay(0);
                                            daysBean.setSystem_event_count(0);
                                            daysBean.setLife_month_id(0);
                                            daysBean.setSystemEvents(new ArrayList<SystemEventsBean>());
                                            daysBean.setUser_content("");
                                            daysBean.setUser_event_count(0);
                                            setBean.add(a,daysBean);
                                        }
                                        for (int i = 0;i <dayBean.size();i++){
                                            setBean.add(i+fill,dayBean.get(i));
                                        }
                                        bean.setLifeDays(setBean);
                                        adapter.setItems(itemBeen.dayItems(setBean.size(),bean));
                                        popupWindow.dismiss();
                                    }else{
                                        setBean.addAll(dayBean);
                                        bean.setLifeDays(setBean);
                                        adapter.setItems(itemBeen.dayItems(setBean.size(),bean));
                                        popupWindow.dismiss();
                                    }
                                }
                            } else {
                                if (dayBean.size() != 0) {
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
    private void loadLifeDays(int page){
        popupWindow.show();
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
                Log.i("123",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            YuShengDayDetailsBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YuShengDayDetailsBean>() {
                            }.getType());
                            dayBean.clear();
                            dayBean = bean.getLifeDays();
                            if (dayBean.size() > 0) {
                                setBean.addAll(dayBean);
                                bean.setLifeDays(setBean);
                                adapter.setItems(itemBeen.dayItems(setBean.size(),bean));
                                popupWindow.dismiss();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (setBean.get(i).getDate() != ""){
            try {
                Date today = SystemUtil.StringToUtilDate(SystemUtil.getCurrentDateOnly());
                Date getDate =  SystemUtil.StringToUtilDate(setBean.get(i).getDate());
                if (!getDate.equals(today)){
                    if (getDate.before(today)){
                        ToastXutil.show("已过的天数不能编辑");
                    }else{
                        ShowPopupWindow.dayPopupwindow(view,getActivity(),setBean.get(i).getDay(),"day");
                    }
                }
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    /**
     * 今天的编辑事件
     * @param view
     */
    @Override
    public void click(View view) {
        ShowPopupWindow.dayPopupwindow(this.view,getActivity(),setBean.get((int)view.getTag()).getDay(),"day");
    }
}

package com.cn.uca.ui.fragment.home.footprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.footprint.ChinaPrintAdapter;
import com.cn.uca.bean.home.footprint.CityBean;
import com.cn.uca.bean.home.footprint.CityDetailsBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.home.footprint.AddFootActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.ChinaMapView;
import com.cn.uca.view.NoScrollListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/11/7.
 */

public class ChinaFragment extends Fragment implements CompoundButton.OnCheckedChangeListener,View.OnClickListener{

    private View view;
    private ChinaMapView chinaMapView;
    private TextView yearNum,cityNum,share;
    private int travelYearNum,travelCityNum;
    private NoScrollListView listView;
    private ChinaPrintAdapter printAdapter;
    private List<CityBean> listCity;
    private SmartRefreshLayout refreshLayout;
    private int page = 1;
    private Intent intent;
    private RelativeLayout mapLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_china,null);

        initView();
        return view;
    }

    private void initView() {
        intent = new Intent();
        yearNum = (TextView)view.findViewById(R.id.yearNum);
        cityNum = (TextView)view.findViewById(R.id.cityNum);
        share = (TextView)view.findViewById(R.id.share);
        share.setOnClickListener(this);
        listView = (NoScrollListView)view.findViewById(R.id.listView);
        mapLayout = (RelativeLayout)view.findViewById(R.id.mapLayout);
        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        listCity = new ArrayList<>();
        printAdapter = new ChinaPrintAdapter(listCity,getActivity());
        listView.setAdapter(printAdapter);
        SetLayoutParams.setLinearLayout(mapLayout, MyApplication.width,MyApplication.height-
                SystemUtil.dip2px(50)-SystemUtil.getStatusHeight(getActivity()));//设置布局高度= 屏幕高-状态栏-标题栏
        chinaMapView = (ChinaMapView)view.findViewById(R.id.map);
        chinaMapView.setOnProvinceSelectedListener(new ChinaMapView.OnProvinceSelectedListener() {
            @Override
            public void onprovinceSelected(ChinaMapView.Area pArea) {
                intent.setClass(getActivity(),AddFootActivity.class);
                intent.putExtra("type","china");
                intent.putExtra("name",pArea.getName());
                intent.putExtra("code",pArea.getCode());
                intent.putExtra("yearNum",travelYearNum);
                intent.putExtra("cityNum",travelCityNum);
                startActivityForResult(intent,0);
            }
        });
        chinaMapView.setSelectdColor(getResources().getColor(R.color.yellow));
        getFootprintChina(5);

        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        loadMoreFootprintChina(page);
                        refreshlayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
    }



    private void getFootprintChina(int pageCount){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("pageCount",pageCount);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getFootprintChina(sign, time_stamp, account_token, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            CityDetailsBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<CityDetailsBean>() {
                            }.getType());
                            travelYearNum = bean.getTravel_age();
                            travelCityNum = bean.getCity_number();
                            yearNum.setText(travelYearNum+"");
                            cityNum.setText(travelCityNum+"");
                            for (ChinaMapView.Area area : ChinaMapView.Area.values()) {
                                for (int i = 0;i<bean.getFootprintProvinces().size();i++){
                                    if (area.getCode().equals(bean.getFootprintProvinces().get(i).getProvince_code())){
                                        chinaMapView.setPaintColor(area,getResources().getColor(R.color.yellow),true);
                                    }else{
//                                        chinaMapView.setPaintColor(area,getResources().getColor(R.color.brown),true);
                                    }
                                }
                            }

                            if (bean.getFootprintChinaCityRets().size() > 0){
                                listCity.clear();
                                listCity.addAll(bean.getFootprintChinaCityRets());
                                printAdapter.setList(listCity);
                            }else {
                                if (listCity.size() != 0){
                                    ToastXutil.show("没有更多数据了");
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

    private void loadMoreFootprintChina(int page){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("pageCount",5);
        map.put("page",page);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.loadMoreFootprintChina(sign, time_stamp, account_token, page, 5, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<CityBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("footprintChinaCityRets").toString(),new TypeToken<List<CityBean>>() {
                            }.getType());
                            if (bean.size() > 0) {
                                listCity.addAll(bean);
                                printAdapter.setList(listCity);
                            } else {
                                if (listCity.size() != 0) {
                                    ToastXutil.show("没有更多数据了");
                                } else {
                                    printAdapter.setList(listCity);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (data != null){
                    String name = data.getStringExtra("cityName");
                    for (ChinaMapView.Area area : ChinaMapView.Area.values()) {
                        if (area.getName().equals(name)){
                            chinaMapView.setPaintColor(area,getResources().getColor(R.color.yellow),true);
                            getFootprintChina(page*5+1);
                        }
                    }
                }
                break;
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share:
                getShare();
                break;
        }
    }

    private void getShare(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("shareType","ZUJI");
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.getShare(account_token, time_stamp, sign, "ZUJI", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            String share_title = jsonObject.getJSONObject("data").getString("share_title");
                            String web_url = jsonObject.getJSONObject("data").getString("web_url");
                            WeChatManager.instance().sendWebPageToWX(getActivity(),true,web_url,R.mipmap.logo,share_title,"快来围观一下我的足迹呗！");
                            break;
                        default:
                            ToastXutil.show("分享失败");
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

    private void sendWebPageToWeiXin(){
        if (WeChatManager.instance().isWXAppInstalled()) {

        } else {
            ToastXutil.show(R.string.wechat_not_installed);
        }
    }
}

package com.cn.uca.ui.view.home.footprint;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.footprint.FootPrintAdapter;
import com.cn.uca.bean.home.footprint.CityBean;
import com.cn.uca.bean.home.footprint.CityDetailsBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.ChinaMapView;
import com.cn.uca.view.NoScrollListView;
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

public class FootPrintActivity extends BaseBackActivity implements View.OnClickListener {

    private ChinaMapView chinaMapView;
//    private View view;
    private RelativeLayout llTitle;
    private RelativeLayout mapLayout;
    private TextView back,yearNum,cityNum;
    private Intent intent;
    private int travelYearNum,travelCityNum;
    private NoScrollListView listView;
    private FootPrintAdapter printAdapter;
    private List<CityBean> listCity;
    private SmartRefreshLayout refreshLayout;
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_foot_print);

        initView();
    }

    private void initView() {
        intent = new Intent();
        llTitle = (RelativeLayout)findViewById(R.id.llTitle);
        StatusMargin.setLinearLayout(this,llTitle);
        mapLayout = (RelativeLayout)findViewById(R.id.mapLayout);
        back = (TextView)findViewById(R.id.back);
        yearNum = (TextView)findViewById(R.id.yearNum);
        cityNum = (TextView)findViewById(R.id.cityNum);
        listView = (NoScrollListView)findViewById(R.id.listView);
        refreshLayout = (SmartRefreshLayout)findViewById(R.id.refreshLayout);
        listCity = new ArrayList<>();
        printAdapter = new FootPrintAdapter(listCity,this);
        listView.setAdapter(printAdapter);
        back.setOnClickListener(this);
        SetLayoutParams.setLinearLayout(mapLayout, MyApplication.width,MyApplication.height-
                SystemUtil.dip2px(50)-SystemUtil.getStatusHeight(this));//设置布局高度= 屏幕高-状态栏-标题栏
        chinaMapView = (ChinaMapView)findViewById(R.id.map);
        chinaMapView.setOnProvinceSelectedListener(new ChinaMapView.OnProvinceSelectedListener() {
            @Override
            public void onprovinceSelected(ChinaMapView.Area pArea) {
                intent.setClass(getApplicationContext(),AddFootActivity.class);
                intent.putExtra("name",pArea.getName());
                intent.putExtra("code",pArea.getCode());
                intent.putExtra("yearNum",travelYearNum);
                intent.putExtra("cityNum",travelCityNum);
                startActivityForResult(intent,0);
            }
        });
        chinaMapView.setMapColor(getResources().getColor(R.color.black));
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
                            for (int i = 0;i<bean.getFootprintProvinces().size();i++){
                                for (ChinaMapView.Area area : ChinaMapView.Area.values()) {
                                    if (area.getCode().equals(bean.getFootprintProvinces().get(i).getProvince_code())){
                                        chinaMapView.setPaintColor(area,getResources().getColor(R.color.aa),true);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (data != null){
                    String name = data.getStringExtra("cityName");
                    for (ChinaMapView.Area area : ChinaMapView.Area.values()) {
                        if (area.getName().equals(name)){
                            chinaMapView.setPaintColor(area,getResources().getColor(R.color.aa),true);
                            getFootprintChina(page*5+1);
                        }
                    }
                }
                break;
        }
    }
}

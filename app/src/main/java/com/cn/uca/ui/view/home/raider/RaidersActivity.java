package com.cn.uca.ui.view.home.raider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.LetterAdapter;
import com.cn.uca.adapter.home.raiders.RaidersAdapter;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.CallBackValue;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.impl.raider.SubmitClickListener;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.popupwindows.BuyRaiderPopupWindow;
import com.cn.uca.server.home.HomeHttp;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaidersActivity extends BaseBackActivity implements View.OnClickListener,SubmitClickListener,CollectionClickListener{

    private TextView back,collection;
    private ListView letter;
    private GridView gridView;
    private LetterAdapter letterAdapter;
    private String [] data;
    private List<RaidersBean> list;
    private RaidersAdapter raidersAdapter;
    private RefreshLayout refreshLayout;
    private int page = 1;
    private int pageCount = 5;
    private String name,order;
    private double price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raiders);

        initView();
        cpyGetCityRaiders(page,pageCount,"");

    }

    private void initView() {
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        back = (TextView)findViewById(R.id.back);
        collection = (TextView)findViewById(R.id.collection);
        back.setOnClickListener(this);
        collection.setOnClickListener(this);
        letter = (ListView)findViewById(R.id.letter);
        gridView = (GridView) findViewById(R.id.gridView);
        data = getResources().getStringArray(R.array.letter_list);
        letterAdapter = new LetterAdapter(data,this);
        letter.setAdapter(letterAdapter);

        list = new ArrayList<>();
        raidersAdapter = new RaidersAdapter(list,this,this);
        gridView.setAdapter(raidersAdapter);

        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page ++ ;
                        cpyGetCityRaiders(page,pageCount,"");
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
                        refreshlayout.finishLoadmore();
                    }
                }, 2000);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.get(i).isLock()){
                    Intent intent = new Intent();
                    intent.setClass(RaidersActivity.this,RaidersDetailActivity.class);
                    intent.putExtra("id",list.get(i).getCity_raiders_id());
                    intent.putExtra("cityName",list.get(i).getCity_name());
                    startActivity(intent);
                }else{
                    purchaseCityRaiders(list.get(i).getCity_raiders_id());
                }
            }
        });
    }

    /**
     * 创建订单
     * @param id
     */
    private  void purchaseCityRaiders(int id){
        Map<String,Object> map = new HashMap<>();
        String timr_temp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",timr_temp);
        String token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", token);
        map.put("city_raiders_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.purchaseCityRaiders(sign, timr_temp, token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            name = jsonObject.getJSONObject("data").getString("city_name");
                            order = jsonObject.getJSONObject("data").getString("order_number");
                            price = jsonObject.getJSONObject("data").getDouble("payables");
                            new BuyRaiderPopupWindow(RaidersActivity.this, getWindow().getDecorView(),
                                    name, order, price, RaidersActivity.this);
                            break;
                        case 18:
                            ToastXutil.show("登录已过期");
                            break;
                        case 20:
                            ToastXutil.show("请先登录");
                            break;
                    }

                }catch (Exception e){
                    Log.i("123",e.getMessage());
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

    private void cpyGetCityRaiders(int page,int pageCount,String city_pinyin){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        String timr_temp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",timr_temp);
        map.put("city_pinyin",city_pinyin);
        String token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", token);
        String sign = SignUtil.sign(map);
        HomeHttp.cpyGetCityRaiders(page,pageCount, city_pinyin, sign, timr_temp, token, new CallBack() {
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
                            if (bean.size() > 0){
                                list.clear();
                                list.addAll(bean);
                                raidersAdapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
                                }else{
                                    raidersAdapter.setList(list);
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
            case R.id.collection:
                startActivity(new Intent(RaidersActivity.this,RaiderCollectionActivity.class));
                break;
        }
    }
    @Override
    public void clickCall(final PopupWindow window) {
        Map<String,Object> map = new HashMap<>();
        String timr_temp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",timr_temp);
        String token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", token);
        map.put("actual_payment",price);
        map.put("order_number",order);
        String sign = SignUtil.sign(map);
        HomeHttp.orderPayment(token, price, 0, order, sign, timr_temp, new CallBack() {
            @Override
            public void onResponse(Object response) {
                switch ((int)response){
                    case 0:
                        window.dismiss();
                        ToastXutil.show("支付成功");
                        cpyGetCityRaiders(page,pageCount,"");
                        break;
                    case 172:
                        window.dismiss();
                        ToastXutil.show("钱包余额不足");
                        break;
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
                        tv.setBackgroundResource(R.mipmap.nocollection);
                        list.get((int) v.getTag()).setCollection(false);
                        raidersAdapter.setList(list);
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
                        raidersAdapter.setList(list);
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

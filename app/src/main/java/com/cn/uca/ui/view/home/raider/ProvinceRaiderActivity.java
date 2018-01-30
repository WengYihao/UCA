package com.cn.uca.ui.view.home.raider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.raiders.LetterAdapter;
import com.cn.uca.adapter.home.raiders.RaidersAdapter;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.bean.user.order.OrderTypeBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.raider.BuyRaiderImpl;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.loading.LoadingLayout;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.fragment.home.raider.CityFragment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProvinceRaiderActivity extends BaseBackActivity implements View.OnClickListener,CollectionClickListener,BuyRaiderImpl{

    private TextView back;
    private ListView letter;
    private List<OrderTypeBean> letterString;
    private GridView gridView;
    private LetterAdapter letterAdapter;
    private String [] data;
    private List<RaidersBean> list;
    private RaidersAdapter raidersAdapter;
    private LoadingLayout loading;
    private RefreshLayout refreshLayout;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private String cityStr = "";
    private int type = 0;//刷新加载状态1：刷新2：加载3：搜索
    private int integral,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_raider);

        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        loading = (LoadingLayout)findViewById(R.id.loading);
        letter = (ListView)findViewById(R.id.letter);
        gridView = (GridView)findViewById(R.id.gridView);
        data = getResources().getStringArray(R.array.letter_list);
        letterString = new ArrayList<>();

        OrderTypeBean bean = null;
        for (int i = 0;i<data.length;i++){
            bean = new OrderTypeBean();
            bean.setCheck(false);
            bean.setName(data[i]);
            letterString.add(bean);
        }
        letterAdapter = new LetterAdapter(letterString,this);
        letter.setAdapter(letterAdapter);

        back.setOnClickListener(this);
        list = new ArrayList<>();
        raidersAdapter = new RaidersAdapter(list,this,this);
        gridView.setAdapter(raidersAdapter);

        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        type = 1;
                        if (page*pageCount >= 30){
                            cpyGetCityRaiders(1,30);
                        }else{
                            cpyGetCityRaiders(1,pageCount*page);
                        }
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
                        type = 2;
                        page ++ ;
                        cpyGetCityRaiders(page,pageCount);
                        refreshlayout.finishLoadmore();
                    }
                }, 500);
            }
        });

        letter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (OrderTypeBean typeBean :letterString){
                    typeBean.setCheck(false);
                }
                letterString.get(position).setCheck(true);
                letterAdapter.notifyDataSetChanged();
                cityStr = data[position];
                list.clear();
                page = 1;
                type = 3;
                cpyGetCityRaiders(page,pageCount);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.get(i).isLock()){
                    Intent intent = new Intent();
                    intent.setClass(ProvinceRaiderActivity.this,RaidersDetailActivity.class);
                    intent.putExtra("id",list.get(i).getCity_raiders_id());
                    intent.putExtra("cityName",list.get(i).getCity_name());
                    startActivity(intent);
                }else{
                    //积分兑换
                    ShowPopupWindow.raiderIntegral(getWindow().getDecorView(),ProvinceRaiderActivity.this,list.get(i).getCity_raiders_id(),list.get(i).getPacture_url(),list.get(i).getCity_name(),integral,ProvinceRaiderActivity.this);
                }
            }
        });
    }

    private void cpyGetCityRaiders(int page,int pageCount){
        loading.setStatus(LoadingLayout.Loading);
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("city_pinyin",cityStr);
        String timr_temp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",timr_temp);
        String token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", token);
        map.put("province_id", id);
        String sign = SignUtil.sign(map);
        HomeHttp.cpyGetCityRaiders(page,pageCount, cityStr,id, sign, timr_temp, token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                loading.setStatus(LoadingLayout.Success);
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            integral = jsonObject.getJSONObject("data").getInt("integral");
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("cityRaidersRets");
                            Gson gson = new Gson();
                            List<RaidersBean> bean = gson.fromJson(array.toString(),new TypeToken<List<RaidersBean>>() {
                            }.getType());
                            switch (type){
                                case 0:
                                    if (bean.size() > 0){
                                        list.addAll(bean);
                                        loading.setStatus(LoadingLayout.Success);
                                        raidersAdapter.setList(list);
                                    }else{
                                        loading.setStatus(LoadingLayout.Empty);
                                    }
                                    break;
                                case 1:
                                    if (bean.size() > 0){
                                        list.clear();
                                        list.addAll(bean);
                                        Log.e("123",list.size()+"--"+list.toString());
                                        loading.setStatus(LoadingLayout.Success);
                                        raidersAdapter.setList(list);
                                    }else{
                                        if (list.size() > 0){
                                            //没有数据
                                        }else{
                                            loading.setStatus(LoadingLayout.Empty);
                                            raidersAdapter.setList(bean);
                                        }
                                    }
                                    break;
                                case 2:
                                    if (bean.size() > 0){
                                        list.addAll(bean);
                                        loading.setStatus(LoadingLayout.Success);
                                        raidersAdapter.setList(list);
                                    }else{
                                        if (list.size() > 0){
                                            //没有数据
                                        }else{
                                            loading.setStatus(LoadingLayout.Empty);
                                            raidersAdapter.setList(bean);
                                        }
                                    }
                                    break;
                                case 3:
                                    if (bean.size() > 0){
                                        list.addAll(bean);
                                        loading.setStatus(LoadingLayout.Success);
                                        raidersAdapter.setList(list);
                                    }else{
                                        if (list.size() > 0){
                                            //没有数据
                                        }else{
                                            loading.setStatus(LoadingLayout.Empty);
                                            raidersAdapter.setList(bean);
                                        }
                                    }
                                    break;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void purchaseCityRaiders(int city_raiders_id){
        Map<String,Object> map = new HashMap<>();
        map.put("city_raiders_id",city_raiders_id);
        String timr_temp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",timr_temp);
        String token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", token);
        String sign = SignUtil.sign(map);
        HomeHttp.purchaseCityRaiders(sign, timr_temp, token, city_raiders_id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            ToastXutil.show("兑换成功");
                            refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
                            break;
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                ToastXutil.show(errorMsg);
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

    @Override
    public void buyRaider(int id) {
        purchaseCityRaiders(id);
    }
}

package com.cn.uca.ui.fragment.home.raider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.raiders.LetterAdapter;
import com.cn.uca.adapter.home.raiders.ProvinceAdapter;
import com.cn.uca.adapter.home.raiders.RaidersAdapter;
import com.cn.uca.bean.home.raider.ProvinceBean;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.bean.user.order.OrderTypeBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.loading.LoadingLayout;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.raider.ProvinceRaiderActivity;
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
 * Created by asus on 2018/1/25.
 */

public class ProvinceFragment extends Fragment{

    private View view;
    private ListView letter;
    private List<OrderTypeBean> letterString;
    private GridView gridView;
    private LetterAdapter letterAdapter;
    private String [] data;
    private List<ProvinceBean> list;
    private ProvinceAdapter provinceAdapter;
    private LoadingLayout loading;
    private RefreshLayout refreshLayout;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private String provinceStr = "";
    private int type = 0;//刷新加载状态1：刷新2：加载3：搜索

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_province_city,null);

        initView();
        getProvinceRaiders(provinceStr);
        return view;
    }

    private void initView() {
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        loading = (LoadingLayout)view.findViewById(R.id.loading);
        letter = (ListView)view.findViewById(R.id.letter);
        gridView = (GridView)view.findViewById(R.id.gridView);
        data = getResources().getStringArray(R.array.letter_list);
        letterString = new ArrayList<>();
        OrderTypeBean bean = null;
        for (int i = 0;i<data.length;i++){
            bean = new OrderTypeBean();
            bean.setCheck(false);
            bean.setName(data[i]);
            letterString.add(bean);
        }
        letterAdapter = new LetterAdapter(letterString,getActivity());
        letter.setAdapter(letterAdapter);

        list = new ArrayList<>();
        provinceAdapter = new ProvinceAdapter(list,getActivity());
        gridView.setAdapter(provinceAdapter);

//        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(final RefreshLayout refreshlayout) {
//                refreshlayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        type = 1;
//                        if (page*pageCount >= 30){
//                            getProvinceRaiders(provinceStr);
//                        }else{
//                            getProvinceRaiders(provinceStr);
//                        }
//                        refreshlayout.finishRefresh();
//                        refreshlayout.setLoadmoreFinished(false);
//                    }
//                }, 2000);
//            }
//        });
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(final RefreshLayout refreshlayout) {
//                refreshlayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        type = 2;
//                        page ++ ;
//                        cpyGetCityRaiders(page,pageCount,cityStr);
//                        refreshlayout.finishLoadmore();
//                    }
//                }, 2000);
//            }
//        });
//
        letter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (OrderTypeBean typeBean :letterString){
                    typeBean.setCheck(false);
                }
                letterString.get(position).setCheck(true);
                letterAdapter.notifyDataSetChanged();
                provinceStr = data[position];
                list.clear();
                page = 1;
                type = 3;
                getProvinceRaiders(provinceStr);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ProvinceRaiderActivity.class);
                intent.putExtra("id",list.get(i).getProvince_id());
                startActivity(intent);
            }
        });
    }

    private void getProvinceRaiders(String province_pinyin){
        loading.setStatus(LoadingLayout.Loading);
        Map<String,Object> map = new HashMap<>();
        String timr_temp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",timr_temp);
        map.put("province_pinyin",province_pinyin);
        String token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", token);
        String sign = SignUtil.sign(map);
        HomeHttp.getProvinceRaiders(province_pinyin, sign, timr_temp, token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                loading.setStatus(LoadingLayout.Success);
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONArray("data");
                            Gson gson = new Gson();
                            List<ProvinceBean> bean = gson.fromJson(array.toString(),new TypeToken<List<ProvinceBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);
                                loading.setStatus(LoadingLayout.Success);
                                provinceAdapter.setList(list);
                            }else{
                                loading.setStatus(LoadingLayout.Empty);
                            }
                            break;
//                        case 0:
//
//                            switch (type){
//
//                                case 1:
//                                    if (bean.size() > 0){
//                                        list.clear();
//                                        list.addAll(bean);
//                                        Log.e("123",list.size()+"--"+list.toString());
//                                        loading.setStatus(LoadingLayout.Success);
//                                        raidersAdapter.setList(list);
//                                    }else{
//                                        if (list.size() > 0){
//                                            //没有数据
//                                        }else{
//                                            loading.setStatus(LoadingLayout.Empty);
//                                            raidersAdapter.setList(bean);
//                                        }
//                                    }
//                                    break;
//                                case 2:
//                                    if (bean.size() > 0){
//                                        list.addAll(bean);
//                                        loading.setStatus(LoadingLayout.Success);
//                                        raidersAdapter.setList(list);
//                                    }else{
//                                        if (list.size() > 0){
//                                            //没有数据
//                                        }else{
//                                            loading.setStatus(LoadingLayout.Empty);
//                                            raidersAdapter.setList(bean);
//                                        }
//                                    }
//                                    break;
//                                case 3:
//                                    if (bean.size() > 0){
//                                        list.addAll(bean);
//                                        loading.setStatus(LoadingLayout.Success);
//                                        raidersAdapter.setList(list);
//                                    }else{
//                                        if (list.size() > 0){
//                                            //没有数据
//                                        }else{
//                                            loading.setStatus(LoadingLayout.Empty);
//                                            raidersAdapter.setList(bean);
//                                        }
//                                    }
//                                    break;
//                            }
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

package com.cn.uca.ui.fragment.home.travel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.DomesticTravelPlaceAdapter;
import com.cn.uca.adapter.home.travel.StyleAdapter;
import com.cn.uca.bean.home.travel.StyleBean;
import com.cn.uca.bean.home.travel.TravelBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/3/2.
 * 出境游--
 */

public class ExitFragment extends Fragment  {

    private View view;
    private ListView listView;
    private GridView gridView;
    private StyleAdapter styleAdapter;
    private DomesticTravelPlaceAdapter travelPlaceAdapter;
    private List<StyleBean> list;
    private List<TravelBean> list2;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private String code = "";

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exit,null);

        initView();
        getStyle();
        getTravel("outbound",0,"",0,"");
        return view;
    }

    private void initView() {
        listView = (ListView)view.findViewById(R.id.listView);
        gridView = (GridView)view.findViewById(R.id.gridView);

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        styleAdapter = new StyleAdapter(list,getActivity());
        listView.setAdapter(styleAdapter);

        travelPlaceAdapter = new DomesticTravelPlaceAdapter(list2,getActivity(), (MyApplication.width-SystemUtil.dip2px(100))/2,(MyApplication.width-SystemUtil.dip2px(100))/2);
        gridView.setAdapter(travelPlaceAdapter);
    }
    private void getStyle(){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String sign = SignUtil.sign(map);
        HomeHttp.getStyle(sign, time_stamp, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<StyleBean> bean = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<StyleBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);
                                styleAdapter.setList(list);
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
    private void getTravel(String type, int regionId, String py, int styleId, final String sort){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("gaode_code",code);
        map.put("tourismType",type);
        map.put("city_pinyin",py);
        map.put("sort",sort);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getTourism(sign, time_stamp, account_token,code,type,regionId,py,styleId,sort,page,pageCount,new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<TravelBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("tourisms").toString(),new TypeToken<List<TravelBean>>() {
                            }.getType());
                            if (bean.size()>0){
                                list2.addAll(bean);
                                travelPlaceAdapter.setList(list2);
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage()+"---");
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

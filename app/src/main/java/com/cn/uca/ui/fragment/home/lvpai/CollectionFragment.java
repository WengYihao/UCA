package com.cn.uca.ui.fragment.home.lvpai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.CollectionAdapter;
import com.cn.uca.bean.home.lvpai.CommodityBean;
import com.cn.uca.bean.home.lvpai.MerchantBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.lvpai.CommodityDetailActivity;
import com.cn.uca.ui.view.home.lvpai.MerchantDetailActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/12/18.
 */

public class CollectionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView type;
    private CollectionAdapter adapter;
    private List<MerchantBean> listMerchant;
    private List<CommodityBean> listComment;
    private ListView listView;
    private int id = 1;
    private int page = 1;
    private int pageCount = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collection, null);

        initView();
        getFollowMers();
        return view;
    }

    private void initView() {
        type = (TextView)view.findViewById(R.id.type);
        listView = (ListView)view.findViewById(R.id.listView);

        type.setOnClickListener(this);

        listMerchant = new ArrayList<>();
        listComment = new ArrayList<>();
        adapter = new CollectionAdapter(listMerchant,getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                Intent intent = new Intent();
                switch (id){
                    case 1:
                        intent.setClass(getActivity(),MerchantDetailActivity.class);
                        intent.putExtra("id",listMerchant.get(position).getTrip_shoot_merchant_id());
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(getActivity(),CommodityDetailActivity.class);
                        intent.putExtra("id",listComment.get(position).getTrip_shoot_id());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void getCollTs(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getCollTs(time_stamp, sign, account_token, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.e("456", jsonObject.toString() + "--");
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("tripShoots");
                            List<CommodityBean> bean = gson.fromJson(array.toString(), new TypeToken<List<CommodityBean>>() {
                            }.getType());
                            if (bean.size() > 0) {
                                listComment.addAll(bean);
                                adapter.setList(listComment);
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.e("456", e.getMessage() + "--");
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

    private void getFollowMers(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getFollowMers(time_stamp, sign, account_token, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code) {
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("merchants");
                            List<MerchantBean> bean = gson.fromJson(array.toString(), new TypeToken<List<MerchantBean>>() {
                            }.getType());
                            if (bean.size() > 0) {
                                listMerchant.addAll(bean);
                                adapter.setList(listMerchant);
                            }
                    }
                }catch (Exception e){
                    Log.e("456", e.getMessage() + "--");
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
        switch (v.getId()) {
            case R.id.type:
                switch (id) {
                    case 1:
                        type.setText("商品");
                        id = 2;
                        if (listComment.size() > 0){
                            adapter.setList(listComment);
                        }else{
                            getCollTs();
                        }
                        break;
                    case 2:
                        type.setText("商家");
                        id = 1;
                        if (listMerchant.size() > 0){
                            adapter.setList(listMerchant);
                        }else{
                            getFollowMers();
                        }
                        break;
                }
                break;
        }
    }
}

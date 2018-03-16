package com.cn.uca.ui.fragment.home.samecityka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.CollectionAdapter;
import com.cn.uca.bean.home.samecityka.CollectionBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.samecityka.ActionDetailActivity;
import com.cn.uca.ui.view.home.samecityka.ActionManageActivity;
import com.cn.uca.ui.view.home.samecityka.MyFollowActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/1/4.
 * 发起者-我的活动-全部
 */

public class ActionFragment extends Fragment {

    private View view;
    private ListView listView;
    private CollectionAdapter adapter;
    private List<CollectionBean> list;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_action,null);

        initView();
        getMyCityCafe();
        return view;
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new CollectionAdapter(list,getActivity());
        listView = (ListView)view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ActionManageActivity.class);
                intent.putExtra("id",list.get(position).getCity_cafe_id());
                startActivity(intent);
            }
        });
    }

    private void getMyCityCafe(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("state","All");
        String sign = SignUtil.sign(map);
        HomeHttp.getMyCityCafe(account_token, time_stamp, sign, page, pageCount, "All", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<CollectionBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("cityCafes").toString(),new TypeToken<List<CollectionBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);
                                adapter.setList(list);
                            }else{
                                ToastXutil.show("暂无数据");
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
}

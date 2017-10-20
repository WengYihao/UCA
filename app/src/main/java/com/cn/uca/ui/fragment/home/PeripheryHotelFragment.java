package com.cn.uca.ui.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.PeripheryHotleAdapter;
import com.cn.uca.bean.home.travel.PeripheryHotelBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/10/9.
 */

public class PeripheryHotelFragment extends Fragment {

    private View view;
    private ListView listView;
    private PeripheryHotleAdapter adapter;
    private List<PeripheryHotelBean> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hotel_periphery,null);

        initView();
        getJiuDian();
        return view;
    }

    private void initView() {
        listView = (ListView)view.findViewById(R.id.listView);

        list = new ArrayList<>();
        adapter = new PeripheryHotleAdapter(list,getActivity());
        listView.setAdapter(adapter);

    }

    private void getJiuDian(){
        HomeHttp.getJiuDian(1, 5, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("juiDianRets");
                            Gson gson = new Gson();
                            List<PeripheryHotelBean> bean = gson.fromJson(array.toString(),new TypeToken<List<PeripheryHotelBean>>() {
                            }.getType());
                            list.addAll(bean);
                            adapter.setList(list);
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

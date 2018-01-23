package com.cn.uca.ui.fragment.user.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.user.OrderAdapter;
import com.cn.uca.bean.user.OrderBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.TypeClickCallBack;
import com.cn.uca.loading.LoadingLayout;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.user.order.YueKaOrderDetailActivity;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/1/11.
 */

public class YueKaFragment extends Fragment implements View.OnClickListener,TypeClickCallBack {

    private View view;
    private LoadingLayout loading;
    private RadioButton title01,title02,title03,title04,title05;
    private List<OrderBean> list;
    private OrderAdapter orderAdapter;
    private ListView listView;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yue_ka,null);

        initView();
        return view;
}
    private void initView() {
        loading = (LoadingLayout)view.findViewById(R.id.loading);
        title01 = (RadioButton)view.findViewById(R.id.title01);
        title02 = (RadioButton)view.findViewById(R.id.title02);
        title03 = (RadioButton)view.findViewById(R.id.title03);
        title04 = (RadioButton)view.findViewById(R.id.title04);
        title05 = (RadioButton)view.findViewById(R.id.title05);
        listView = (ListView)view.findViewById(R.id.listView);
        list = new ArrayList<>();
        orderAdapter = new OrderAdapter(list,getActivity(),this);
        listView.setAdapter(orderAdapter);

        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
        title04.setOnClickListener(this);
        title05.setOnClickListener(this);
        title01.setChecked(true);
        getUserOrder("all");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),YueKaOrderDetailActivity.class);
                intent.putExtra("id",list.get(position).getUser_order_id());
                startActivity(intent);
            }
        });
    }

    private void getUserOrder(String state){
        UserHttp.getUserOrder(page, pageCount,1 , state, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("getUserOrderInfos");
                            List<OrderBean> bean = gson.fromJson(array.toString(),new TypeToken<List<OrderBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.clear();
                                list.addAll(bean);
                                loading.setStatus(LoadingLayout.Success);
                                orderAdapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    list.clear();
                                    loading.setStatus(LoadingLayout.Success);
                                    orderAdapter.setList(list);
                                    ToastXutil.show("没有更多数据了");
                                }else {
                                    //没有数据
                                    loading.setStatus(LoadingLayout.Empty);
                                }
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage());
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
            case R.id.title01:
                list.clear();
                getUserOrder("all");
                break;
            case R.id.title02:
                list.clear();
                getUserOrder("waitingPayment");
                break;
            case R.id.title03:
                list.clear();
                getUserOrder("alreadyPaid");
                break;
            case R.id.title04:
                list.clear();
                getUserOrder("refund");
                break;
            case R.id.title05:
                list.clear();
                getUserOrder("complete");
                break;
        }
    }

    @Override
    public void onClick(int type, View v) {

    }
}


package com.cn.uca.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.OrderAdapter;
import com.cn.uca.bean.OrderBean;
import com.cn.uca.util.ToastXutil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/24.
 */

public class OrderFragment  extends Fragment implements View.OnClickListener{

    private View view;
    private RadioButton title01,title02,title03,title04,title05;
    private String type;
    private List<OrderBean> allList,hotelList,titckList;
    private OrderAdapter orderAdapter;
    private ListView listView;

    public static OrderFragment newInstance(String type){
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order,null);

        initDate();
        initView();

        return view;
    }

    private void initDate(){
        allList = new ArrayList<>();
        hotelList = new ArrayList<>();
        titckList = new ArrayList<>();
        OrderBean bean1 = new OrderBean();
        bean1.setType("酒店");
        bean1.setName("深圳维也纳酒店");
        bean1.setTime("2017-08-24 -- 2017-08-26");
        bean1.setPlace("豪华总统套房");
        bean1.setPrice(650);
        bean1.setState("待支付");
        allList.add(bean1);
        hotelList.add(bean1);
        OrderBean bean2 = new OrderBean();
        bean2.setType("约咖");
        bean2.setName("深圳包车一日游");
        bean2.setTime("2017-08-24 08:00 -21:00");
        bean2.setPlace("深圳西站");
        bean2.setPrice(300);
        bean2.setState("待确认");
        allList.add(bean2);
        OrderBean bean3 = new OrderBean();
        bean3.setType("机票");
        bean3.setName("北京-上海");
        bean3.setTime("2017-08-24 08:00");
        bean3.setPlace("中国南方航空");
        bean3.setPrice(1300);
        bean3.setState("订单取消");
        allList.add(bean3);
        titckList.add(bean3);
    }
    private void initView() {
        type = getArguments().getString("type");

        title01 = (RadioButton)view.findViewById(R.id.title01);
        title02 = (RadioButton)view.findViewById(R.id.title02);
        title03 = (RadioButton)view.findViewById(R.id.title03);
        title04 = (RadioButton)view.findViewById(R.id.title04);
        title05 = (RadioButton)view.findViewById(R.id.title05);

        listView = (ListView)view.findViewById(R.id.listView);

        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
        title04.setOnClickListener(this);
        title05.setOnClickListener(this);

        title01.setChecked(true);

        switch (type){
            case "1":
                orderAdapter = new OrderAdapter(allList,getActivity());
                listView.setAdapter(orderAdapter);
                break;
            case "2":

                break;
            case "3":

                break;
            case "4":

                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.title01:
                switch (type){
                    case "1":
                        ToastXutil.show("全部--全部");
                        orderAdapter = new OrderAdapter(allList,getActivity());
                        listView.setAdapter(orderAdapter);
                        orderAdapter.notifyDataSetChanged();
                        break;
                    case "2":
                        ToastXutil.show("有效单--全部");
                        break;
                    case "3":
                        ToastXutil.show("待支付--全部");
                        break;
                    case "4":
                        ToastXutil.show("退款单--全部");
                        break;
                }
                break;
            case R.id.title02:
                switch (type){
                    case "1":
                        ToastXutil.show("全部--机票");
                        orderAdapter = new OrderAdapter(titckList,getActivity());
                        listView.setAdapter(orderAdapter);
                        orderAdapter.notifyDataSetChanged();
                        break;
                    case "2":
                        ToastXutil.show("有效单--机票");
                        break;
                    case "3":
                        ToastXutil.show("待支付--机票");
                        break;
                    case "4":
                        ToastXutil.show("退款单--机票");
                        break;
                }
                break;
            case R.id.title03:
                switch (type){
                    case "1":
                        ToastXutil.show("全部--酒店");
                        orderAdapter = new OrderAdapter(hotelList,getActivity());
                        listView.setAdapter(orderAdapter);
                        orderAdapter.notifyDataSetChanged();
                        break;
                    case "2":
                        ToastXutil.show("有效单--酒店");
                        break;
                    case "3":
                        ToastXutil.show("待支付--酒店");
                        break;
                    case "4":
                        ToastXutil.show("退款单--酒店");
                        break;
                }
                break;
            case R.id.title04:
                switch (type){
                    case "1":
                        ToastXutil.show("全部--旅游");
                        break;
                    case "2":
                        ToastXutil.show("有效单--旅游");
                        break;
                    case "3":
                        ToastXutil.show("待支付--旅游");
                        break;
                    case "4":
                        ToastXutil.show("退款单--旅游");
                        break;
                }
                break;
            case R.id.title05:
                switch (type){
                    case "1":
                        ToastXutil.show("全部--约咖");
                        break;
                    case "2":
                        ToastXutil.show("有效单--约咖");
                        break;
                    case "3":
                        ToastXutil.show("待支付--约咖");
                        break;
                    case "4":
                        ToastXutil.show("退款单--约咖");
                        break;
                }
                break;
        }

    }
}

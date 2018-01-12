//package com.cn.uca.ui.fragment.user.order;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//import android.widget.RadioButton;
//
//import com.android.volley.VolleyError;
//import com.cn.uca.R;
//import com.cn.uca.adapter.user.OrderAdapter;
//import com.cn.uca.bean.user.OrderBean;
//import com.cn.uca.impl.CallBack;
//import com.cn.uca.impl.yueka.TypeClickCallBack;
//import com.cn.uca.server.home.HomeHttp;
//import com.cn.uca.server.user.UserHttp;
//import com.cn.uca.ui.view.user.BackOrderActivity;
//import com.cn.uca.util.SharePreferenceXutil;
//import com.cn.uca.util.SignUtil;
//import com.cn.uca.util.SystemUtil;
//import com.cn.uca.util.ToastXutil;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by asus on 2017/8/24.
// * 订单
// */
//
//public class OrderFragment  extends Fragment implements View.OnClickListener,TypeClickCallBack{
//
//    private View view;
//    private RadioButton title01,title02,title03,title04;
//    private String type;
//    private List<OrderBean> list;
//    private OrderAdapter orderAdapter;
//    private ListView listView;
//    private int page = 1;
//    private int pageCount = 5;
//
//    public static OrderFragment newInstance(String type){
//        OrderFragment orderFragment = new OrderFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("type",type);
//        orderFragment.setArguments(bundle);
//        return orderFragment;
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_order,null);
//        initView();
//
//        return view;
//    }
//
//    private void getUserOrder(int commodity_id,int user_order_state_id){
//        UserHttp.getUserOrder(page, pageCount, commodity_id, user_order_state_id, new CallBack() {
//            @Override
//            public void onResponse(Object response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.toString());
//                    int code = jsonObject.getInt("code");
//                    switch (code){
//                        case 0:
//                            Gson gson = new Gson();
//                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("getUserOrderInfos");
//                            List<OrderBean> bean = gson.fromJson(array.toString(),new TypeToken<List<OrderBean>>() {
//                            }.getType());
//                            if (bean.size() > 0){
//                                list.clear();
//                                list.addAll(bean);
//                                orderAdapter.setList(list);
//                            }else{
//                               if (list.size() != 0){
//                                   list.clear();
//                                   orderAdapter.setList(list);
//                                   ToastXutil.show("没有更多数据了");
//                               }else {
//                                   list.clear();
//                                   orderAdapter.setList(list);
//                                   ToastXutil.show("暂无数据");
//                               }
//                            }
//                            break;
//                    }
//                }catch (Exception e){
//                    Log.i("456",e.getMessage());
//                }
//            }
//
//            @Override
//            public void onErrorMsg(String errorMsg) {
//
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//
//            }
//        });
//    }
//    private void initView() {
//        type = getArguments().getString("type");
//
//        title01 = (RadioButton)view.findViewById(R.id.title01);
//        title02 = (RadioButton)view.findViewById(R.id.title02);
//        title03 = (RadioButton)view.findViewById(R.id.title03);
//        title04 = (RadioButton)view.findViewById(R.id.title04);
////        title05 = (RadioButton)view.findViewById(R.id.title05);
////        title06 = (RadioButton)view.findViewById(R.id.title06);
////        title07 = (RadioButton)view.findViewById(R.id.title07);
//
//        listView = (ListView)view.findViewById(R.id.listView);
//        list = new ArrayList<>();
//        orderAdapter = new OrderAdapter(list,getActivity(),this);
//        listView.setAdapter(orderAdapter);
//
//        title01.setOnClickListener(this);
//        title02.setOnClickListener(this);
//        title03.setOnClickListener(this);
//        title04.setOnClickListener(this);
////        title05.setOnClickListener(this);
////        title06.setOnClickListener(this);
////        title07.setOnClickListener(this);
//
//        title01.setChecked(true);
//
//        switch (type){
//            case "0":
//                getUserOrder(0,0);
//                break;
//            case "1":
//                getUserOrder(1,0);
//                break;
//            case "2":
//                getUserOrder(2,0);
//                break;
//            case "3":
//                getUserOrder(3,0);
//                break;
//            case "4":
//                getUserOrder(4,0);
//                break;
//            case "5":
//                getUserOrder(5,0);
//                break;
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()){
//            case R.id.title01:
//                switch (type){
//                    case "0":
//                        getUserOrder(0,0);
//                        break;
//                    case "1":
//                        getUserOrder(1,0);
//                        break;
//                    case "2":
//                        getUserOrder(2,0);
//                        break;
//                    case "3":
//                        getUserOrder(3,0);
//                        break;
//                    case "4":
//                        getUserOrder(4,0);
//                        break;
//                    case "5":
//                        getUserOrder(5,0);
//                        break;
//                }
//                break;
//            case R.id.title02:
//                switch (type){
//                    case "0":
//                        getUserOrder(0,1);
//                        break;
//                    case "1":
//                        getUserOrder(1,1);
//                        break;
//                    case "2":
//                        getUserOrder(2,1);
//                        break;
//                    case "3":
//                        getUserOrder(3,1);
//                        break;
//                    case "4":
//                        getUserOrder(4,1);
//                        break;
//                    case "5":
//                        getUserOrder(5,1);
//                        break;
//                }
//                break;
//            case R.id.title03:
//                switch (type){
//                    case "0":
//                        getUserOrder(0,2);
//                        break;
//                    case "1":
//                        getUserOrder(1,2);
//                        break;
//                    case "2":
//                        getUserOrder(2,2);
//                        break;
//                    case "3":
//                        getUserOrder(3,2);
//                        break;
//                    case "4":
//                        getUserOrder(4,2);
//                        break;
//                    case "5":
//                        getUserOrder(5,2);
//                        break;
//                }
//                break;
//            case R.id.title04:
//                switch (type){
//                    case "0":
//                        getUserOrder(0,3);
//                        break;
//                    case "1":
//                        getUserOrder(1,3);
//                        break;
//                    case "2":
//                        getUserOrder(2,3);
//                        break;
//                    case "3":
//                        getUserOrder(3,3);
//                        break;
//                    case "4":
//                        getUserOrder(4,3);
//                        break;
//                    case "5":
//                        getUserOrder(5,3);
//                        break;
//                }
//                break;
//        }
//
//    }
//
//    @Override
//    public void onClick(int type, View v) {
//        switch (type){
//            case 1://退单
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), BackOrderActivity.class);
//                intent.putExtra("orderId",list.get((int)v.getTag()).getUser_order_id());
//                startActivity(intent);
//                break;
//            case 2://结算
//                double price = list.get((int)v.getTag()).getOrder_price();
//                String order = list.get((int)v.getTag()).getOrder_number();
//                pay(price,order);
//                break;
//        }
//    }
//
//    /**
//     * 支付结算（钱包）
//     * @param price
//     * @param order
//     */
//    private void pay(double price,String order){
//        Map<String,Object> map = new HashMap<>();
//        String timr_temp = SystemUtil.getCurrentDate2();
//        map.put("time_stamp",timr_temp);
//        String token = SharePreferenceXutil.getAccountToken();
//        map.put("account_token", token);
//        map.put("actual_payment",price);
//        map.put("order_number",order);
//        String sign = SignUtil.sign(map);
//        HomeHttp.orderPayment(token, price, 0, order, sign, timr_temp, new CallBack() {
//            @Override
//            public void onResponse(Object response) {
//                switch ((int)response){
//                    case 0:
//                        ToastXutil.show("支付成功");
//                        getUserOrder(0,0);
//                        break;
//                    case 172:
//                        ToastXutil.show("钱包余额不足");
//                        break;
//                }
//            }
//
//            @Override
//            public void onErrorMsg(String errorMsg) {
//
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//
//            }
//        });
//    }
//
//}

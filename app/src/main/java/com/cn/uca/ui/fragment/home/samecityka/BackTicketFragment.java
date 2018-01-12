package com.cn.uca.ui.fragment.home.samecityka;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.BackTicketAdapter;
import com.cn.uca.bean.home.samecityka.BackTicketBean;
import com.cn.uca.bean.home.samecityka.SignUpBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.samecityka.ExamineItemBack;
import com.cn.uca.server.home.HomeHttp;
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
 * Created by asus on 2017/12/6.
 * 审核-退票
 */

public class BackTicketFragment extends Fragment implements ExamineItemBack{
    private View view;
    private ListView listView;
    private BackTicketAdapter adapter;
    private List<BackTicketBean> list;
    private int page = 1;
    private int pageCount = 5;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_back_ticket,null);
        initView();
        getRefundTicket();
        return view;
    }

    private void initView() {
        listView = (ListView)view.findViewById(R.id.listView);

        list = new ArrayList<>();
        adapter = new BackTicketAdapter(list,getActivity(),this);
        listView.setAdapter(adapter);
    }

    private void getRefundTicket(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getRefundTicket(account_token, time_stamp, sign, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code) {
                        case 0:
                            Gson gson = new Gson();
                            List<BackTicketBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("refundTickets").toString(),
                                    new TypeToken<List<BackTicketBean>>() {
                                    }.getType());
                            if (bean.size() > 0){
                                list.clear();
                                list.addAll(bean);
                                adapter.setList(list);
                            }else{
                                ToastXutil.show("暂无数据");
                            }
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
    public void more(View v) {

    }

    @Override
    public void refuse(View v) {
        int id = list.get((int) v.getTag()).getRefund_ticket_id();
        approvalRefundTicket(id,"no");
    }

    @Override
    public void adopt(View v) {
        int id = list.get((int) v.getTag()).getRefund_ticket_id();
        approvalRefundTicket(id,"yes");
    }

    private void approvalRefundTicket(int id,final String state){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("refund_ticket_id",id);
        map.put("state",state);
        String sign = SignUtil.sign(map);
        HomeHttp.approvalRefundTicket(account_token, time_stamp, sign, id, state, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            switch (state){
                                case "yes":
                                    ToastXutil.show("已同意");
                                    getRefundTicket();
                                    break;
                                case "no":
                                    ToastXutil.show("已拒绝");
                                    getRefundTicket();
                                    break;
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
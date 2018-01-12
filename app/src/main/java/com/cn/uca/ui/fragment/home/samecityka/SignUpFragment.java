package com.cn.uca.ui.fragment.home.samecityka;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.SignUpAdapter;
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
 * 报名
 */

public class SignUpFragment extends Fragment implements ExamineItemBack{
    private View view;
    private ListView listView;
    private SignUpAdapter adapter;
    private List<SignUpBean> list;
    private int page = 1;
    private int pageCount = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up,null);

        initView();
        getTicketApproval();
        return view;
    }

    private void initView() {
        listView = (ListView)view.findViewById(R.id.listView);

        list = new ArrayList<>();
        adapter = new SignUpAdapter(list,getActivity(),this);
        listView.setAdapter(adapter);
    }

    private void getTicketApproval(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getTicketApproval(account_token, time_stamp, sign, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code) {
                        case 0:
                            Gson gson = new Gson();
                            List<SignUpBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("ticketApprovals").toString(),
                                    new TypeToken<List<SignUpBean>>() {
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
        ToastXutil.show("更多");
    }

    @Override
    public void refuse(View v) {
        int id = list.get((int) v.getTag()).getTicket_approval_content_id();
        approvalTicket(id,"no");
    }

    @Override
    public void adopt(View v) {
        int id = list.get((int) v.getTag()).getTicket_approval_content_id();
        approvalTicket(id,"yes");
    }

    private void approvalTicket(int id, final String state){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("ticket_approval_content_id",id);
        map.put("state",state);
        String sign = SignUtil.sign(map);
        HomeHttp.approvalTicket(account_token, time_stamp, sign, id, state, new CallBack() {
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
                                    getTicketApproval();
                                    break;
                                case "no":
                                    ToastXutil.show("已拒绝");
                                    getTicketApproval();
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
package com.cn.uca.ui.fragment.user.order;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.user.OrderAdapter;
import com.cn.uca.bean.user.OrderBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.loading.LoadingLayout;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.user.order.LvPaiOrderDetailActivity;
import com.cn.uca.util.SetLayoutParams;
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
 * Created by asus on 2018/1/11.
 */

public class LvPaiFragment extends Fragment  implements View.OnClickListener {

    private View view;
    private LoadingLayout loading;
    private RadioButton title01,title02,title03,title04,title05;
    private List<OrderBean> list;
    private OrderAdapter orderAdapter;
    private ListView listView;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private RefreshLayout refreshLayout;
    private String state = "all";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lv_pai,null);

        initView();
        return view;
    }

    private void initView() {
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        loading = (LoadingLayout)view.findViewById(R.id.loading);
        title01 = (RadioButton)view.findViewById(R.id.title01);
        title02 = (RadioButton)view.findViewById(R.id.title02);
        title03 = (RadioButton)view.findViewById(R.id.title03);
        title04 = (RadioButton)view.findViewById(R.id.title04);
        title05 = (RadioButton)view.findViewById(R.id.title05);
        listView = (ListView)view.findViewById(R.id.listView);
        list = new ArrayList<>();
        orderAdapter = new OrderAdapter(list,getActivity());
        listView.setAdapter(orderAdapter);

        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
        title04.setOnClickListener(this);
        title05.setOnClickListener(this);
        title01.setChecked(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),LvPaiOrderDetailActivity.class);
                intent.putExtra("type","id");
                intent.putExtra("id",list.get(position).getUser_order_id());
                startActivity(intent);
            }
        });

        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUserOrder(state);
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 500);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getUserOrder(state);
                        refreshlayout.finishLoadmore();
                    }
                }, 500);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /**震动服务*/
                Vibrator vib = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(100);//只震动一秒，一次
                switch (list.get(position).getUser_order_state_id()){
                    case 3:
                        showDeleteDialog(list.get(position).getUser_order_id());
                        break;
                    case 5:
                        showDeleteDialog(list.get(position).getUser_order_id());
                        break;
                    case 8:
                        showDeleteDialog(list.get(position).getUser_order_id());
                        break;
                    default:
                        ToastXutil.show("该状态下不可操作");
                        break;
                }
                return true;
            }
        });

    }

    private void getUserOrder(String state){
        UserHttp.getUserOrder(page, pageCount, 7, state, new CallBack() {
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
                state = "all";
                refreshLayout.autoRefresh();
                break;
            case R.id.title02:
                list.clear();
                state = "waitingPayment";
                refreshLayout.autoRefresh();
                break;
            case R.id.title03:
                list.clear();
                state = "alreadyPaid";
                refreshLayout.autoRefresh();
                break;
            case R.id.title04:
                list.clear();
                state = "refund";
                refreshLayout.autoRefresh();
                break;
            case R.id.title05:
                list.clear();
                state = "complete";
                refreshLayout.autoRefresh();
                break;
        }
    }

    private void showDeleteDialog(final int id){
        final Dialog dialog  = new Dialog(getActivity());
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.list_delete_dialog, null);
        TextView multiplexing = (TextView)inflate.findViewById(R.id.multiplexing);//重新发布
        TextView widerrufen = (TextView)inflate.findViewById(R.id.widerrufen);//撤销
        TextView delete = (TextView) inflate.findViewById(R.id.delete);
        multiplexing.setVisibility(View.GONE);
        widerrufen.setVisibility(View.GONE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                deleteOrder(id);
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.CENTER);
        SetLayoutParams.setFrameLayout(inflate, MyApplication.width/2,0);
        dialog.show();//显示对话框
    }

    //删除订单
    public void deleteOrder(int id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("escort_record_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.deleteOrder(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    if ((int) response == 0){
                        ToastXutil.show("删除成功");
                        refreshLayout.autoRefresh();
                    }}catch (Exception e){
                    Log.e("456",e.getMessage());
                }

            }

            @Override
            public void onErrorMsg(String errorMsg) {
                ToastXutil.show(errorMsg);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}

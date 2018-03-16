package com.cn.uca.ui.view.yueka;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.yueka.SendListAdapter;
import com.cn.uca.bean.yueka.SendEscortBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendListActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;
    private int count = 0;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private SendListAdapter adapter;
    private List<SendEscortBean> list;
    private RefreshLayout refreshLayout;
    private LinearLayout layout;
    private RelativeLayout no_layout;//没有数据布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_list);

        initView();
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);
        layout = (LinearLayout)findViewById(R.id.layout);
        no_layout = (RelativeLayout)findViewById(R.id.no_layout);

        list = new ArrayList<>();
        adapter = new SendListAdapter(list,getApplicationContext());
        listView.setAdapter(adapter);
        back.setOnClickListener(this);


        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count = 0;
                        getReleaseEscortRecords(1,page,pageCount);
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 200);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count ++;
                        getReleaseEscortRecords(2,count+page,pageCount);
                        refreshlayout.finishLoadmore();
                    }
                }, 200);
            }
        });
        refreshLayout.autoRefresh();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(list.get(position).getEscort_record_name(),list.get(position).getEscort_record_id(),list.get(position).getEscort_record_state_id(),list.get(position).getEscort_record_id());
                /**震动服务*/
                Vibrator vib = (Vibrator)SendListActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(100);//只震动一秒，一次
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (list.get(position).getEscort_record_state_id()){
                    case 1:
                        if (list.get(position).getInitial_number() > 0){
                            Intent intent = new Intent();
                            intent.setClass(SendListActivity.this,ConfirmOrderActivity.class);
                            intent.putExtra("id",list.get(position).getEscort_record_id());
                            startActivity(intent);
                        }else{
                            ToastXutil.show("请耐心等待用户下单");
                        }
                        break;
                    case 2://已响应

                        break;
                    case 3://已完成-可删除

                        break;
                    case 4://已过期

                        break;
                    case 5://等待用户付款

                        break;
                    case 6://已付款

                        break;
                    case 7://已退单

                        break;
                    case 8://等待审批退单

                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getReleaseEscortRecords(final int type,int page,int pageCount){
        YueKaHttp.getReleaseEscortRecords(page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<SendEscortBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("getReleaseEscortRecordsRets").toString(),new TypeToken<List<SendEscortBean>>() {
                            }.getType());
                            switch (type){
                                case 1://刷新
                                    if (bean.size() > 0){
                                        layout.setVisibility(View.VISIBLE);
                                        no_layout.setVisibility(View.GONE);
                                        list.clear();
                                        list.addAll(bean);
                                        adapter.setList(list);
                                    }else{
                                        layout.setVisibility(View.GONE);
                                        no_layout.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 2://加载
                                    if (bean.size() > 0){
                                        list.addAll(bean);
                                        adapter.setList(list);
                                    }
                                    break;
                            }

                            break;
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

    private void showDeleteDialog(final String line,final int roadId,final int state_id,final int id){
        final Dialog dialog  = new Dialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.list_delete_dialog, null);
        TextView multiplexing = (TextView)inflate.findViewById(R.id.multiplexing);//重新发布
        TextView widerrufen = (TextView)inflate.findViewById(R.id.widerrufen);//撤销
        TextView delete = (TextView) inflate.findViewById(R.id.delete);
        multiplexing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新发布
                Intent intent = new Intent();
                intent.setClass(SendListActivity.this,SendYueKaActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("line",line);
                intent.putExtra("roadId",roadId);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        widerrufen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //撤销
                if (state_id == 1){
                    revokeEscortOrder(id);
                    dialog.dismiss();
                }else{
                    ToastXutil.show("该状态下不可撤回");
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                if (state_id == 3||state_id == 4 || state_id == 7 || state_id == 10){
                    deleteEscortRecords(id);
                    dialog.dismiss();
                }else{
                    ToastXutil.show("该状态下不可删除");
                }
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

    //撤销发布
    private void revokeEscortOrder(int id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("escort_record_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        YueKaHttp.revokeEscortOrder(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{  if ((int) response == 0){
                    ToastXutil.show("撤销成功");
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

    //删除发布
    private void deleteEscortRecords(int id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("escort_record_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        YueKaHttp.deleteEscortRecords(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{  if ((int) response == 0){
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

package com.cn.uca.ui.view.yueka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.yueka.SendListAdapter;
import com.cn.uca.bean.home.footprint.CityDetailsBean;
import com.cn.uca.bean.yueka.SendEscortBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SendListActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;
    private int page = 1;
    private int pageCount = 5;
    private SendListAdapter adapter;
    private List<SendEscortBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_list);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);

        list = new ArrayList<>();
        adapter = new SendListAdapter(list,getApplicationContext());
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
        getReleaseEscortRecords();

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
                    case 3://已完成

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

    private void getReleaseEscortRecords(){
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
                            if (bean.size() > 0){
                                list.clear();
                                list.addAll(bean);
                                adapter.setList(list);
                            }else{
                                if (list.size() != 0 ){
                                    ToastXutil.show("暂无更多数据");
                                }else{
                                    adapter.setList(list);
                                }
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

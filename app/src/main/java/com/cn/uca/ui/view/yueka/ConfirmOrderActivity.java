package com.cn.uca.ui.view.yueka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.yueka.ConfirmOrderAdapter;
import com.cn.uca.bean.yueka.ConfirmOrderBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.ConfirmOrderback;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderActivity extends BaseBackActivity implements View.OnClickListener,ConfirmOrderback{

    private TextView back;
    private ListView listView;
    private int id;
    private List<ConfirmOrderBean> list;
    private ConfirmOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        getInfo();
        initView();
        getEscprtRecordUsers();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    private void initView() {
        list = new ArrayList<>();
        adapter = new ConfirmOrderAdapter(list,getApplicationContext(),this);
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }


    private void getEscprtRecordUsers(){
        YueKaHttp.getEscprtRecordUsers(id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<ConfirmOrderBean> bean = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<ConfirmOrderBean>>() {
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

    @Override
    public void onBack(View v) {
       int id =  list.get((int)v.getTag()).getEscort_user_id();
        acceptanceEscortUser(id,0);
    }

    @Override
    public void onConfirm(View v) {
        int id =  list.get((int)v.getTag()).getEscort_user_id();
        acceptanceEscortUser(id,1);
    }

    @Override
    public void onChat(View v) {

    }

    private void acceptanceEscortUser(int id, final int orders){
        YueKaHttp.acceptanceEscortUser(id,orders, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            if (orders == 0){
                                ToastXutil.show("拒绝成功");
                            }else{
                                ToastXutil.show("接单成功，待用户支付");
                            }
                            getEscprtRecordUsers();
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

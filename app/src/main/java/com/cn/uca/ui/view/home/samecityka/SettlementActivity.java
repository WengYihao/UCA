package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.SettlementAdapter;
import com.cn.uca.bean.home.samecityka.SettlementBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.samecityka.SettlementBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算
 */
public class SettlementActivity extends BaseBackActivity implements View.OnClickListener,SettlementBack{

    private TextView back,textView;
    private ListView listView;
    private SettlementAdapter adapter;
    private List<SettlementBean> list;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);

        getInfo();
        initView();
        getSettlement();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new SettlementAdapter(list,this,this);
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

    private void getSettlement(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        if(id != 0){
            map.put("city_cafe_id",id);
        }
        String sign = SignUtil.sign(map);
        HomeHttp.getSettlement(account_token, time_stamp, sign, page, pageCount,id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("settlementRet");
                            List<SettlementBean> bean = gson.fromJson(array.toString(),new TypeToken<List<SettlementBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);adapter.setList(list);
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
    public void selttlement(View v) {
        int id = list.get((int) v.getTag()).getCity_cafe_id();
        LinearLayout linearLayout = (LinearLayout) listView.getChildAt((int) v.getTag());
        textView = (TextView) linearLayout.findViewById(R.id.settlement);
        settlement(id);
    }

    private void settlement(int id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("city_cafe_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.settlement(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            textView.setText("已结算");
                            break;}
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
}

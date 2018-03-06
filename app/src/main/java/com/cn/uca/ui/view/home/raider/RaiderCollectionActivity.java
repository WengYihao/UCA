package com.cn.uca.ui.view.home.raider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.raiders.RaidersCollectionAdapter;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
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

public class RaiderCollectionActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private GridView gridView;
    private TextView title01,title04;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private List<RaidersBean> list;
    private RaidersCollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raider_collection);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title04 = (TextView)findViewById(R.id.title04);

        gridView = (GridView)findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new RaidersCollectionAdapter(list,getApplicationContext());
        gridView.setAdapter(adapter);
        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title04.setOnClickListener(this);

        show(0);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(RaiderCollectionActivity.this,RaidersDetailActivity.class);
                intent.putExtra("id",list.get(i).getCity_raiders_id());
                intent.putExtra("cityName",list.get(i).getCity_name());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.title01:
                show(0);
                break;
            case R.id.title04:
                show(3);
                break;
        }
    }

    private void show(int index){
        switch (index){
            case 0:
                title01.setTextColor(getResources().getColor(R.color.ori));
                title01.setBackgroundResource(R.color.white);

                title04.setTextColor(getResources().getColor(R.color.gray2));
                title04.setBackgroundResource(R.color.gray);
                getMyRaider(1);
                break;
            case 3:
                title01.setTextColor(getResources().getColor(R.color.gray2));
                title01.setBackgroundResource(R.color.gray);

                title04.setTextColor(getResources().getColor(R.color.ori));
                title04.setBackgroundResource(R.color.white);
                getMyRaider(1);
                break;
        }
    }

    private void getMyRaider(int type){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("type",type);
        String sign = SignUtil.sign(map);
        HomeHttp.getMyRaiders(page, pageCount, sign, time_stamp, account_token, type, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<RaidersBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("myRaidersRets").toString(),new TypeToken<List<RaidersBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.clear();
                                list.addAll(bean);
                                adapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
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

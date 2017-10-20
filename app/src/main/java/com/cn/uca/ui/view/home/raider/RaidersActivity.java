package com.cn.uca.ui.view.home.raider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.LetterAdapter;
import com.cn.uca.adapter.home.raiders.RaidersAdapter;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
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

public class RaidersActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private ListView letter;
    private GridView gridView;
    private LetterAdapter letterAdapter;
    private String [] data;
    private List<RaidersBean> list;
    private RaidersAdapter raidersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);//设置状态栏沉浸式
        setContentView(R.layout.activity_raiders);

        initView();
        cpyGetCityRaiders(1,6,SystemUtil.getCurrentDate2(),"",SharePreferenceXutil.getAccountToken());
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);
        letter = (ListView)findViewById(R.id.letter);
        gridView = (GridView) findViewById(R.id.gridView);
        data = getResources().getStringArray(R.array.letter_list);
        letterAdapter = new LetterAdapter(data,this);
        letter.setAdapter(letterAdapter);

        list = new ArrayList<>();
        raidersAdapter = new RaidersAdapter(list,this);
        gridView.setAdapter(raidersAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(RaidersActivity.this,RaidersDetailActivity.class);
                intent.putExtra("id",list.get(i).getCity_raiders_id());
                intent.putExtra("cityName",list.get(i).getCity_name());
                startActivity(intent);

            }
        });
    }

    private void cpyGetCityRaiders(int page,int pageCount,String time_stamp,String city_pinyin,String account_token){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("time_stamp",time_stamp);
        map.put("city_pinyin",city_pinyin);
        map.put("account_token", account_token);
        String sign = SignUtil.sign(map);
        HomeHttp.cpyGetCityRaiders(page,pageCount, city_pinyin, sign, time_stamp, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("cityRaidersRets");
                            Gson gson = new Gson();
                            List<RaidersBean> bean = gson.fromJson(array.toString(),new TypeToken<List<RaidersBean>>() {
                            }.getType());
                            list.addAll(bean);
                            raidersAdapter.setList(list);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }
}

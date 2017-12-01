package com.cn.uca.ui.view.home.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.CitySpotTicketAdapter;
import com.cn.uca.bean.home.travel.CitySpotTicketBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
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
 * 城市景区列表
 */
public class CitySpotListActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,cityName;
    private ListView listView;
    private int cityId;
    private String name;
    private List<CitySpotTicketBean> list;
    private CitySpotTicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_spot_list);

        initView();
        getInfo();
        getScenicSpot();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        cityName = (TextView)findViewById(R.id.cityName);
        listView = (ListView)findViewById(R.id.listView);

        back.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new CitySpotTicketAdapter(list,this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(CitySpotListActivity.this,SpotTicketActivity.class);
                intent.putExtra("id",list.get(i).getScenic_spot_id());
                startActivity(intent);
            }
        });
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            cityId = intent.getIntExtra("id",0);
            name = intent.getStringExtra("name");
            cityName.setText(name+"站");
        }
    }

    private void getScenicSpot(){
        Map<String,Object> map = new HashMap<>();
        map.put("page",1);
        map.put("pageCount",6);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("city_id",cityId);
        String sign = SignUtil.sign(map);
        HomeHttp.getScenicSpot(1, 6, sign, time_stamp, cityId, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString()+"---");
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("scenicSpotRets");
                            Gson gson = new Gson();
                            List<CitySpotTicketBean> bean = gson.fromJson(array.toString(),new TypeToken<List<CitySpotTicketBean>>() {
                            }.getType());
                            list.addAll(bean);
                            adapter.setList(list);
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

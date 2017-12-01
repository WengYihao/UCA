package com.cn.uca.ui.view.home.travel;

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
import com.cn.uca.adapter.home.CityAdapter;
import com.cn.uca.bean.home.CityBean;
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

public class SpotListActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView letter;
    private GridView gridView;
    private LetterAdapter letterAdapter;
    private CityAdapter cityAdapter;
    private String [] data;
    private List<CityBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_list);

        initView();
        getScenicSpotCity();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        letter = (ListView)findViewById(R.id.letter);
        gridView = (GridView) findViewById(R.id.gridView);

        back.setOnClickListener(this);

        data = getResources().getStringArray(R.array.letter_list);
        letterAdapter = new LetterAdapter(data,this);
        letter.setAdapter(letterAdapter);

        list = new ArrayList<>();
        cityAdapter = new CityAdapter(list,this);
        gridView.setAdapter(cityAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(SpotListActivity.this,CitySpotListActivity.class);
                intent.putExtra("id",list.get(i).getCity_id());
                intent.putExtra("name",list.get(i).getCity_name());
                startActivity(intent);
            }
        });
    }


    private void getScenicSpotCity(){
        Map<String,Object> map = new HashMap<>();
        map.put("page",1);
        map.put("pageCount",6);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("city_pinyin","s");
        String sign = SignUtil.sign(map);
        HomeHttp.getScenicSpotCity(1, 6, sign, time_stamp, "s", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    Log.i("123",response.toString()+"--");
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("scenicSpotCitys");
                            Gson gson = new Gson();
                            List<CityBean> bean = gson.fromJson(array.toString(),new TypeToken<List<CityBean>>() {
                            }.getType());
                            list.addAll(bean);
                            Log.i("123",list.toString()+"***");
                            cityAdapter.setList(list);
                            break;
                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage()+"---");
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

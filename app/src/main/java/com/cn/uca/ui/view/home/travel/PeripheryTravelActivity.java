package com.cn.uca.ui.view.home.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.AroundPlayAdapter;
import com.cn.uca.bean.home.travel.AroundPlayBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.fragment.home.PeripheryFragment;
import com.cn.uca.ui.fragment.home.PeripheryHotelFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.NoScrollGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 周边游
 */
public class PeripheryTravelActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,more;
    private NoScrollGridView gridView;
    private List<AroundPlayBean> list;
    private AroundPlayAdapter adapter;
//    private RadioButton hot,hotel;
//    private PeripheryFragment peripheryFragment;
//    private PeripheryHotelFragment peripheryHotelFragment;
//    private FragmentTransaction fragmentTransaction;
//    private int currentIndex = -1;

    private String code = "";
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periphery_travel);

        initView();
        getMustPlayAround(code);
//        getTravel();
    }


    private void getTravel(String type,int regionId,String py,int styleId,String sort){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("gaode_code",code);
        map.put("tourismType",type);
        map.put("region_id",regionId);
        map.put("city_pinyin",py);
        map.put("tourism_style_id",styleId);
        map.put("sort",sort);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getTourism(sign, time_stamp, account_token,code,type,regionId,py,styleId,sort,page,pageCount,new CallBack() {
            @Override
            public void onResponse(Object response) {

            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        more = (TextView)findViewById(R.id.more);
        gridView = (NoScrollGridView)findViewById(R.id.gridView);

        back.setOnClickListener(this);
        more.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new AroundPlayAdapter(list,this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(PeripheryTravelActivity.this,SpotTicketActivity.class);
                intent.putExtra("id",list.get(position).getScenic_spot_id());
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
            case R.id.more:
                list.clear();
                getMustPlayAround(code);
                break;
        }
    }

    private void getMustPlayAround(String code){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("gaode_code",code);
        String sign = SignUtil.sign(map);
        HomeHttp.getMustPlayAround(sign, time_stamp, code, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<AroundPlayBean> bean =gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<AroundPlayBean>>() {
                            }.getType());
                            if (bean.size() != 0){
                                list.addAll(bean);
                                adapter.setList(list);
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

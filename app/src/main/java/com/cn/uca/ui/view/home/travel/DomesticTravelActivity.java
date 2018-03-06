package com.cn.uca.ui.view.home.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.DomesticTravelAdapter;
import com.cn.uca.adapter.home.travel.DomesticTravelPlaceAdapter;
import com.cn.uca.bean.home.travel.TravelBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.NoScrollListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 国内游
 */
public class DomesticTravelActivity extends BaseBackActivity  implements View.OnClickListener{

    private GridView gridView;
    private NoScrollListView listView;
    private List<TravelBean> list,list2;
    private DomesticTravelPlaceAdapter travelPlaceAdapter;
    private DomesticTravelAdapter travelAdapter;
    private TextView back,more;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domestic_travel);

        initView();
        getTravel("domestic",0,"",0,"");
        getTravel("domestic",0,"",0,"hot");
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        more = (TextView)findViewById(R.id.more);
        gridView = (GridView)findViewById(R.id.gridView);
        listView = (NoScrollListView)findViewById(R.id.listView);

        list = new ArrayList<>();
        travelPlaceAdapter = new DomesticTravelPlaceAdapter(list,getApplicationContext());
        gridView.setAdapter(travelPlaceAdapter);
        SetLayoutParams.setLinearLayout(gridView,MyApplication.width-20,((MyApplication.width-SystemUtil.dip2px(40))/3)*2+10);

        list2 = new ArrayList<>();
        travelAdapter = new DomesticTravelAdapter(list2,getApplicationContext());
        listView.setAdapter(travelAdapter);
        SetListView.setListViewHeightBasedOnChildren(listView);

        back.setOnClickListener(this);
        more.setOnClickListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(DomesticTravelActivity.this,TravelDetailActivity.class);
                intent.putExtra("id",list.get(position).getTourism_id());
                startActivity(intent);
            }
        });
    }

    private void getTravel(String type, int regionId, String py, int styleId, final String sort){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("gaode_code",code);
        map.put("tourismType",type);
        map.put("city_pinyin",py);
        map.put("sort",sort);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getTourism(sign, time_stamp, account_token,code,type,regionId,py,styleId,sort,page,pageCount,new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<TravelBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("tourisms").toString(),new TypeToken<List<TravelBean>>() {
                            }.getType());
                            if (bean.size()>0){
                                if(sort != ""){
                                    list2.addAll(bean);
                                    travelAdapter.setList(list2);
                                }else{
                                    list.addAll(bean);
                                    travelPlaceAdapter.setList(list);
                                }
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage()+"---");
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
            case R.id.more:
                startActivity(new Intent(this,MoreTravelActivity.class));
                break;
        }
    }
}

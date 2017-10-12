package com.cn.uca.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.travel.DomesticTravelAdapter;
import com.cn.uca.adapter.travel.DomesticTravelPlaceAdapter;
import com.cn.uca.bean.travel.DomesticTravelBean;
import com.cn.uca.bean.travel.DomesticTravelPlaceBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.NoScrollListView;
import com.cn.uca.view.RdListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DomesticTravelActivity extends BaseBackActivity  implements View.OnClickListener{

    private GridView gridView;
    private NoScrollListView listView;
    private List<DomesticTravelPlaceBean> list;
    private List<DomesticTravelBean> list2;
    private DomesticTravelPlaceAdapter travelPlaceAdapter;
    private DomesticTravelAdapter travelAdapter;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_domestic_travel);

        initView();
        getGuoNeiYou();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        gridView = (GridView)findViewById(R.id.gridView);
        listView = (NoScrollListView)findViewById(R.id.listView);

        list = new ArrayList<>();
        travelPlaceAdapter = new DomesticTravelPlaceAdapter(list,getApplicationContext());
        gridView.setAdapter(travelPlaceAdapter);
        SetLayoutParams.setLinearLayout(gridView,MyApplication.width,((MyApplication.width- SystemUtil.dip2px(40))/3)*2+SystemUtil.dip2px(10));

        list2 = new ArrayList<>();
        travelAdapter = new DomesticTravelAdapter(list2,getApplicationContext());
        listView.setAdapter(travelAdapter);
        SetListView.setListViewHeightBasedOnChildren(listView);

        back.setOnClickListener(this);
    }

    private void getGuoNeiYou(){
        HomeHttp.getGuoNeiYou(1, 5, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("recommendedCities");
                            JSONArray array1 = jsonObject.getJSONObject("data").getJSONArray("guoNeiYouRets");
                            Gson gson = new Gson();
                            List<DomesticTravelPlaceBean> bean = gson.fromJson(array.toString(),new TypeToken<List<DomesticTravelPlaceBean>>() {
                            }.getType());
                            list.addAll(bean);
                            travelPlaceAdapter.setList(list);
                            Log.i("123",list.toString()+"--");

                            List<DomesticTravelBean> bean1 = gson.fromJson(array1.toString(),new TypeToken<List<DomesticTravelBean>>() {
                            }.getType());
                            list2.addAll(bean1);
                            travelAdapter.setList(list2);
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

package com.cn.uca.ui.view.home.travel;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.ExitTravelAdapter;
import com.cn.uca.adapter.home.travel.ExitTravelPlaceAdapter;
import com.cn.uca.bean.home.travel.ExitTravelBean;
import com.cn.uca.bean.home.travel.ExitTravelPlaceBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.NoScrollGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 出境游
 */
public class ExitTravelActivity extends BaseBackActivity implements View.OnClickListener{

    private GridView gridView;
    private NoScrollGridView gridView2;
    private List<ExitTravelPlaceBean> list;
    private List<ExitTravelBean> list2;
    private ExitTravelPlaceAdapter travelPlaceAdapter;
    private ExitTravelAdapter travelAdapter;
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_exit_travel);

        initView();
        getChuJingYou();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        gridView = (GridView)findViewById(R.id.gridView);
        gridView2 = (NoScrollGridView) findViewById(R.id.gridView2);

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        travelPlaceAdapter = new ExitTravelPlaceAdapter(list,getApplicationContext());
        travelAdapter = new ExitTravelAdapter(list2,getApplicationContext());
        gridView.setAdapter(travelPlaceAdapter);
        gridView2.setAdapter(travelAdapter);

        SetLayoutParams.setLinearLayout(gridView, MyApplication.width,((MyApplication.width- SystemUtil.dip2px(40))/3)*2+SystemUtil.dip2px(10));

        back.setOnClickListener(this);
    }

    public void getChuJingYou(){
        HomeHttp.getChuJingYou(1, 5, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("hotSpots");
                            JSONArray array1 = jsonObject.getJSONObject("data").getJSONArray("chuJingYouRets");
                            Gson gson = new Gson();
                            List<ExitTravelPlaceBean> bean = gson.fromJson(array.toString(),new TypeToken<List<ExitTravelPlaceBean>>() {
                            }.getType());
                            list.addAll(bean);
                            travelPlaceAdapter.setList(list);

                            List<ExitTravelBean> bean1 = gson.fromJson(array1.toString(),new TypeToken<List<ExitTravelBean>>() {
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

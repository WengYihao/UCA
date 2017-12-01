package com.cn.uca.ui.view.home.travel;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.adapter.home.travel.HoneymoonTravelAdapter;
import com.cn.uca.bean.home.travel.HoneymoonTravelBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 蜜月游
 */
public class HoneymoonTravelActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private ListView listView;
    private HoneymoonTravelAdapter honeymoonTravelAdapter;
    private List<HoneymoonTravelBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honeymoon_travel);

        initView();
        getMiYueYou();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);

        list = new ArrayList<>();
        honeymoonTravelAdapter = new HoneymoonTravelAdapter(list,getApplicationContext());
        listView.setAdapter(honeymoonTravelAdapter);

        back.setOnClickListener(this);
    }

    private void getMiYueYou(){
        HomeHttp.getMiYeuYou(1, 5, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("miYeuYouRets");
                            Gson gson = new Gson();
                            List<HoneymoonTravelBean> bean = gson.fromJson(array.toString(),new TypeToken<List<HoneymoonTravelBean>>() {
                            }.getType());
                            list.addAll(bean);
                            honeymoonTravelAdapter.setList(list);
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

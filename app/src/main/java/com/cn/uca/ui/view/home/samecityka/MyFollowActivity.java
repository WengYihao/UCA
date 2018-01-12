package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.CollectionAdapter;
import com.cn.uca.bean.home.samecityka.CollectionBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的关注-参与者
 */
public class MyFollowActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private ListView listView;
    private CollectionAdapter adapter;
    private List<CollectionBean> list;
    private int page = 1;
    private int pageCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow);

        initView();
        getCollectionCityCafe();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new CollectionAdapter(list,this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MyFollowActivity.this,ActionDetailActivity.class);
                intent.putExtra("id",list.get(position).getCity_cafe_id());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getCollectionCityCafe(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        String sign = SignUtil.sign(map);
        HomeHttp.getCollectionCityCafe(account_token, time_stamp, sign, page, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<CollectionBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("collectionCityCafes").toString(),new TypeToken<List<CollectionBean>>() {
                            }.getType());
                            if (bean.size() > 0){
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

package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.CardManagerAdapter;
import com.cn.uca.bean.home.samecityka.AddCardBean;
import com.cn.uca.bean.home.samecityka.CardBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetListView;
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

/**
 * 名片管理-同城咖
 */
public class CardManageActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,addCard;
    private ListView listView;
    private List<CardBean> list;
    private CardManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_manage);

        initView();
        getUserCard();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.listView);

        addCard = (TextView)findViewById(R.id.addCard);
        addCard.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new CardManagerAdapter(list,this);
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.addCard:
                Intent intent = new Intent();
                intent.setClass(CardManageActivity.this,CardEditActivity.class);
                intent.putExtra("id",-1);
                intent.putExtra("type","add");
                startActivityForResult(intent,0);
                break;
        }
    }

    private void getUserCard(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getUserCard(account_token, time_stamp, sign, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONArray("data");
                            List<CardBean> bean = gson.fromJson(array.toString(),new TypeToken<List<CardBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);
                                adapter.setList(list);
                                SetListView.setListViewHeightBasedOnChildren(listView);
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

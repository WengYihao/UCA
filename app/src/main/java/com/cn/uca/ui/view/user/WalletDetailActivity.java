package com.cn.uca.ui.view.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.user.WalletDetailAdapter;
import com.cn.uca.bean.user.WalletDetailBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalletDetailActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;
    private int page = 1;
    private int pageCount = 10;
    private List<WalletDetailBean> list;
    private WalletDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);

        initView();
        getPusrsRecord();
    }

    private void initView() {
        list = new ArrayList<>();
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);
        back.setOnClickListener(this);

        adapter = new WalletDetailAdapter(list,this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getPusrsRecord(){
        String token = SharePreferenceXutil.getAccountToken();
        UserHttp.getPurseRecords(token, page, pageCount, "", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("purseRecordDatas");
                            List<WalletDetailBean> been  = gson.fromJson(array.toString(),new TypeToken<List<WalletDetailBean>>(){
                            }.getType());
                            if (been.size() > 0){
                                list.clear();
                                list.addAll(been);
                                adapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
                                }else{
                                    adapter.setList(list);
                                }
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

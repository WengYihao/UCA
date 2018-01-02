package com.cn.uca.ui.view.home.lvpai;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.AddressAdapter;
import com.cn.uca.bean.home.lvpai.setAddressBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
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
 * 商家地址
 */
public class MerchantAddressActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;
    private AddressAdapter adapter;
    private List<setAddressBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_address);

        initView();
        getAddress();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);

        back.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new AddressAdapter(list,this);
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

    private void getAddress(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.gerAddress(time_stamp, sign, account_token, 0, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONArray("data");
                            List<setAddressBean> bean = gson.fromJson(array.toString(),new TypeToken<List<setAddressBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);
                                adapter.setList(list);
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.e("456", "onResponse: "+e.getMessage());
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

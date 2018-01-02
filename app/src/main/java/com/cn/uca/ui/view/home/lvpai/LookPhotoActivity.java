package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.PhotoDetailAdapter;
import com.cn.uca.bean.home.lvpai.PhotoDetailBean;
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
 * 游客浏览商家相册
 */
public class LookPhotoActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;
    private List<PhotoDetailBean> list;
    private PhotoDetailAdapter adapter;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_photo);
        getInfo();
        initView();
        getMerAllPic();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);

        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new PhotoDetailAdapter(list,this);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getMerAllPic(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("trip_shoot_merchant_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.getMerAllPic(time_stamp, sign, account_token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONArray("data");
                            List<PhotoDetailBean> bean = gson.fromJson(array.toString(),new TypeToken<List<PhotoDetailBean>>() {
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

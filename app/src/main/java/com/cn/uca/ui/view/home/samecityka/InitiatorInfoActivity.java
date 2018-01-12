package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.CollectionAdapter;
import com.cn.uca.bean.home.samecityka.CollectionBean;
import com.cn.uca.bean.home.samecityka.UserInfoBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitiatorInfoActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,name,type_name,type_icon,phone,content;
    private CircleImageView pic;
    private ListView listView;
    private CollectionAdapter adapter;
    private List<CollectionBean> list;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiator_info);

        getInfo();
        initView();
        getCafeUser();

    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        type_name = (TextView)findViewById(R.id.type_name);
        type_icon = (TextView)findViewById(R.id.type_icon);
        phone = (TextView)findViewById(R.id.phone);
        content = (TextView)findViewById(R.id.content);
        pic = (CircleImageView)findViewById(R.id.pic);
        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new CollectionAdapter(list,this);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(InitiatorInfoActivity.this, ActionDetailActivity.class);
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

    private void getCafeUser(){
        Map<String,Object> map = new HashMap<>();
        map.put("city_cafe_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getCafeUser(time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            UserInfoBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(), new TypeToken<UserInfoBean>() {
                            }.getType());
                            name.setText(bean.getUser_card_name());
                            ImageLoader.getInstance().displayImage(bean.getHead_portrait_url(),pic);
                            type_name.setText(bean.getUser_card_type_name());
                            switch (bean.getUser_card_type_id()){
                                case 1:
                                    type_icon.setBackgroundResource(R.mipmap.personal_type);
                                    break;
                                case 2:
                                    type_icon.setBackgroundResource(R.mipmap.enterprise_type);
                                    break;
                                case 3:
                                    type_icon.setBackgroundResource(R.mipmap.college_type);
                                break;
                            }
                            phone.setText(bean.getHand_phone());
                            content.setText(bean.getIntroduce());
                            list = bean.getCityCafes();
                            adapter.setList(list);
                            SetListView.setListViewHeightBasedOnChildren(listView);
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

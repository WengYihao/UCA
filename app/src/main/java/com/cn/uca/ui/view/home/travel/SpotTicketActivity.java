package com.cn.uca.ui.view.home.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.SpotTicketAdapter;
import com.cn.uca.bean.home.travel.SpotTicketBean;
import com.cn.uca.bean.home.travel.TicketBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.NoScrollListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SpotTicketActivity extends BaseBackActivity implements View.OnClickListener{

    private int id;
    private TextView back,name,address,introduce_link,content_num;
    private ImageView pic,spot_pic;
    private RelativeLayout spot_content;
    private List<TicketBean> list;
    private SpotTicketAdapter adapter;
    private NoScrollListView ticket_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_ticket);
        initView();
        getInfo();
        getTicket();
    }

    private void getInfo(){
        Intent in = getIntent();
        if (in != null){
            id = in.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        name = (TextView)findViewById(R.id.name);
        address = (TextView)findViewById(R.id.address);
        introduce_link = (TextView)findViewById(R.id.introduce_link);
        content_num = (TextView)findViewById(R.id.content_num);
        spot_content = (RelativeLayout)findViewById(R.id.spot_content);
        pic = (ImageView)findViewById(R.id.pic);
        spot_pic = (ImageView)findViewById(R.id.spot_pic);
        ticket_listview = (NoScrollListView)findViewById(R.id.ticket_listview);
        back.setOnClickListener(this);
        address.setOnClickListener(this);
        introduce_link.setOnClickListener(this);
        spot_content.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new SpotTicketAdapter(list,getApplicationContext());
        ticket_listview.setAdapter(adapter);

    }

    private void getTicket(){
        Map<String,Object> map = new HashMap<>();
        map.put("scenic_spot_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getTicket(id, sign, time_stamp, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            Gson gson = new Gson();
                            SpotTicketBean bean = gson.fromJson(object.toString(),new TypeToken<SpotTicketBean>(){}.getType());
                            Glide.with(SpotTicketActivity.this)
                                    .load(bean.getPicture_url())
                                    .into(spot_pic);
                            Glide.with(SpotTicketActivity.this).load(bean.getPicture_url()).bitmapTransform(new BlurTransformation(SpotTicketActivity.this, 20)).into(pic);
                            name.setText(bean.getScenic_spot_name());
                            address.setText(bean.getAddress());
                            content_num.setText(bean.getEvaluation_quantity()+"条评论");
                            JSONArray array = object.getJSONArray("ticketRets");
                            List<TicketBean> listBean = gson.fromJson(array.toString(),new TypeToken<List<TicketBean>>() {
                            }.getType());
                            list.addAll(listBean);
                            adapter.setList(list);
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
            case R.id.spot_content:
                ToastXutil.show("看什么简介，看我不好吗？");
                break;
        }
    }
}

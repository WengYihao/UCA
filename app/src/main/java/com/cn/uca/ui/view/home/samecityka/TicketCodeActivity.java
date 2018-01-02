package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.bean.home.samecityka.MyTicketCodeBean;
import com.cn.uca.bean.home.samecityka.TicketCodeBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.fragment.home.samecityka.TicketFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.L;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketCodeActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ViewPager mPager;
    private int id;
    private List<Fragment> fragmentList;
    private List<TicketCodeBean> listCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_code);

        initView();
    }
    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
            if (id != 0){
                getUserTickets(id);
            }
        }
    }
    private void initFragment(List<TicketCodeBean> list) {
        for (int i= 0;i<list.size();i++){
            TicketFragment fragment = TicketFragment.newInstance(list.get(i).getUser_cafe_ticket_id()+list.get(i).getTicket_code());
            fragmentList.add(fragment);
        }
        mPager.setAdapter( new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        mPager = (ViewPager) findViewById(R.id.container);
        SetLayoutParams.setLinearLayout(mPager, MyApplication.width- SystemUtil.dip2px(20),MyApplication.width- SystemUtil.dip2px(20));
        back.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        getInfo();
    }
    private void getUserTickets(int id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("city_cafe_order_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getUserTickets(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONArray("data");
                            listCode = gson.fromJson(array.toString(),new TypeToken<List<TicketCodeBean>>() {
                            }.getType());
                            if (listCode.size() != 0 && listCode != null){
                                initFragment(listCode);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
//            case R.id.text1:
//                list.clear();
//                list.add(4);
//                list.add(5);
//                list.add(6);
//                initFragment();
//                break;
//            case R.id.text2:
//                list.clear();
//                list.add(7);
//                list.add(8);
//                list.add(9);
//                initFragment();
//                break;
        }
    }
}

package com.cn.uca.ui.view.home.travel;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.AroundPlayAdapter;
import com.cn.uca.bean.home.travel.AroundPlayBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.fragment.home.PeripheryFragment;
import com.cn.uca.ui.fragment.home.PeripheryHotelFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
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
 * 周边游
 */
public class PeripheryTravelActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private GridView gridView;
    private List<AroundPlayBean> list;
    private AroundPlayAdapter adapter;
    private RadioButton hot,hotel;
    private PeripheryFragment peripheryFragment;
    private PeripheryHotelFragment peripheryHotelFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;
    private String code = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periphery_travel);

        initView();
        getMustPlayAround(code);
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        gridView = (GridView)findViewById(R.id.gridView);
        hot = (RadioButton) findViewById(R.id.hot);
        hotel = (RadioButton)findViewById(R.id.hotel);

        back.setOnClickListener(this);
        hot.setOnClickListener(this);
        hotel.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new AroundPlayAdapter(list,this);
        gridView.setAdapter(adapter);
        hot.setChecked(true);
        show(0);
    }
    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (index) {
            case 0:
                if (peripheryFragment == null) {
                    peripheryFragment = new PeripheryFragment();
                    fragmentTransaction.add(R.id.container, peripheryFragment);
                }
                fragmentTransaction.show(peripheryFragment);
                hot.setBackgroundResource(R.color.white);
                hot.setTextColor(getResources().getColor(R.color.ori));
                hotel.setBackgroundResource(R.color.ori);
                hotel.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                if (peripheryHotelFragment == null) {
                    peripheryHotelFragment = new PeripheryHotelFragment();
                    fragmentTransaction.add(R.id.container, peripheryHotelFragment);
                }
                fragmentTransaction.show(peripheryHotelFragment);
                hot.setBackgroundResource(R.color.ori);
                hot.setTextColor(getResources().getColor(R.color.white));
                hotel.setBackgroundResource(R.color.white);
                hotel.setTextColor(getResources().getColor(R.color.ori));
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(peripheryFragment);
                break;
            case 1:
                fragmentTransaction.hide(peripheryHotelFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.hot:
                show(0);
                break;
            case R.id.hotel:
                show(1);
                break;
        }
    }

    private void getMustPlayAround(String code){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("gaode_code",code);
        String sign = SignUtil.sign(map);
        HomeHttp.getMustPlayAround(sign, time_stamp, code, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<AroundPlayBean> bean =gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<AroundPlayBean>>() {
                            }.getType());
                            if (bean.size() != 0){
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

package com.cn.uca.ui.fragment.home.samecityka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.SameCityKaAdapter;
import com.cn.uca.bean.home.samecityka.SameCityKaBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.popupwindows.LoadingPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.samecityka.ActionDetailActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.NoScrollListView;
import com.cn.uca.view.ObservableScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by asus on 2017/12/4.
 * 活动广场
 */

public class ActionSquareFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TextView num1,num2,num3,num4,num5,num6;
    private ObservableScrollView scrollView;
    private NoScrollListView listView;
    private LinearLayout layout_1,layout_2;
    private RelativeLayout layout1,layout2,layout3,layout4,layout5,layout6;
    private int page = 1;
    private int pageCount = 5;
    private List<SameCityKaBean> list;
    private SameCityKaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_action_square,null);

        initView();
        getCityCafe(2);
        return view;
    }

    private void initView() {
        scrollView = (ObservableScrollView)view.findViewById(R.id.scrollView);
        layout_1 = (LinearLayout)view.findViewById(R.id.layout_1);
        layout_2 = (LinearLayout)view.findViewById(R.id.layout_2);
        layout1 = (RelativeLayout)view.findViewById(R.id.layout1);
        layout2 = (RelativeLayout)view.findViewById(R.id.layout2);
        layout3 = (RelativeLayout)view.findViewById(R.id.layout3);
        layout4 = (RelativeLayout)view.findViewById(R.id.layout4);
        layout5 = (RelativeLayout)view.findViewById(R.id.layout5);
        layout6 = (RelativeLayout)view.findViewById(R.id.layout6);
        num1 = (TextView)view.findViewById(R.id.num1);
        num2 = (TextView)view.findViewById(R.id.num2);
        num3 = (TextView)view.findViewById(R.id.num3);
        num4 = (TextView)view.findViewById(R.id.num4);
        num5 = (TextView)view.findViewById(R.id.num5);
        num6 = (TextView)view.findViewById(R.id.num6);

        listView = (NoScrollListView)view.findViewById(R.id.listView);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);

        SetLayoutParams.setRelativeLayout(layout1, (MyApplication.width- SystemUtil.dip2px(30))/3,SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout2, (MyApplication.width- SystemUtil.dip2px(30))/3,SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout3, (MyApplication.width- SystemUtil.dip2px(30))/3,SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout4, (MyApplication.width- SystemUtil.dip2px(30))/3+SystemUtil.dip2px(12),SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout5, (MyApplication.width- SystemUtil.dip2px(30))/3+SystemUtil.dip2px(24),SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout6, (MyApplication.width- SystemUtil.dip2px(30))/3+SystemUtil.dip2px(12),SystemUtil.dip2px(80));

        layout2.setVisibility(View.GONE);
        layout5.setVisibility(View.VISIBLE);

        scrollView.setOnScollChangedListener(new ObservableScrollView.OnScollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy){
                if (y>=220){
                    layout_1.setVisibility(View.VISIBLE);
                    layout_2.setVisibility(View.GONE);
                }else{
                    layout_1.setVisibility(View.GONE);
                    layout_2.setVisibility(View.VISIBLE);
                }
            }
        });

        list = new ArrayList<>();
        adapter = new SameCityKaAdapter(list,getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ActionDetailActivity.class);
                intent.putExtra("id",list.get(position).getCity_cafe_id());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout1:
                getCityCafe(1);
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.GONE);
                break;
            case R.id.layout2:
                getCityCafe(2);
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.VISIBLE);
                layout6.setVisibility(View.GONE);
                break;
            case R.id.layout3:
                getCityCafe(3);
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.VISIBLE);
                break;
//            case R.id.la
//            case R.id.action:
//                startActivity(new Intent(getActivity(), ActionDetailActivity.class));
//                return;
        }
    }

    private void getCityCafe(int id){
        Map<String,Object> map = new HashMap<>();
        map.put("user_card_type_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("city_id",2);
        map.put("beg_time","");
        map.put("end_time","");
        map.put("charge","");
        map.put("label_id","");
        String sign = SignUtil.sign(map);
        HomeHttp.getCityCafe(time_stamp, id, sign, page, pageCount, 2, "", "", "", "", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONObject object = jsonObject.getJSONObject("data");
                            num1.setText(object.getString("ordinary_sum_count"));
                            num4.setText(object.getString("ordinary_sum_count"));
                            num2.setText(object.getString("enterprise_sum_count"));
                            num5.setText(object.getString("enterprise_sum_count"));
                            num3.setText(object.getString("school_sum_count"));
                            num6.setText(object.getString("school_sum_count"));
                            List<SameCityKaBean> bean = gson.fromJson(object.getJSONArray("cityCafes").toString(),new TypeToken<List<SameCityKaBean>>() {
                            }.getType());
                            list.clear();
                            if (bean.size() > 0){
                                list.addAll(bean);
                                adapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
                                }else{
                                    ToastXutil.show("暂无数据");
                                }
                                adapter.setList(list);
                            }
                            break;
                        default:
                            break;
                    }

                }catch (Exception e){
                    Log.e("456",e.getMessage());
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

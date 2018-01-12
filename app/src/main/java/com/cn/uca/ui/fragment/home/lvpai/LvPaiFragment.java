package com.cn.uca.ui.fragment.home.lvpai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.MerchantAdapter;
import com.cn.uca.bean.home.lvpai.CommodityBean;
import com.cn.uca.bean.home.lvpai.MerchantBean;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.lvpai.MerchantDetailActivity;
import com.cn.uca.ui.view.home.lvpai.CommodityDetailActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/12/18.
 */

public class LvPaiFragment extends Fragment implements View.OnClickListener{

    private View view;
    private ListView listView;
    private MerchantAdapter merchantAdapter;
    private List<MerchantBean> listMerchant;
    private List<CommodityBean> listComment;
    private TextView type;
    private int id = 1;
    private int page = 1;
    private int pageCount = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lvpai,null);
        initView();
        getMerchants("","","");
        return view;
    }

    private void initView() {
        listMerchant = new ArrayList<>();
        listComment = new ArrayList<>();
        listView = (ListView)view.findViewById(R.id.listView);
        merchantAdapter = new MerchantAdapter(listMerchant,getActivity());
        listView.setAdapter(merchantAdapter);
        type = (TextView)view.findViewById(R.id.type);
        type.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                Intent intent = new Intent();
               switch (id){
                   case 1:
                       intent.setClass(getActivity(),MerchantDetailActivity.class);
                       intent.putExtra("id",listMerchant.get(position).getTrip_shoot_merchant_id());
                       startActivity(intent);
                       break;
                   case 2:
                       intent.setClass(getActivity(),CommodityDetailActivity.class);
                       intent.putExtra("id",listComment.get(position).getTrip_shoot_id());
                       startActivity(intent);
                       break;
               }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.type:
                switch (id){
                    case 1:
                        type.setText("商品");
                        id = 2;
                        if (listComment.size() > 0){
                            merchantAdapter.setList(listComment);
                        }else{
                            getCommodity("","","","","",0);
                        }
                        break;
                    case 2:
                        type.setText("商家");
                        id = 1  ;
                        if (listMerchant.size() > 0){
                            merchantAdapter.setList(listMerchant);
                        }else{
                            getMerchants("","","");
                        }
                        break;
                }
                break;
            case R.id.layout:
                startActivity(new Intent(getActivity(), MerchantDetailActivity.class));
                break;
            case R.id.layout2:
                startActivity(new Intent(getActivity(), CommodityDetailActivity.class));
                break;
        }
    }

    /**
     * 获取商家
     * @param sortType
     * @param sortMode
     * @param keyword
     */
    private void getMerchants(String sortType,String sortMode,String keyword){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("gaode_code","0755");
        map.put("sortType",sortType);
        map.put("sortMode",sortMode);
        map.put("keyword ",keyword );
        String sign = SignUtil.sign(map);
        HomeHttp.getMerchants(time_stamp, sign, account_token, page, pageCount, "0755", sortType, sortMode, keyword, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200) {
                        JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                        int code = jsonObject.getInt("code");
                        switch (code) {
                            case 0:
                                Gson gson = new Gson();
                                JSONArray array = jsonObject.getJSONObject("data").getJSONArray("merchants");
                                List<MerchantBean> bean = gson.fromJson(array.toString(), new TypeToken<List<MerchantBean>>() {
                                }.getType());
                                if (bean.size() > 0) {
                                    listMerchant.addAll(bean);
                                    merchantAdapter.setList(listMerchant);
                            }
                        }
                    }
                }catch (Exception e){
                    Log.e("456", e.getMessage() + "--");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    private void getCommodity(String labels,String type,String sortType,String sortMode,String keyword,int id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("labels",labels);
        map.put("type",type);
        map.put("sortType",sortType);
        map.put("sortMode",sortMode);
        map.put("keyword ",keyword );
        if (id != 0){
            map.put("trip_shoot_merchant_id",id);
        }
        String sign = SignUtil.sign(map);
        HomeHttp.getTs(time_stamp, sign, account_token, page, pageCount, labels, type, sortType, sortMode, id, keyword, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200) {
                        JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                        Log.e("456", jsonObject.toString() + "--");
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                Gson gson = new Gson();
                                JSONArray array = jsonObject.getJSONObject("data").getJSONArray("tripShoots");
                                List<CommodityBean> bean = gson.fromJson(array.toString(), new TypeToken<List<CommodityBean>>() {
                                }.getType());
                                if (bean.size() > 0) {
                                    listComment.addAll(bean);
                                    merchantAdapter.setList(listComment);
                                }
                                break;
                        }
                    }
                }catch (Exception e){
                    Log.e("456", e.getMessage() + "--");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                    Log.e("456", jsonObject.toString() + "--");
                }catch (Exception e){
                    Log.e("456", e.getMessage() + "--");
                }
            }
        });
    }
}

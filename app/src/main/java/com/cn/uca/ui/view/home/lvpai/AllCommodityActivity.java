package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.MerchantAdapter;
import com.cn.uca.bean.home.lvpai.CommodityBean;
import com.cn.uca.config.Constant;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
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

//旅拍全部商品
public class AllCommodityActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;
    private int pageCount = Constant.PAGE_COUNT;
    private int page = Constant.PAGE;
    private List<CommodityBean> listComment;
    private MerchantAdapter merchantAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_commodity);

        initView();
        getCommodity("","","","","",0);
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);
        listComment = new ArrayList<>();
        merchantAdapter = new MerchantAdapter(listComment,this);
        listView.setAdapter(merchantAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(AllCommodityActivity.this,CommodityDetailActivity.class);
                intent.putExtra("id",listComment.get(position).getTrip_shoot_id());
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

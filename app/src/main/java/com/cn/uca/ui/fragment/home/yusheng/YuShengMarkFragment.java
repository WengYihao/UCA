package com.cn.uca.ui.fragment.home.yusheng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.yusheng.YuShengMarkAdapter;
import com.cn.uca.bean.home.yusheng.LifeHistoricalsBean;
import com.cn.uca.bean.home.yusheng.YushengMarkBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/10/25.
 */

public class YuShengMarkFragment extends Fragment {
    private View view;
    private SmartRefreshLayout refreshLayout;
    private ListView listView;
    private YuShengMarkAdapter markAdapter;
    private List<LifeHistoricalsBean> list;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mark_yusheng,null);
        
        initView();
        return view;
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        listView = (ListView)view.findViewById(R.id.listView);

        list = new ArrayList<>();
        markAdapter = new YuShengMarkAdapter(list,getActivity());
        listView.setAdapter(markAdapter);

        getLifeHistorical(1);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLifeHistorical(1);
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getLifeHistorical(page);
                        refreshlayout.finishLoadmore();
                    }
                }, 1000);
            }
        });
//        refreshLayout.autoRefresh();
    }

    private void getLifeHistorical(int page){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String sign = SignUtil.sign(map);
        HomeHttp.getLifeHistorical(sign, time_stamp, account_token, page, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
//                    Log.i("123",response)
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            YushengMarkBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YushengMarkBean>() {
                            }.getType());
                            if (bean.getLifeHistoricals().size() > 0) {
                                list.clear();
                                list.addAll(bean.getLifeHistoricals());
                                markAdapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
                                }else{
                                    markAdapter.setList(list);
                                }

//                                if (bean.getLifeHistoricals().size() != 0){
//
//                                }
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage()+"--");
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

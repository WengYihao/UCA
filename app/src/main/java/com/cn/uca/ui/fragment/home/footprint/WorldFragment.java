package com.cn.uca.ui.fragment.home.footprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.footprint.WorldPrintAdapter;
import com.cn.uca.bean.home.footprint.CountryDetailsBean;
import com.cn.uca.bean.home.footprint.WorldCountryBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.home.footprint.AddFootActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.NoScrollListView;
import com.cn.uca.view.TouchWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/11/7.
 */

public class WorldFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TouchWebView webView;
    private RelativeLayout addLayout;
    private int travelYearNum,travelCityNum;
    private TextView share,yearNum,cityNum;
    private SmartRefreshLayout refreshLayout;
    private NoScrollListView listView;
    private WorldPrintAdapter worldPrintAdapter;
    private List<WorldCountryBean> list;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_world,null);

        initView();
        return view;
    }

    private void initView() {
        share = (TextView)view.findViewById(R.id.share);
        webView = (TouchWebView) view.findViewById(R.id.webView);
        addLayout = (RelativeLayout)view.findViewById(R.id.addLayout);
        yearNum = (TextView)view.findViewById(R.id.yearNum);
        cityNum = (TextView)view.findViewById(R.id.cityNum);
        listView = (NoScrollListView)view.findViewById(R.id.listView);
        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        list = new ArrayList<>();
        worldPrintAdapter = new WorldPrintAdapter(list,getActivity());
        listView.setAdapter(worldPrintAdapter);
        share.setOnClickListener(this);
        Map<String ,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        String url = "http://www.szyouka.com:8080/youkatravel/api/footprint/page/footprintMap.do?sign="+sign+"&time_stamp="+time_stamp;
        webView.loadUrl(url);

        SetLayoutParams.setLinearLayout(addLayout, MyApplication.width,MyApplication.height-SystemUtil.dip2px(70));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setBackgroundColor(0); // 设置背景色
        webView.addJavascriptInterface(new footprint(), "footprint");
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    String msg = "world";
                    String bc = "b29b71";
                    String sc = "37200d";
                    String sic = "b29b71";
                    String ssc = "7e5a30";
                    String shc = "7e5a30";
                    webView.loadUrl("javascript:setConfig('" + bc + "','" + sc +  "','" + sic + "','" + ssc + "','" + shc + "')");
                    webView.loadUrl("javascript:initialMap('" + msg + "')");

                    getFootprintWorld(5);
                }
            }
        });

        loadMoreFoodPrintWorld(1);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        loadMoreFoodPrintWorld(page);
                        refreshlayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
    }

    private void getFootprintWorld(int pageCount){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("pageCount",pageCount);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getFootprintWorld(sign, time_stamp, account_token, pageCount, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code) {
                        case 0:
                            Gson gson = new Gson();
                            CountryDetailsBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<CountryDetailsBean>() {
                            }.getType());
                            travelYearNum = bean.getTravel_age();
                            travelCityNum = bean.getCountry_number();
                            yearNum.setText(travelYearNum+"");
                            cityNum.setText(travelCityNum+"");
                            for (int i = 0;i < bean.getFootprintWorldCountrys().size();i++){
                                String countryCode = bean.getFootprintWorldCountrys().get(i).getCountry_id()+"";
                                String color = "f8e58c";
                                webView.loadUrl("javascript:setMapColor('" + countryCode + "','" + color + "')");
                            }
//                            if (backlayout.getVisibility() == View.VISIBLE){
//                                backlayout.setVisibility(View.GONE);
//                            }
                            if (bean.getFootprintWorldCountrys().size() > 0){
                                list.clear();
                                list.addAll(bean.getFootprintWorldCountrys());
                                worldPrintAdapter.setList(list);
                            }else {
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
                                }
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

    private void loadMoreFoodPrintWorld(int page){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("pageCount",5);
        map.put("page",page);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.loadMoreFoodPrintWorld(sign, time_stamp, account_token, page, 5, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<WorldCountryBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("footprintWorldCountrys").toString(),new TypeToken<List<WorldCountryBean>>() {
                            }.getType());
                            if (bean.size() > 0) {
                                list.addAll(bean);
                                worldPrintAdapter.setList(list);
                            } else {
                                if (list.size() != 0) {
                                    ToastXutil.show("没有更多数据了");
                                } else {
                                    worldPrintAdapter.setList(list);
                                }
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
            case R.id.share:
                getShare();
                break;
        }
    }

    private void getShare(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("shareType","ZUJI");
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.getShare(account_token, time_stamp, sign, "ZUJI",0, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            String share_title = jsonObject.getJSONObject("data").getString("share_title");
                            String web_url = jsonObject.getJSONObject("data").getString("web_url");
                            WeChatManager.instance().sendWebPageToWX(getActivity(),true,web_url,R.mipmap.logo,share_title,"快来围观一下我的足迹呗！");
                            break;
                        default:
                            ToastXutil.show("分享失败");
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
    public class footprint{
            @JavascriptInterface
             public void mapClick(String name,String code){
                if (!StringXutil.isEmpty(name)){
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),AddFootActivity.class);
                    intent.putExtra("type","world");
                    intent.putExtra("name",name);
                    intent.putExtra("code",code);
                    intent.putExtra("yearNum",travelYearNum);
                    intent.putExtra("cityNum",travelCityNum);
                    startActivityForResult(intent,0);
                }
            }
     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (data != null){
                    String name = data.getStringExtra("code");
                    String color = "f8e58c";
                    webView.loadUrl("javascript:setMapColor('" + name + "','" + color + "')");
                    webView.loadUrl("javascript:setMapColor('" + name + "','" + color + "')");
                    getFootprintWorld(page*5+1);
                }
                break;
        }
    }
}

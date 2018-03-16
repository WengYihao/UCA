package com.cn.uca.ui.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.SearchAdapter;
import com.cn.uca.bean.home.SearchBean;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.raider.RaiderCityActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.MyEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,cancel;
    private MyEditText search;
    private LinearLayout layout;
    private ListView listView;
    private FluidLayout recommendView,historyView;
    private List<String> recommendList,historyList;
    private List <SearchBean> list;
    private SearchAdapter adapter;
    private String keyword = "";
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
        emptyView = LayoutInflater.from(this).inflate(R.layout.emptyview_layout,null);
        back = (TextView)findViewById(R.id.back);
        cancel = (TextView)findViewById(R.id.cancel);
        recommendView = (FluidLayout)findViewById(R.id.recommendView);
        historyView = (FluidLayout)findViewById(R.id.historyView);

        layout = (LinearLayout)findViewById(R.id.layout);
        search = (MyEditText)findViewById(R.id.search);
        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new SearchAdapter(list,this);
        listView.setAdapter(adapter);
        cancel.setOnClickListener(this);
        SpannableString ss = new SpannableString("搜索关键字");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        search.setHint(new SpannedString(ss));

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    keyword = StringXutil.checkEditText(search);
                    if (!"".equals(keyword)) {
                        search(keyword);
                    }
                }
                return false;
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString() == null || "".equals(s.toString())) {
                    listView.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                } else {
                    cancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (list.get(position).getAppPage().getAppPageType()){
                    case NO:

                        break;
                    case GONGLUE_ALL:
                        startActivity(new Intent(SearchActivity.this, RaiderCityActivity.class));
                        break;
                }
            }
        });

        recommendList = new ArrayList<>();
        recommendList.add("九寨沟");
        recommendList.add("北京天安门");
        recommendList.add("世界之窗");
        recommendList.add("交通方案");
        recommendList.add("维也纳酒店");
        historyList = new ArrayList<>();
        historyList.add("万里长城");
        historyList.add("嘻嘻");
        historyList.add("哈哈");
        historyList.add("嗯嗯");
        historyList.add("哦哦");

        genTag(recommendList,recommendView);
        genTag(historyList,historyView);
        back.setOnClickListener(this);

    }

    private void genTag(List<String> list,FluidLayout fluidLayout) {
        fluidLayout.removeAllViews();
        fluidLayout.setGravity(Gravity.TOP);
        for (int i = 0; i < list.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText(list.get(i));
            tv.setTextSize(13);
            tv.setBackgroundResource(R.drawable.text_search_bg);


            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(12, 12, 12, 12);
            fluidLayout.addView(tv, params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.cancel:
                search.setText("");
                listView.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void search(String keyword){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("keyword",keyword);
        String sign = SignUtil.sign(map);
        HomeHttp.search(sign, account_token, time_stamp, keyword, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200){
                        JSONObject jsonObject =new JSONObject(new String(bytes,"UTF-8"));
                        Log.e("456",jsonObject.toString()+"--");
                        int code  = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                Gson gson = new Gson();
                                List<SearchBean> bean = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<SearchBean>>() {
                                }.getType());
                                list.clear();
                                adapter.setList(list);
                                listView.setVisibility(View.VISIBLE);
                                if (bean.size() > 0){
                                    layout.setVisibility(View.GONE);
                                    list.addAll(bean);
                                    adapter.setList(list);
                                }else{
//                                    listView.setEmptyView(emptyView);
                                    ToastXutil.show("暂无搜索内容");
                                }
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("456",e.getMessage()+"报错");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}

package com.cn.uca.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.cn.uca.R;
import com.cn.uca.adapter.HistoryAdapter;
import com.cn.uca.adapter.SeachAdapter;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SetListView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseBackActivity {

    private GridView recommendView,historyView;
    private HistoryAdapter historyAdapter;
    private SeachAdapter recommendAdapter;
    private List<String> recommendList,historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
        recommendView = (GridView)findViewById(R.id.recommendView);
        historyView = (GridView)findViewById(R.id.historyView);

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
        recommendAdapter = new SeachAdapter(recommendList,this);
        historyAdapter = new HistoryAdapter(historyList,this);

        recommendView.setAdapter(recommendAdapter);
        SetListView.setGridViewHeightBasedOnChildren(recommendView);
        historyView.setAdapter(historyAdapter);
        SetListView.setGridViewHeightBasedOnChildren(historyView);
    }
}

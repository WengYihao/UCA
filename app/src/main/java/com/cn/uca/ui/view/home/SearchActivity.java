package com.cn.uca.ui.view.home;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.view.FluidLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private FluidLayout recommendView,historyView;
    private List<String> recommendList,historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        recommendView = (FluidLayout)findViewById(R.id.recommendView);
        historyView = (FluidLayout)findViewById(R.id.historyView);

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
        }
    }
}

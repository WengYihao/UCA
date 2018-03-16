package com.cn.uca.ui.view.user;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.user.CollectionAdapter;
import com.cn.uca.bean.user.CollectionBean;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,title01,title03;
    private LinearLayout layout;
    private RefreshLayout refreshLayout;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title03 = (TextView)findViewById(R.id.title03);

        layout = (LinearLayout)findViewById(R.id.layout);

        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title03.setOnClickListener(this);

        show(0);
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshlayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();

    }

    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        switch (index) {
            case 0:
                layout.setBackgroundResource(R.drawable.gradient);
                title01.setBackgroundResource(0);
                title03.setBackgroundResource(0);
                title01.setTextColor(getResources().getColor(R.color.ori));
                title01.setBackgroundResource(R.color.white);
                title03.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                layout.setBackgroundResource(R.drawable.gradient);
                title01.setBackgroundResource(0);
                title03.setBackgroundResource(0);
                title01.setTextColor(getResources().getColor(R.color.white));
                title03.setTextColor(getResources().getColor(R.color.ori));
                title03.setBackgroundResource(R.color.white);
                break;
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.title01:
                show(0);
                break;
            case R.id.title03:
                show(1);
                break;
        }
    }
}

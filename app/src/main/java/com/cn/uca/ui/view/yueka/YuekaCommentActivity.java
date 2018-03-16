package com.cn.uca.ui.view.yueka;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

//约咖-我的评价
public class YuekaCommentActivity extends BaseBackActivity implements View.OnClickListener {

    private RefreshLayout refreshLayout;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yueka_comment);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }
}

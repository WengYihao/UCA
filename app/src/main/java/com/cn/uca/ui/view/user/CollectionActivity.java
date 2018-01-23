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

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,title01,title02,title03;
    private ListView listView;
    private int currentIndex = -1;
    private CollectionAdapter collectionAdapter;
    private List<CollectionBean> list;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        title03 = (TextView)findViewById(R.id.title03);

        layout = (LinearLayout)findViewById(R.id.layout);

        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);

        list = new ArrayList<>();
        CollectionBean bean = new CollectionBean();
        bean.setName("柬埔寨酒店");
        bean.setSum(280);
        bean.setPrice("4500-5000");
        bean.setType("豪华总统套房");
        bean.setStart(4);
        CollectionBean bean1 = new CollectionBean();
        bean1.setName("深圳东部华侨城");
        bean1.setSum(450);
        bean1.setPrice("180-250");
        bean1.setType("两日游（含费用）");
        bean1.setStart(3);
        list.add(bean);
        list.add(bean1);
        collectionAdapter = new CollectionAdapter(list,this);
        listView.setAdapter(collectionAdapter);
        show(0);

    }

    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        switch (index) {
            case 0:
                layout.setBackgroundResource(R.drawable.gradient);
                title01.setBackgroundResource(0);
                title02.setBackgroundResource(0);
                title03.setBackgroundResource(0);
                title01.setTextColor(getResources().getColor(R.color.ori));
                title01.setBackgroundResource(R.color.white);
                title02.setTextColor(getResources().getColor(R.color.white));
                title03.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                layout.setBackgroundResource(R.drawable.gradient);
                title01.setBackgroundResource(0);
                title02.setBackgroundResource(0);
                title03.setBackgroundResource(0);
                title01.setTextColor(getResources().getColor(R.color.white));
                title02.setTextColor(getResources().getColor(R.color.ori));
                title02.setBackgroundResource(R.color.white);
                title03.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                layout.setBackgroundResource(R.drawable.gradient);
                title01.setBackgroundResource(0);
                title02.setBackgroundResource(0);
                title03.setBackgroundResource(0);
                title01.setTextColor(getResources().getColor(R.color.white));
                title02.setTextColor(getResources().getColor(R.color.white));
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
            case  R.id.title02:
                show(1);
                break;
            case R.id.title03:
                show(2);
                break;
        }
    }
}

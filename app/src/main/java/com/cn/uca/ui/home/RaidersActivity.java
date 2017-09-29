package com.cn.uca.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.LetterAdapter;
import com.cn.uca.adapter.raiders.RaidersAdapter;
import com.cn.uca.bean.home.RaidersBean;
import com.cn.uca.ui.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;

import java.util.ArrayList;
import java.util.List;

public class RaidersActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private ListView letter,bbb;
    private LetterAdapter letterAdapter;
    private String [] data;
    private List<RaidersBean> list;
    private RaidersAdapter raidersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);//设置状态栏沉浸式
        setContentView(R.layout.activity_raiders);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);
        letter = (ListView)findViewById(R.id.letter);
        bbb = (ListView)findViewById(R.id.bbb);
        data = getResources().getStringArray(R.array.letter_list);
        letterAdapter = new LetterAdapter(data,this);
        letter.setAdapter(letterAdapter);

        list = new ArrayList<>();
        RaidersBean bean = new RaidersBean();
        bean.setName("深圳");
        bean.setSize("2.3m");
        bean.setUrl("http://www.szyouka.com/1.png");
        list.add(bean);

        RaidersBean bean1 = new RaidersBean();
        bean1.setName("广州");
        bean1.setSize("2.5m");
        bean1.setUrl("http://www.szyouka.com/2.png");
        list.add(bean1);

        RaidersBean bean2 = new RaidersBean();
        bean2.setName("上海");
        bean2.setSize("2.8m");
        bean2.setUrl("http://www.szyouka.com/3.png");
        list.add(bean2);

        RaidersBean bean3 = new RaidersBean();
        bean3.setName("北京");
        bean3.setSize("3.1,");
        bean3.setUrl("http://www.szyouka.com/4.png");
        list.add(bean3);

        raidersAdapter = new RaidersAdapter(list,this);
        bbb.setAdapter(raidersAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }
}

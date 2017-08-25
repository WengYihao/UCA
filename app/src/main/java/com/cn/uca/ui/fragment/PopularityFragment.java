package com.cn.uca.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cn.uca.R;
import com.cn.uca.adapter.PopularityAdapter;
import com.cn.uca.bean.PopularityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/23.
 */

public class PopularityFragment extends Fragment {

    private View view;
    private ListView listView;
    private PopularityAdapter popularityAdapter;
    private List<PopularityBean> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_popularity,null);

        initView();
        return view;
    }

    private void initView() {
        listView = (ListView)view .findViewById(R.id.listView);

        list = new ArrayList<>();
        popularityAdapter = new PopularityAdapter(list,getActivity());

        PopularityBean bean = new PopularityBean();
        bean.setName("【双飞机票】新疆往返机票一份");
        bean.setCount(500);
        bean.setSum(240);
        list.add(bean);
        PopularityBean bean2 = new PopularityBean();
        bean2.setName("【旅游门票】北京故宫三天游");
        bean2.setCount(800);
        bean2.setSum(670);
        list.add(bean2);

        listView.setAdapter(popularityAdapter);

    }
}

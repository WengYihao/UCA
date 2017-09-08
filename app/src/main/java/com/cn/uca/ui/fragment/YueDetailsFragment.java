package com.cn.uca.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cn.uca.R;
import com.cn.uca.adapter.ContentAdapter;
import com.cn.uca.adapter.LineAdapter;
import com.cn.uca.adapter.RecommendAdapter;
import com.cn.uca.bean.RecommendBean;
import com.cn.uca.util.SetListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/9/6.
 */

public class YueDetailsFragment extends Fragment implements View.OnClickListener{

    private View view;
    private ListView line,content;
    private RelativeLayout layout1,layout2;
    private LinearLayout notes;//退款须知
    private GridView recommend;
    private List<String> list,listContent;
    private LineAdapter lineAdapter;
    private ContentAdapter contentAdapter;
    private boolean isShow = true;
    private ImageView icon;
    private List<RecommendBean> listRecomment;
    private RecommendAdapter recommendAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details_yue,null);

        initView();
        return view;
    }

    private void initView() {
        line = (ListView)view.findViewById(R.id.line);
        content = (ListView)view.findViewById(R.id.content);

        list = new ArrayList<>();
        list.add("深圳北站");
        list.add("世界之窗");
        list.add("腾讯总部");
        list.add("游咔公司");
        list.add("随便去哪");

        lineAdapter = new LineAdapter(list,getActivity());
        line.setAdapter(lineAdapter);
        SetListView.setListViewHeightBasedOnChildren(line);

        listContent = new ArrayList<>();
        listContent.add("三月，醉一场青春的流年。慢步在三月的春光里，走走停停,看花开嫣然,看春雨绵绵，感受春风拂面，春天，就是青春的流年。青春，是人生中最美的风景。青春，是一场花开的遇见");
        listContent.add("青春，是一场痛并快乐着的旅行；青春，是一场轰轰烈烈的比赛；青春，是一场鲜衣奴马的争荣岁月；青春，是一场风花雪月的光阴。");
        listContent.add("三月的鲜花，一树树，一束束，一簇簇，而青春，就是像三月的鲜花一样美丽迷人，生机盎然，姹紫嫣红，生机勃勃。");
        contentAdapter = new ContentAdapter(listContent,getActivity());
        content.setAdapter(contentAdapter);
        SetListView.setListViewHeightBasedOnChildren(content);

        layout1 = (RelativeLayout)view.findViewById(R.id.layout1);
        layout2 = (RelativeLayout)view.findViewById(R.id.layout2);
        notes = (LinearLayout)view.findViewById(R.id.notes);

        icon = (ImageView)view.findViewById(R.id.icon);

        recommend = (GridView)view.findViewById(R.id.recommend);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);

        listRecomment = new ArrayList<>();
        RecommendBean bean = new RecommendBean();
        bean.setPrice("￥230-330");
        bean.setPlace("【北京】");
        bean.setTitle("包你爽翻天");
        listRecomment.add(bean);

        RecommendBean bean2 = new RecommendBean();
        bean2.setPrice("￥350-450");
        bean2.setPlace("【上海】");
        bean2.setTitle("不爽不嗨我倒贴");
        listRecomment.add(bean2);

        RecommendBean bean3 = new RecommendBean();
        bean3.setPrice("￥380-500");
        bean3.setPlace("【广州】");
        bean3.setTitle("老司机带路");
        listRecomment.add(bean3);

        RecommendBean bean4 = new RecommendBean();
        bean4.setPrice("￥400-530");
        bean4.setPlace("【深圳】");
        bean4.setTitle("快上我的车");
        listRecomment.add(bean4);

        recommendAdapter = new RecommendAdapter(listRecomment,getActivity());
        recommend.setAdapter(recommendAdapter);
        SetListView.setGridViewHeightBasedOnChildren(recommend);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout1:

                break;
            case R.id.layout2:
                if (isShow){
                    notes.setVisibility(View.VISIBLE);
                    isShow = false;
                    icon.setImageResource(R.mipmap.down);
                }else {
                    notes.setVisibility(View.GONE);
                    icon.setImageResource(R.mipmap.right);
                    isShow = true;
                }
                break;
        }
    }
}

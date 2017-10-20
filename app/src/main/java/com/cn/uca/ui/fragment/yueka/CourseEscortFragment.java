package com.cn.uca.ui.fragment.yueka;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cn.uca.R;
import com.cn.uca.adapter.yueka.CourseEscortAdapter;
import com.cn.uca.bean.CourseEscortBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/9/6.
 */

public class CourseEscortFragment extends Fragment {

    private View view;
    private ListView courseEscort;
    private List<CourseEscortBean> list;
    private CourseEscortAdapter courseEscortAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_escort_course,null);

        initView();
        return view;
    }

    private void initView() {
        courseEscort = (ListView)view.findViewById(R.id.courseEscort);

        list = new ArrayList<>();
        CourseEscortBean bean = new CourseEscortBean();
        List<String> list1 =  new ArrayList<>();
        list1.add("http://www.szyouka.com/1.png");
        list1.add("http://www.szyouka.com/2.png");
        list1.add("http://www.szyouka.com/3.png");
        bean.setPicList(list1);
        bean.setType("北京-两天一夜");
        bean.setTime("2017-08-28");
        bean.setEvaluate("长得这么可爱，人家当然是男孩子");
        list.add(bean);

        CourseEscortBean bean1 = new CourseEscortBean();
        bean1.setPicList(list1);
        bean1.setType("上海-两天一夜");
        bean1.setTime("2017-07-25");
        bean1.setEvaluate("羁绊，是什么意思？");
        list.add(bean1);

        CourseEscortBean bean2 = new CourseEscortBean();
        bean2.setPicList(list1);
        bean2.setType("深圳-两天两夜");
        bean2.setTime("2017-09-18");
        bean2.setEvaluate("努力有用的话还要天才干嘛？");
        list.add(bean2);

        courseEscortAdapter = new CourseEscortAdapter(list,getActivity());
        courseEscort.setAdapter(courseEscortAdapter);
//        SetListView.setListViewHeightBasedOnChildren(courseEscort);
    }
}

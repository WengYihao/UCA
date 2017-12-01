package com.cn.uca.impl.yueka;

import android.widget.ListView;

import com.cn.uca.adapter.yueka.LinePointAdapter;
import com.cn.uca.bean.yueka.PlacesBean;

import java.util.List;

/**
 * Created by asus on 2017/11/14.
 */

public interface LinePointCallBack {
    void changPoint(ListView listView, LinePointAdapter adapter,List<PlacesBean> list);
}

package com.cn.uca.impl.yusheng;


import com.cn.uca.bean.home.yusheng.YuShengDayBean;
import com.cn.uca.bean.home.yusheng.YuShengMonthBean;

import java.util.List;

/**
 * Created by asus on 2017/10/24.
 */

public interface YuShengMonthImpl extends ListAdapter {

    void appendItems(List<YuShengMonthBean> newItems);

    void setItems(List<YuShengMonthBean> moreItems);
}

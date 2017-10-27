package com.cn.uca.impl.yusheng;


import com.cn.uca.bean.home.yusheng.YuShengDayBean;

import java.util.List;

/**
 * Created by asus on 2017/10/24.
 */

public interface YuShengDayImpl extends ListAdapter {

    void appendItems(List<YuShengDayBean> newItems);

    void setItems(List<YuShengDayBean> moreItems);
}

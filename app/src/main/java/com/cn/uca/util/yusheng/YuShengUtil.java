package com.cn.uca.util.yusheng;

import com.cn.uca.bean.home.yusheng.YuShengDayBean;
import com.cn.uca.bean.home.yusheng.YuShengDayDetailsBean;
import com.cn.uca.bean.home.yusheng.YuShengMonthBean;
import com.cn.uca.bean.home.yusheng.YuShengMonthDetailsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/10/24.
 */

public class YuShengUtil {
    int currentOffset;
    public List<YuShengDayBean> dayItems(int qty, YuShengDayDetailsBean dayBean) {
        List<YuShengDayBean> items = new ArrayList<>();
        for (int i = 0; i < qty; i++) {
            int colSpan = 1;
            int rowSpan = colSpan*5/3 ;
            if (dayBean.getNow_days() == dayBean.getLifeDays().get(i).getDay()){
                colSpan = 7;
                rowSpan = 6;
            }
            YuShengDayBean item = new YuShengDayBean(colSpan, rowSpan, currentOffset + i,dayBean);
            items.add(item);
        }
        currentOffset += qty;
        return items;
    }

    public List<YuShengMonthBean> monthItems(int qty, YuShengMonthDetailsBean monthBean) {
        List<YuShengMonthBean> items = new ArrayList<>();
        for (int i = 0; i < qty; i++) {
            int colSpan = 1;
            int rowSpan = colSpan ;
            if (monthBean.getNow_month() == monthBean.getLifeMonthsRet().get(i).getMonth()){
                colSpan = 4;
                rowSpan = 4;
            }
            YuShengMonthBean item = new YuShengMonthBean(colSpan, rowSpan, currentOffset + i,monthBean);
            items.add(item);
        }
        currentOffset += qty;
        return items;
    }
}

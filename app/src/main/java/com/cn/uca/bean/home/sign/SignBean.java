package com.cn.uca.bean.home.sign;

import java.util.List;

/**
 * Created by asus on 2017/11/21.
 */

public class SignBean {
    private int integral;
    private List<SignDayBean> clockDays;
    private int continuity_clock;
    private int month_clock_sum;

    public int getIntegral() {
        return integral;
    }

    public List<SignDayBean> getClockDays() {
        return clockDays;
    }

    public int getContinuity_clock() {
        return continuity_clock;
    }

    public int getMonth_clock_sum() {
        return month_clock_sum;
    }
}

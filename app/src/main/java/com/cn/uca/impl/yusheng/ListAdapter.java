package com.cn.uca.impl.yusheng;

import android.widget.Adapter;

/**
 * Created by asus on 2017/10/24.
 */

public interface ListAdapter extends Adapter {
    boolean areAllItemsEnabled();
    boolean isEnabled(int var1);
}

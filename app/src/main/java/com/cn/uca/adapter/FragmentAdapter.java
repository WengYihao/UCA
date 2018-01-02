package com.cn.uca.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.cn.uca.bean.yueka.AcceptOrderBean;

import java.util.List;

/**
 * Created by asus on 2017/8/2.
 * fragment适配器
 */

public class FragmentAdapter extends FragmentPagerAdapter{
    private List<Fragment> list;
    private FragmentManager fm;

    //构造方法
    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void setFragments(List<Fragment> fragments) {
        if (this.list != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.list) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.list = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
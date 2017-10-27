package com.cn.uca.ui.fragment.home.yusheng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.uca.R;

/**
 * Created by asus on 2017/10/25.
 */

public class YuShengMarkFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mark_yusheng,null);
        
        initView();
        return view;
    }

    private void initView() {

    }
}

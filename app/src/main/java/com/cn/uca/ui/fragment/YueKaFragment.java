package com.cn.uca.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.jaeger.library.StatusBarUtil;

/**
 * Created by asus on 2017/8/11.
 */

public class YueKaFragment extends Fragment{

    private View view;
    private ImageView aaa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yueka, null);

        aaa = (ImageView)view.findViewById(R.id.aaa);
//        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), aaa);
        StatusBarUtil.setTranslucentForImageView(getActivity(), 255, aaa);//设置状态栏的透明度
        return view;
    }
}

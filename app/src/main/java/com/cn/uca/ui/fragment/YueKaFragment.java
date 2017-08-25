package com.cn.uca.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.RatingStarView;
import com.jaeger.library.StatusBarUtil;

/**
 * Created by asus on 2017/8/11.
 */

public class YueKaFragment extends Fragment{

    private View view;
    private ImageView aaa;
    private TextView wawa;
    private RatingStarView haha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yueka, null);

        aaa = (ImageView)view.findViewById(R.id.aaa);
//        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), aaa);
        StatusBarUtil.setTranslucentForImageView(getActivity(), 255, aaa);//设置状态栏的透明度

        wawa = (TextView)view.findViewById(R.id.wawa);

        haha = (RatingStarView)view.findViewById(R.id.haha);
        haha.setRating(3);

        wawa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastXutil.show(haha.getRating()+"---");
            }
        });
        return view;
    }
}

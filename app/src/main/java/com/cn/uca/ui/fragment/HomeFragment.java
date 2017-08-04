package com.cn.uca.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cn.uca.R;
import com.cn.uca.animate.ScaleInOutTransformer;
import com.cn.uca.config.BannerConfig;
import com.cn.uca.impl.OnBannerListener;
import com.cn.uca.loader.GlideImageLoader;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/2.
 */

public class HomeFragment extends Fragment implements OnBannerListener{

    private View view;
    private Banner banner;
    private List<String> images=new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private static int width;
    private static int height;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        width = SystemUtil.getWindowWidth(getActivity());
        height = SystemUtil.getWindowHeight(getActivity());
        initView();

        return view;
    }


    private void initView(){
        banner = (Banner)view.findViewById(R.id.banner);

        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");

        titles.add("十大星级品牌联盟，全场2折起");
        titles.add("生命不是要超越别人，而是要超越自己。");
        titles.add("己所不欲，勿施于人。——孔子");
        titles.add("嗨购5折不要停");

        banner.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height/3));
        //简单使用
        banner.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .setBannerAnimation(ScaleInOutTransformer.class)
                .setBannerTitles(titles)
                .start();
        banner.updateBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
    }
    @Override
    public void OnBannerClick(int position) {
        ToastXutil.show(position+""+images.get(position));
        ArrayList<String> list = new ArrayList<>();
        list.add(images.get(position));
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }


    // 获取屏幕宽度
    public static int getWidth() {
        return width;
    }

    // 获取屏幕高度
    public static int getHeight() {
        return height;
    }
}

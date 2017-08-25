package com.cn.uca.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.animate.ScaleInOutTransformer;
import com.cn.uca.config.BannerConfig;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.OnBannerListener;
import com.cn.uca.loader.GlideImageLoader;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.ui.CityActivity;
import com.cn.uca.ui.HotleActivity;
import com.cn.uca.ui.IndianaActivity;
import com.cn.uca.ui.PlaneTicketActivity;
import com.cn.uca.ui.RaidersActivity;
import com.cn.uca.ui.SearchActivity;
import com.cn.uca.ui.TourismActivity;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.Banner;
import com.cn.uca.view.MyEditText;
import com.cn.uca.view.PullToRefreshView;
import com.cn.uca.view.StickyScrollView;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/2.
 */

public class HomeFragment extends Fragment implements View.OnClickListener,OnBannerListener, StickyScrollView.OnScrollChangedListener{

    private View view;
    private StickyScrollView stickyScrollView;//滑动界面
    private RelativeLayout llTitle;//搜索框布局
    private TextView locationHint;//当前定位
    private Banner banner;//图片轮播
    private List<String> images=new ArrayList<>();//图片地址集合
    private List<String> titles = new ArrayList<>();//标题集合
    private PullToRefreshView pullToRefreshView;//刷新界面
    private int height; //透明内容高度
    private TextView planeTicket,hotle,tourism,oneRaiders,oneIndiana;//机票、酒店、旅游、一元攻略、一元夺宝
    private ImageView img;
    private TextView search_et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        initView();
        initListeners();
        return view;
    }

    private void initView(){
        pullToRefreshView = (PullToRefreshView)view.findViewById(R.id.pull);
        stickyScrollView = (StickyScrollView) view.findViewById(R.id.scrollView);
        banner = (Banner)view.findViewById(R.id.banner);
        llTitle = (RelativeLayout) view.findViewById(R.id.ll_good_detail);
        locationHint = (TextView)view.findViewById(R.id.locationHint);
        planeTicket = (TextView)view.findViewById(R.id.planeTicket);
        hotle = (TextView)view.findViewById(R.id.hotel);
        tourism = (TextView)view.findViewById(R.id.tourism);
        oneRaiders = (TextView)view.findViewById(R.id.oneRaiders);
        oneIndiana = (TextView)view.findViewById(R.id.oneIndiana);
        img = (ImageView)view.findViewById(R.id.img);
        search_et = (TextView) view.findViewById(R.id.search_et);

        locationHint.setOnClickListener(this);
        planeTicket.setOnClickListener(this);
        hotle.setOnClickListener(this);
        tourism.setOnClickListener(this);
        oneRaiders.setOnClickListener(this);
        oneIndiana.setOnClickListener(this);
        search_et.setOnClickListener(this);

        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");

        titles.add("十大星级品牌联盟，全场2折起");
        titles.add("生命不是要超越别人，而是要超越自己。");
        titles.add("己所不欲，勿施于人。——孔子");
        titles.add("嗨购5折不要停");

        banner.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyApplication.height/3));
        //简单使用
        banner.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .setBannerAnimation(ScaleInOutTransformer.class)//翻转动画
                .setBannerTitles(titles)//标题样式
                .start();
        banner.updateBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);

        stickyScrollView.setOnScrollListener(this);
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), img);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llTitle.getLayoutParams();
        params.setMargins(0, getStatusHeight(), 0, 0);
        llTitle.setLayoutParams(params);

        pullToRefreshView.setIsUpLoad(true);
        pullToRefreshView.setIsPull(false);
         //上拉加载
        pullToRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                pullToRefreshView.onFooterRefreshComplete();
                ToastXutil.show("上拉");
            }
        });
        // 下拉更新
        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {

            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                pullToRefreshView.onHeaderRefreshComplete();
//                llTitle.setVisibility(View.GONE);
                ToastXutil.show("下拉");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.locationHint:
                startActivity(new Intent(getActivity(), CityActivity.class));
                break;
            case R.id.planeTicket:
                startActivity(new Intent(getActivity(), PlaneTicketActivity.class));
                break;
            case R.id.hotel:
                startActivity(new Intent(getActivity(), HotleActivity.class));
                break;
            case R.id.tourism:
                startActivity(new Intent(getActivity(), TourismActivity.class));
                break;
            case R.id.oneRaiders:
                startActivity(new Intent(getActivity(), RaidersActivity.class));
                break;
            case R.id.oneIndiana:
                startActivity(new Intent(getActivity(),IndianaActivity.class));
                break;
            case R.id.search_et:
                startActivity(new Intent(getActivity(),SearchActivity.class));
                break;
        }
    }

    /**
     * 获取状态栏高度
     * @return
     */
    private int getStatusHeight() {
        int resourceId = getActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);

    }
    @Override
    public void OnBannerClick(int position) {
        ToastXutil.show(position+""+images.get(position));
        ArrayList<String> list = new ArrayList<>();
        list.add(images.get(position));
        OpenPhoto.imageUrl(getActivity(),list.size(),list);
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
    private void initListeners() {
        //获取内容总高度
        final ViewTreeObserver vto = banner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = banner.getHeight();
                //注意要移除
                banner.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);

            }
        });
        //获取title高度
        ViewTreeObserver viewTreeObserver1 = llTitle.getViewTreeObserver();
        viewTreeObserver1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = height - llTitle.getHeight() - getStatusHeight();//计算滑动的总距离
                stickyScrollView.setStickTop(llTitle.getHeight() + getStatusHeight());//设置距离多少悬浮
                //注意要移除
                llTitle.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });
    }
    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            llTitle.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            StatusBarUtil.setTranslucentForImageView(getActivity(), 0, img);
        } else if (t > 0 && t <= height) {
            float scale = (float) t / height;
            int alpha = (int) (255 * scale);
            llTitle.setBackgroundColor(Color.argb((int) alpha, 19, 181, 177));//设置标题栏的透明度及颜色
            StatusBarUtil.setTranslucentForImageView(getActivity(), alpha, img);//设置状态栏的透明度
        } else {
            llTitle.setBackgroundColor(Color.argb((int) 255, 19, 181, 177));
            StatusBarUtil.setTranslucentForImageView(getActivity(), 255, img);
        }
        int scrollY = stickyScrollView.getScrollY();
        if (scrollY<0){
            llTitle.setVisibility(View.GONE);
        }else{
            llTitle.setVisibility(View.VISIBLE);
        }

    }
}

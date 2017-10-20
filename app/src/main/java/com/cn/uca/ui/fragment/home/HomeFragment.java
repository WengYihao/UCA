package com.cn.uca.ui.fragment.home;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.animate.ScaleInOutTransformer;
import com.cn.uca.bean.home.CarouselFiguresBean;
import com.cn.uca.config.BannerConfig;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.banner.OnBannerListener;
import com.cn.uca.loader.GlideImageLoader;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.hotel.HotleActivity;
import com.cn.uca.ui.view.home.raider.RaidersActivity;
import com.cn.uca.ui.view.home.SearchActivity;
import com.cn.uca.ui.view.home.travel.TourismActivity;
import com.cn.uca.ui.view.home.yusheng.YuShengActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.Banner;
import com.cn.uca.view.StickyScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/2.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private StickyScrollView stickyScrollView;//滑动界面
    private RelativeLayout llTitle;//搜索框布局
    private Banner banner;//图片轮播
    private List<String> images=new ArrayList<>();//图片地址集合
    private int height; //透明内容高度
    private TextView planeTicket,hotle,tourism,oneRaiders;//机票、酒店、旅游、一元攻略、一元夺宝
    private TextView search_et;
    private List<CarouselFiguresBean> listPic;
    private String version,new_version;
    private String loadUrl,linkUrl;
//    TextToSpeech speech;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        getUserState();
        getLineVersion();
        initView();
        getCarouselFigures();
        initListeners();

        return view;
    }

    private void  getUserState(){
        QueryHttp.getUserState(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    if (code ==0 ){
                        JSONObject object = jsonObject.getJSONObject("data");
                        int bind_identity_state_id = object.getInt("bind_identity_state_id");
                        if (bind_identity_state_id == 1){
                            SharePreferenceXutil.setAuthentication(true);
                        }else{
                            SharePreferenceXutil.setAuthentication(false);
                        }
                    }
                }catch (Exception e){
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                Log.i("789",errorMsg.toString()+"--");
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    private void initView(){
        version = getVersion();
        listPic = new ArrayList<>();
        stickyScrollView = (StickyScrollView) view.findViewById(R.id.scrollView);
        banner = (Banner)view.findViewById(R.id.banner);
        llTitle = (RelativeLayout) view.findViewById(R.id.ll_good_detail);
        planeTicket = (TextView)view.findViewById(R.id.planeTicket);
        hotle = (TextView)view.findViewById(R.id.hotel);
        tourism = (TextView)view.findViewById(R.id.tourism);
        oneRaiders = (TextView)view.findViewById(R.id.oneRaiders);
        search_et = (TextView) view.findViewById(R.id.search_et);

        planeTicket.setOnClickListener(this);
        hotle.setOnClickListener(this);
        tourism.setOnClickListener(this);
        oneRaiders.setOnClickListener(this);
        search_et.setOnClickListener(this);

        SetLayoutParams.setRelativeLayout(banner,MyApplication.width,MyApplication.height/3);
        //简单使用
        StatusMargin.setRelativeLayout(getActivity(),llTitle);
//        speech = new TextToSpeech(getActivity(),new MyOnInitialListener());
    }

    private void getLineVersion() {
        QueryHttp.getLineVersion(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject object = jsonObject.getJSONObject("data");
                    new_version = object.getString("version_no");
                    linkUrl = object.getString("introduce_url");
                    loadUrl = object.getString("download_link");
                    if (!version.equals(new_version)){
                        handler.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    ShowPopupWindow.updateWindow(view,getActivity(),linkUrl,loadUrl);
                    break;
            }
        }
    };
//    class MyOnInitialListener implements TextToSpeech.OnInitListener{
//
//        @Override
//        public void onInit(int status) {
//            speech.setLanguage(Locale.CHINESE);
//        }
//    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.planeTicket:
//                startActivity(new Intent(getActivity(), PlaneTicketActivity.class));
                startActivity(new Intent(getActivity(), YuShengActivity.class));
//                speech.speak("铜锣湾（因为粤音比较顺口的关系，部分香港人会把铜锣湾读成铜锣“环”，使三字都变成阳平声）位于香港岛的中心北岸之西，是香港的主要商业及娱乐场所集中地。", TextToSpeech.QUEUE_FLUSH, null);
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
            case R.id.search_et:
                startActivity(new Intent(getActivity(),SearchActivity.class));
                break;
        }
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

    /**
     * 获取当前版本号
     * @return
     */
    private String getVersion()
    {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }
    private void getCarouselFigures(){
        HomeHttp.getCarouselFigures(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONArray("data");
                            Gson gson = new Gson();
                            List<CarouselFiguresBean> bean = gson.fromJson(array.toString(),new TypeToken<List<CarouselFiguresBean>>(){}.getType());
                            listPic.addAll(bean);
                            for (int i = 0;i<listPic.size();i++){
                                images.add(listPic.get(i).getPicture_url());
                            }
                            banner.setImages(images)
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(new OnBannerListener() {
                                        @Override
                                        public void OnBannerClick(int position) {

                                        }
                                    })
                                    .setBannerAnimation(ScaleInOutTransformer.class)//翻转动画
                                    .start();
                            banner.updateBannerStyle(BannerConfig.NUM_INDICATOR);
                            break;
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
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
                height = height - llTitle.getHeight() - SystemUtil.getStatusHeight(getActivity());//计算滑动的总距离
                stickyScrollView.setStickTop(llTitle.getHeight() + SystemUtil.getStatusHeight(getActivity()));//设置距离多少悬浮
                //注意要移除
                llTitle.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });
    }
}

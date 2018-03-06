package com.cn.uca.ui.fragment.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.alipay.AliPayUtil;
import com.cn.uca.animate.ScaleInOutTransformer;
import com.cn.uca.bean.home.CarouselFiguresBean;
import com.cn.uca.config.BannerConfig;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.banner.OnBannerListener;
import com.cn.uca.loader.GlideImageLoader;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.MainActivity;
import com.cn.uca.ui.view.home.hotel.HotleActivity;
import com.cn.uca.ui.view.home.lvpai.LvPaiActivity;
import com.cn.uca.ui.view.home.lvpai.MerchantManageActivity;
import com.cn.uca.ui.view.home.samecityka.SameCityKaActivity;
import com.cn.uca.ui.view.home.sign.SignActivity;
import com.cn.uca.ui.view.home.footprint.FootPrintActivity;
import com.cn.uca.ui.view.home.planeticket.PlaneTicketActivity;
import com.cn.uca.ui.view.home.raider.RaidersActivity;
import com.cn.uca.ui.view.home.SearchActivity;
import com.cn.uca.ui.view.home.travel.TourismActivity;
import com.cn.uca.ui.view.home.yusheng.YuShengActivity;
import com.cn.uca.ui.view.home.yusheng.YuShengDetailsActivity;
import com.cn.uca.ui.view.util.CountyActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.Banner;
import com.cn.uca.view.StickyScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/8/2.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private StickyScrollView stickyScrollView;//滑动界面
    private RelativeLayout llTitle,seachLayout;//搜索框布局
    private Banner banner;//图片轮播
    private List<String> images=new ArrayList<>();//图片地址集合
    private int height; //透明内容高度
    private TextView backlayout,sign;
    private LinearLayout planeTicket,hotel,tourism,oneRaiders,yusheng,footprint,samecityka,lvpai;//机票、酒店、旅游、一元攻略、余生、足迹
    private TextView search_et;
    private List<CarouselFiguresBean> listPic;
    private String version,new_version;
    private String loadUrl,linkUrl;
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
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            int bind_identity_state_id = object.getInt("bind_identity_state_id");
                            if (bind_identity_state_id == 1){
                                SharePreferenceXutil.setAuthentication(true);
                            }else{
                                SharePreferenceXutil.setAuthentication(false);
                            }
                            int open_life_id = object.getInt("open_life_id");
                            if (open_life_id == 1){
                                SharePreferenceXutil.setOpenYS(true);
                            }else{
                                SharePreferenceXutil.setOpenYS(false);
                            }
                            int orders_state_id = object.getInt("open_orders_state_id");
                            if (orders_state_id == 1){
                                SharePreferenceXutil.setLingKa(true);
                            }else{
                                SharePreferenceXutil.setLingKa(false);
                            }
                            SharePreferenceXutil.setClock(object.getBoolean("clock"));
                            SharePreferenceXutil.setEnter(object.getBoolean("owners"));
                            int weixin_state_id = object.getInt("bind_weixin_state_id");
                            if (weixin_state_id == 1){
                                SharePreferenceXutil.setBindWeCaht(true);
                            }else{
                                SharePreferenceXutil.setBindWeCaht(false);
                            }
                            int zhifubao_state_id = object.getInt("bind_zhifubao_state_id");
                            if (zhifubao_state_id == 1){
                                SharePreferenceXutil.setBindPay(true);
                            }else{
                                SharePreferenceXutil.setBindPay(false);
                            }
                            break;
                        case 17:
                            ToastXutil.show("登录已过期，请重新登录！");
                            SharePreferenceXutil.setExit(true);
                            SharePreferenceXutil.setSuccess(false);
                            SharePreferenceXutil.saveAccountToken("");
                            SharePreferenceXutil.saveAccessToken("");
                            SharePreferenceXutil.setOpenYS(false);
                            SharePreferenceXutil.setAuthentication(false);
                            break;
                    }
                }catch (Exception e){
                    Log.i("123",e.getMessage());
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
//        version = "1.1.0";
        version = getVersion();
        listPic = new ArrayList<>();
        stickyScrollView = (StickyScrollView) view.findViewById(R.id.scrollView);
        banner = (Banner)view.findViewById(R.id.banner);
        llTitle = (RelativeLayout) view.findViewById(R.id.ll_good_detail);
        seachLayout = (RelativeLayout)view.findViewById(R.id.seachLayout);
        planeTicket = (LinearLayout) view.findViewById(R.id.planeTicket);
        hotel = (LinearLayout)view.findViewById(R.id.hotel);
        tourism = (LinearLayout)view.findViewById(R.id.tourism);
        samecityka = (LinearLayout)view.findViewById(R.id.samecityka);
        lvpai = (LinearLayout)view.findViewById(R.id.lvpai);
        oneRaiders = (LinearLayout)view.findViewById(R.id.oneRaiders);
        yusheng = (LinearLayout)view.findViewById(R.id.yusheng);
        footprint = (LinearLayout)view.findViewById(R.id.footprint);
        search_et = (TextView) view.findViewById(R.id.search_et);
        backlayout = (TextView)view.findViewById(R.id.backlayout);
        sign = (TextView)view.findViewById(R.id.sign);

        planeTicket.setOnClickListener(this);
        hotel.setOnClickListener(this);
        tourism.setOnClickListener(this);
        samecityka.setOnClickListener(this);
        lvpai.setOnClickListener(this);
        oneRaiders.setOnClickListener(this);
        yusheng.setOnClickListener(this);
        footprint.setOnClickListener(this);
        search_et.setOnClickListener(this);
        sign.setOnClickListener(this);

        SetLayoutParams.setRelativeLayout(banner,MyApplication.width,MyApplication.height/3);
        //简单使用
        StatusMargin.setRelativeLayout(getActivity(),llTitle);
        StatusMargin.setRelativeLayoutTop(getActivity(),backlayout,MyApplication.height/3);
        SetLayoutParams.setRelativeLayout(seachLayout,MyApplication.width/2,SystemUtil.dip2px(35));
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
                    verifyStoragePermissions();
                    break;
            }
        }
    };

    public void verifyStoragePermissions() {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(getActivity(),
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                HomeFragment.this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Constant.WRITE_PERMISSIONS_REQUEST_CODE);
            }else{
                    ShowPopupWindow.updateWindow(view,getActivity(),linkUrl,loadUrl);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("456","--------"+requestCode);
        switch (requestCode) {
            case Constant.WRITE_PERMISSIONS_REQUEST_CODE: {
                Log.e("456","--------");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ShowPopupWindow.updateWindow(view,getActivity(),linkUrl,loadUrl);
                }
                break;
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign:
                startActivity(new Intent(getActivity(), SignActivity.class));
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
            case R.id.samecityka:
                 startActivity(new Intent(getActivity(), SameCityKaActivity.class));
                break;
            case R.id.lvpai:
                startActivity(new Intent(getActivity(), LvPaiActivity.class));
                break;
            case R.id.oneRaiders:
                startActivity(new Intent(getActivity(), RaidersActivity.class));
                break;
            case R.id.yusheng:
                if (SharePreferenceXutil.isOpenYS()){
                    startActivity(new Intent(getActivity(), YuShengDetailsActivity.class));
                }else{
                    startActivity(new Intent(getActivity(),YuShengActivity.class));
                }
                break;
            case R.id.footprint:
                if(SharePreferenceXutil.isSuccess()){
                    startActivity(new Intent(getActivity(), FootPrintActivity.class));
                }else{
                    ToastXutil.show("请先登录");
                }
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
                                            if (position == 0){
                                                MainActivity.mPager.setCurrentItem(1);
                                            }
                                        }
                                    })
                                    .setBannerAnimation(ScaleInOutTransformer.class)//翻转动画
                                    .start();
                            banner.updateBannerStyle(BannerConfig.NOT_INDICATOR);
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

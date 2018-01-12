package com.cn.uca.ui.view.yueka;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.animate.ScaleInOutTransformer;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.bean.yueka.SubmitYuekaBean;
import com.cn.uca.bean.yueka.YueKaDetailsBean;
import com.cn.uca.config.BannerConfig;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.banner.OnBannerListener;
import com.cn.uca.loader.GlideImageLoader;
import com.cn.uca.popupwindows.BuyYuaKaPopupWindow;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.ui.fragment.yueka.CourseEscortFragment;
import com.cn.uca.ui.fragment.yueka.YueDetailsFragment;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.Banner;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.RatingStarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.rong.imkit.RongIM;

public class YueChatActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView stateTitle,title01,title02;
    private int currentIndex = -1;
    private YueDetailsFragment yueDetailsFragment;
    private CourseEscortFragment courseEscortFragment;
    private FragmentTransaction fragmentTransaction;
    private int recordId,chatId;
    private TextView back,title,collection,name,age,sex,sum,price,maxPeople,title03,title04;
    private CircleImageView pic;
    private RatingStarView start;
    private ArrayList<PlacesBean> list;
    private String url;
    private FluidLayout lable;
    private Banner banner;
    private List<String> images=new ArrayList<>();//图片地址集合
    private RelativeLayout layout;
    private int isCollection = 0;
    private ArrayList<String> listPhoto;
    private List<String> StringData = new ArrayList<>();
    private SubmitYuekaBean bean;
    private String titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_chat);

        initView();
        getInfo();
        getEscortRecordInfo();
    }

    private void initView() {
        listPhoto = new ArrayList<>();
        stateTitle  =(TextView)findViewById(R.id.stateTitle);
        stateTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.getStatusHeight(this)));
        layout = (RelativeLayout)findViewById(R.id.layout);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        back = (TextView)findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        collection = (TextView)findViewById(R.id.collection);
        banner = (Banner)findViewById(R.id.banner);
        name = (TextView) findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        sex = (TextView) findViewById(R.id.sex);
        lable = (FluidLayout)findViewById(R.id.lable);
        sum = (TextView)findViewById(R.id.sum);
        price = (TextView) findViewById(R.id.price);
        maxPeople = (TextView)findViewById(R.id.maxPeople);
        pic = (CircleImageView)findViewById(R.id.pic);
        start = (RatingStarView)findViewById(R.id.start);
        title03 = (TextView)findViewById(R.id.title03);
        title04 = (TextView)findViewById(R.id.title04);

        back.setOnClickListener(this);
        collection.setOnClickListener(this);

        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
        title04.setOnClickListener(this);
        layout.getBackground().setAlpha(225);
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            recordId = intent.getIntExtra("id",0);
            YueKaHttp.getConfirmCoffee(recordId, new CallBack() {
                @Override
                public void onResponse(Object response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response.toString());
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                Gson gson = new Gson();
                                bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<SubmitYuekaBean>() {
                                }.getType());
                                Date start = SystemUtil.StringToUtilDate(bean.getBeg_time());
                                Date end  = SystemUtil.StringToUtilDate(bean.getEnd_time());
                                List<Date> listDate = SystemUtil.getBetweenDates(start,end);
                                for (int i = 0;i<listDate.size();i++){
                                    StringData.add(SystemUtil.UtilDateToString(listDate.get(i)));
                                }
                                break;
                        }
                    }catch (Exception e){
                        Log.i("456",e.getMessage()+"---");
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
    }
    private void getEscortRecordInfo(){
        YueKaHttp.getEscortRecordInfo(recordId, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            Gson gson = new Gson();
                            YueKaDetailsBean bean = gson.fromJson(object.toString(),new TypeToken<YueKaDetailsBean>(){}.getType());
                            ImageLoader.getInstance().displayImage(bean.getUser_head_portrait_url(), pic);
                            name.setText(bean.getUser_nick_name());
                            title.setText(bean.getEscort_record_name());
                            chatId = bean.getAccount_number_id();
                            titleName = bean.getUser_nick_name();
                            if (bean.isCollection()){
                                isCollection = 1;
                                collection.setBackgroundResource(R.mipmap.collection);
                            }else{
                                isCollection = 2;
                                collection.setBackgroundResource(R.mipmap.nocollection);
                            }
                            start.setRating((float)bean.getAverage_score());
                            if (!StringXutil.isEmpty(bean.getUser_birth_date())){
                                Date date = SystemUtil.StringToUtilDate(bean.getUser_birth_date());
                                age.setText(SystemUtil.getAge(date)+"岁");
                            }else {
                                age.setText("未知");
                            }
                            switch (bean.getSex_id()){
                                case 1:
                                    sex.setBackgroundResource(R.mipmap.man);
                                    break;
                                case 2:
                                    sex.setBackgroundResource(R.mipmap.woman);
                                    break;
                                case 3:
                                    sex.setVisibility(View.GONE);
                                    break;
                            }
                            images.addAll(bean.getCover_photo_urls());
                            for (int i = 0;i<images.size();i++){
                                listPhoto.add(images.get(i));
                            }
                            banner.setImages(images)
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(new OnBannerListener() {
                                        @Override
                                        public void OnBannerClick(int position) {
                                            OpenPhoto.imageUrl(YueChatActivity.this,position,listPhoto);
                                        }
                                    })
                                    .setBannerAnimation(ScaleInOutTransformer.class)//翻转动画
                                    .start();
                            banner.updateBannerStyle(BannerConfig.NUM_INDICATOR);
                            lable.removeAllViews();
                            lable.setGravity(Gravity.TOP);
                            for (int i = 0;i< bean.getEscortLabels().size();i++){
                                TextView tv = new TextView(getApplicationContext());
                                tv.setText(bean.getEscortLabels().get(i).getEscort_label_name());
                                tv.setTextSize(10);
                                if (i == 0) {
                                    tv.setBackgroundResource(R.drawable.text_bg_yellow);
                                    tv.setTextColor(getResources().getColor(R.color.white));
                                } else if (i == 1) {
                                    tv.setBackgroundResource(R.drawable.text_bg_ori);
                                    tv.setTextColor(getResources().getColor(R.color.white));
                                } else {
                                    tv.setBackgroundResource(R.drawable.text_bg);
                                    tv.setTextColor(getResources().getColor(R.color.white));
                                }
                                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(12, 12, 12, 12);
                                lable.addView(tv, params);
                            }
                            if (bean.getBrowse_times() < 10){
                                sum.setText("浏览10次");
                            }else{
                                sum.setText("浏览"+bean.getBrowse_times()+"次");
                            }
                            price.setText("￥"+(int)bean.getMin_consumption()+"~￥"+(int)bean.getMax_consumption());
                            maxPeople.setText("限"+bean.getRequirement_people_number()+"人以下");
                            list = bean.getPlaces();
                            url = bean.getEscort_details_url();
                            show(0);
                            break;
                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage()+"---");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.collection:
                if (recordId != 0){
                    switch (isCollection){
                        case 0:
                            ToastXutil.show("收藏失败，请稍后再试");
                            break;
                        case 1:
                            YueKaHttp.collectionEscortRecord(recordId, new CallBack() {
                                @Override
                                public void onResponse(Object response) {
                                    if ((int) response == 0){
                                        ToastXutil.show("取消收藏");
                                        collection.setBackgroundResource(R.mipmap.nocollection);
                                        isCollection = 2;
                                    }
                                }

                                @Override
                                public void onErrorMsg(String errorMsg) {

                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });
                            break;
                        case 2:
                            YueKaHttp.collectionEscortRecord(recordId, new CallBack() {
                                @Override
                                public void onResponse(Object response) {
                                    if ((int) response == 0){
                                        ToastXutil.show("收藏成功");
                                        collection.setBackgroundResource(R.mipmap.collection);
                                        isCollection = 1;
                                    }
                                }

                                @Override
                                public void onErrorMsg(String errorMsg) {

                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });
                            break;
                    }
                }else{
                    ToastXutil.show("获取数据失败");
                }
                break;
            case R.id.title01:
                show(0);
                break;
            case R.id.title02:
                show(1);
                break;
            case R.id.title03:
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                    RongIM.getInstance().startPrivateChat(this, chatId+"",titleName);
                }
                break;
            case R.id.title04:
                BuyYuaKaPopupWindow window = new BuyYuaKaPopupWindow(this,getWindow().getDecorView(),StringData,bean);
                break;
        }
    }
    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (index) {
            case 0:
                if (yueDetailsFragment == null) {
                    yueDetailsFragment = YueDetailsFragment.newInstance(list,url);
                    fragmentTransaction.add(R.id.container, yueDetailsFragment);
                }
                fragmentTransaction.show(yueDetailsFragment);
                title01.setTextColor(getResources().getColor(R.color.black));
                title01.setBackgroundResource(R.color.white);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.ori);

                break;
            case 1:
                if (courseEscortFragment == null) {
                    courseEscortFragment = new CourseEscortFragment();
                    fragmentTransaction.add(R.id.container, courseEscortFragment);
                }
                fragmentTransaction.show(courseEscortFragment);
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.ori);

                title02.setTextColor(getResources().getColor(R.color.black));
                title02.setBackgroundResource(R.color.white);
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(yueDetailsFragment);
                break;
            case 1:
                fragmentTransaction.hide(courseEscortFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }

}

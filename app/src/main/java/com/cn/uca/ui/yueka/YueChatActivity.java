package com.cn.uca.ui.yueka;

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
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.bean.yueka.YueKaDetailsBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.util.BaseBackActivity;
import com.cn.uca.ui.fragment.yueka.CourseEscortFragment;
import com.cn.uca.ui.fragment.yueka.YueDetailsFragment;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.RatingStarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class YueChatActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView stateTitle,title01,title02;
    private int currentIndex = -1;
    private YueDetailsFragment yueDetailsFragment;
    private CourseEscortFragment courseEscortFragment;
    private FragmentTransaction fragmentTransaction;
    private int recordId;
    private TextView back,title,collection,name,age,sex,sum,price,maxPeople;
    private CircleImageView pic;
    private RatingStarView start;
    private ArrayList<PlacesBean> list;
    private String url;
    private FluidLayout lable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_yue_chat);

        initView();
        getInfo();
        getEscortRecordInfo();
    }

    private void initView() {
        stateTitle  =(TextView)findViewById(R.id.stateTitle);
        stateTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.getStatusHeight(this)));
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        back = (TextView)findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        collection = (TextView)findViewById(R.id.collection);
        name = (TextView) findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        sex = (TextView) findViewById(R.id.sex);
        lable = (FluidLayout)findViewById(R.id.lable);
        sum = (TextView)findViewById(R.id.sum);
        price = (TextView) findViewById(R.id.price);
        maxPeople = (TextView)findViewById(R.id.maxPeople);
        pic = (CircleImageView)findViewById(R.id.pic);
        start = (RatingStarView)findViewById(R.id.start);

        back.setOnClickListener(this);
        collection.setOnClickListener(this);

        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            recordId = intent.getIntExtra("id",0);
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
                            if (bean.isCollection()){
                                collection.setBackgroundResource(R.mipmap.collection_white);
                            }else{
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
                            price.setText("￥"+(int)bean.getMin_consumption()+"--"+(int)bean.getMax_consumption());
                            maxPeople.setText(bean.getRequirement_people_number()+"人以下");
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.collection:
//                if (true){
//                    collection.setBackgroundResource(R.mipmap.collection_white);//收藏
//                }else{
//                    collection.setBackgroundResource(R.mipmap.nocollection);//取消收藏
//                }
                break;
            case R.id.title01:
                show(0);
                break;
            case R.id.title02:
                show(1);
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

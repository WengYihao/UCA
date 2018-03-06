package com.cn.uca.ui.view.home.yusheng;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.fragment.home.yusheng.YuShengDayFragment;
import com.cn.uca.ui.fragment.home.yusheng.YuShengMarkFragment;
import com.cn.uca.ui.fragment.home.yusheng.YuShengMonthFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YuShengDetailsActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,share,day,month,mark;
    private YuShengDayFragment dayFragment;
    private YuShengMonthFragment monthFragment;
    private YuShengMarkFragment markFragment;
    private int lastIndex = 0;//声明蓝线滑动的距离
    private ViewPager mPager;
    private List<Fragment> fragmentList;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yu_sheng_details);

        initView();
        initFragment();
    }



    private void initView() {
        layout = (RelativeLayout)findViewById(R.id.layout);
        back = (TextView)findViewById(R.id.back);
        share = (TextView)findViewById(R.id.share);
        day = (TextView)findViewById(R.id.day);
        month = (TextView)findViewById(R.id.month);
        mark = (TextView)findViewById(R.id.mark);

        mPager = (ViewPager) findViewById(R.id.container);
        mPager.setOffscreenPageLimit(3);//viewpager缓存的界面数
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        day.setOnClickListener(this);
        month.setOnClickListener(this);
        mark.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        day.setOnClickListener(new MyOnClickListener(0));
        month.setOnClickListener(new MyOnClickListener(1));
        mark.setOnClickListener(new MyOnClickListener(2));

    }
    private void initFragment() {
        dayFragment = new YuShengDayFragment();
        monthFragment = new YuShengMonthFragment();
        markFragment = new YuShengMarkFragment();
        fragmentList.add(dayFragment);
        fragmentList.add(monthFragment);
        fragmentList.add(markFragment);

        mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(onPageChangeListener);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.share:
                getShare();
                break;
        }
    }
    private void getShare(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("shareType","YS");
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.getShare(account_token, time_stamp, sign, "YS",0, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            String share_title = jsonObject.getJSONObject("data").getString("share_title");
                            String web_url = jsonObject.getJSONObject("data").getString("web_url");
                            WeChatManager.instance().sendWebPageToWX(YuShengDetailsActivity.this,true,web_url,R.mipmap.logo,share_title,"");
                            break;
                        default:
                            ToastXutil.show("分享失败");
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
    public class MyOnClickListener implements View.OnClickListener{
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View v) {
            //通过ViewPager的方法setCurrentItem(positon), c此时ViewPager会切换到position所对应item
            mPager.setCurrentItem(index);
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    if (lastIndex == 1) {
                        month.setTextColor(getResources().getColor(R.color.white));
                        month.setBackgroundResource(0);
                    } else if (lastIndex == 2) {
                        mark.setTextColor(getResources().getColor(R.color.white));
                        mark.setBackgroundResource(0);
                    }
                    day.setTextColor(getResources().getColor(R.color.ori));
                    day.setBackgroundResource(R.drawable.twenty_circular_white_background);
                    break;
                case 1:
                    if (lastIndex == 0) {
                        day.setTextColor(getResources().getColor(R.color.white));
                        day.setBackgroundResource(0);
                    } else if (lastIndex == 2) {
                        mark.setTextColor(getResources().getColor(R.color.white));
                        mark.setBackgroundResource(0);
                    }
                    month.setTextColor(getResources().getColor(R.color.ori));
                    month.setBackgroundResource(R.drawable.twenty_circular_white_background);
                    break;
                case 2:
                    if (lastIndex == 0) {
                        day.setTextColor(getResources().getColor(R.color.white));
                        day.setBackgroundResource(0);
                    } else if (lastIndex == 1) {
                        month.setTextColor(getResources().getColor(R.color.white));
                        month.setBackgroundResource(0);
                    }
                    mark.setTextColor(getResources().getColor(R.color.ori));
                    mark.setBackgroundResource(R.drawable.twenty_circular_white_background);
                    break;
            }
            lastIndex = position;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

}

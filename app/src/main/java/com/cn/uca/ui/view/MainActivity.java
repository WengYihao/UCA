package com.cn.uca.ui.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.bean.MessageNumBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.singleton.MessageNum;
import com.cn.uca.ui.fragment.home.HomeFragment;
import com.cn.uca.ui.fragment.user.UserFragment;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.AndroidWorkaround;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusBarUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.DragPointView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends FragmentActivity implements View.OnClickListener,IUnReadMessageObserver,DragPointView.OnDragListencer{

    private HomeFragment homeFragment;
    private UserFragment userFragment;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private View homeLayout;
    private View userLayout;


    private ImageView homeImage;
    private ImageView userImage;

    private TextView homeText;
    private TextView userText;
    private DragPointView mUnreadNumView;
    final Conversation.ConversationType[] conversationTypes = {
            Conversation.ConversationType.PRIVATE,
            Conversation.ConversationType.SYSTEM,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this);

        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        initView();
        initFragment();

        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);//未读消息
    }

    /**
     * 初始化控件
     */
    private void initView() {
        autoPermission();
        homeLayout = findViewById(R.id.home_layout);
        userLayout = findViewById(R.id.user_layout);

        homeImage = (ImageView) findViewById(R.id.home_image);
        userImage = (ImageView) findViewById(R.id.user_image);

        homeText = (TextView) findViewById(R.id.home_text);
        userText = (TextView) findViewById(R.id.user_text);

        mUnreadNumView = (DragPointView)findViewById(R.id.seal_num) ;
        mUnreadNumView.setOnClickListener(this);
        mUnreadNumView.setDragListencer(this);

        mPager = (ViewPager) findViewById(R.id.content);
        mPager.setOffscreenPageLimit(2);//viewpager缓存的界面数
        homeLayout.setOnClickListener(new MyOnClickListener(0));
        userLayout.setOnClickListener(new MyOnClickListener(1));

        fragmentList = new ArrayList<>();
        setTabSelection(0);//设置默认显示的界面
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(userFragment);

        mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragmentList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(onPageChangeListener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_layout:
                setTabSelection(0);
                mPager.setCurrentItem(0);
                break;
            case R.id.user_layout:
                setTabSelection(1);
                mPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onCountChanged(int i) {
        if (i == 0) {
            ShortcutBadger.removeCount(this); //1.1.4版本删除数字“徽章”的方法
            mUnreadNumView.setVisibility(View.GONE);
        } else if (i > 0 && i < 100) {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(String.valueOf(i));
            ShortcutBadger.applyCount(this, i); //1.1.4版本添加数字“徽章”的方法
        } else {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(R.string.no_read_message);
        }
    }

    @Override
    public void onDragOut() {
        mUnreadNumView.setVisibility(View.GONE);
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0) {
                    for (Conversation c : conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        }, conversationTypes);

    }

    private class MyOnClickListener implements View.OnClickListener{
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
            setTabSelection(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        switch (index) {
            case 0:
                // 当点击了主页tab时，改变控件的图片和文字颜色
                homeImage.setImageResource(R.mipmap.home_select);
                homeText.setTextColor(getResources().getColor(R.color.ori));
                break;
            case 1:
                userImage.setImageResource(R.mipmap.user_select);
                userText.setTextColor(getResources().getColor(R.color.ori));
                break;
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        homeImage.setImageResource(R.mipmap.home_normal);
        homeText.setTextColor(getResources().getColor(R.color.gray2));
        userImage.setImageResource(R.mipmap.user_normal);
        userText.setTextColor(getResources().getColor(R.color.gray2));
    }

    //自动权限
    private void autoPermission() {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Constant.WRITE_PERMISSIONS_REQUEST_CODE);
            }
            //相机
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.CAMERA_PERMISSIONS_REQUEST_CODE);
                }
            }
            //定位
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.WRITE_PERMISSIONS_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

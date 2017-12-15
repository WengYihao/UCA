package com.cn.uca.ui.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.ui.fragment.home.HomeFragment;
import com.cn.uca.ui.fragment.user.UserFragment;
import com.cn.uca.ui.fragment.yueka.YueKaFragment;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.AndroidWorkaround;
import com.cn.uca.util.StatusBarUtil;
import com.cn.uca.view.DragPointView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends FragmentActivity implements View.OnClickListener,IUnReadMessageObserver,DragPointView.OnDragListencer{

    private HomeFragment homeFragment;
    private YueKaFragment yueKaFragment;
    private UserFragment userFragment;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private View homeLayout;
    private View yuekaLayout;
    private View userLayout;


    private ImageView homeImage;
    private ImageView yuekaImage;
    private ImageView userImage;

    private TextView homeText;
    private TextView yuekaText;
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
        homeLayout = findViewById(R.id.home_layout);
        yuekaLayout = findViewById(R.id.yueka_layout);
        userLayout = findViewById(R.id.user_layout);

        homeImage = (ImageView) findViewById(R.id.home_image);
        yuekaImage = (ImageView)findViewById(R.id.yueka_image);
        userImage = (ImageView) findViewById(R.id.user_image);

        homeText = (TextView) findViewById(R.id.home_text);
        yuekaText = (TextView)findViewById(R.id.yueka_text);
        userText = (TextView) findViewById(R.id.user_text);

        mUnreadNumView = (DragPointView)findViewById(R.id.seal_num) ;
        mUnreadNumView.setOnClickListener(this);
        mUnreadNumView.setDragListencer(this);

        mPager = (ViewPager) findViewById(R.id.content);
        mPager .setOffscreenPageLimit(3);//viewpager缓存的界面数
        homeLayout.setOnClickListener(this);
        yuekaLayout.setOnClickListener(this);
        userLayout.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        setTabSelection(0);//设置默认显示的界面
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        homeFragment = new HomeFragment();
        yueKaFragment = new YueKaFragment();
        userFragment = new UserFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(yueKaFragment);
        fragmentList.add(userFragment);

        mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
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
            case R.id.yueka_layout:
                setTabSelection(1);
                mPager.setCurrentItem(1);
                break;
            case R.id.user_layout:
                setTabSelection(2);
                mPager.setCurrentItem(2);
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
            setTabSelection(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        switch (index) {
            case 0:
                // 当点击了主页tab时，改变控件的图片和文字颜色
                homeImage.setImageResource(R.mipmap.home_select);
                homeText.setTextColor(getResources().getColor(R.color.ori));
                break;
            case 1:
                // 当点击了语言设置tab时，改变控件的图片和文字颜色
                yuekaImage.setImageResource(R.mipmap.yueka_select);
                yuekaText.setTextColor(getResources().getColor(R.color.ori));
                break;
            case 2:
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
        yuekaImage.setImageResource(R.mipmap.yueka_normal);
        yuekaText.setTextColor(getResources().getColor(R.color.gray2));
        userImage.setImageResource(R.mipmap.user_normal);
        userText.setTextColor(getResources().getColor(R.color.gray2));
    }
}

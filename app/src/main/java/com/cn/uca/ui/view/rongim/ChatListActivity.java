package com.cn.uca.ui.view.rongim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.rongim.ConversationListAdapterEx;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.ToastXutil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;

public class ChatListActivity extends BaseBackActivity implements ViewPager.OnPageChangeListener{

    private TextView back,clear;
    public static ViewPager mViewPager;
    private List<Fragment> mFragment = new ArrayList<>();
    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatListActivity.this.finish();
            }
        });
        clear = (TextView)findViewById(R.id.clear);

        Fragment conversationList = initConversationList();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragment.add(conversationList);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(this);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().clearConversations(new RongIMClient.ResultCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        ToastXutil.show("清空成功");
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        ToastXutil.show("清空失败,稍后再试");
                    }
                },mConversationsTypes);
            }
        });

        initData();
    }
    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
//                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{
                    Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
//                    Conversation.ConversationType.SYSTEM,
                    Conversation.ConversationType.DISCUSSION
            };
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    protected void initData() {



        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();
    }
    /**
     * 得到 push 消息
     */
    private void getPushMessage() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                    RongIM.connect(SharePreferenceXutil.getRongToken(), new RongIMClient.ConnectCallback() {
                        @Override
                        public void onTokenIncorrect() {
                        }

                        @Override
                        public void onSuccess(String s) {
                            Log.i("123",s+"id");
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                }
            }
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {

            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");


            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {

                    if (conversation != null) {

                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
//                            startActivity(new Intent(MainActivity.this, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }
}

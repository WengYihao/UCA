package com.cn.uca.ui.view.rongim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.rongim.ConversationListAdapterEx;
import com.cn.uca.ui.fragment.rongim.ConversationFragmentEx;
import com.cn.uca.ui.view.LoginActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.AndroidWorkaround;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;

public class ConversationActivity extends BaseBackActivity {
    private TextView mName,back;
    //对方id
    private String mTargetId;
    //会话类型
    private Conversation.ConversationType mConversationType;
   //title
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mName = (TextView) findViewById(R.id.name);
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConversationActivity.this.finish();
            }
        });

        RongIM.getInstance().setMessageAttachedUserInfo(true);
        mTargetId = getIntent().getData().getQueryParameter("targetId");   // targetId:单聊即对方ID，群聊即群组ID
        title = getIntent().getData().getQueryParameter("title");    // 获取昵称
        mConversationType = Conversation.ConversationType.valueOf(getIntent().getData()
                .getLastPathSegment().toUpperCase(Locale.US));
        mName.setText(title);
//        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(mTargetId);
//        if (userInfo != null) {
//            Log.i("123","123");
//            mName.setText(userInfo.getName());
//        }else{
//            Log.i("123","456");
//        }
//        if (!TextUtils.isEmpty(mTargetId)) {
//            UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(mTargetId);
//            if (userInfo != null) {
//                mName.setText(userInfo.getName());
//            }
//        }
        isPushMessage(getIntent());
        ActivityCollector.pushActivity(this);
    }
    private void reconnect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
            }

            @Override
            public void onSuccess(String s) {
                enterFragment(mConversationType, mTargetId);

            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                enterFragment(mConversationType, mTargetId);
            }
        });

    }
    /**
     * 判断是否是 Push 消息，判断是否需要做 connect 操作
     */
    private void isPushMessage(Intent intent) {
        Log.i("123","999999999999999");
        if (intent == null || intent.getData() == null)
            return;
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {
            Log.i("123","11111111111111");
            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
                Log.i("123","222222222222");
                //只有收到系统消息和不落地 push 消息的时候，pushId 不为 null。而且这两种消息只能通过 server 来发送，客户端发送不了。
                isFromPush = true;
                enterActivity();
            } else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                Log.i("123","3333333333333333");
                if (intent.getData().getPath().contains("conversation/system")) {
                    Log.i("123","4444444444444");
                    Intent intent1 = new Intent(ConversationActivity.this, ChatListActivity.class);
                    intent1.putExtra("systemconversation", true);
                    startActivity(intent1);
                    ActivityCollector.popAllActivity();
                    return;
                }
                enterActivity();
            } else {
                Log.i("123","55555555555");
                if (intent.getData().getPath().contains("conversation/system")) {
                    Log.i("123","6666666666666");
                    Intent intent1 = new Intent(ConversationActivity.this, ChatListActivity.class);
                    intent1.putExtra("systemconversation", true);
                    startActivity(intent1);
                    ActivityCollector.popAllActivity();
                    return;
                }
                enterFragment(mConversationType, mTargetId);
                Log.i("123","8888888888888");
            }

        } else {
            Log.i("123","////////////");
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                Log.i("123","-------------------");
//                if (mDialog != null && !mDialog.isShowing()) {
//                    mDialog.show();
//                }
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enterActivity();
                    }
                }, 300);
            } else {
                Log.i("123","*********************");
                enterFragment(mConversationType, mTargetId);
            }
        }
    }

    private void enterActivity() {
        String token = SharePreferenceXutil.getRongToken();
        if (token.equals("")) {
            startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
            ActivityCollector.popAllActivity();
        } else {
            reconnect(token);
        }
    }
    /**
     * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         会话 Id
     */
    private ConversationFragmentEx fragment;
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        RongIM.getInstance().setMessageAttachedUserInfo(true);//设置消息体内携带用户信息
        fragment = new ConversationFragmentEx();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId)
                .appendQueryParameter("title",title).build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commitAllowingStateLoss();
    }
    private boolean isFromPush = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            if (fragment != null && !fragment.onBackPressed()) {
                if (isFromPush) {
                    isFromPush = false;
                    startActivity(new Intent(this, ChatListActivity.class));
                    ActivityCollector.popAllActivity();
                } else {
                    if (fragment.isLocationSharing()) {
                        fragment.showQuitLocationSharingDialog(this);
                        return true;
                    }
                    if (mConversationType.equals(Conversation.ConversationType.CHATROOM)
                            || mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                        ActivityCollector.popActivity(this);
                    } else {
                        ActivityCollector.popActivity(this);
                    }
                }
            }
        }
        return false;
    }

}
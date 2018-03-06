package com.cn.uca.ui.view.rongim;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.rongim.ConversationListAdapterEx;
import com.cn.uca.adapter.rongim.ReportTypeAdapter;
import com.cn.uca.bean.rongim.ReportTypeBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.fragment.rongim.ConversationFragmentEx;
import com.cn.uca.ui.view.LoginActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.AndroidBug5497Workaround;
import com.cn.uca.util.AndroidWorkaround;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusBarUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utilities.PromptPopupDialog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;

public class ConversationActivity extends BaseBackActivity {
    private TextView mName,back,more;
    private TextView blacklist,chat_setting,report;//举报与投诉
    //对方id
    private String mTargetId;
    //会话类型
    private Conversation.ConversationType mConversationType;
   //title
    private String title;
    private View inflate;
    private ListView listView;
    private TextView btn_cancel;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        AndroidBug5497Workaround.assistActivity(this);
        more = (TextView)findViewById(R.id.more);
        mName = (TextView) findViewById(R.id.name);
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConversationActivity.this.finish();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().getBlacklistStatus(mTargetId, new RongIMClient.ResultCallback<RongIMClient.BlacklistStatus>() {
                    @Override
                    public void onSuccess(RongIMClient.BlacklistStatus blacklistStatus) {
                        show(blacklistStatus);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {

                    }
                });
            }
        });
        mTargetId = getIntent().getData().getQueryParameter("targetId");   // targetId:单聊即对方ID，群聊即群组ID
        title = getIntent().getData().getQueryParameter("title");    // 获取昵称
        mConversationType = Conversation.ConversationType.valueOf(getIntent().getData()
                .getLastPathSegment().toUpperCase(Locale.US));
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(mTargetId);
        if (userInfo != null) {
            mName.setText(userInfo.getName());
        }else{
            mName.setText(title);
        }
        MyApplication.getInfo(mTargetId);
        isPushMessage(getIntent());
        ActivityCollector.pushActivity(this);
    }
    private void show(final RongIMClient.BlacklistStatus blacklistStatus){
        View show = LayoutInflater.from(this).inflate(R.layout.chat_more_dialog, null);
        blacklist = (TextView)show.findViewById(R.id.blacklist);//加入黑名单
        chat_setting = (TextView)show.findViewById(R.id.chat_setting);//会话设置
        report = (TextView)show.findViewById(R.id.report);//举报与投诉
        if (blacklistStatus == RongIMClient.BlacklistStatus.IN_BLACK_LIST) {
            blacklist.setText("移除黑名单");
        } else {
            blacklist.setText("加入黑名单");
        }
        final PopupWindow popupWindow = new PopupWindow(show, MyApplication.width/4,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT|Gravity.TOP,20,50);
        blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blacklistStatus == RongIMClient.BlacklistStatus.IN_BLACK_LIST) {
                   RongIM.getInstance().removeFromBlacklist(mTargetId, new RongIMClient.OperationCallback() {
                       @Override
                       public void onSuccess() {
                           ToastXutil.show("移除黑名单成功");
                       }
                       @Override
                       public void onError(RongIMClient.ErrorCode errorCode) {
                           ToastXutil.show("移除黑名单失败");
                       }
                   });
                } else {
                    PromptPopupDialog.newInstance(ConversationActivity.this, "加入黑名单",
                            "你将不再收到对方的消息").setPromptButtonClickedListener(new PromptPopupDialog.OnPromptButtonClickedListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            RongIM.getInstance().addToBlacklist(mTargetId, new RongIMClient.OperationCallback() {
                                @Override
                                public void onSuccess() {
                                    ToastXutil.show("加入黑名单成功");
                                }
                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    ToastXutil.show("加入黑名单失败");
                                }
                            });
                        }
                    }).show();
                }
                popupWindow.dismiss();
            }
        });
        chat_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConversationActivity.this,ChatSettingActivity.class));
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReportType();
                popupWindow.dismiss();
            }
        });

    }

    private void getReportType(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.getReportType(sign, time_stamp, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<ReportTypeBean> bean = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<ReportTypeBean>>() {
                            }.getType());
                            show(bean);
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
    private void show(List<ReportTypeBean> list){
        dialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.chat_report_dialog, null);
        listView = (ListView)inflate.findViewById(R.id.listView);
        ReportTypeAdapter adapter = new ReportTypeAdapter(list,this);
        listView.setAdapter(adapter);
        SetListView.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        btn_cancel = (TextView)inflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialogWindow.setAttributes(params);
        StatusMargin.setFrameLayoutBottom(this,inflate,0);
        dialog.show();//显示对话框
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
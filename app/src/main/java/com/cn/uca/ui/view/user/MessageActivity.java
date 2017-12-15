package com.cn.uca.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.rongim.ChatListActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.view.DragPointView;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MessageActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,message_text,message_text2;
    private DragPointView system_num,chat_num;
    private LinearLayout system_message_layout,chat_message_loyout;
    final Conversation.ConversationType[] privaetTypes = {
            Conversation.ConversationType.PRIVATE
    };
    final Conversation.ConversationType[] systemTypes = {
            Conversation.ConversationType.SYSTEM
    };
    private int systemNum,chatNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initView();

    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        message_text = (TextView)findViewById(R.id.message_text);
        message_text2 = (TextView)findViewById(R.id.message_text2);

        system_num = (DragPointView)findViewById(R.id.system_num);
        chat_num = (DragPointView)findViewById(R.id.chat_num);

        system_message_layout = (LinearLayout)findViewById(R.id.system_message_layout);
        chat_message_loyout = (LinearLayout)findViewById(R.id.chat_message_layout);

        system_message_layout.setOnClickListener(this);
        chat_message_loyout.setOnClickListener(this);

        back.setOnClickListener(this);
        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                chatNum = i;
                if (i == 0) {
                    chat_num.setVisibility(View.GONE);
                    message_text2.setText("暂无消息");

                } else if (i > 0 && i < 100) {
                    chat_num.setVisibility(View.VISIBLE);
                    chat_num.setText(String.valueOf(i));
                    message_text2.setText(i+"条新消息");
                } else {
                    chat_num.setVisibility(View.VISIBLE);
                    chat_num.setText(R.string.no_read_message);
                }
            }
        }, privaetTypes);//未读会话消息

        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                systemNum = i;
                if (i == 0) {
                    system_num.setVisibility(View.GONE);
                    message_text.setText("暂无消息");
                } else if (i > 0 && i < 100) {
                    system_num.setVisibility(View.VISIBLE);
                    system_num.setText(String.valueOf(i));
                    message_text.setText(i+"条新消息");
                } else {
                    system_num.setVisibility(View.VISIBLE);
                    system_num.setText(R.string.no_read_message);
                }
            }
        }, systemTypes);//未读系统消息

        chat_num.setDragListencer(new DragPointView.OnDragListencer() {
            @Override
            public void onDragOut() {
                chat_num.setVisibility(View.GONE);
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
                }, privaetTypes);
            }
        });
        system_num.setDragListencer(new DragPointView.OnDragListencer() {
            @Override
            public void onDragOut() {
                system_num.setVisibility(View.GONE);
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
                }, systemTypes);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.system_message_layout:
                if (systemNum > 0){

                }
                break;
            case R.id.chat_message_layout:
                if (chatNum > 0){
                    startActivity(new Intent(MessageActivity.this, ChatListActivity.class));
                }
                break;
        }
    }
}

package com.cn.uca.ui.fragment.rongim;

import android.content.Intent;
import android.util.Log;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by asus on 2017/11/29.
 * 加载会话页面
 */

public class ConversationFragmentEx extends ConversationFragment {

    @Override
    public void onReadReceiptStateClick(io.rong.imlib.model.Message message) {
        Log.i("123","--------------------------------");
//        if (message.getConversationType() == Conversation.ConversationType.GROUP) { //目前只适配了群组会话
//            Intent intent = new Intent(getActivity(), ReadReceiptDetailActivity.class);
//            intent.putExtra("message", message);
//            getActivity().startActivity(intent);
//        }
    }

    public void onWarningDialog(String msg) {
        String typeStr = getUri().getLastPathSegment();
        if (!typeStr.equals("chatroom")) {
            super.onWarningDialog(msg);
        }
    }
}


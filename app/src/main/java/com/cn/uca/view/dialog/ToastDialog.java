package com.cn.uca.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.cn.uca.ui.LoginActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.SharePreferenceXutil;

/**
 * Created by asus on 2017/9/5.
 */

public class ToastDialog {
    /**
     * 退出登录
     *
     * @param context
     */
    public static void exit(final Context context) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("确定退出吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置你的操作事项
                SharePreferenceXutil.setExit(true);
                context.startActivity(new Intent(context, LoginActivity.class));
                ActivityCollector.finishAll();
            }
        });
        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}

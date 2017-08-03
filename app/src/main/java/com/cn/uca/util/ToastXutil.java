package com.cn.uca.util;

import com.cn.uca.config.MyApplication;
import android.widget.Toast;

public class ToastXutil {
	public static void show(String msg) {
		Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
	}
	public static void showLong(String msg){
		Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG).show();
	}
}

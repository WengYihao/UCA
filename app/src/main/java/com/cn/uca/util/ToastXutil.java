package com.cn.uca.util;

import com.cn.uca.config.MyApplication;
import android.widget.Toast;

public class ToastXutil {
	public static void show(String msg) {
		Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
	}
	public static void show(int resId){
		Toast.makeText(MyApplication.getContext(), resId, Toast.LENGTH_SHORT).show();
	}
	public static void showLong(String msg){
		Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG).show();
	}
	public static void showLong(int resId){
		Toast.makeText(MyApplication.getContext(), resId, Toast.LENGTH_SHORT).show();
	}
}

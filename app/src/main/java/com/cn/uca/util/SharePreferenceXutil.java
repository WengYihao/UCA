package com.cn.uca.util;

import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地保存工具类
 */
public class SharePreferenceXutil {
	private static SharedPreferences sp;

	private static SharedPreferences getSp() {
		if (sp == null) {
			sp = MyApplication.getContext().getSharedPreferences(Constant.userIdKey, Context.MODE_PRIVATE);
		}
		return sp;
	}
}

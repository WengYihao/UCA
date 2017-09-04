package com.cn.uca.impl;

import com.android.volley.VolleyError;

public interface CallBack {
	void onResponse(Object response);   //成功返回
	void onErrorMsg(String errorMsg);   //错误提示
	void onError(VolleyError error);    //出错返回
}
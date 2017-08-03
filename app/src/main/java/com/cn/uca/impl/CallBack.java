package com.cn.uca.impl;

import com.android.volley.VolleyError;

public interface CallBack {
	void onResponse(String response);   //成功返回
	void onError(VolleyError error);    //出错返回
}
package com.cn.uca.config.base;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.MyConfig;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.L;
import com.cn.uca.util.ToastXutil;

public class BaseServer {
	/**
	 * 服务器请求父类方法
	 * 
	 *            ：接口名称
	 * @param map
	 *            ：post数据
	 * @param callback
	 *            ：回调接口
	 */
	public void post(String url, Map<String, Object> map, final CallBack callback) {
		RequestQueue quene = MyApplication.getHttpQueue();
		JSONObject params = new JSONObject(map);
		L.i("post url", url + "--" + params);
		JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, params,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						L.i("volley post response", response);
						if (response != null) {
							callback.onResponse(response.toString());
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						L.e("volley post", error.toString());
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		quene.add(request);

	}

	public void post2(String url, Map<String, Object> map, final CallBack callback) {
		RequestQueue quene = MyApplication.getHttpQueue();
		JSONObject params = new JSONObject(map);
		String paramsStr = params.toString();
		L.i(url, paramsStr);
		StringPostRequest request = new StringPostRequest(Method.POST, url, paramsStr, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				L.i("volley post2 response", response);
				if (response != null) {
					callback.onResponse(response);
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				L.e("volley post2 error", error.toString());
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		quene.add(request);
	}

	public static void post3(String url, final Map<String, String> map, final CallBack callBack) {
		RequestQueue quene = MyApplication.getHttpQueue();
		StringRequest stringRequest = new StringRequest(Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response != null) {
					Log.i("post3",response.toString());
					try {
						JSONObject jsonObject = new JSONObject(response);
						int code = jsonObject.getInt("code");
						if (code == 0){
							callBack.onResponse(jsonObject.getJSONObject("data").toString());
						}else{
							callBack.onErrorMsg(jsonObject.getString("msg").toString());
						}
					}catch (Exception e){
						Log.i("post3 erroe",e.getMessage()+"封装报错");
					}

				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null) {
					callBack.onError(error);
				}
				Log.i("post3 erroe", error.getMessage() + "-报错" );
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Log.i("post3 map", map.toString() + "传递参数");
				return map;
			}
		};
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		quene.add(stringRequest);
	}
	public static void post4(String url, final Map<String, String> map, final CallBack callBack) {
		RequestQueue quene = MyApplication.getHttpQueue();
		StringRequest stringRequest = new StringRequest(Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response != null) {
					Log.i("post4",response.toString()+"*************");
					try {
						JSONObject jsonObject = new JSONObject(response);
						int code = jsonObject.getInt("code");
						if (code == 0){
							callBack.onResponse(code);
						}else{
							callBack.onResponse(code);
							callBack.onErrorMsg(jsonObject.getString("msg").toString());
							ToastXutil.show(jsonObject.getString("msg").toString());
						}
					}catch (Exception e){
						Log.i("post4",e.getMessage()+"封装报错");
					}

				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null) {
					callBack.onError(error);
				}
				Log.i("post4", error.getMessage() + "-报错");
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Log.i("post4", map.toString() + "传递参数");
				return map;
			}
		};
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		quene.add(stringRequest);
	}
	public static void post5(String url, final Map<String, String> map, final CallBack callBack) {
		RequestQueue quene = MyApplication.getHttpQueue();
		StringRequest stringRequest = new StringRequest(Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response != null) {
					Log.e("post5",response.toString());
					try{
						JSONObject jsonObject = new JSONObject(response);
						int code = jsonObject.getInt("code");
						if (code == 0){
							callBack.onResponse(response);
						}else{
							callBack.onResponse(response);
							callBack.onErrorMsg(jsonObject.getString("msg").toString());
							ToastXutil.show(jsonObject.getString("msg").toString());
						}
					}catch (Exception e){

					}
				}else{
					callBack.onErrorMsg("访问出错");
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null) {
					callBack.onError(error);
				}
				Log.i("post5", error.getMessage() + "-报错");
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Log.i("post5", map.toString() + "传递参数");
				return map;
			}
		};
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		quene.add(stringRequest);
	}
	/**
	 * get请求
	 * 
	 * @param url
	 * @param map
	 * @param callback
	 */
	public static void get(String url, Map<String, Object> map, final CallBack callback) {
		String params = "";
		if (map != null) {
			Set<?> entries = map.entrySet();
			if (entries != null) {
				Iterator<?> iterator = entries.iterator();
				while (iterator.hasNext()) {
					Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					params += "&" + key.toString() + "=" + value.toString();
				}
			}
			if (params.length() > 1) {
				params = params.substring(1, params.length());
				url += "?" + params;
			}
		}
		RequestQueue quene = MyApplication.getHttpQueue();
		Log.e("quene", url);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				try{
					Log.e("volley get response", response);
					callback.onResponse(response);
				}catch (Exception e){
					Log.e("volley getlll", e.toString());
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				L.e("volley get", error.getMessage()+"-"+error.getCause()+"-"+error.getLocalizedMessage()+"-"+error.toString());
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		quene.add(request);
	}
}

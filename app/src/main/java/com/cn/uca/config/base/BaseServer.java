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
//		String url = MyConfig.url + port;
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
//		String url = MyConfig.url + port;
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

	public void post3(String url, final Map<String, String> map, final CallBack callBack) {
		RequestQueue quene = MyApplication.getHttpQueue();
		StringRequest stringRequest = new StringRequest(Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response != null) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						int code = jsonObject.getInt("code");
						if (code == 0){
							callBack.onResponse(jsonObject.getJSONObject("data").toString());
						}else{
							callBack.onErrorMsg(jsonObject.getString("msg").toString());
						}
					}catch (Exception e){
						Log.i("456",e.getMessage()+"封装报错");
					}

				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null) {
					callBack.onError(error);
				}
				Log.i("456", error.getMessage() + "-报错" + error.getMessage());
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Log.i("456", map.toString() + "传递参数");
				return map;
			}
		};
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		quene.add(stringRequest);
	}

	public void post4(String url, final Map<String, String> map, final CallBack callBack) {
		RequestQueue quene = MyApplication.getHttpQueue();
		StringRequest stringRequest = new StringRequest(Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.i("123",response+"*****************");
				if (response != null) {
					try {
						callBack.onResponse(response);
//						JSONObject jsonObject = new JSONObject(response);
//						Log.i("123",response.toString()+"9090909");
//						int code = jsonObject.getInt("code");
//						if (code == 0){
//							callBack.onResponse(code);
//						}else{
//							callBack.onErrorMsg(jsonObject.getString("msg").toString());
//						}
					}catch (Exception e){
						Log.i("456",e.getMessage()+"封装报错");
					}

				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null) {
					callBack.onError(error);
				}
				Log.i("456", error.getMessage() + "-报错");
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Log.i("456", map.toString() + "传递参数");
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
	public void get(String url, Map<String, Object> map, final CallBack callback) {
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
				try {
					Log.e("volley get response", response);
					if (response != null) {
						callback.onResponse(response);
					}
				} catch (Exception e) {
					Log.e("volley getlll", e.toString());
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				L.e("volley get", error.toString());
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		quene.add(request);
	}

	/**
	 * 上传文件
	 * @param url
	 * @param callBack
	 */
	public void sendFiles(String url,Map<String,File> files, final Map<String, String> params, final CallBack callBack) {
		RequestQueue quene = MyApplication.getHttpQueue();
		MultipartRequest request = new MultipartRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					if (response != null) {
						callBack.onResponse(response);
					}
				} catch (Exception e) {
				}

			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.i("456", volleyError.getMessage() + "-报错");
			}
		}, files, params);
		quene.add(request);
	}
}

package com.cn.uca.config.base;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
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
	 * @param port
	 *            ：接口名称
	 * @param map
	 *            ：post数据
	 * @param callback
	 *            ：回调接口
	 */
	public void post(String port, Map<String, Object> map, final CallBack callback) {
		String url = MyConfig.url + port;
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
		quene.add(request);

	}

	public void post2(String port, Map<String, Object> map, final CallBack callback) {
		String url = MyConfig.url + port;
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
		quene.add(request);
	}

	public void post3(String port, final Map<String, String> map, final CallBack callBack) {
		String url = MyConfig.url + port;
		RequestQueue quene = MyApplication.getHttpQueue();
		StringRequest stringRequest = new StringRequest(Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response != null) {
					callBack.onResponse(response);
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
		quene.add(stringRequest);
	}

	/**
	 * get请求
	 * 
	 * @param port
	 * @param map
	 * @param callback
	 */
	public void get(String port, Map<String, Object> map, final CallBack callback) {
		String url = MyConfig.url + port;
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
		quene.add(request);
	}

	/**
	 * 上传文件
	 * @param url
	 * @param files
	 * @param params
	 * @param callBack
	 */
//	public void sendFiles(String url, List<File> files, final Map<String, String> params, final CallBack callBack) {
//		RequestQueue quene = MyApplication.getHttpQueue();
//		MultipartRequest request = new MultipartRequest(url, new Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				try {
//					if (response != null) {
//						callBack.onResponse(response);
//					}
//				} catch (Exception e) {
//				}
//
//			}
//		}, new ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError volleyError) {
//				Log.i("456", volleyError.getMessage() + "-报错");
//			}
//		}, files, params);
//		quene.add(request);
//	}
	
	public void Test(String url, final Map<String, String> map, final CallBack callBack){
		RequestQueue quene = MyApplication.getHttpQueue();
		StringRequest stringRequest = new StringRequest(Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response != null) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						int resultnumber = jsonObject.getInt("resultnumber");
						if (resultnumber == 200) {
							String result = jsonObject.getString("result");
							callBack.onResponse(result);
						}
					}catch (Exception e){
						Log.i(Constant.TAG,e.getMessage());
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
		quene.add(stringRequest);
	}
	public void delete(String url, final Map<String, String> map, final CallBack callBack){
		RequestQueue quene = MyApplication.getHttpQueue();
		StringRequest stringRequest = new StringRequest(Method.DELETE, url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (response != null) {
					callBack.onResponse(response);
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
		quene.add(stringRequest);
	}
}

package com.cn.uca.config;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cn.uca.bean.wechat.WeChatAccessToken;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

	private static MyApplication instance;
	public static RequestQueue queue;
	private static Context mContext;
	public static int width;
	public static int height;
	public static QueryHttp server;
	public static WeChatAccessToken accessToken;

	public static Context getContext() {
		return mContext;
	}

	public static QueryHttp getServer(){
		return server;
	}

	public void setAccessToken(WeChatAccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public static WeChatAccessToken getAccessToken() {
		return accessToken;
	}

	public static String getToken(){
		if (SharePreferenceXutil.getAccountToken() != null){
			return SharePreferenceXutil.getAccountToken();
		}else{
			return "";
		}
	}

	// 应用程序的入口
	@Override
	public void onCreate() {
		super.onCreate();
		getScreen(this);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCacheSize(50 * 1024 * 1024).defaultDisplayImageOptions(defaultOptions)
				.diskCacheSize(200 * 1024 * 1024)//
				.diskCacheFileCount(150) // 缓存图片数量
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
		// 上下文
		mContext = getApplicationContext();
		server = new QueryHttp();

		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
//        Log.i("999", JPushInterface.getRegistrationID(getApplicationContext())+"****************");
//        SharePreferenceXutil.saveChannelId(JPushInterface.getRegistrationID(getApplicationContext()));
		queue = Volley.newRequestQueue(getApplicationContext());

		regToWeChat();
		Fresco.initialize(this);
	}


	public static RequestQueue getHttpQueue() {
		return queue;
	}

	// 单例模式中获取唯一的ExitApplication实例
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	public void getScreen(Context aty) {
		DisplayMetrics dm = aty.getResources().getDisplayMetrics();
		height = dm.heightPixels;
		width = dm.widthPixels;
	}
	/**
	 * 将应用的注册到微信
	 */
	private void regToWeChat() {
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		IWXAPI wxApi = WXAPIFactory.createWXAPI(this, Constant.WeChat_APP_ID, true);
		// 将应用的注册到微信
		wxApi.registerApp(Constant.WeChat_APP_ID);
		WeChatManager.instance().setApi(wxApi);
	}

	/**
	 * 发送手机验证码
	 * @param code
	 */
	public static  void sendCode(String code){
		if (code.equals("") || code == null){
			ToastXutil.show("手机号不能为空");
		}else{
			if (StringXutil.isPhoneNumberValid(code) && code.length() == 11){
				getServer().sendCode(code, new CallBack() {
					@Override
					public void onResponse(Object response) {
						Log.i("123",response.toString());
					}

					@Override
					public void onErrorMsg(String errorMsg) {
						ToastXutil.show(errorMsg);
					}

					@Override
					public void onError(VolleyError error) {

					}
				});

			}else{
				ToastXutil.show("手机号不合法");
			}
		}

	}
}

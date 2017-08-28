package com.cn.uca.config;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.util.SystemUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
	private static MyApplication instance;
	public static RequestQueue queue;
	private static Context mContext;
	public static int width;
	public static int height;
	public static QueryHttp server;

	public static Context getContext() {
		return mContext;
	}

	public static QueryHttp getServer(){
		return server;
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
}

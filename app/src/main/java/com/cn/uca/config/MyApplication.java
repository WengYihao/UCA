package com.cn.uca.config;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	private static MyApplication instance;
	public static RequestQueue queue;
	private static Context mContext;
	public static Context getContext() {
		return mContext;
	}

	// 应用程序的入口
	@Override
	public void onCreate() {
		super.onCreate();
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
//		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush
//        Log.i("999", JPushInterface.getRegistrationID(getApplicationContext())+"****************");
//        SharePreferenceXutil.saveChannelId(JPushInterface.getRegistrationID(getApplicationContext()));
		queue = Volley.newRequestQueue(getApplicationContext());
//		初始化短信验证
//		SMSSDK.initSDK(getContext(), Constant.APPKEY, Constant.AppSecret);
//		regist();//注册短信监听
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
	/**
	 * 注册短信验证的监听
	 */
//	public static void regist() {
//		 SMSSDK.registerEventHandler(aa());
//	}
//	/**
//	 * 注销短信回调
//	 */
//	public static void unregist(){
//		SMSSDK.unregisterEventHandler(aa());
//	}
//
//	public static EventHandler aa(){
//		EventHandler eh = new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//            	if (result == SMSSDK.RESULT_ERROR) {
//            		new SDKThread().start();
//				}
//            }
//
//        };
//		return eh;
//	}
//
//	static Handler handler1 = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 1:
//				ToastXutil.show("当前手机号发送短信的数量超过限额");
//				break;
//			}
//		};
//	};
//	public  static class SDKThread extends Thread{
//		@Override
//		public void run() {
//			try {
//				handler1.sendEmptyMessage(1);
//			} catch (Exception e) {
//			}
//		}
//	};
}

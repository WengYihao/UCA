package com.cn.uca.config;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cn.uca.R;
import com.cn.uca.bean.wechat.WeChatAccessToken;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.util.AndroidWorkaround;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

	private static MyApplication instance;
	public static RequestQueue queue;
	private static Context mContext;
	public static int width;
	public static int height;
	public static WeChatAccessToken accessToken;
	// 创建一个以当前时间为名称的文件
	public static File tempFile = new File(Environment.getExternalStorageDirectory(), SystemUtil.getPhotoFileName());

	public static Context getContext() {
		return mContext;
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
		SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
			@Override
			public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
				layout.setPrimaryColorsId(R.color.white, R.color.black3);//全局设置主题颜色
				return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
			}
		});
		SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
			@Override
			public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
				layout.setPrimaryColorsId(R.color.white, R.color.gray2);//全局设置主题颜色
				return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
			}
		});
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

		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
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
				QueryHttp.sendCode(code, new CallBack() {
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

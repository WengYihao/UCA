package com.cn.uca.config;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cn.uca.R;
import com.cn.uca.bean.rongim.RongInfoBean;
import com.cn.uca.bean.wechat.WeChatAccessToken;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.ui.view.rongim.ChatListActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;
import io.rong.push.common.RongException;

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
		RongPushClient.registerMiPush(this, "2882303761517653802", "5761765373802");
		RongIM.init(this);
		try {
			Log.i("hao","----------------------");
			RongPushClient.checkManifest(this);
			Log.i("hao","**********************");
		} catch (RongException e) {
			Log.i("hao",e.getMessage());
		}
		LoggerInterface newLogger = new LoggerInterface() {
			@Override
			public void setTag(String tag) {
				// ignore
				Log.i("789","////////////////");
			}
			@Override
			public void log(String content, Throwable t) {
				Log.i("789", content, t);
			}
			@Override
			public void log(String content) {
				Log.i("789", content);
			}
		};
		Logger.setLogger(this, newLogger);
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

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
			StrictMode.setVmPolicy(builder.build());
		}

		SharePreferenceXutil.saveChannelId("12345678998547854");
		if (SharePreferenceXutil.getRongToken() != ""){
			connectRongServer(SharePreferenceXutil.getRongToken());
		}
	}
	public static void checkManifest(Context context) throws RongException {}

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
	 * 获取屏幕宽高
	 * @param aty
	 */
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
	public static  void sendCode(final String code){
		if (code.equals("") || code == null){
			ToastXutil.show("手机号不能为空");
		}else{
			if (StringXutil.isPhoneNumberValid(code) && code.length() == 11){
				Map<String,Object> map = new HashMap<>();
				map.put("code",code);
				String account_token = SharePreferenceXutil.getAccountToken();
				map.put("account_token",account_token);
				String time_stamp = SystemUtil.getCurrentDate2();
				map.put("time_stamp",time_stamp);
				final String sign = SignUtil.sign(map);
				QueryHttp.sendCode(code,sign,time_stamp, new CallBack() {
					@Override
					public void onResponse(Object response) {
						Log.i("123",response.toString());
						if ((int)response == 0){
							ToastXutil.show("已发送短信验证码至+86"+code);
						}
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

	/**
	 * 验证融云token
	 * @param token
	 */
	public static void  connectRongServer(String token) {
		RongIM.connect(token, new RongIMClient.ConnectCallback() {
			@Override
			public void onSuccess(String userId) {
				getInfo(userId);
			}

			@Override
			public void onError(RongIMClient.ErrorCode errorCode) {
				Log.i("123",errorCode+"---报错");
			}

			@Override
			public void onTokenIncorrect() {
				Log.i("123","---报错");
			}
		});
	}
	public static void getInfo(String id){
		Map<String,Object> map = new HashMap<>();
		String account_token = SharePreferenceXutil.getAccountToken();
		map.put("account_token",account_token);
		map.put("accountIds",id);
		String time_stamp = SystemUtil.getCurrentDate2();
		map.put("time_stamp",time_stamp);
		String sign = SignUtil.sign(map);
		QueryHttp.getUserInfos(account_token, id, sign, time_stamp, new CallBack() {
			@Override
			public void onResponse(Object response) {
				try{
					JSONObject jsonObject = new JSONObject(response.toString());
					int code = jsonObject.getInt("code");
					switch (code){
						case 0:
							Gson gson = new Gson();
							final List<RongInfoBean> bean = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<RongInfoBean>>() {
							}.getType());
							SharePreferenceXutil.saveRongUserName(bean.get(0).getNick_name());
							SharePreferenceXutil.saveRongUserPortrait(bean.get(0).getHead_portrait());
							SharePreferenceXutil.saveRongUserId(bean.get(0).getUser_id());
							RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
								@Override
								public UserInfo getUserInfo(String s) {
									return new UserInfo(bean.get(0).getUser_id(),bean.get(0).getNick_name(),Uri.parse(bean.get(0).getHead_portrait()));
								}
							},true);
							break;
					}
				}catch (Exception e){
					Log.i("456",e.getMessage()+"---");

				}
			}

			@Override
			public void onErrorMsg(String errorMsg) {

			}

			@Override
			public void onError(VolleyError error) {

			}
		});
	}
}

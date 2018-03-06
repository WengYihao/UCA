package com.cn.uca.popupwindows;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.ShowAdapter;
import com.cn.uca.adapter.home.lvpai.PopupWindowAddressAdapter;
import com.cn.uca.bean.home.lvpai.setAddressBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.ItemClick;
import com.cn.uca.impl.ServiceBack;
import com.cn.uca.impl.raider.BuyRaiderImpl;
import com.cn.uca.impl.user.PayBack;
import com.cn.uca.receiver.UpdateService;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.PasswordInputView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowPopupWindow {

	public static void show(Context context, List<String> list, View view,final ItemClick click){
		View show = LayoutInflater.from(context).inflate(R.layout.show_list, null);
		ListView listView = (ListView)show.findViewById(R.id.list);
		ShowAdapter showAdapter = new ShowAdapter(list,context);
		listView.setAdapter(showAdapter);

		final PopupWindow popupWindow = new PopupWindow(show, MyApplication.width/4,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		popupWindow.showAsDropDown(view);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				click.click(i);
				popupWindow.dismiss();
			}
		});
	}

	/**
	 * 升级提示
	 * @param view
	 * @param context
	 * @param linkUrl
	 * @param loadUrl
	 */
	public static void updateWindow(View view, final Context context, String linkUrl, final String loadUrl){
		View update = LayoutInflater.from(context).inflate(R.layout.update_dialog,null);
		TextView positiveButton = (TextView)update.findViewById(R.id.positiveButton);
		TextView negativeButton = (TextView)update.findViewById(R.id.negativeButton);
		WebView webView = (WebView)update.findViewById(R.id.webView);
		webView.loadUrl(linkUrl);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});

		final PopupWindow popupWindow = new PopupWindow(update, MyApplication.width*4/5,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		popupWindow.showAtLocation(view, Gravity.CENTER,0,0);


		positiveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent updateIntent = new Intent(
						context,
						UpdateService.class);
				updateIntent.putExtra("titleId",
						R.string.app_name);
				updateIntent.putExtra("loadUrl",loadUrl);
				context.startService(updateIntent);
				popupWindow.dismiss();
			}
		});

		negativeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});
	}
	/**
	 * 服务须知
	 * @param view
	 * @param context
	 * @param loadUrl
	 */
	public static void seviceWindow(View view, final Context context, final String loadUrl, final ServiceBack back){
		final Dialog dialog = new Dialog(context,R.style.dialog_style);
		View update = LayoutInflater.from(context).inflate(R.layout.service_notice_dialog,null);
		TextView positiveButton = (TextView)update.findViewById(R.id.positiveButton);
		TextView negativeButton = (TextView)update.findViewById(R.id.negativeButton);
		WebView webView = (WebView)update.findViewById(R.id.webView);
		webView.loadUrl(loadUrl);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});

		dialog.setContentView(update);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		params.width = MyApplication.width-SystemUtil.dip2px(36);
		params.height = MyApplication.height*5/7  ;
		dialog.show();//显示对话框

		positiveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				back.sure();
				dialog.dismiss();
			}
		});

		negativeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
	}
	public static void dayPopupwindow(View view, Context context, final int day, final String type, final int id, String str){
		View dayPopupwindow = LayoutInflater.from(context).inflate(R.layout.day_popupwindow,null);
		TextView num = (TextView)dayPopupwindow.findViewById(R.id.num);
		TextView close = (TextView)dayPopupwindow.findViewById(R.id.close);
		final EditText content = (EditText)dayPopupwindow.findViewById(R.id.content);
		switch (type){
			case "day":
				num.setText("余"+day+"天");
				content.setHint("今天有什么计划呢？");
				break;
			case "month":
				num.setText("余"+day+"月");
				content.setHint("今月有什么计划呢？");
				break;
		}
		switch (id){
			case 1:
				//添加
				break;
			case 2:
				//修改
				content.setText(str);
				break;
			case 3:
				//查看
				content.setText(str);
				content.setEnabled(false);
				break;
		}
		TextView save = (TextView)dayPopupwindow.findViewById(R.id.save);

		final PopupWindow popupWindow = new PopupWindow(dayPopupwindow,MyApplication.width*6/7,MyApplication.height*3/4);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
		popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

		AssetManager mgr = context.getAssets();//得到AssetManager
		Typeface tf=Typeface.createFromAsset(mgr, "fonts/ttf.ttf");//根据路径得到Typeface
		num.setTypeface(tf);//设置字体
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String val = content.getText().toString().trim();
				if (StringXutil.isEmpty(val)){
					ToastXutil.show("请编辑要添加的内容");
				}else {
					Map<String,Object> map = new HashMap<>();
					String time_stamp = SystemUtil.getCurrentDate2();
					String account_token = SharePreferenceXutil.getAccountToken();
					String sign;
					switch (type){
						case "day":
							map.put("time_stamp",time_stamp);
							map.put("day",day);
							map.put("content",val);
							map.put("account_token",account_token);
							sign = SignUtil.sign(map);
							HomeHttp.addLifeDayEvent(sign, time_stamp, account_token, val, day, new CallBack() {
								@Override
								public void onResponse(Object response) {
									switch ((int) response){
										case 0:
										    popupWindow.dismiss();
											switch (id){
												case 1:
													ToastXutil.show("添加成功");
													break;
												case 2:
													ToastXutil.show("修改成功");
													break;
											}
											break;
									}
								}

								@Override
								public void onErrorMsg(String errorMsg) {
                                    popupWindow.dismiss();
									ToastXutil.show(errorMsg);
								}

								@Override
								public void onError(VolleyError error) {

								}
							});
							break;
						case "month":
							map.put("time_stamp",time_stamp);
							map.put("month",day);
							map.put("content",val);
							map.put("account_token",account_token);
							sign = SignUtil.sign(map);
							HomeHttp.addLifeMonthEvent(sign, time_stamp, account_token, val, day, new CallBack() {
								@Override
								public void onResponse(Object response) {
									switch ((int) response){
										case 0:
											popupWindow.dismiss();
											switch (id){
												case 1:
													ToastXutil.show("添加成功");
													break;
												case 2:
													ToastXutil.show("修改成功");
													break;
											}
											break;
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
							break;
					}
				}
			}
		});
	}

	public static void addresspopuWindow(View view, Context context, List<setAddressBean> list){
		View inflate = LayoutInflater.from(context).inflate(R.layout.choose_spot_dialog, null);
		ListView listView = (ListView)inflate.findViewById(R.id.listView);
//		PopupWindowAddressAdapter adapter = new PopupWindowAddressAdapter(list,context);
//		listView.setAdapter(adapter);

		PopupWindow popupWindow = new PopupWindow(inflate, MyApplication.width-80,
				MyApplication.height*3/5, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		popupWindow.showAsDropDown(view);
	}

	public static void paymentWindow(final Context context, final String payName, final double actual_payment, final int type, final PayBack payBack){
		final Dialog dialog = new Dialog(context,R.style.dialog_style);
		View payment = LayoutInflater.from(context).inflate(R.layout.payment_dialog,null);
		TextView close = (TextView)payment.findViewById(R.id.close);
		TextView name = (TextView)payment.findViewById(R.id.name);
		TextView price = (TextView)payment.findViewById(R.id.price);
		TextView mode_type = (TextView)payment.findViewById(R.id.mode_type);
        TextView icon = (TextView)payment.findViewById(R.id.icon);
		final PasswordInputView passwordView = (PasswordInputView)payment.findViewById(R.id.passwordView);
		TextView pay = (TextView)payment.findViewById(R.id.pay);
		SetLayoutParams.setLinearLayout(pay,(MyApplication.width*2/3)-SystemUtil.dip2px(24),((MyApplication.width*2/3)-SystemUtil.dip2px(24))/6);
		RelativeLayout layout = (RelativeLayout)payment.findViewById(R.id.layout);
		name.setText(payName);
		price.setText("￥"+actual_payment);
		switch (type){
			case 1://钱包
				mode_type.setText("钱包");
                icon.setBackgroundResource(R.mipmap.wallet_pay_icon);
				break;
			case 2://微信
				mode_type.setText("微信");
                icon.setBackgroundResource(R.mipmap.wechat_pay_icon);
				pay.setVisibility(View.VISIBLE);
				passwordView.setVisibility(View.GONE);
				break;
			case 3://支付宝
				mode_type.setText("支付宝");
                icon.setBackgroundResource(R.mipmap.alipay_icon);
				pay.setVisibility(View.VISIBLE);
				passwordView.setVisibility(View.GONE);
				break;
		}
		SetLayoutParams.setLinearLayout(passwordView,(MyApplication.width*2/3)-SystemUtil.dip2px(24),((MyApplication.width*2/3)-SystemUtil.dip2px(24))/6);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentModeWindow(context,payName,actual_payment,type,payBack);
				dialog.dismiss();
			}
		});
		passwordView.setOnFinishListener(new PasswordInputView.OnFinishListener() {
			@Override
			public void setOnPasswordFinished() {
				if (passwordView.getOriginText().length() == passwordView.getMaxPasswordLength()) {
//					ToastXutil.show("支付成功");
					payBack.walletPay(passwordView.getOriginText());
					dialog.dismiss();
				}
			}
		});
		pay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				ToastXutil.show("支付成功");
				payBack.otherPay(type);
				dialog.dismiss();
			}
		});
		dialog.setContentView(payment);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		params.width = MyApplication.width*2/3;
		params.height = MyApplication.height*2/5;
		dialog.show();//显示对话框
	}

	public static void paymentModeWindow(final Context context,final String payName,final double actual_payment,int type,final PayBack payBack){
		final Dialog dialog = new Dialog(context,R.style.dialog_style);
		View payment = LayoutInflater.from(context).inflate(R.layout.payment_mode_dialog,null);
		RadioButton btn1 = (RadioButton)payment.findViewById(R.id.btn1);
		RadioButton btn2 = (RadioButton)payment.findViewById(R.id.btn2);
		RadioButton btn3 = (RadioButton)payment.findViewById(R.id.btn3);
		switch (type){
			case 1:
				btn1.setChecked(true);
				break;
			case 2:
				btn2.setChecked(true);
				break;
			case 3:
				btn3.setChecked(true);
				break;
		}
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentWindow(context,payName,actual_payment,1,payBack);
				dialog.dismiss();
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentWindow(context,payName,actual_payment,2,payBack);
				dialog.dismiss();
			}
		});
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentWindow(context,payName,actual_payment,3,payBack);
				dialog.dismiss();
			}
		});
		dialog.setContentView(payment);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		params.width = MyApplication.width*2/3;
		params.height = MyApplication.height*2/5;
		dialog.show();//显示对话框
	}

	public static void raiderIntegral(View view, final  Context context, final int id, String url, String name, final int integral, final BuyRaiderImpl buyRaider){
		final Dialog dialog = new Dialog(context);
		View raider = LayoutInflater.from(context).inflate(R.layout.raider_integral_dialog,null);
		ImageView pic = (ImageView)raider.findViewById(R.id.pic);
		TextView nameTv = (TextView)raider.findViewById(R.id.name);
		TextView integralTv = (TextView)raider.findViewById(R.id.integral);
		TextView buy = (TextView)raider.findViewById(R.id.buy);
		if (integral < 50){
			buy.setText("获取积分");
		}else{
			buy.setText("立即解锁");
		}
		ImageLoader.getInstance().displayImage(url,pic);
		nameTv.setText("【"+name+"】");
		integralTv.setText("当前积分:"+integral);
		buy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (integral < 50){
					buyRaider.buyRaider(2,id);//签到
				}else{
					buyRaider.buyRaider(1,id);//兑换
				}

			}
		});
		dialog.setContentView(raider);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		params.width = MyApplication.width-200;
		params.height = MyApplication.height*5/16;
		dialog.show();//显示对话框
	}
}

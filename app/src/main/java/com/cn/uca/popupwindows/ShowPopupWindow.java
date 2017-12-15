package com.cn.uca.popupwindows;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.ShowAdapter;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.ItemClick;
import com.cn.uca.receiver.UpdateService;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
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

	public static void dayPopupwindow(View view, Context context, final int day, final String type){
		View dayPopupwindow = LayoutInflater.from(context).inflate(R.layout.day_popupwindow,null);
		TextView num = (TextView)dayPopupwindow.findViewById(R.id.num);
		TextView close = (TextView)dayPopupwindow.findViewById(R.id.close);
		final EditText content = (EditText)dayPopupwindow.findViewById(R.id.content);
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
											ToastXutil.show("添加成功");
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
											ToastXutil.show("添加成功");
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

}

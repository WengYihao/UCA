package com.cn.uca.popupwindows;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.ShowAdapter;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.yueka.OnSelectItemListener;
import com.cn.uca.receiver.UpdateService;
import com.cn.uca.ui.LoadActivity;
import com.cn.uca.ui.MainActivity;
import com.cn.uca.util.ToastXutil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ShowPopupWindow {

	public static void show(Context context,List<String> list,View view){
		View show = LayoutInflater.from(context).inflate(R.layout.show_list, null);
		ListView listView = (ListView)show.findViewById(R.id.list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

			}
		});

		ShowAdapter showAdapter = new ShowAdapter(list,context);

		listView.setAdapter(showAdapter);

		final PopupWindow popupWindow = new PopupWindow(show, MyApplication.width/4,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		popupWindow.showAsDropDown(view);
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

	public static void showSpot(View view,Context context,String name,String content,String url){
		View showSpot = LayoutInflater.from(context).inflate(R.layout.spot_marker_show, null);
		TextView spot_name = (TextView) showSpot.findViewById(R.id.spot_name);
		ImageView spot_pic = (ImageView)showSpot.findViewById(R.id.spot_pic) ;
		TextView spot_content = (TextView)showSpot.findViewById(R.id.spot_content);

		spot_name.setText(name);
		ImageLoader.getInstance().displayImage(url,spot_pic);
		spot_content.setText(content);
		final PopupWindow popupWindow = new PopupWindow(showSpot, MyApplication.width/4,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		popupWindow.showAsDropDown(view);
	}
}

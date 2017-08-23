package com.cn.uca.popupwindows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cn.uca.R;
import com.cn.uca.adapter.ShowAdapter;
import com.cn.uca.config.MyApplication;

import java.util.List;

public class ShowPopupWindow {

	public static void show(Context context,List<String> list,View view){
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
	}
}

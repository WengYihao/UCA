package com.cn.uca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;

import java.util.List;

/**
 * 城市选择字母适配器
 */

public class PlaceAdapter extends BaseAdapter{
	private String [] list;
	private Context context;

	public PlaceAdapter(){}
	public PlaceAdapter(String [] list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(String [] list) {
		if (list != null) {
			this.list = (String []) list;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.place_item, parent, false);
			holder.backlayout = (RelativeLayout)convertView.findViewById(R.id.backlayout);
			holder.item = (TextView)convertView.findViewById(R.id.place);
			RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) holder.backlayout.getLayoutParams();
			linearParams.height = MyApplication.width *2/7;
			linearParams.width = MyApplication.width *2/7;
			holder.backlayout.setLayoutParams(linearParams);
			RelativeLayout.LayoutParams linearParams2 =(RelativeLayout.LayoutParams) holder.item.getLayoutParams(); //取控件textView当前的布局参数
			linearParams2.height = MyApplication.width * 2/8;// 控件的高强制设成20
			linearParams2.width = MyApplication.width * 2/8;// 控件的宽强制设成30
			holder.item.setLayoutParams(linearParams2); //使设置好的布局参数应用到控件
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item.setText(list[position]);
		return convertView;
	}
	class ViewHolder {
		RelativeLayout backlayout;
		TextView item;
	}
}

package com.cn.uca.adapter.tourism;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.tourism.TourismPlaceBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 城市选择字母适配器
 */

public class PlaceAdapter extends BaseAdapter{
	private List<TourismPlaceBean> list;
	private Context context;

	public PlaceAdapter(){}
	public PlaceAdapter(List<TourismPlaceBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<TourismPlaceBean> list) {
		if (list != null) {
			this.list = list;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
			holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
			holder.item = (TextView)convertView.findViewById(R.id.place);
			SetLayoutParams.setRelativeLayout(holder.backlayout,MyApplication.width *2/7,MyApplication.width *2/7);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item.setText(list.get(position).getName());
		holder.item.getBackground().setAlpha(120);
		Uri uri = Uri.parse(list.get(position).getUrl());
		holder.pic.setImageURI(uri);
		return convertView;
	}
	class ViewHolder {
		RelativeLayout backlayout;
		TextView item;
		SimpleDraweeView pic;
	}
}

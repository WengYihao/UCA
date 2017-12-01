package com.cn.uca.adapter.yueka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.PlacesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 约咖详情路线点
 */
public class PointAdapter extends BaseAdapter{
	private List<String> list;
	private Context context;

	public PointAdapter(){}
	public PointAdapter(List<String> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<String> list) {
		if (list != null) {
			this.list = (List<String>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.point_item, parent, false);
			holder.logo = (TextView)convertView.findViewById(R.id.logo);
			holder.placeName = (TextView)convertView.findViewById(R.id.placeName);
			holder.icon = (ImageView)convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.placeName.setText(list.get(position));
		if (position != list.size()-1){
			holder.icon.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	class ViewHolder {
		TextView logo,placeName;
		ImageView icon;
	}
}

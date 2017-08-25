package com.cn.uca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;

import java.util.List;

/**
 * 夺宝城市适配器
 */
public class RaidersAdapter extends BaseAdapter{
	private List<String> list;
	private Context context;

	public RaidersAdapter(){}
	public RaidersAdapter(List<String> list, Context context) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.raiders_item, parent, false);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position));
		return convertView;
	}
	class ViewHolder {
		TextView name;
	}
}

package com.cn.uca.adapter.raiders;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.RaidersBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 夺宝城市适配器
 */
public class RaidersAdapter extends BaseAdapter{
	private List<RaidersBean> list;
	private Context context;

	public RaidersAdapter(){}
	public RaidersAdapter(List<RaidersBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<RaidersBean> list) {
		if (list != null) {
			this.list = (List<RaidersBean>) list;
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
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
			holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.size = (TextView)convertView.findViewById(R.id.size);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getName());
		holder.name.getBackground().setAlpha(120);
		Uri uri = Uri.parse(list.get(position).getUrl());
		holder.pic.setImageURI(uri);
		holder.size.setText(list.get(position).getSize());
		holder.layout.getBackground().setAlpha(120);
		return convertView;
	}
	class ViewHolder {
		RelativeLayout layout;
		SimpleDraweeView pic;
		TextView name,size;
	}
}

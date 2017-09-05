package com.cn.uca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.PopularityBean;

import java.util.List;


public class PopularityAdapter extends BaseAdapter{
	private List<PopularityBean> list;
	private Context context;

	public PopularityAdapter(){}
	public PopularityAdapter(List<PopularityBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<PopularityBean> list) {
		if (list != null) {
			this.list = (List<PopularityBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.popularity_item, parent, false);
			holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.count = (TextView)convertView.findViewById(R.id.count);
			holder.sum = (TextView)convertView.findViewById(R.id.sum);
			holder.progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getName());
		holder.count.setText("总需"+list.get(position).getCount()+"份");
		holder.sum.setText("剩余"+list.get(position).getSum()+"份");
		holder.progressBar.setMax(list.get(position).getCount());
		holder.progressBar.setProgress(list.get(position).getCount()-list.get(position).getSum());
		return convertView;
	}
	class ViewHolder {
		ImageView imageView;
		TextView name,count,sum;
		ProgressBar progressBar;
	}
}

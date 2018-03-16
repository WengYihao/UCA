package com.cn.uca.adapter.home.translate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.translate.HistoryBean;
import com.cn.uca.bean.home.translate.LanguageBean;

import java.util.List;


public class HistoryAdapter extends BaseAdapter{
	private List<HistoryBean> list;
	private Context context;
	public HistoryAdapter(List<HistoryBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<HistoryBean> list) {
		if (list != null) {
			this.list = (List<HistoryBean>) list;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.translate_history_item, parent, false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder = new ViewHolder();
		holder.src = (TextView) convertView.findViewById(R.id.src);
		holder.dst = (TextView)convertView.findViewById(R.id.dst);
		holder.src.setText(list.get(position).getSrc());
		holder.dst.setText(list.get(position).getDst());
		return convertView;
	}
	class ViewHolder {
		TextView src,dst;
	}
}

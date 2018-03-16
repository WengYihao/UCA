package com.cn.uca.adapter.home.translate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.translate.LanguageBean;
import com.cn.uca.bean.home.travel.AroundPlayBean;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class LanguageAdapter extends BaseAdapter{
	private List<LanguageBean> list;
	private Context context;
	public LanguageAdapter(List<LanguageBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<LanguageBean> list) {
		if (list != null) {
			this.list = (List<LanguageBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.language_item, parent, false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder = new ViewHolder();
		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.abbreviate = (TextView)convertView.findViewById(R.id.abbreviate);
		holder.name.setText(list.get(position).getName());
		holder.abbreviate.setText(list.get(position).getShorthand());
		return convertView;
	}
	class ViewHolder {
		TextView name,abbreviate;
	}
}

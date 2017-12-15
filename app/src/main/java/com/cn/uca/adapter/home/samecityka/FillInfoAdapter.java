package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.FillInfoBean;

import java.util.List;

/**
 * 约咖详情路线点
 */
public class FillInfoAdapter extends BaseAdapter{
	private List<FillInfoBean> list;
	private Context context;

	public FillInfoAdapter(){}
	public FillInfoAdapter(List<FillInfoBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<FillInfoBean> list) {
		if (list != null) {
			this.list = (List<FillInfoBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.fillinfo_item, parent, false);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.info = (EditText)convertView.findViewById(R.id.info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getInfo_name());
		return convertView;
	}
	class ViewHolder {
		TextView name;
		EditText info;
	}
}

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
 * 路线点
 */

public class LinePointAdapter extends BaseAdapter{
	private List<PlacesBean> list;
	private Context context;

	public LinePointAdapter(){}
	public LinePointAdapter(List<PlacesBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<PlacesBean> list) {
		if (list != null) {
			this.list = (ArrayList<PlacesBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.add_line_item, parent, false);
			holder.logo = (ImageView)convertView.findViewById(R.id.logo);
			holder.placeName = (TextView)convertView.findViewById(R.id.placeName);
			holder.editText = (TextView) convertView.findViewById(R.id.editText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.placeName.setText(list.get(position).getPlace_name());
		holder.editText.setText(list.get(position).getDeparture_address());
		return convertView;
	}
	class ViewHolder {
		ImageView logo;
		TextView placeName;
		TextView editText;
	}
}

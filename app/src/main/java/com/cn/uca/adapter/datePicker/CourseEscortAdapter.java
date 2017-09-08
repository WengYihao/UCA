package com.cn.uca.adapter.datePicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.ImgAdapter;
import com.cn.uca.bean.CourseEscortBean;

import java.util.List;


public class CourseEscortAdapter extends BaseAdapter{
	private List<CourseEscortBean> list;
	private Context context;

	public CourseEscortAdapter(){}
	public CourseEscortAdapter(List<CourseEscortBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<CourseEscortBean> list) {
		if (list != null) {
			this.list = (List<CourseEscortBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
			holder.pic = (GridView)convertView.findViewById(R.id.pic);
			holder.type = (TextView)convertView.findViewById(R.id.type);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.evaluate = (TextView)convertView.findViewById(R.id.evaluate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImgAdapter imgAdapter = new ImgAdapter(list.get(position).getPicList(),context);
		holder.pic.setAdapter(imgAdapter);
		holder.type.setText(list.get(position).getType());
		holder.time.setText(list.get(position).getTime());
		holder.evaluate.setText(list.get(position).getEvaluate());
		return convertView;
	}
	class ViewHolder {
		GridView pic;
		TextView type,time,evaluate;
	}
}

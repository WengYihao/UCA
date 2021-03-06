package com.cn.uca.adapter.yueka;

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
import com.cn.uca.bean.OrderCourseBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 约咖历程
 */

public class OrderCourseAdapter extends BaseAdapter{
	private List<OrderCourseBean> list;
	private Context context;

	public OrderCourseAdapter(){}
	public OrderCourseAdapter(List<OrderCourseBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<OrderCourseBean> list) {
		if (list != null) {
			this.list = (List<OrderCourseBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.order_course_item, parent, false);
			holder.pic = (RelativeLayout)convertView.findViewById(R.id.pic);
			holder.half = (RelativeLayout)convertView.findViewById(R.id.half);
			SetLayoutParams.setLinearLayout(holder.pic,MyApplication.width*2/5,MyApplication.width/4);
			holder.imageView  =(SimpleDraweeView)convertView.findViewById(R.id.imageView);
			holder.num = (TextView)convertView.findViewById(R.id.num);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.place = (TextView)convertView.findViewById(R.id.place);
			holder.text = (TextView)convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageView.setImageURI(Uri.parse(list.get(position).getImg()));
		holder.num.setText(list.get(position).getNum()+"");
		holder.text.setText(list.get(position).getTime());
		holder.place.setText(list.get(position).getPlace());
		holder.text.setText(list.get(position).getText());
		holder.half.getBackground().setAlpha(120);
		return convertView;
	}
	class ViewHolder {
		RelativeLayout pic,half;
		SimpleDraweeView imageView;
		TextView num,time,place,text;
	}
}

package com.cn.uca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.OrderBean;

import java.util.List;


public class OrderAdapter extends BaseAdapter{
	private List<OrderBean> list;
	private Context context;

	public OrderAdapter(){}
	public OrderAdapter(List<OrderBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<OrderBean> list) {
		if (list != null) {
			this.list = (List<OrderBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
			holder.pic = (TextView)convertView.findViewById(R.id.pic);
			holder.type = (TextView)convertView.findViewById(R.id.type);
			holder.state = (TextView)convertView.findViewById(R.id.state);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.place = (TextView)convertView.findViewById(R.id.place);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.type.setText(list.get(position).getType());
		holder.state.setText(list.get(position).getState());
		holder.name.setText(list.get(position).getName());
		holder.price.setText("￥"+list.get(position).getPrice());
		holder.time.setText(list.get(position).getTime());
		holder.place.setText(list.get(position).getPlace());
		return convertView;
	}
	class ViewHolder {
		TextView pic,type,state,name,price,time,place;
	}
}

package com.cn.uca.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.CardBean;
import com.cn.uca.bean.user.order.OrderTypeBean;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 名片管理
 */
public class OrderTypeAdapter extends BaseAdapter{
	private List<OrderTypeBean> list;
	private Context context;

	public OrderTypeAdapter(List<OrderTypeBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<OrderTypeBean> list) {
		if (list != null) {
			this.list = list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.order_type_item, parent, false);
			holder.typename = (TextView)convertView.findViewById(R.id.typename);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).isCheck()){
			holder.typename.setBackgroundResource(R.color.white);
			holder.typename.setTextColor(context.getResources().getColor(R.color.ori));
		}else{
			holder.typename.setBackgroundResource(0);
			holder.typename.setTextColor(context.getResources().getColor(R.color.white));
		}
		holder.typename.setText(list.get(position).getName());

		return convertView;
	}
	class ViewHolder {
		TextView typename;
	}
}

package com.cn.uca.adapter.home.raiders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.user.order.OrderTypeBean;

import java.util.List;

/**
 * 字母选择
 */
public class LetterAdapter extends BaseAdapter{
	private List<OrderTypeBean> list;
	private Context context;

	public LetterAdapter(List<OrderTypeBean> list, Context context) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.letter_item, parent, false);
			holder.item = (TextView)convertView.findViewById(R.id.item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).isCheck()){
			holder.item.setBackgroundResource(R.drawable.twenty_circular_ori_right_background);
			holder.item.setTextColor(context.getResources().getColor(R.color.white));
		}else{
			holder.item.setBackgroundResource(0);
			holder.item.setTextColor(context.getResources().getColor(R.color.gray2));
		}
		holder.item.setText(list.get(position).getName());
		return convertView;
	}
	class ViewHolder {
		TextView item;
	}
}

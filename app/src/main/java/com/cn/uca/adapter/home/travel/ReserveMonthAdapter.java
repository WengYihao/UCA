package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.AroundPlayBean;
import com.cn.uca.bean.home.travel.ReserveMonthBean;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
//旅游出行日期

public class ReserveMonthAdapter extends BaseAdapter{
	private List<ReserveMonthBean> list;
	private Context context;
	public ReserveMonthAdapter(List<ReserveMonthBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<ReserveMonthBean> list) {
		if (list != null) {
			this.list = (List<ReserveMonthBean>) list;
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
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.reserve_month_item, parent, false);
			holder.month = (TextView)convertView.findViewById(R.id.month);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).getName().substring(0,1).equals("0")){
			holder.month.setText(list.get(position).getName().substring(1)+"月");
		}else{
			holder.month.setText(list.get(position).getName()+"月");
		}
		if (list.get(position).isCheck()){
			holder.month.setBackgroundResource(R.color.white);
			holder.month.setTextColor(context.getResources().getColor(R.color.ori));
		}else{
			holder.month.setBackgroundResource(0);
			holder.month.setTextColor(context.getResources().getColor(R.color.white));
		}
		return convertView;
	}
	class ViewHolder {
		TextView month;
	}
}

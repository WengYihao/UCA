package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.RegionBean;
import com.cn.uca.bean.home.travel.ScheduleBean;
import com.cn.uca.util.SystemUtil;

import java.util.List;

/**
 * 旅游团期选择
 */
public class ScheduleAdapter extends BaseAdapter{
	private List<ScheduleBean> list;
	private Context context;

	public ScheduleAdapter(List<ScheduleBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<ScheduleBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.schedule_date_item, parent, false);
			holder.layout = (LinearLayout)convertView.findViewById(R.id.layout);
			holder.date = (TextView)convertView.findViewById(R.id.date);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		switch (list.get(position).isCheck()){
			case 0:
				holder.layout.setBackgroundResource(0);
				holder.price.setTextColor(context.getResources().getColor(R.color.grey));
				holder.date.setTextColor(context.getResources().getColor(R.color.grey));
				break;
			case 1:
				holder.layout.setBackgroundResource(R.drawable.ten_radius_red_background);
				holder.price.setTextColor(context.getResources().getColor(R.color.white));
				holder.date.setTextColor(context.getResources().getColor(R.color.white));
				break;
		}
		holder.date.setText(list.get(position).getSchedule_date().substring(5)+ SystemUtil.dateToWeek(list.get(position).getSchedule_date()));
		holder.price.setText("￥"+(int)list.get(position).getPerson_peer_price());
		return convertView;
	}
	class ViewHolder {
		LinearLayout layout;
		TextView date,price;
	}
}

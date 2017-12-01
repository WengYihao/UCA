package com.cn.uca.adapter.user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.user.OrderBean;
import com.cn.uca.bean.user.WalletDetailBean;
import com.cn.uca.util.SystemUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 我的订单
 */
public class WalletDetailAdapter extends BaseAdapter{
	private List<WalletDetailBean> list;
	private Context context;

	public WalletDetailAdapter(){}
	public WalletDetailAdapter(List<WalletDetailBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<WalletDetailBean> list) {
		if (list != null) {
			this.list = (List<WalletDetailBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.wallet_detail_item, parent, false);
			holder.month = (TextView)convertView.findViewById(R.id.month);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {
			Date date = SystemUtil.StringToUtilDate2(list.get(position).getTransaction_time());
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间
			int day = SystemUtil.getIntervalDays(date,curDate);
			int month = date.getMonth();
			if (position >= 1){
				int curMonth = (position-1) >= 0 ? SystemUtil.StringToUtilDate2(list.get(position-1).getTransaction_time()).getMonth():0;
				if (month != curMonth && curMonth != 0){
					holder.month.setVisibility(View.VISIBLE);
					holder.month.setText(curMonth+1+"月");
				}else{
					holder.month.setVisibility(View.GONE);
				}
			}else{
				holder.month.setVisibility(View.VISIBLE);
				holder.month.setText(month+1+"月");
			}
			if (day == 0){
				int index  = list.get(position).getTransaction_time().indexOf(" ");
				String time = list.get(position).getTransaction_time().substring(index);
				holder.time.setText("今天 "+time);
			}else if (day == 1){
				int index  = list.get(position).getTransaction_time().indexOf(" ");
				String time = list.get(position).getTransaction_time().substring(index);
				holder.time.setText("昨天 "+time);
			}else if (day == 2){
				int index  = list.get(position).getTransaction_time().indexOf(" ");
				String time = list.get(position).getTransaction_time().substring(index);
				holder.time.setText("前天 "+time);
			}else if(day >2 && day < 5){
				int index  = list.get(position).getTransaction_time().indexOf(" ");
				String time = list.get(position).getTransaction_time().substring(index);
				holder.time.setText(day+"天前 "+time);
			}else{
				holder.time.setText(list.get(position).getTransaction_time());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		switch (list.get(position).getGain_loss()){
			case 0:
				holder.price.setText("+"+list.get(position).getPrice());
				break;
			case 1:
				holder.price.setText("-"+list.get(position).getPrice());
				break;
		}
		holder.name.setText(list.get(position).getRemarks());
		return convertView;
	}
	class ViewHolder {
		TextView month,name,price,time;
	}
}

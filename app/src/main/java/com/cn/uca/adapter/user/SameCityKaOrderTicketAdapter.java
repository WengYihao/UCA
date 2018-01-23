package com.cn.uca.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.MyTicketCodeBean;
import com.cn.uca.bean.user.message.MessageBean;

import java.util.List;

/**
 * 同城咖活动订单门票信息
 */
public class SameCityKaOrderTicketAdapter extends BaseAdapter{
	private List<MyTicketCodeBean> list;
	private Context context;

	public SameCityKaOrderTicketAdapter(){}
	public SameCityKaOrderTicketAdapter(List<MyTicketCodeBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<MyTicketCodeBean> list) {
		if (list != null) {
			this.list = (List<MyTicketCodeBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.samecity_order_ticket_item, parent, false);
			holder.ticket_name = (TextView)convertView.findViewById(R.id.ticket_name);
			holder.ticket_num = (TextView)convertView.findViewById(R.id.ticket_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ticket_name.setText(list.get(position).getTicket_name());
		holder.ticket_num.setText(list.get(position).getNumber()+"");
		return convertView;
	}
	class ViewHolder {
		TextView ticket_name,ticket_num;
	}
}

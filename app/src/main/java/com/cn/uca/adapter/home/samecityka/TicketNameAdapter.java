package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.FillInfoBean;
import com.cn.uca.bean.home.samecityka.TicketsRets;

import java.util.List;

/**
 * 购票名称
 */
public class TicketNameAdapter extends BaseAdapter{
	private List<TicketsRets> list;
	private Context context;

	public TicketNameAdapter(){}
	public TicketNameAdapter(List<TicketsRets> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<TicketsRets> list) {
		if (list != null) {
			this.list = (List<TicketsRets>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.samecityka_ticket_item, parent, false);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText("【"+list.get(position).getTicket_name()+"】 x"+list.get(position).getNumber());
		return convertView;
	}
	class ViewHolder {
		TextView name;
	}
}

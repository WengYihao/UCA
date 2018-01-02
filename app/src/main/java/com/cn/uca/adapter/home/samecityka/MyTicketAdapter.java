package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.MyTicketBean;
import com.cn.uca.bean.home.samecityka.TicketsRets;
import com.cn.uca.impl.samecityka.TicketItemListener;

import java.util.List;

/**
 * 购票名称
 */
public class MyTicketAdapter extends BaseAdapter{
	private List<MyTicketBean> list;
	private Context context;
	private TicketItemListener listener;

	public MyTicketAdapter(){}
	public MyTicketAdapter(List<MyTicketBean> list, Context context,TicketItemListener listener) {
		this.list = list;
		this.context = context;
		this.listener = listener;
	}
	public void setList(List<MyTicketBean> list) {
		if (list != null) {
			this.list = (List<MyTicketBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.my_ticket_item, parent, false);
			holder.ticket_name = (TextView)convertView.findViewById(R.id.ticket_name);
			holder.action_name = (TextView)convertView.findViewById(R.id.action_name);
			holder.ticket_type = (TextView)convertView.findViewById(R.id.ticket_type);
			holder.state = (TextView)convertView.findViewById(R.id.state);
			holder.code_layout = (LinearLayout)convertView.findViewById(R.id.code_layout);
			holder.action_layout = (RelativeLayout)convertView.findViewById(R.id.action_layout);
			holder.code_layout.setTag(position);
			holder.code_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onCodeLayout(v);
				}
			});
			holder.action_layout.setTag(position);
			holder.action_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onActionLayout(v);
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0;i<list.get(position).getTickets().size();i++){
			if (i == list.size()-1){
				builder.append(list.get(position).getTickets().get(i).getTicket_name()+"x"+list.get(position).getTickets().get(i).getNumber());
			}else{
				builder.append(list.get(position).getTickets().get(i).getTicket_name()+"x"+list.get(position).getTickets().get(i).getNumber()+",");
			}
		}
		holder.ticket_name.setText(builder.toString());
		holder.action_name.setText(list.get(position).getTitle());
		switch (list.get(position).getState()){
			case "Enrolment":
				holder.state.setText("已报名");
				break;
			case "Conduct":
				holder.state.setText("进行中");
				break;
			case "End":
				holder.state.setText("已结束");
				break;
		}

		return convertView;
	}
	class ViewHolder {
		TextView ticket_name,action_name,ticket_type,state;
		LinearLayout code_layout;
		RelativeLayout action_layout;
	}
}

package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.TicketBean;
import com.cn.uca.impl.samecityka.DialogListener;

import java.util.List;

/**
 * 同城咖购票弹窗
 */
public class BuyTicketAdapter extends BaseAdapter{
	private List<TicketBean> list;
	private Context context;
	private DialogListener listener;

	public BuyTicketAdapter(){}
	public BuyTicketAdapter(List<TicketBean> list, Context context,DialogListener listener) {
		this.list = list;
		this.context = context;
		this.listener = listener;
	}
	public void setList(List<TicketBean> list) {
		if (list != null) {
			this.list = (List<TicketBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.buy_ticket_item, parent, false);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.text = (TextView)convertView.findViewById(R.id.text);
			holder.add = (TextView)convertView.findViewById(R.id.add);
			holder.reduce = (TextView)convertView.findViewById(R.id.reduce);
			holder.num = (TextView)convertView.findViewById(R.id.num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getTicket_name());
		if (list.get(position).getPrice() == 0){
			holder.price.setText("(免费)");
		}else{
			holder.price.setText("￥"+(int) list.get(position).getPrice());
		}
		if (list.get(position).getSurplus() < 0){
			if (list.get(position).getLimit_ticket() < 0){
				holder.text.setText("票量充足，不限购");
			}else{
				holder.text.setText("票量充足，限购"+list.get(position).getLimit_ticket()+"张");
			}
		}else{
			if (list.get(position).getLimit_ticket() < 0){
				holder.text.setText("剩余"+list.get(position).getSurplus()+"张，"+"不限购");
			}else{
				holder.text.setText("剩余"+list.get(position).getSurplus()+"张，"+"限购"+list.get(position).getLimit_ticket()+"张");
			}
		}
		holder.add.setTag(position);
		holder.reduce.setTag(position);
		holder.add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.add(v);
			}
		});
		holder.reduce.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.reduce(v);
			}
		});
		return convertView;
	}
	class ViewHolder {
		TextView name,price,text,add,reduce,num;
	}
}

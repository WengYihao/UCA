package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.SettlementBean;
import com.cn.uca.bean.home.samecityka.SettlementTicketBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 同城咖结算适配器
 */
public class SettlementTicketAdapter extends BaseAdapter{
	private List<SettlementTicketBean> list;
	private Context context;

	public SettlementTicketAdapter(List<SettlementTicketBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<SettlementTicketBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.samecityka_settlement_ticket_item, parent, false);
			holder.purchasenum = (TextView)convertView.findViewById(R.id.purchasenum);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.inspectnum = (TextView)convertView.findViewById(R.id.inspectnum);
			holder.backnum = (TextView)convertView.findViewById(R.id.backnum);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getTicket_name()+" ￥"+(int)list.get(position).getPrice());
		holder.purchasenum.setText("购票 "+list.get(position).getSell_out());
		holder.inspectnum.setText("检票 "+list.get(position).getCheck_ticket_number());
		holder.backnum.setText("退票 "+list.get(position).getRefund_ticket_number());
		return convertView;
	}
	class ViewHolder {
		TextView purchasenum,name,inspectnum,backnum;
	}
}

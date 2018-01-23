package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.ActionDescribeBean;
import com.cn.uca.bean.home.samecityka.MyTicketCodeBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 门票码门票名称
 */
public class TicketCodeAdapter extends BaseAdapter{
	private List<MyTicketCodeBean> list;
	private Context context;

	public TicketCodeAdapter(List<MyTicketCodeBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<MyTicketCodeBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.action_ticket_code_item, parent, false);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getTicket_name()+"x"+list.get(position).getNumber());
		return convertView;
	}

	class ViewHolder {
		TextView name;
	}
}

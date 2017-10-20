package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.TicketBean;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;

import java.util.List;


public class SpotTicketAdapter extends BaseAdapter{
	private List<TicketBean> list;
	private Context context;

	public SpotTicketAdapter(){}
	public SpotTicketAdapter(List<TicketBean> list, Context context) {
		this.list = list;
		this.context = context;
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
		final int height;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.spot_ticket_item, parent, false);
			holder.spot_name = (TextView)convertView.findViewById(R.id.spot_name);
			holder.link = (TextView)convertView.findViewById(R.id.link);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.price_customer = (TextView)convertView.findViewById(R.id.price_customer);
			holder.price_original = (TextView)convertView.findViewById(R.id.price_original);
			holder.reserve = (TextView)convertView.findViewById(R.id.reserve);
			holder.ll_item = (LinearLayout)convertView.findViewById(R.id.ll_item);
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
			holder.price = (RelativeLayout)convertView.findViewById(R.id.price);
			int intw=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			int inth=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			holder.ll_item.measure(intw, inth);
			int intwidth = holder.ll_item.getMeasuredWidth();
			int intheight =  holder.ll_item.getMeasuredHeight();
			SetLayoutParams.setLinearLayout(holder.layout,SystemUtil.dip2px(50),intheight);
			SetLayoutParams.setRelativeLayout(holder.price,SystemUtil.dip2px(50),intheight*5/8);
			StatusMargin.setTop(holder.reserve,intheight*5/8-SystemUtil.dip2px(25));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.spot_name.setText(list.get(position).getScenic_name());
		holder.time.setText(list.get(position).getScheduled_time());
		holder.price_customer.setText("￥"+(int)list.get(position).getPrice_customer());
		holder.price_original.setText("￥"+(int)list.get(position).getPrice_original());
		holder.price_original.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		return convertView;
	}
	class ViewHolder {
		LinearLayout ll_item;
		RelativeLayout layout,price;
		TextView spot_name,link,time,price_customer,price_original,reserve;
	}
}

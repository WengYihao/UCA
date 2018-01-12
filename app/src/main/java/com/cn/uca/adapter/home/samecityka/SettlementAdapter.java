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
import com.cn.uca.bean.home.samecityka.CollectionBean;
import com.cn.uca.bean.home.samecityka.SettlementBean;
import com.cn.uca.impl.samecityka.SettlementBack;
import com.cn.uca.util.SetListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 同城咖结算适配器
 */
public class SettlementAdapter extends BaseAdapter{
	private List<SettlementBean> list;
	private Context context;
	private SettlementBack settlementBack;

	public SettlementAdapter(List<SettlementBean> list, Context context,SettlementBack settlementBack) {
		this.list = list;
		this.context = context;
		this.settlementBack = settlementBack;
	}
	public void setList(List<SettlementBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.samecityka_settlement_item, parent, false);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			holder.state = (TextView)convertView.findViewById(R.id.state);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.address = (TextView)convertView.findViewById(R.id.address);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.settlement = (TextView)convertView.findViewById(R.id.settlement);
			holder.listView = (ListView)convertView.findViewById(R.id.listView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getCover_url(),holder.pic);
		switch (list.get(position).getCityCafeState()){
			case "Enrolment":
				holder.state.setText("报名中");
				holder.settlement.setEnabled(false);
				holder.settlement.setBackgroundResource(R.drawable.twenty_circular_gray2_background);
				break;
			case "Conduct":
				holder.state.setText("进行中");
				holder.settlement.setEnabled(false);
				holder.settlement.setBackgroundResource(R.drawable.twenty_circular_gray2_background);
				break;
			case "End":
				holder.state.setText("已结束");
				holder.settlement.setBackgroundResource(R.drawable.twenty_circular_ori_background);
				holder.settlement.setTag(position);
				holder.settlement.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						settlementBack.selttlement(v);
					}
				});
				break;
		}
		holder.state.getBackground().setAlpha(120);
		holder.title.setText(list.get(position).getTitle());
		switch (list.get(position).getActivity_type_id()){
			case 1:
				holder.address.setText("线上活动");
				break;
			case 2:
				holder.address.setText(list.get(position).getPosition().getAddress());
				break;
		}
		holder.time.setText(list.get(position).getBeg_time()+"~"+list.get(position).getEnd_time());
		holder.price.setText("￥"+list.get(position).getTotal());
		SettlementTicketAdapter adapter = new SettlementTicketAdapter(list.get(position).getCityCafeTickets(),context);
		holder.listView.setAdapter(adapter);
		SetListView.setListViewHeightBasedOnChildren(holder.listView);
		return convertView;
	}
	class ViewHolder {
		ImageView pic;
		TextView state,title,address,time,price,settlement;
		ListView listView;
	}
}

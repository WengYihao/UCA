package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.TravelBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class DomesticTravelAdapter extends BaseAdapter{
	private List<TravelBean> list;
	private Context context;

	public DomesticTravelAdapter(){}
	public DomesticTravelAdapter(List<TravelBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<TravelBean> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.domestic_travel_item, parent, false);
			holder.pic = (SimpleDraweeView) convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.rule = (TextView)convertView.findViewById(R.id.rule);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.score = (TextView)convertView.findViewById(R.id.score);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Uri uri = Uri.parse(list.get(position).getCompress_picture());
		holder.pic.setImageURI(uri);
		holder.name.setText(list.get(position).getProduct_name());
		holder.rule.setText(list.get(position).getJourney_days()+"天"+list.get(position).getCheck_in_days()+"晚");
		holder.price.setText("￥"+(int)list.get(position).getMin_price());
		holder.score.setText(list.get(position).getScore()+"分");
		return convertView;
	}
	class ViewHolder {
		SimpleDraweeView pic;
		TextView name,rule,price,score;
	}
}

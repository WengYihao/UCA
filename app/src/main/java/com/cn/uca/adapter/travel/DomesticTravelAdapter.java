package com.cn.uca.adapter.travel;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.travel.DomesticTravelBean;
import com.cn.uca.bean.travel.TravelPlaceBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class DomesticTravelAdapter extends BaseAdapter{
	private List<DomesticTravelBean> list;
	private Context context;
	private int height,width;

	public DomesticTravelAdapter(){}
	public DomesticTravelAdapter(List<DomesticTravelBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public DomesticTravelAdapter(List<DomesticTravelBean> list, Context context, int height, int width) {
		this.list = list;
		this.context = context;
		this.height = height;
		this.width = width;
	}
	public void setList(List<DomesticTravelBean> list) {
		if (list != null) {
			this.list = (List<DomesticTravelBean>) list;
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
		Uri uri = Uri.parse(list.get(position).getPicture_url());
		holder.pic.setImageURI(uri);
		holder.name.setText(list.get(position).getTitle());
		holder.rule.setText(list.get(position).getRule());
		holder.price.setText("￥"+list.get(position).getPrice());
		holder.score.setText(list.get(position).getScore()+"分");
		return convertView;
	}
	class ViewHolder {
		SimpleDraweeView pic;
		TextView name,rule,price,score;
	}
}

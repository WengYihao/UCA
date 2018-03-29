package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.CitySpotTicketBean;
import com.cn.uca.config.MyConfig;
import com.cn.uca.ownerview.MyRatingBar;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class CitySpotTicketAdapter extends BaseAdapter{
	private List<CitySpotTicketBean> list;
	private Context context;

	public CitySpotTicketAdapter(){}
	public CitySpotTicketAdapter(List<CitySpotTicketBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<CitySpotTicketBean> list) {
		if (list != null) {
			this.list = (List<CitySpotTicketBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.city_spot_list_item, parent, false);
			holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.product_num = (TextView)convertView.findViewById(R.id.product_num);
			holder.start = (MyRatingBar)convertView.findViewById(R.id.start);
			holder.comment_num = (TextView)convertView.findViewById(R.id.comment_num);
			holder.address = (TextView)convertView.findViewById(R.id.address);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Uri uri = Uri.parse(list.get(position).getPicture_url()+MyConfig.corpPhoto);
		holder.pic.setImageURI(uri);
		holder.name.setText(list.get(position).getScenic_spot_name());
		holder.product_num.setText("共"+list.get(position).getProduct_quantity()+"个产品");
		if (list.get(position).getScore() < 0){
			holder.start.setRating(1);
		}else {
			holder.start.setRating((float)list.get(position).getScore());
		}
		holder.comment_num.setText(list.get(position).getEvaluation_quantity()+"条评价");
		holder.address.setText(list.get(position).getAddress());
		holder.price.setText("￥"+(int)list.get(position).getMin_price());
		return convertView;
	}
	class ViewHolder {
		SimpleDraweeView pic;
		TextView name,product_num,comment_num,address,price;
		MyRatingBar start;
	}
}

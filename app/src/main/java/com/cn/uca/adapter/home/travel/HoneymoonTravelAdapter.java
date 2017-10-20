package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.HoneymoonTravelBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class HoneymoonTravelAdapter extends BaseAdapter{
	private List<HoneymoonTravelBean> list;
	private Context context;
	private int height,width;

	public HoneymoonTravelAdapter(){}
	public HoneymoonTravelAdapter(List<HoneymoonTravelBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public HoneymoonTravelAdapter(List<HoneymoonTravelBean> list, Context context, int height, int width) {
		this.list = list;
		this.context = context;
		this.height = height;
		this.width = width;
	}
	public void setList(List<HoneymoonTravelBean> list) {
		if (list != null) {
			this.list = (List<HoneymoonTravelBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.honeymoon_travel_item, parent, false);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.days = (TextView)convertView.findViewById(R.id.days);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.purchase_number = (TextView)convertView.findViewById(R.id.purchase_number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getPicture_url(),holder.pic);
		holder.name.setText(list.get(position).getMiyeu_title());
		holder.days.setText("行程天数:"+list.get(position).getDays());
		holder.price.setText("￥"+list.get(position).getPrice());
		holder.purchase_number.setText(list.get(position).getPurchase_number()+"人已购买");
		return convertView;
	}
	class ViewHolder {
		ImageView pic;
		TextView name,days,purchase_number,price;
	}
}

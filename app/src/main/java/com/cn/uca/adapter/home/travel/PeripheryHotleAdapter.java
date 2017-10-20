package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.PeripheryHotelBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class PeripheryHotleAdapter extends BaseAdapter{
	private List<PeripheryHotelBean> list;
	private Context context;
	private int height,width;

	public PeripheryHotleAdapter(){}
	public PeripheryHotleAdapter(List<PeripheryHotelBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public PeripheryHotleAdapter(List<PeripheryHotelBean> list, Context context, int height, int width) {
		this.list = list;
		this.context = context;
		this.height = height;
		this.width = width;
	}
	public void setList(List<PeripheryHotelBean> list) {
		if (list != null) {
			this.list = (List<PeripheryHotelBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.periphery_travel_item, parent, false);
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			holder.view = (TextView)convertView.findViewById(R.id.view);
			holder.view.getBackground().setAlpha(80);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.placeAndPrice = (TextView)convertView.findViewById(R.id.placeAndPrice);
			SetLayoutParams.setRelativeLayout(holder.layout , MyApplication.width,MyApplication.height/7*2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getPicture(),holder.pic);
		holder.name.setText(list.get(position).getHotel_name());
		holder.placeAndPrice.setText(list.get(position).getAddress()+"·"+"￥"+list.get(position).getPrice());
		return convertView;
	}
	class ViewHolder {
		RelativeLayout layout;
		ImageView pic;
		TextView view,name,placeAndPrice;
	}
}

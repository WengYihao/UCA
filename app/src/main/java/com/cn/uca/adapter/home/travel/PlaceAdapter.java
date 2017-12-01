package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.TravelPlaceBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class PlaceAdapter extends BaseAdapter{
	private List<TravelPlaceBean> list;
	private Context context;

	public PlaceAdapter(){}
	public PlaceAdapter(List<TravelPlaceBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<TravelPlaceBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.place_item, parent, false);
			holder.backlayout = (RelativeLayout)convertView.findViewById(R.id.backlayout);
			holder.pic = (CircleImageView)convertView.findViewById(R.id.pic);
			holder.item = (TextView)convertView.findViewById(R.id.place);
			SetLayoutParams.setRelativeLayout(holder.backlayout,MyApplication.width *2/7,MyApplication.width *2/7);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item.setText(list.get(position).getScenic_spot_name());
		ImageLoader.getInstance().displayImage(list.get(position).getPicture_url(),holder.pic);
		return convertView;
	}
	class ViewHolder {
		RelativeLayout backlayout;
		TextView item;
		CircleImageView pic;
	}
}

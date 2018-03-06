package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.TravelBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SystemUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class  	DomesticTravelPlaceAdapter extends BaseAdapter{
	private List<TravelBean> list;
	private Context context;
	private int width = 0;
	private int height = 0;
	public DomesticTravelPlaceAdapter(List<TravelBean> list, Context context) {
		this.list = list;
		this.context = context;
	}


	public DomesticTravelPlaceAdapter(List<TravelBean> list, Context context,int width,int height) {
		this.list = list;
		this.context = context;
		this.width = width;
		this.height = height;
	}
	public void setList(List<TravelBean> list) {
		if (list != null) {
			this.list = (List<TravelBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.domestic_travel_place_item, parent, false);
			holder.backlayout = (RelativeLayout)convertView.findViewById(R.id.backlayout);
			holder.pic = (SimpleDraweeView) convertView.findViewById(R.id.pic);
			if (width != 0){
				SetLayoutParams.setLinearLayout(holder.backlayout, width,height);
			}else{
				SetLayoutParams.setLinearLayout(holder.backlayout, (MyApplication.width- SystemUtil.dip2px(40))/3,(MyApplication.width- SystemUtil.dip2px(40))/3);
			}
			holder.place = (TextView)convertView.findViewById(R.id.place);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Uri uri = Uri.parse(list.get(position).getCompress_picture());
		holder.pic.setImageURI(uri);
		holder.place.setText(list.get(position).getProduct_name());
		holder.place.getBackground().setAlpha(120);
		return convertView;
	}
	class ViewHolder {
		SimpleDraweeView pic;
		RelativeLayout backlayout;
		TextView place;
	}
}

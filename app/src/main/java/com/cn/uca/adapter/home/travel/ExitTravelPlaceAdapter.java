package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.ExitTravelPlaceBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SystemUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class ExitTravelPlaceAdapter extends BaseAdapter{
	private List<ExitTravelPlaceBean> list;
	private Context context;
	private int height,width;

	public ExitTravelPlaceAdapter(){}
	public ExitTravelPlaceAdapter(List<ExitTravelPlaceBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public ExitTravelPlaceAdapter(List<ExitTravelPlaceBean> list, Context context, int height, int width) {
		this.list = list;
		this.context = context;
		this.height = height;
		this.width = width;
	}
	public void setList(List<ExitTravelPlaceBean> list) {
		if (list != null) {
			this.list = (List<ExitTravelPlaceBean>) list;
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
			holder.pic = (SimpleDraweeView) convertView.findViewById(R.id.pic);
			SetLayoutParams.setRelativeLayout(holder.pic, (MyApplication.width- SystemUtil.dip2px(40))/3,(MyApplication.width- SystemUtil.dip2px(40))/3);
			holder.place = (TextView)convertView.findViewById(R.id.place);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Uri uri = Uri.parse(list.get(position).getPicture_url());
		holder.pic.setImageURI(uri);
		holder.place.setText(list.get(position).getHot_spot_name());
		holder.place.getBackground().setAlpha(120);
		return convertView;
	}
	class ViewHolder {
		SimpleDraweeView pic;
		TextView place;
	}
}

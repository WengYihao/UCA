package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.CollectionBean;
import com.cn.uca.bean.home.samecityka.SameCityKaBean;
import com.cn.uca.view.FluidLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 夺宝城市适配器
 */
public class CollectionAdapter extends BaseAdapter{
	private List<CollectionBean> list;
	private Context context;

	public CollectionAdapter(List<CollectionBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<CollectionBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.samecityka_collection_item, parent, false);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.state = (TextView)convertView.findViewById(R.id.state);
			holder.place = (TextView)convertView.findViewById(R.id.place);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getCover_url(),holder.pic);
		holder.name.setText(list.get(position).getTitle());
		switch (list.get(position).getActivity_type_id()){
			case 1:
				holder.place.setText("线上活动");
				break;
			case 2:
				holder.place.setText(list.get(position).getPosition().getAddress());
				break;
		}
		holder.time.setText(list.get(position).getBeg_time());
		if (list.get(position).isCharge()){
			holder.price.setText("收费");
		}else{
			holder.price.setText("免费");
		}
		switch (list.get(position).getState()){
			case "Enrolment":
				holder.state.setText("报名中");
				break;
			case "Conduct":
				holder.state.setText("进行中");
				break;
			case "End":
				holder.state.setText("已结束");
				break;
		}
		return convertView;
	}
	class ViewHolder {
		ImageView pic;
		TextView name,state,place,time,price;
	}
}

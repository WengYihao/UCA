package com.cn.uca.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.user.CollectionBean;
import com.cn.uca.view.RatingStarView;

import java.util.List;

/**
 * 我的收藏
 */
public class CollectionAdapter extends BaseAdapter{
	private List<CollectionBean> list;
	private Context context;

	public CollectionAdapter(){}
	public CollectionAdapter(List<CollectionBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<CollectionBean> list) {
		if (list != null) {
			this.list = (List<CollectionBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.collection_item, parent, false);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.type = (TextView)convertView.findViewById(R.id.type);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.start = (RatingStarView)convertView.findViewById(R.id.start);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getName());
		holder.type.setText(list.get(position).getType());
		holder.start.setRating(list.get(position).getStart());
		holder.price.setText("￥"+list.get(position).getPrice());
		return convertView;
	}
	class ViewHolder {
		ImageView pic;
		TextView name,type,price;
		RatingStarView start;
	}
}

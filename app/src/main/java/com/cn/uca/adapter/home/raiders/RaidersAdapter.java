package com.cn.uca.adapter.home.raiders;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 夺宝城市适配器
 */
public class RaidersAdapter extends BaseAdapter{
	private List<RaidersBean> list;
	private Context context;
	private int TypeOne = 0;// 注意这个不同布局的类型起始值必须从0开始
	private int TypeTwo = 1;
	private CollectionClickListener listener;

	public RaidersAdapter(List<RaidersBean> list, Context context,CollectionClickListener listener) {
		this.list = list;
		this.context = context;
		this.listener = listener;
	}

	public void setList(List<RaidersBean> list) {
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
	public int getItemViewType(int position) {
		if (list.get(position).isLock()){
			return TypeOne;
		}else{
			return TypeTwo;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type){
				case 0:
					holder = new ViewHolder();
					convertView = LayoutInflater.from(context).inflate(R.layout.raiders_lock_item, parent, false);
					holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
					holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
					holder.name = (TextView)convertView.findViewById(R.id.name);
					holder.collection = (TextView)convertView.findViewById(R.id.collection);
					convertView.setTag(holder);
					break;
				case 1:
					holder2 = new ViewHolder2();
					convertView = LayoutInflater.from(context).inflate(R.layout.raiders_unlock_item, parent, false);
					holder2.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
					holder2.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
					holder2.name = (TextView)convertView.findViewById(R.id.name);
					holder2.collection = (TextView)convertView.findViewById(R.id.collection);
					convertView.setTag(holder2);
					break;
			}
		}else{
			switch (type){
				case 0:
					holder = (ViewHolder) convertView.getTag();
					break;
				case 1:
					holder2 = (ViewHolder2) convertView.getTag();
					break;
			}
		}
		switch (type) {
			case 0:
				holder.name.setText(list.get(position).getCity_name());
				Uri uri = Uri.parse(list.get(position).getPacture_url());
				holder.pic.setImageURI(uri);
				holder.layout.getBackground().setAlpha(120);
				holder.collection.setTag(position);
				holder.collection.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						listener.onCollectionClick(v);
					}
				});
				if (list.get(position).isCollection()) {
					holder.collection.setBackgroundResource(R.mipmap.collection_white);
				} else {
					holder.collection.setBackgroundResource(R.mipmap.nocollection_white);
				}
				break;
			case 1:
				holder2.name.setText(list.get(position).getCity_name());
				if (list.get(position).isCollection()){
					holder2.collection.setBackgroundResource(R.mipmap.collection_white);
				}else{
					holder2.collection.setBackgroundResource(R.mipmap.nocollection_white);
				}
				Uri uri2 = Uri.parse(list.get(position).getPacture_url());
				holder2.pic.setImageURI(uri2);
				holder2.layout.getBackground().setAlpha(120);
				break;
		}
		return convertView;
	}


	class ViewHolder {
		RelativeLayout layout;
		SimpleDraweeView pic;
		TextView name,collection;
	}
	class ViewHolder2{
		SimpleDraweeView pic;
		RelativeLayout layout;
		TextView name,collection;
	}
}

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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.raiders_lock_item, parent, false);
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
			holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.collection = (TextView)convertView.findViewById(R.id.collection);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
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
		if (list.get(position).isCollection()){
			holder.collection.setBackgroundResource(R.mipmap.collection_white);
		}else{
			holder.collection.setBackgroundResource(R.mipmap.nocollection);
		}
		return convertView;
	}

	class ViewHolder {
		RelativeLayout layout;
		SimpleDraweeView pic;
		TextView name,collection;
	}
}

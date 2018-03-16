package com.cn.uca.adapter.yueka;

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
import com.cn.uca.bean.yueka.CollectionBean;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.view.CircleImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 夺宝城市适配器
 */
public class YuekaCollectionAdapter extends BaseAdapter{
	private List<CollectionBean> list;
	private Context context;
	private CollectionClickListener listener;

	public YuekaCollectionAdapter(List<CollectionBean> list, Context context, CollectionClickListener listener) {
		this.list = list;
		this.context = context;
		this.listener = listener;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.yueka_collection_item, parent, false);
			holder.pic = (CircleImageView) convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.place = (TextView)convertView.findViewById(R.id.place);
			holder.state = (TextView)convertView.findViewById(R.id.state);
			holder.cancel = (TextView)convertView.findViewById(R.id.cancel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getUser_nick_name());
		ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait_path(),holder.pic);
		holder.place.setText(list.get(position).getBeg_city_name());
		switch (list.get(position).getEscort_record_state_id()){
			case 1:
				holder.state.setText(list.get(position).getRelease_time()+"发布了新的约咖");
				break;
			case 2:

				break;
			case 3:

				break;
			case 4:
				holder.state.setText("当前约咖已过期");
				break;
			case 5:

				break;
			case 6:

				break;
		}
		holder.cancel.setTag(position);
		holder.cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onCollectionClick(v);
			}
		});
		return convertView;
	}
	class ViewHolder {
		CircleImageView pic;
		TextView name,place,state,cancel;
	}
}

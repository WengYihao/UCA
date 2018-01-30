package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.AroundPlayBean;
import com.cn.uca.view.CircleImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class AroundPlayAdapter extends BaseAdapter{
	private List<AroundPlayBean> list;
	private Context context;
	public AroundPlayAdapter(List<AroundPlayBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<AroundPlayBean> list) {
		if (list != null) {
			this.list = (List<AroundPlayBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.around_play_item, parent, false);
			holder.pic = (CircleImageView) convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.num = (TextView)convertView.findViewById(R.id.num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getPicture_url(),holder.pic);
		holder.name.setText(list.get(position).getScenic_spot_name());
		holder.num.setText(position+1+"");
		return convertView;
	}
	class ViewHolder {
		CircleImageView pic;
		TextView name,num;
	}
}

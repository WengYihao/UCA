package com.cn.uca.adapter.home.raiders;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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
public class RaidersCollectionAdapter extends BaseAdapter{
	private List<RaidersBean> list;
	private Context context;

	public RaidersCollectionAdapter(List<RaidersBean> list, Context context) {
		this.list = list;
		this.context = context;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.raider_collection_item, parent, false);
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
			holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.size = (TextView)convertView.findViewById(R.id.size);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getCity_name());
		Uri uri = Uri.parse(list.get(position).getPacture_url());
		holder.pic.setImageURI(uri);
		holder.layout.getBackground().setAlpha(120);
		float a = (float)list.get(position).getFile_resources_size()/1024/1024;
		int index = (a+"").indexOf(".");
		Log.i("123",a+"---");
		holder.size.setText((a+"").substring(0,index+2)+"m");
		return convertView;
	}
	class ViewHolder {
		RelativeLayout layout;
		SimpleDraweeView pic;
		TextView name,size;
	}
}

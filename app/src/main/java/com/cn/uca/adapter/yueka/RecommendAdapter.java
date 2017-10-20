package com.cn.uca.adapter.yueka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.RecommendBean;

import java.util.List;

/**
 * 相似推荐适配器
 */

public class RecommendAdapter extends BaseAdapter{
	private List<RecommendBean> list;
	private Context context;

	public RecommendAdapter(){}
	public RecommendAdapter(List<RecommendBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<RecommendBean> list) {
		if (list != null) {
			this.list = (List<RecommendBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.recommend_item, parent, false);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.place = (TextView)convertView.findViewById(R.id.place);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.price.setText(list.get(position).getPrice());
		holder.place.setText(list.get(position).getPlace());
		holder.title.setText(list.get(position).getTitle());
		return convertView;
	}
	class ViewHolder {
		TextView price,place,title;
	}
}

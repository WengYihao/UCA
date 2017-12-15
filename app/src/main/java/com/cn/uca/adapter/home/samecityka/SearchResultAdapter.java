package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.cn.uca.R;

import java.util.List;

/**
 * 夺宝城市适配器
 */
public class SearchResultAdapter extends BaseAdapter{
	private List<PoiItem> list;
	private Context context;

	public SearchResultAdapter(List<PoiItem> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<PoiItem> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.address = (TextView)convertView.findViewById(R.id.address);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getTitle());
		holder.address.setText(list.get(position).getSnippet());
		return convertView;
	}
	class ViewHolder {
		TextView name,address;
	}
}

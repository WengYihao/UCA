package com.cn.uca.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.YueKaLineBean;
import com.cn.uca.impl.yueka.ItemClickListener;
import com.cn.uca.util.SetListView;
import com.cn.uca.view.MyEditText;
import com.cn.uca.view.MyListView;

import java.util.List;

/**
 * 路线预设
 */

public class EditLineAdapter extends BaseAdapter implements View.OnClickListener{
	private List<YueKaLineBean> list;
	private Context context;
	private ItemClickListener itemClickListener;

	public EditLineAdapter(){}
	public EditLineAdapter(List<YueKaLineBean> list, Context context,ItemClickListener itemClickListener) {
		this.list = list;
		this.context = context;
		this.itemClickListener = itemClickListener;
	}
	public void setList(List<YueKaLineBean> list) {
		if (list != null) {
			this.list = (List<YueKaLineBean>) list;
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.route_item, parent, false);
			holder.arrowView = (TextView) convertView.findViewById(R.id.arrowView);
			holder.trueLine = (TextView) convertView.findViewById(R.id.trueLine);
			holder.editLine = (TextView)convertView.findViewById(R.id.editLine);
			holder.lineName = (MyEditText)convertView.findViewById(R.id.lineName);
			holder.listView = (MyListView)convertView.findViewById(R.id.listView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.lineName.setText(list.get(position).getRoute_name());
//		SetListView.setListViewHeightBasedOnChildren(holder.listView);
		holder.arrowView.setOnClickListener(this);
		holder.trueLine.setOnClickListener(this);
		holder.editLine.setOnClickListener(this);
		holder.arrowView.setTag(position);
		holder.trueLine.setTag(position);
		holder.editLine.setTag(position);
		if (list.get(position).getPlaces() != null){
			Log.i("123",position+"-1");
			if (list.get(position).getPlaces().size() != 0){
				Log.i("123",position+"-2");
				holder.arrowView.setBackgroundResource(R.mipmap.down);
				AllLineAdapter lineAdapter = new AllLineAdapter(list.get(position).getPlaces(),context);
                holder.listView.setVisibility(View.VISIBLE);
				holder.listView.setAdapter(lineAdapter);
				SetListView.setListViewHeightBasedOnChildren(holder.listView);
			}else{
				Log.i("123",position+"-3");
				holder.arrowView.setBackgroundResource(R.mipmap.right_gray);
                holder.listView.setVisibility(View.GONE);
			}
		}else{
			Log.i("123",position+"-4");
			holder.arrowView.setBackgroundResource(R.mipmap.right_gray);
            holder.listView.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.arrowView:
				itemClickListener.clickShow(view);
				break;
			case R.id.trueLine:
				itemClickListener.clickTrue(view);
				break;
			case R.id.editLine:
				itemClickListener.clickEdit(view);
				break;
		}
	}

	class ViewHolder {
		MyEditText lineName;
		TextView arrowView,trueLine,editLine;
		MyListView listView;
	}
}

package com.cn.uca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.CommentBean;

import java.util.List;


public class CommentAdapter extends BaseAdapter{
	private List<CommentBean> list;
	private Context context;

	public CommentAdapter(){}
	public CommentAdapter(List<CommentBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<CommentBean> list) {
		if (list != null) {
			this.list = (List<CommentBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
			holder.text = (TextView)convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setText(list.get(position).getText());
		return convertView;
	}
	class ViewHolder {
		TextView text;
	}
}

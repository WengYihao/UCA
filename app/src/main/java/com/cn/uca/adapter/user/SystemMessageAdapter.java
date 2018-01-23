package com.cn.uca.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.user.message.MessageBean;

import java.util.List;

/**
 * 系统消息
 */
public class SystemMessageAdapter extends BaseAdapter{
	private List<MessageBean> list;
	private Context context;

	public SystemMessageAdapter(){}
	public SystemMessageAdapter(List<MessageBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<MessageBean> list) {
		if (list != null) {
			this.list = (List<MessageBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.system_message_item, parent, false);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.content = (TextView)convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.time.setText(list.get(position).getSystem_push_time());
		holder.name.setText(list.get(position).getTitle());
		holder.content.setText(list.get(position).getContent());
		return convertView;
	}
	class ViewHolder {
		TextView time,name,content;
	}
}

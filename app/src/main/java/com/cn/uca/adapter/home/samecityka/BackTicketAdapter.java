package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.BackTicketBean;
import com.cn.uca.impl.samecityka.ExamineItemBack;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 报名审核
 */
public class BackTicketAdapter extends BaseAdapter{
	private List<BackTicketBean> list;
	private Context context;
	private ExamineItemBack  itemBack;

	public BackTicketAdapter(List<BackTicketBean> list, Context context, ExamineItemBack itemBack) {
		this.list = list;
		this.context = context;
		this.itemBack = itemBack;
	}
	public void setList(List<BackTicketBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.samecityka_backticket_item, parent, false);
			holder.pic = (CircleImageView) convertView.findViewById(R.id.pic);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.ticket = (TextView)convertView.findViewById(R.id.ticket);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.actionname = (TextView)convertView.findViewById(R.id.actionname);
			holder.reason = (TextView)convertView.findViewById(R.id.reason);
			holder.title01 = (TextView)convertView.findViewById(R.id.title01);
			holder.title02 = (TextView)convertView.findViewById(R.id.title02);
			holder.title01.setTag(position);
			holder.title02.setTag(position);
			holder.title01.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemBack.refuse(v);//拒绝
				}
			});
			holder.title02.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemBack.adopt(v);//通过
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait(),holder.pic);
		holder.time.setText(list.get(position).getCreate_time());
		holder.actionname.setText(list.get(position).getTitle());
		holder.reason.setText(list.get(position).getReason());
		holder.ticket.setText("【"+list.get(position).getTickets().get(0).getTicket_name()+"】"+"x"+list.get(position).getTickets().get(0).getNumber());
		return convertView;
	}
	class ViewHolder {
		CircleImageView pic;
		TextView time,title,ticket,actionname,reason,title01,title02;
	}
}

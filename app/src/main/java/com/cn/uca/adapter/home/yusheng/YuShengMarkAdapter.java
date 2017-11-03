package com.cn.uca.adapter.home.yusheng;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.travel.CitySpotTicketBean;
import com.cn.uca.bean.home.yusheng.LifeHistoricalsBean;
import com.cn.uca.bean.home.yusheng.YushengMarkBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.view.RatingStarView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class YuShengMarkAdapter extends BaseAdapter{
	private List<LifeHistoricalsBean> list;
	private Context context;

	public YuShengMarkAdapter(){}
	public YuShengMarkAdapter(List<LifeHistoricalsBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<LifeHistoricalsBean> list) {
		if (list != null) {
			this.list = (List<LifeHistoricalsBean>) list;
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
		final int height;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.mark_item, parent, false);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
			holder.num = (TextView)convertView.findViewById(R.id.num);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.content = (TextView)convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SetLayoutParams.setAbsListView(holder.layout, MyApplication.width,MyApplication.height/4);
		switch (list.get(position).getType()){
			case "day":
				holder.layout.setBackgroundResource(R.drawable.twenty_circular_gray_background);
				holder.num.setText("余"+list.get(position).getRemaining_time()+"天");
				break;
			case "systemDay":
				holder.layout.setBackgroundResource(R.drawable.twenty_circular_green_background);
				holder.num.setText("系统记事");
				break;
			case "month":
				holder.layout.setBackgroundResource(R.drawable.twenty_circular_ori_background);
				holder.num.setText("余"+list.get(position).getRemaining_time()+"月");
				break;
		}
		holder.time.setText(list.get(position).getDate());
		holder.content.setText(list.get(position).getContent());
		return convertView;
	}
	class ViewHolder {
		LinearLayout layout;
		TextView num,time,content;
	}
}

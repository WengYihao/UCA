package com.cn.uca.adapter.home.yusheng;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.yusheng.LifeDaysBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;

import java.util.List;

/**
 * 余生天数
 */
public class YuShengDayAdapter extends BaseAdapter{
	private int nowDay;
	private Context context;
	private List<LifeDaysBean> list;

	public YuShengDayAdapter(Context context, List<LifeDaysBean> list,int nowDay) {
		this.context = context;
		this.list = list;
		this.nowDay = nowDay;
	}

	public void setList(List<LifeDaysBean> list,int nowDay) {
		if (list != null) {
			this.list =  list;
			this.nowDay = nowDay;
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
	public View getView(int position, View convertView,ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.otherday_item,parent,false);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
			holder.day = (TextView)convertView.findViewById(R.id.day);
			holder.text = (TextView)convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).getDay() == 0){
			holder.day.setText("");
			holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
		}else{
			if (list.get(position).getDay() > nowDay){
				//过去
				holder.day.setText(list.get(position).getDay()+"");
				holder.layout.setBackgroundColor(context.getResources().getColor(R.color.yusheng1));
				holder.day.setTextColor(context.getResources().getColor(R.color.yusheng2));
				holder.text.setTextColor(context.getResources().getColor(R.color.yusheng2));
			}else{
				//未来
				holder.day.setText(list.get(position).getDay()+"");
				holder.layout.setBackgroundColor(context.getResources().getColor(R.color.yusheng3));
				holder.day.setTextColor(context.getResources().getColor(R.color.yusheng4));
				holder.text.setTextColor(context.getResources().getColor(R.color.yusheng4));
			}
		}
		SetLayoutParams.setAbsListView(holder.layout, (MyApplication.width-6)/7,MyApplication.width/7*27/20);
		return convertView;
	}

	class ViewHolder{
		RelativeLayout layout;
		TextView day,text;
	}
}
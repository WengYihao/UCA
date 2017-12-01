package com.cn.uca.adapter.yueka;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.BackImageBean;
import com.cn.uca.bean.yueka.SendEscortBean;
import com.cn.uca.config.MyConfig;
import com.cn.uca.impl.yueka.BackImageListener;
import com.cn.uca.util.L;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.view.CircleImageView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 我的发布路线点
 */
public class SendListAdapter extends BaseAdapter{
	private List<SendEscortBean> list;
	private Context context;

	public SendListAdapter(){}
	public SendListAdapter(List<SendEscortBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<SendEscortBean> list) {
		if (list != null) {
			this.list = (List<SendEscortBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.send_list_item, parent, false);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.place = (TextView)convertView.findViewById(R.id.place);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.sum = (TextView)convertView.findViewById(R.id.sum);
			holder.a = (TextView)convertView.findViewById(R.id.a);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getEscort_record_name());
		holder.place.setText(list.get(position).getBeg_city_name());
		holder.time.setText(list.get(position).getBeg_time()+"~"+list.get(position).getEnd_time());

		switch (list.get(position).getEscort_record_state_id()){
			case 1:
				if (list.get(position).getInitial_number() > 0){
					holder.sum.setText(list.get(position).getInitial_number()+"");
					holder.sum.setBackgroundResource(R.drawable.circular_ori_background);
				}else{
					holder.sum.setText("等待响应");
					SetLayoutParams.setRelativeLayout(holder.sum,0,0);
					holder.sum.setTextColor(context.getResources().getColor(R.color.milky));
				}
				break;
			case 2:
				holder.sum.setText("已响应");
				SetLayoutParams.setRelativeLayout(holder.sum,0,0);
				holder.sum.setTextColor(context.getResources().getColor(R.color.milky));
				break;
			case 3:
				holder.sum.setText("完成");
				SetLayoutParams.setRelativeLayout(holder.sum,0,0);
				holder.sum.setTextColor(context.getResources().getColor(R.color.milky));
				break;
			case 4:
				holder.sum.setText("过期");
				SetLayoutParams.setRelativeLayout(holder.sum,0,0);
				holder.sum.setTextColor(context.getResources().getColor(R.color.milky));
				break;
			case 5:
				holder.sum.setText("等待用户付款");
				SetLayoutParams.setRelativeLayout(holder.sum,0,0);
				holder.sum.setTextColor(context.getResources().getColor(R.color.milky));
				break;
			case 6:
				holder.sum.setText("已付款");
				SetLayoutParams.setRelativeLayout(holder.sum,0,0);
				holder.sum.setTextColor(context.getResources().getColor(R.color.milky));
				break;
			case 7:
				holder.sum.setText("已退单");
				SetLayoutParams.setRelativeLayout(holder.sum,0,0);
				holder.sum.setTextColor(context.getResources().getColor(R.color.milky));
				break;
			case 8:
				holder.sum.setText("等待审批退单");
				SetLayoutParams.setRelativeLayout(holder.sum,0,0);
				holder.sum.setTextColor(context.getResources().getColor(R.color.milky));
				break;
		}
		return convertView;
	}

	class ViewHolder {
		TextView name,place,time,sum,a;
	}
}

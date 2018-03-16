package com.cn.uca.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.AcceptOrderBean;
import com.cn.uca.impl.yueka.BackOrderCallBack;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 我的订单
 */
public class AcceptOrderAdapter extends BaseAdapter{
	private List<AcceptOrderBean> list;
	private Context context;
	private BackOrderCallBack callBack;

	public AcceptOrderAdapter(){}
	public AcceptOrderAdapter(List<AcceptOrderBean> list, Context context,BackOrderCallBack callBack) {
		this.list = list;
		this.context = context;
		this.callBack = callBack;
	}
	public void setList(List<AcceptOrderBean> list) {
		if (list != null) {
			this.list = (List<AcceptOrderBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.accept_order_item, parent, false);
			holder.pic = (CircleImageView) convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.state = (TextView)convertView.findViewById(R.id.state);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.server = (TextView)convertView.findViewById(R.id.server);
			holder.back = (TextView)convertView.findViewById(R.id.back);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait(),holder.pic);
		holder.name.setText(list.get(position).getUser_nick_name());
		holder.title.setText(list.get(position).getEscort_record_name());
		holder.price.setText("￥"+(int)list.get(position).getPrice());
		try{
			holder.time.setText("时段:"+ SystemUtil.UtilDateToString2(SystemUtil.StringToUtilDate(list.get(position).getBeg_time()))+"~"+SystemUtil.UtilDateToString2(SystemUtil.StringToUtilDate(list.get(position).getEnd_time())));
		}catch (Exception e){
		}
		holder.server.setText("暂无服务");
		switch (list.get(position).getEscort_record_state_id()){
			case 1:
				holder.state.setText("等待响应");
				holder.back.setVisibility(View.GONE);
				break;
			case 2:
				holder.state.setText("已响应");
				holder.back.setVisibility(View.GONE);
				break;
			case 3:
				holder.state.setText("已完成");
				holder.back.setVisibility(View.GONE);
				break;
			case 4:
				holder.state.setText("已过期");
				holder.back.setVisibility(View.GONE);
				break;
			case 5:
				holder.state.setText("待付款");
				holder.back.setVisibility(View.GONE);
				break;
			case 6:
				holder.state.setText("已付款");
				holder.back.setTag(position);
				holder.back.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						callBack.onBack(v);
					}
				});
				break;
			case 7:
				holder.state.setText("已退单");
				holder.back.setVisibility(View.GONE);
				break;
			case 8:
				holder.state.setText("待审批退单");
				holder.back.setTag(position);
				holder.back.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						callBack.onBack(v);
					}
				});
				break;

		}
		return convertView;
	}
	class ViewHolder {
		CircleImageView pic;
		TextView name,state,title,price,time,server,back;
	}
}

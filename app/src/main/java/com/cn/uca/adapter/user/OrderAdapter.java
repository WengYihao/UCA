package com.cn.uca.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.user.OrderBean;
import com.cn.uca.impl.yueka.TypeClickCallBack;
import com.cn.uca.util.ToastXutil;

import java.util.List;

/**
 * 我的订单
 */
public class OrderAdapter extends BaseAdapter{
	private List<OrderBean> list;
	private Context context;
	private TypeClickCallBack callBack;

	public OrderAdapter(){}
	public OrderAdapter(List<OrderBean> list, Context context,TypeClickCallBack callBack) {
		this.list = list;
		this.context = context;
		this.callBack = callBack;
	}
	public void setList(List<OrderBean> list) {
		if (list != null) {
			this.list = (List<OrderBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
			holder.pic = (TextView)convertView.findViewById(R.id.pic);
			holder.type = (TextView)convertView.findViewById(R.id.type);
			holder.state = (TextView)convertView.findViewById(R.id.state);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.place = (TextView)convertView.findViewById(R.id.place);
			holder.back = (TextView)convertView.findViewById(R.id.back);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		switch (list.get(position).getCommodity_id()){
            case 1:
                holder.pic.setBackgroundResource(R.mipmap.yueka_state_back);
                holder.type.setText("约咖");
                break;
            case 2:
                holder.pic.setBackgroundResource(R.mipmap.travel_state_back);
                holder.type.setText("旅行");
                break;
            case 3:
                holder.pic.setBackgroundResource(R.mipmap.hotel_state_back);
                holder.type.setText("酒店");
                break;
            case 4:
                holder.pic.setBackgroundResource(R.mipmap.plant_state_back);
                holder.type.setText("机票");
                break;
			case 6:
				holder.pic.setBackgroundResource(R.mipmap.samecityka_state_back);
				holder.type.setText("同城咖");
				break;
			case 7:
				holder.pic.setBackgroundResource(R.mipmap.lvpai_state_back);
				holder.type.setText("旅拍");
				break;
        }
        switch (list.get(position).getUser_order_state_id()){
            case 1:
                holder.state.setText("已支付");
				if (list.get(position).getCommodity_id() == 1){
					holder.back.setText("退单");
					holder.back.setTag(position);
					holder.back.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							callBack.onClick(1,v);
						}
					});
				}else{
					holder.back.setVisibility(View.GONE);
				}
                break;
            case 2:
                holder.state.setText("待支付");
				holder.back.setText("");
				holder.back.setBackgroundResource(R.mipmap.settlement_back);
				holder.back.setTag(position);
				holder.back.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						callBack.onClick(2,v);
					}
				});
                break;
            case 3:
                holder.state.setText("已失效");
				holder.back.setVisibility(View.GONE);
                break;
            case 4:
                holder.state.setText("待确认");
				holder.back.setVisibility(View.GONE);
                break;
            case 5:
                holder.state.setText("已取消");
				holder.back.setVisibility(View.GONE);
                break;
            case 6:
                holder.state.setText("退单");
				holder.back.setVisibility(View.GONE);
                break;
        }
		holder.name.setText(list.get(position).getMerchandise_order_title());
		holder.price.setText("￥"+(int)list.get(position).getOrder_price());
		holder.time.setText(list.get(position).getCreate_order_time());
		holder.place.setText(list.get(position).getPublisher_name());

		return convertView;
	}
	class ViewHolder {
		TextView pic,type,state,name,price,time,place,back;
	}
}

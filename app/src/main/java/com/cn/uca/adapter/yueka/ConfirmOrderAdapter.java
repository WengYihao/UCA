package com.cn.uca.adapter.yueka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.CommentBean;
import com.cn.uca.bean.yueka.ConfirmOrderBean;
import com.cn.uca.impl.yueka.ConfirmOrderback;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 领咖确认订单
 */
public class ConfirmOrderAdapter extends BaseAdapter implements ConfirmOrderback{
	private List<ConfirmOrderBean> list;
	private Context context;
	private ConfirmOrderback orderback;

	public ConfirmOrderAdapter(){}
	public ConfirmOrderAdapter(List<ConfirmOrderBean> list, Context context,ConfirmOrderback orderback) {
		this.list = list;
		this.context = context;
		this.orderback = orderback;
	}
	public void setList(List<ConfirmOrderBean> list) {
		if (list != null) {
			this.list = (List<ConfirmOrderBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.confirm_order_item, parent, false);
			holder.pic = (CircleImageView)convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.chat = (TextView)convertView.findViewById(R.id.chat);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.server = (TextView)convertView.findViewById(R.id.server);
			holder.num = (TextView)convertView.findViewById(R.id.num);
			holder.back = (TextView)convertView.findViewById(R.id.back);
			holder.confirm = (TextView)convertView.findViewById(R.id.confirm);
			switch (list.get(position).getEscort_user_state_id()){
				case 3:
					holder.back.setTag(position);
					holder.confirm.setTag(position);
					holder.back.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							orderback.onBack(v);
						}
					});
					holder.confirm.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							orderback.onConfirm(v);
						}
					});
					break;
			}
			holder.chat.setTag(position);
			holder.chat.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					orderback.onChat(v);
				}
			});

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait(),holder.pic);
		holder.name.setText(list.get(position).getUser_nick_name());
		holder.price.setText("￥"+(int)list.get(position).getActual_payment()+"");
		try{
			holder.time.setText("时段:"+SystemUtil.UtilDateToString2(SystemUtil.StringToUtilDate(list.get(position).getBeg_time()))+"~"+SystemUtil.UtilDateToString2(SystemUtil.StringToUtilDate(list.get(position).getEnd_time())));
		}catch (Exception e){
		}
		holder.server.setText("服务:暂不需要");
		holder.num.setText("人数:"+list.get(position).getActual_number()+"人");
		return convertView;
	}
	@Override
	public void onBack(View v) {

	}

	@Override
	public void onConfirm(View v) {

	}

	@Override
	public void onChat(View v) {

	}

	class ViewHolder {
		CircleImageView pic;
		TextView name,chat,price,time,server,num,back,confirm;

	}
}

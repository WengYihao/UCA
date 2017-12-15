package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.impl.yusheng.EditItemClick;
import com.cn.uca.util.ToastXutil;

import java.util.List;

/**
 * 约咖详情路线点
 */
public class AddTicketAdapter extends BaseAdapter implements EditItemClick{
	private List<Integer> list;
	private Context context;
	private EditItemClick click;
	public AddTicketAdapter(){}
	public AddTicketAdapter(List<Integer> list, Context context,EditItemClick click) {
		this.list = list;
		this.context = context;
		this.click = click;
	}
	public void setList(List<Integer> list) {
		if (list != null) {
			this.list = (List<Integer>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.add_ticket_item, parent, false);
			holder.delete = (TextView)convertView.findViewById(R.id.delete);
			holder.checkPrice = (CheckBox)convertView.findViewById(R.id.checkPrice);
			holder.checkMax = (CheckBox)convertView.findViewById(R.id.checkPrice);
			holder.checkSum = (CheckBox)convertView.findViewById(R.id.checkSum);
			holder.examine = (Switch)convertView.findViewById(R.id.examine);
			holder.ha = (TextView)convertView.findViewById(R.id.ha);
			holder.icon = (TextView)convertView.findViewById(R.id.icon);
			holder.checkPrice.setTag(position);
			holder.checkPrice.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					click.click(v);
				}
			});
//			if (holder.checkPrice.isChecked()){
//				holder.checkPrice.setBackgroundResource(R.drawable.text_lable_bg);
//			}else{
//				holder.checkPrice.setBackgroundResource(R.drawable.text_lable_gray_bg);
//			}
//			if (holder.checkSum.isChecked()){
//				holder.checkSum.setBackgroundResource(R.drawable.text_lable_bg);
//			}else{
//				holder.checkSum.setBackgroundResource(R.drawable.text_lable_gray_bg);
//			}
//			if (holder.checkMax.isChecked()){
//				holder.checkMax.setBackgroundResource(R.drawable.text_lable_bg);
//			}else{
//				holder.checkMax.setBackgroundResource(R.drawable.text_lable_gray_bg);
//			}
//			holder.examine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					if (isChecked){
//					}else {
//					}
//				}
//			});
//			if (holder.examine.isChecked()){
//
//			}else{
//				holder.icon.setBackgroundResource(R.mipmap.service_notice_gray);
//				holder.ha.setEnabled(false);
//			}
//			holder.delete.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					list.remove(position);
//					notifyDataSetChanged();
//				}
//			});
//			holder.ha.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					ToastXutil.show("等等我");
//				}
//			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	@Override
	public void click(View view) {

	}

	class ViewHolder {
		TextView delete,ha,icon;
		CheckBox checkPrice,checkSum,checkMax;
		Switch examine;
	}
}

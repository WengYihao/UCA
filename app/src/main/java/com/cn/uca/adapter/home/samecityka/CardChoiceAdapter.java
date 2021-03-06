package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.CardBean;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 名片管理
 */
public class CardChoiceAdapter extends BaseAdapter{
	private List<CardBean> list;
	private Context context;

	public CardChoiceAdapter(List<CardBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<CardBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.card_choice_item, parent, false);
			holder.pic = (CircleImageView)convertView.findViewById(R.id.pic);
			holder.type = (TextView)convertView.findViewById(R.id.type);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.check = (TextView)convertView.findViewById(R.id.check);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait(),holder.pic);
		if (list.get(position).isCheck()){
			holder.check.setVisibility(View.VISIBLE);
		}else{
			holder.check.setVisibility(View.GONE);
		}
		holder.type.setText("【"+list.get(position).getUser_card_type_name()+"】");
		holder.name.setText(list.get(position).getUser_card_name());

		return convertView;
	}
	class ViewHolder {
		CircleImageView pic;
		TextView type,name,check;
	}
}

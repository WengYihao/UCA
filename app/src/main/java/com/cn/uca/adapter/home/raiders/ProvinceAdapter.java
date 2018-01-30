package com.cn.uca.adapter.home.raiders;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.raider.ProvinceBean;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 夺宝城市适配器
 */
public class ProvinceAdapter extends BaseAdapter{
	private List<ProvinceBean> list;
	private Context context;

	public ProvinceAdapter(List<ProvinceBean> list, Context context) {
		this.list = list;
		this.context = context;
	}

	public void setList(List<ProvinceBean> list) {
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
		int type = getItemViewType(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.raider_province_item, parent, false);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			holder.num = (TextView)convertView.findViewById(R.id.num);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.num.setText(list.get(position).getRaiders_number()+"");
		holder.name.setText(list.get(position).getProvince_name());
		return convertView;
	}


	class ViewHolder {
		ImageView pic;
		TextView num,name;
	}
}

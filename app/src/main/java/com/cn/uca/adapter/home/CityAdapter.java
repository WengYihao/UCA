package com.cn.uca.adapter.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.CityBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SystemUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 景点门票城市适配器
 */
public class CityAdapter extends BaseAdapter{
	private List<CityBean> list;
	private Context context;

	public CityAdapter(){}
	public CityAdapter(List<CityBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<CityBean> list) {
		if (list != null) {
			this.list = (List<CityBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.city_item, parent, false);
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
			SetLayoutParams.setLinearLayout(holder.layout,(MyApplication.width- SystemUtil.dip2px(60))/2,((MyApplication.width- SystemUtil.dip2px(60))/2)*3/4);
			holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getCity_name());
		holder.name.getBackground().setAlpha(120);
		Uri uri = Uri.parse(list.get(position).getPicture_url());
		holder.pic.setImageURI(uri);
		return convertView;
	}
	class ViewHolder {
		RelativeLayout layout;
		SimpleDraweeView pic;
		TextView name;
	}
}

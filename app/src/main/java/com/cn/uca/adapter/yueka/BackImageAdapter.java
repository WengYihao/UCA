package com.cn.uca.adapter.yueka;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.BackImageBean;
import com.cn.uca.config.MyConfig;
import com.cn.uca.impl.yueka.BackImageListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 约咖详情路线点
 */
public class BackImageAdapter extends BaseAdapter implements BackImageListener{
	private List<BackImageBean> list;
	private Context context;
	private BackImageListener imageListener;

	public BackImageAdapter(){}
	public BackImageAdapter(List<BackImageBean> list, Context context,BackImageListener imageListener1) {
		this.list = list;
		this.context = context;
		this.imageListener = imageListener1;
	}
	public void setList(List<BackImageBean> list) {
		if (list != null) {
			this.list = (List<BackImageBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.back_image_item, parent, false);
			holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
			holder.edit = (TextView)convertView.findViewById(R.id.edit);
			holder.delete = (TextView)convertView.findViewById(R.id.delete);

			holder.delete.setTag(position);
			holder.edit.setTag(position);
			holder.delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					imageListener.deleteClick(view);
				}
			});
			holder.edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					imageListener.editClick(view);
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Uri uri = Uri.parse(list.get(position).getCover_photo_url()+MyConfig.corpPhoto);
		holder.pic.setImageURI(uri);
		return convertView;
	}

	@Override
	public void editClick(View view) {

	}

	@Override
	public void deleteClick(View view) {

	}

	class ViewHolder {
		SimpleDraweeView pic;
		TextView edit,delete;
	}
}

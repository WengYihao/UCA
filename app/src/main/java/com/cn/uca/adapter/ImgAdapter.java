package com.cn.uca.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class ImgAdapter extends BaseAdapter{
	private List<String> list;
	private Context context;
	private int height,width;

	public ImgAdapter(){}
	public ImgAdapter(List<String> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public ImgAdapter(List<String> list, Context context,int height,int width) {
		this.list = list;
		this.context = context;
		this.height = height;
		this.width = width;
	}
	public void setList(List<String> list) {
		if (list != null) {
			this.list = (List<String>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.img_item, parent, false);
			holder.pic = (SimpleDraweeView) convertView.findViewById(R.id.pic);
			if (height != 0 && width != 0){
				LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) holder.pic.getLayoutParams(); //取控件textView当前的布局参数
				linearParams.height = this.height;// 控件的高强制设成20
				linearParams.width = this.width;
				holder.pic.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Uri uri = Uri.parse(list.get(position));
		holder.pic.setImageURI(uri);
		return convertView;
	}
	class ViewHolder {
		SimpleDraweeView pic;
	}
}

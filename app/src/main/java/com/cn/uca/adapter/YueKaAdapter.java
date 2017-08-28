package com.cn.uca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.YueKaBean;
import com.cn.uca.util.SetListView;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.RatingStarView;

import java.util.List;

/**
 * 搜索历史适配器
 */

public class YueKaAdapter extends BaseAdapter{
	private List<YueKaBean> list;
	private Context context;

	public YueKaAdapter(){}
	public YueKaAdapter(List<YueKaBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<YueKaBean> list) {
		if (list != null) {
			this.list = (List<YueKaBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.yueka_item, parent, false);
			holder.pic = (CircleImageView)convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.age = (TextView)convertView.findViewById(R.id.age);
			holder.start = (RatingStarView)convertView.findViewById(R.id.start);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.sum = (TextView)convertView.findViewById(R.id.sum);
			holder.evaluate = (TextView)convertView.findViewById(R.id.evaluate);
			holder.count = (TextView)convertView.findViewById(R.id.count);
			holder.lable = (GridView)convertView.findViewById(R.id.lable);
			holder.img = (GridView)convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getName());
		holder.age.setText(list.get(position).getAge()+"岁");
		holder.start.setRating(list.get(position).getStart());
		holder.price.setText(list.get(position).getPrice());
		holder.count.setText("浏览"+list.get(position).getCount()+"次");
		holder.sum.setText(list.get(position).getSum()+"");
		holder.evaluate.setText(list.get(position).getEvaluate());
		LableAdapter lableAdapter = new LableAdapter(list.get(position).getLable(),context);
		holder.lable.setAdapter(lableAdapter);
		SetListView.setGridViewHeightBasedOnChildren(holder.lable);
		ImgAdapter imgAdapter = new ImgAdapter(list.get(position).getImgList(),context);
		holder.img.setAdapter(imgAdapter);
		SetListView.setGridViewHeightBasedOnChildren(holder.img);
		return convertView;
	}
	class ViewHolder {
		CircleImageView pic;
		TextView name,age,price,sum,evaluate,count;
		GridView lable,img;
		RatingStarView start;
	}
}

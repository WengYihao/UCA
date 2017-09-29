package com.cn.uca.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.ImgAdapter;
import com.cn.uca.bean.CourseEscortBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.view.FluidLayout;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;


public class CourseEscortAdapter extends BaseAdapter{
	private List<CourseEscortBean> list;
	private Context context;

	public CourseEscortAdapter(){}
	public CourseEscortAdapter(List<CourseEscortBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<CourseEscortBean> list) {
		if (list != null) {
			this.list = (List<CourseEscortBean>) list;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
			holder.pic = (FluidLayout)convertView.findViewById(R.id.pic);
			holder.type = (TextView)convertView.findViewById(R.id.type);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.evaluate = (TextView)convertView.findViewById(R.id.evaluate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.pic.removeAllViews();
		holder.pic.setGravity(Gravity.TOP);
		final ArrayList<String> imgUrls = (ArrayList<String>) list.get(position).getPicList();
		for (int i = 0;i<list.get(position).getPicList().size();i++){
			SimpleDraweeView draweeView = new SimpleDraweeView(context);
			draweeView.setId(i);
			Uri uri = Uri.parse(list.get(position).getPicList().get(i));
			draweeView.setImageURI(uri);
			draweeView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					OpenPhoto.imageUrl(context,view.getId(),imgUrls);
				}
			});
			GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources()).
					setRoundingParams(RoundingParams.fromCornersRadius(10)).build();
			draweeView.setHierarchy(hierarchy);
			FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT
			);
			params.setMargins(12, 12, 12, 12);
			holder.pic.addView(draweeView, params);
			FluidLayout.LayoutParams layoutParams =(FluidLayout.LayoutParams) draweeView.getLayoutParams(); //取控件textView当前的布局参数
			layoutParams.height = MyApplication.width/4*3/4;// 控件的高强制设成20
			layoutParams.width = MyApplication.width/4;
			draweeView.setLayoutParams(layoutParams); //使设置好的布局参数应用到控件
		}
		holder.type.setText(list.get(position).getType());
		holder.time.setText(list.get(position).getTime());
		holder.evaluate.setText(list.get(position).getEvaluate());
		return convertView;
	}
	class ViewHolder {
		FluidLayout pic;
		TextView type,time,evaluate;
	}
}

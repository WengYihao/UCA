package com.cn.uca.adapter.yueka;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.EscortRecordsBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.MyConfig;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.RatingStarView;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 约咖信息
 */

public class YueKaAdapter extends BaseAdapter implements View.OnClickListener{
	private List<EscortRecordsBean> list;
	private Context context;
	private CollectionClickListener listener;

	public YueKaAdapter(){}
	public YueKaAdapter(List<EscortRecordsBean> list, Context context,CollectionClickListener listener) {
		this.list = list;
		this.context = context;
		this.listener = listener;
	}
	public void setList(List<EscortRecordsBean> list) {
		if (list != null) {
			this.list = (List<EscortRecordsBean>) list;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.yueka_item, parent, false);
			holder.pic = (CircleImageView)convertView.findViewById(R.id.pic);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.age = (TextView)convertView.findViewById(R.id.age);
			holder.sex = (TextView)convertView.findViewById(R.id.sex);
			holder.click = (TextView)convertView.findViewById(R.id.click);
			holder.start = (RatingStarView)convertView.findViewById(R.id.start);
			holder.startNum = (TextView)convertView.findViewById(R.id.startNum);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.sum = (TextView)convertView.findViewById(R.id.sum);
			holder.evaluate = (TextView)convertView.findViewById(R.id.evaluate);
			holder.count = (TextView)convertView.findViewById(R.id.count);
			holder.fluidLayout = (FluidLayout)convertView.findViewById(R.id.fluidLayout);
			holder.img = (FluidLayout) convertView.findViewById(R.id.img);

			holder.click.setOnClickListener(this);
			holder.click.setTag(position);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait_url(), holder.pic);
			holder.name.setText(list.get(position).getUser_nick_name());
			if (!StringXutil.isEmpty(list.get(position).getUser_birth_date())){
				Date date = SystemUtil.StringToUtilDate(list.get(position).getUser_birth_date());
				holder.age.setText(SystemUtil.getAge(date)+"岁");
			}else {
				holder.age.setVisibility(View.GONE);
			}
			switch (list.get(position).getSex_id()){
				case 1:
					holder.sex.setBackgroundResource(R.mipmap.man);
					break;
				case 2:
					holder.sex.setBackgroundResource(R.mipmap.woman);
					break;
				case 3:
					holder.sex.setVisibility(View.GONE);
					break;
			}
			if (list.get(position).isCollection()){
				holder.click.setBackgroundResource(R.mipmap.collection);
			}else{
				holder.click.setBackgroundResource(R.mipmap.nocollection);
			}
			holder.start.setRating((float)list.get(position).getAverage_score());
			holder.startNum.setText(list.get(position).getAverage_score()+"");
			holder.price.setText("￥"+(int)list.get(position).getMin_consumption()+"--"+(int)list.get(position).getMax_consumption());
			holder.count.setText("浏览"+list.get(position).getBrowse_times()+"次");
			holder.fluidLayout.removeAllViews();
			holder.fluidLayout.setGravity(Gravity.TOP);
			for (int a = 0;a<list.get(position).getEscortLabels().size();a++){
				TextView tv = new TextView(context);
				tv.setText(list.get(position).getEscortLabels().get(a).getEscort_label_name());
				tv.setTextSize(10);
				if (a == 0) {
					tv.setBackgroundResource(R.drawable.text_bg_yellow);
					tv.setTextColor(context.getResources().getColor(R.color.white));
				} else if (a == 1) {
					tv.setBackgroundResource(R.drawable.text_bg_ori);
					tv.setTextColor(context.getResources().getColor(R.color.white));
				} else {
					tv.setBackgroundResource(R.drawable.text_bg);
					tv.setTextColor(context.getResources().getColor(R.color.white));
				}
				FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT
				);
				params.setMargins(12, 12, 12, 12);
				holder.fluidLayout.addView(tv, params);
			}
			holder.img.removeAllViews();
			holder.img.setGravity(Gravity.TOP);
			final ArrayList<String> imgUrls = (ArrayList<String>) list.get(position).getEscort_record_picture_urls();
			for (int b = 0;b < list.get(position).getEscort_record_picture_urls().size();b++){
				if (b >2){
					break;
				}else{
					SimpleDraweeView draweeView = new SimpleDraweeView(context);
					draweeView.setId(b);
					Uri uri = Uri.parse(MyConfig.photo+list.get(position).getEscort_record_picture_urls().get(b));
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
					holder.img.addView(draweeView, params);
					FluidLayout.LayoutParams layoutParams =(FluidLayout.LayoutParams) draweeView.getLayoutParams(); //取控件textView当前的布局参数
					layoutParams.height =MyApplication.width/5*3/4;// 控件的高强制设成20
					layoutParams.width = MyApplication.width/5;
					draweeView.setLayoutParams(layoutParams); //使设置好的布局参数应用到控件
				}
			}
			holder.sum.setText(list.get(position).getComment_number()+"");
			if (!StringXutil.isEmpty(list.get(position).getComment_content())){
				holder.evaluate.setText(list.get(position).getComment_content());
			}else{
				holder.evaluate.setVisibility(View.GONE);
				holder.sum.setPadding(0,0,0,20);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.click:
				listener.onCollectionClick(view);
				break;
		}
	}

	class ViewHolder {
		CircleImageView pic;
		TextView name,age,sex,click,price,startNum,sum,evaluate,count;
		FluidLayout fluidLayout;
		FluidLayout img;
		RatingStarView start;
	}
}

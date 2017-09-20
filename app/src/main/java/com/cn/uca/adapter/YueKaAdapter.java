package com.cn.uca.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.EscortRecordsBean;
import com.cn.uca.bean.yueka.YueKaBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.RatingStarView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.util.Date;
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
			holder.sex = (TextView)convertView.findViewById(R.id.sex);
			holder.click = (TextView)convertView.findViewById(R.id.click);
			holder.start = (RatingStarView)convertView.findViewById(R.id.start);
			holder.startNum = (TextView)convertView.findViewById(R.id.startNum);
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
		for (int i = 0;i < list.get(position).getEscortRecords().size();i++){
			try {
				ImageLoader.getInstance().displayImage(list.get(position).getEscortRecords().get(i).getUser_head_portrait_url(), holder.pic);
				holder.name.setText(list.get(position).getEscortRecords().get(i).getUser_nick_name());
				if (!StringXutil.isEmpty(list.get(position).getEscortRecords().get(i).getUser_birth_date())){
					Date date = SystemUtil.StringToUtilDate(list.get(position).getEscortRecords().get(i).getUser_birth_date());
					holder.age.setText(SystemUtil.getAge(date)+"岁");
				}else {
					holder.age.setText("未知");
				}
				switch (list.get(position).getEscortRecords().get(i).getSex_id()){
					case 1:
//						holder.sex.setBackgroundResource();
						break;
					case 2:
						holder.sex.setBackgroundResource(R.mipmap.woman);
						break;
					case 3:
						holder.sex.setText("保密");
						break;
				}
				if (list.get(position).getEscortRecords().get(i).isCollection()){
					holder.click.setBackgroundResource(R.mipmap.collection);
				}else{
					holder.click.setBackgroundResource(R.mipmap.nocollection);
				}
				holder.start.setRating((float)list.get(position).getEscortRecords().get(i).getAverage_score());
				holder.startNum.setText(list.get(position).getEscortRecords().get(i).getAverage_score()+"");
				holder.price.setText("￥"+(int)list.get(position).getEscortRecords().get(i).getMin_consumption()+"--"+(int)list.get(position).getEscortRecords().get(i).getMax_consumption());
				holder.count.setText("浏览"+list.get(position).getEscortRecords().get(i).getBrowse_times()+"次");
				LableAdapter lableAdapter = new LableAdapter(list.get(position).getEscortRecords().get(i).getEscortLabels(),context);
		        holder.lable.setAdapter(lableAdapter);
				SetListView.setGridViewHeightBasedOnChildren(holder.lable);
				ImgAdapter imgAdapter = new ImgAdapter(list.get(position).getEscortRecords().get(i).getEscort_record_picture_urls(),context, (MyApplication.width*3/10)*3/4,MyApplication.width*3/10);
				holder.img.setAdapter(imgAdapter);
		        SetListView.setGridViewHeightBasedOnChildren(holder.img);
				holder.sum.setText(list.get(position).getEscortRecords().get(i).getComment_number()+10+"");
				if (!StringXutil.isEmpty(list.get(position).getEscortRecords().get(i).getComment_content())){
					holder.evaluate.setText(list.get(position).getEscortRecords().get(i).getComment_content());
				}else{
					holder.evaluate.setText("暂无用户评论");
				}
			} catch (Exception e) {
				Log.i("456",e.getMessage()+"/*/*/*/*/*/*/*");
				e.printStackTrace();
			}
		}
		return convertView;
	}
	class ViewHolder {
		CircleImageView pic;
		TextView name,age,sex,click,price,startNum,sum,evaluate,count;
		GridView lable,img;
		RatingStarView start;
	}
}

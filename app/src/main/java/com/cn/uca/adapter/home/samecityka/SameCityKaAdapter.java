package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.SameCityKaBean;
import com.cn.uca.bean.home.samecityka.SendContentBean;
import com.cn.uca.bean.home.samecityka.SendImgBean;
import com.cn.uca.config.MyConfig;
import com.cn.uca.view.FluidLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 夺宝城市适配器
 */
public class SameCityKaAdapter extends BaseAdapter{
	private List<SameCityKaBean> list;
	private Context context;

	public SameCityKaAdapter(List<SameCityKaBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<SameCityKaBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.samecityka_item, parent, false);
			holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
			holder.lable = (FluidLayout)convertView.findViewById(R.id.lable);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.place = (TextView)convertView.findViewById(R.id.place);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.distance = (TextView)convertView.findViewById(R.id.distance);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Uri uri = Uri.parse(list.get(position).getCover_url());
		holder.pic.setImageURI(uri);
		holder.lable.removeAllViews();
		holder.lable.setGravity(Gravity.TOP);
		for (int a = 0;a<list.get(position).getLabels().size();a++){
			TextView tv = new TextView(context);
			tv.setText(list.get(position).getLabels().get(a).getLable_name());
			tv.setTextSize(10);
			if (a == 0) {
				tv.setBackgroundResource(R.drawable.text_lable_green_bg);
				tv.setTextColor(context.getResources().getColor(R.color.green));
			} else if (a == 1) {
				tv.setBackgroundResource(R.drawable.text_lable_blue_bg);
				tv.setTextColor(context.getResources().getColor(R.color.blue));
			} else {
				tv.setBackgroundResource(R.drawable.text_lable_bg);
				tv.setTextColor(context.getResources().getColor(R.color.ori));
			}
			FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT
			);
			params.setMargins(12, 12,0, 12);
			holder.lable.addView(tv, params);
		}
		holder.title.setText(list.get(position).getTitle());
	    holder.time.setText(list.get(position).getBeg_time()+"~"+list.get(position).getEnd_time());
		if (list.get(position).isCharge()){
			holder.price.setText("收费");
			holder.price.getBackground().setAlpha(120);
		}else{
			holder.price.setText("免费");
			holder.price.getBackground().setAlpha(120);
		}
		if (list.get(position).getPosition() == null){
			holder.place.setText("线上活动");
			holder.distance.setText("");
		}else{
			holder.place.setText(list.get(position).getPosition().getAddress());
			holder.distance.setText("");
		}
		return convertView;
	}
	class ViewHolder {
		SimpleDraweeView pic;
		FluidLayout lable;
		TextView title,place,time,distance,price;
	}
}

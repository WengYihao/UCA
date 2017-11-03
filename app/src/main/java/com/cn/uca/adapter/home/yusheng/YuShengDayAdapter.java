package com.cn.uca.adapter.home.yusheng;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.yusheng.YuShengDayBean;
import com.cn.uca.impl.yueka.ItemClickListener;
import com.cn.uca.impl.yusheng.EditItemClick;
import com.cn.uca.impl.yusheng.YuShengDayImpl;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.util.ToastXutil;

import java.util.List;

/**
 * 余生天数
 */
public class YuShengDayAdapter extends ArrayAdapter<YuShengDayBean> implements YuShengDayImpl,View.OnClickListener,EditItemClick{
	private LayoutInflater layoutInflater;
	private int TypeOne = 0;// 注意这个不同布局的类型起始值必须从0开始
	private int TypeTwo = 1;
	private List<YuShengDayBean> items;
	private Context context;
	private EditItemClick editItemClick;

	public YuShengDayAdapter(Context context, List<YuShengDayBean> items,EditItemClick editItemClick) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
		this.editItemClick = editItemClick;
		layoutInflater = LayoutInflater.from(context);
	}
	@Override
	public View getView(int position, View convertView,ViewGroup parent) {
		ViewHolder holder = null;
		ViewHolder2 holder2 = null;
		YuShengDayBean item = getItem(position);
		boolean isRegular = getItemViewType(position) == 0;
		if (convertView == null) {
			if (isRegular){
				holder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.today_item, parent, false);
				holder.add = (TextView)convertView.findViewById(R.id.add);
				holder.day = (TextView)convertView.findViewById(R.id.day);
				convertView.setTag(holder);
			}else{
				holder2 = new ViewHolder2();
				convertView = layoutInflater.inflate(R.layout.otherday_item,parent,false);
				holder2.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
				holder2.day = (TextView)convertView.findViewById(R.id.day);
				holder2.text = (TextView)convertView.findViewById(R.id.text);
				convertView.setTag(holder2);
			}
		} else {
			if (isRegular) {
				holder = (ViewHolder) convertView.getTag();
			}else{
				holder2 = (ViewHolder2) convertView.getTag();
			}
		}
		if (isRegular) {
			AssetManager mgr = context.getAssets();//得到AssetManager
			Typeface tf=Typeface.createFromAsset(mgr, "fonts/ttf.ttf");//根据路径得到Typeface
			holder.day.setText("余"+item.getDayBean().getLifeDays().get(position).getDay()+"天");
			holder.day.setTypeface(tf);//设置字体
			holder.add.setOnClickListener(this);
			holder.add.setTag(position);
		} else {
			if (item.getDayBean().getLifeDays().get(position).getDay() == 0){
				holder2.day.setText("");
			}else{
				if (item.getDayBean().getLifeDays().get(position).getDay() > item.getDayBean().getNow_days()){
					holder2.day.setText(item.getDayBean().getLifeDays().get(position).getDay()+"");
					holder2.layout.setBackgroundColor(context.getResources().getColor(R.color.yusheng1));
					holder2.day.setTextColor(context.getResources().getColor(R.color.yusheng2));
					holder2.text.setTextColor(context.getResources().getColor(R.color.yusheng2));
				}else{
					holder2.day.setText(item.getDayBean().getLifeDays().get(position).getDay()+"");
					holder2.layout.setBackgroundColor(context.getResources().getColor(R.color.yusheng3));
					holder2.day.setTextColor(context.getResources().getColor(R.color.yusheng4));
					holder2.text.setTextColor(context.getResources().getColor(R.color.yusheng4));
				}

			}
		}
		return convertView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.add:
				editItemClick.click(view);
				break;
		}
	}

	@Override
	public void click(View view) {

	}

	class ViewHolder {
		TextView day,add;
	}
	class ViewHolder2{
		RelativeLayout layout;
		TextView day,text;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		for (int i = 0;i<items.size();i++){
			if (items.get(i).getDayBean().getLifeDays().get(position).getDay() == items.get(i).getDayBean().getNow_days()){
				return TypeOne;
			}else{
				return TypeTwo;
			}
		}
		return 2;
	}

	@Override
	public void appendItems(List<YuShengDayBean> newItems) {
		addAll(newItems);
		notifyDataSetChanged();
	}

	@Override
	public void setItems(List<YuShengDayBean> moreItems) {
		clear();
		appendItems(moreItems);
		notifyDataSetChanged();
	}
}
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
import com.cn.uca.bean.home.yusheng.YuShengMonthBean;
import com.cn.uca.impl.yusheng.EditItemClick;
import com.cn.uca.impl.yusheng.YuShengDayImpl;
import com.cn.uca.impl.yusheng.YuShengMonthImpl;
import com.cn.uca.util.ToastXutil;

import java.util.List;

/**
 * 余生天数
 */
public class YuShengMonthAdapter extends ArrayAdapter<YuShengMonthBean> implements YuShengMonthImpl,View.OnClickListener,EditItemClick{
	private LayoutInflater layoutInflater;
	private int TypeOne = 0;// 注意这个不同布局的类型起始值必须从0开始
	private int TypeTwo = 1;
	private List<YuShengMonthBean> items;
	private Context context;
	private EditItemClick editItemClick;

	public YuShengMonthAdapter(Context context, List<YuShengMonthBean> items,EditItemClick editItemClick) {
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
		YuShengMonthBean item = getItem(position);
		boolean isRegular = getItemViewType(position) == 0;
		if (convertView == null) {
			if (isRegular){
				holder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.month_item, parent, false);
				holder.add = (TextView)convertView.findViewById(R.id.add);
				holder.month = (TextView)convertView.findViewById(R.id.month);
				convertView.setTag(holder);
			}else{
				holder2 = new ViewHolder2();
				convertView = layoutInflater.inflate(R.layout.othermonth_item,parent,false);
				holder2.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
				holder2.month = (TextView)convertView.findViewById(R.id.month);
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
			holder.month.setText("余"+item.getMonthBean().getLifeMonthsRet().get(position).getMonth()+"月");
			holder.month.setTypeface(tf);//设置字体
			holder.add.setOnClickListener(this);
			holder.add.setTag(position);
		} else {
			if (item.getMonthBean().getLifeMonthsRet().get(position).getMonth() == 0){
				holder2.month.setText("");
			}else{
				if (item.getMonthBean().getLifeMonthsRet().get(position).getMonth() > item.getMonthBean().getNow_month()){
					holder2.month.setText(item.getMonthBean().getLifeMonthsRet().get(position).getMonth()+"");
					holder2.layout.setBackgroundColor(context.getResources().getColor(R.color.yusheng1));
					holder2.month.setTextColor(context.getResources().getColor(R.color.yusheng2));
					holder2.text.setTextColor(context.getResources().getColor(R.color.yusheng2));
				}else{
					holder2.month.setText(item.getMonthBean().getLifeMonthsRet().get(position).getMonth()+"");
					holder2.layout.setBackgroundColor(context.getResources().getColor(R.color.yusheng3));
					holder2.month.setTextColor(context.getResources().getColor(R.color.yusheng4));
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
	public void appendItems(List<YuShengMonthBean> newItems) {
		addAll(newItems);
		notifyDataSetChanged();
	}

	@Override
	public void setItems(List<YuShengMonthBean> moreItems) {
		clear();
		appendItems(moreItems);
		notifyDataSetChanged();
	}

	@Override
	public void click(View view) {

	}

	class ViewHolder {
		TextView month,add;
	}
	class ViewHolder2{
		RelativeLayout layout;
		TextView month,text;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		for (int i = 0;i<items.size();i++){
			if (items.get(i).getMonthBean().getLifeMonthsRet().get(position).getMonth() == items.get(i).getMonthBean().getNow_month()){
				return TypeOne;
			}else{
				return TypeTwo;
			}
		}
		return 2;
	}
}
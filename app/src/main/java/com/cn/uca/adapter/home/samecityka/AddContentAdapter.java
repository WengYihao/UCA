package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.SendContentBean;
import com.cn.uca.bean.home.samecityka.SendImgBean;

import java.util.List;

/**
 * 夺宝城市适配器
 */
public class AddContentAdapter extends BaseAdapter{
	private List list;
	private Context context;
	private int TypeOne = 0;// 注意这个不同布局的类型起始值必须从0开始
	private int TypeTwo = 1;

	public AddContentAdapter(List list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List list) {
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
	public int getItemViewType(int position) {
		Object item = list.get(position);
		if (item instanceof SendContentBean){
			return TypeOne;
		}else if(item instanceof SendImgBean){
			return TypeTwo;
		}
		return -1;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type){
				case 0:
					holder = new ViewHolder();
					convertView = LayoutInflater.from(context).inflate(R.layout.action_detail_content_item, parent, false);
					convertView.setTag(holder);
					break;
				case 1:
					holder2 = new ViewHolder2();
					convertView = LayoutInflater.from(context).inflate(R.layout.action_detail_pic_item, parent, false);
					holder2.pic = (ImageView) convertView.findViewById(R.id.pic);
					holder2.addLayout = (LinearLayout)convertView.findViewById(R.id.addLayout);
					convertView.setTag(holder2);
					break;
			}
		} else {
			switch (type){
				case 0:
					holder = (ViewHolder) convertView.getTag();
					break;
				case 1:
					holder2 = (ViewHolder2) convertView.getTag();
					break;
			}
		}
		switch (type){
			case 0:
				break;
			case 1:
				break;
		}
		return convertView;
	}
	class ViewHolder {

	}
	class ViewHolder2{
		ImageView pic;
		LinearLayout addLayout;
	}
}

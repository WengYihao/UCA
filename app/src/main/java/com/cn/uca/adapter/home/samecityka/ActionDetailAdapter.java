package com.cn.uca.adapter.home.samecityka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.ActionDescribeBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 活动详情
 */
public class ActionDetailAdapter extends BaseAdapter{
	private List<ActionDescribeBean> list;
	private Context context;
	private int TypeOne = 0;// 注意这个不同布局的类型起始值必须从0开始
	private int TypeTwo = 1;

	public ActionDetailAdapter(List<ActionDescribeBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<ActionDescribeBean> list) {
		if (list != null) {
			this.list = list;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getItemViewType(int position) {
		switch (list.get(position).getParagraph_type()){
			case "p":
				return TypeOne;
			case "img":
				return TypeTwo;
		}
		return 3;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
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
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type){
				case 0:
					holder = new ViewHolder();
					convertView = LayoutInflater.from(context).inflate(R.layout.samecityka_actiondetail_content_item, parent, false);
					holder.content = (TextView)convertView.findViewById(R.id.content);
					convertView.setTag(holder);
					break;
				case 1:
					holder2 = new ViewHolder2();
					convertView = LayoutInflater.from(context).inflate(R.layout.samecityka_actiondetail_pic_item,parent,false);
					holder2.pic = (ImageView)convertView.findViewById(R.id.pic);
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
				holder.content.setText(list.get(position).getContent());
				break;
			case 1:
				ImageLoader.getInstance().displayImage(list.get(position).getImg_url(),holder2.pic);
				break;
		}
		return convertView;
	}

	class ViewHolder {
		TextView content;
	}
	class ViewHolder2{
		ImageView pic;
	}
}

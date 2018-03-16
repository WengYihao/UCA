package com.cn.uca.adapter.yueka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.bean.yueka.YueKaLineBean;
import com.cn.uca.impl.yueka.ItemClickListener;
import com.cn.uca.impl.yueka.LinePointCallBack;
import com.cn.uca.util.SetListView;
import com.cn.uca.view.NoScrollListView;

import java.util.List;

/**
 * 路线预设、路线预设
 */

public class EditLineAdapter extends BaseAdapter implements View.OnClickListener,LinePointCallBack{
	private List<YueKaLineBean> list;
	private Context context;
	private ItemClickListener itemClickListener;
	private LinePointCallBack callBack;

	public EditLineAdapter(){}
	public EditLineAdapter(List<YueKaLineBean> list, Context context,ItemClickListener itemClickListener,LinePointCallBack callBack) {
		this.list = list;
		this.context = context;
		this.itemClickListener = itemClickListener;
		this.callBack = callBack;
	}
	public void setList(List<YueKaLineBean> list) {
		if (list != null) {
			this.list = (List<YueKaLineBean>) list;
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.route_item, parent, false);
			holder.arrowView = (TextView) convertView.findViewById(R.id.arrowView);
			holder.editLine = (TextView)convertView.findViewById(R.id.editLine);
			holder.lineName = (TextView)convertView.findViewById(R.id.lineName);
			holder.listView = (NoScrollListView)convertView.findViewById(R.id.listView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final LinePointAdapter lineAdapter;
		holder.lineName.setText(list.get(position).getRoute_name());
		holder.lineName.setOnClickListener(this);
		holder.arrowView.setOnClickListener(this);
		holder.editLine.setOnClickListener(this);
		holder.lineName.setTag(position);
		holder.arrowView.setTag(position);
		holder.editLine.setTag(position);
		if (list.get(position).getPlaces() != null){
			if (list.get(position).getPlaces().size() != 0){
				holder.arrowView.setBackgroundResource(R.mipmap.down);
				lineAdapter = new LinePointAdapter(list.get(position).getPlaces(),context);
                holder.listView.setVisibility(View.VISIBLE);
				holder.listView.setAdapter(lineAdapter);
				callBack.changPoint(holder.listView,lineAdapter,list.get(position).getPlaces());
				SetListView.setListViewHeightBasedOnChildren(holder.listView);
			}else{
				holder.arrowView.setBackgroundResource(R.mipmap.right_gray);
                holder.listView.setVisibility(View.GONE);
			}
		}else{
			holder.arrowView.setBackgroundResource(R.mipmap.right_gray);
            holder.listView.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.arrowView:
				itemClickListener.clickShow(view);
				break;
			case R.id.lineName:
				itemClickListener.clickTrue(view);
				break;
			case R.id.editLine:
				itemClickListener.clickEdit(view);
				break;
		}
	}

	@Override
	public void changPoint(ListView listView, LinePointAdapter adapter,List<PlacesBean> list) {

	}

	class ViewHolder {
		TextView lineName,arrowView,editLine;
		NoScrollListView listView;
	}
}

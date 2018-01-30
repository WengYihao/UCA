package com.cn.uca.adapter.home.yusheng;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.yusheng.LifeMonthsBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;

import java.util.List;

/**
 * 余生天数
 */
public class YuShengMonthAdapter extends BaseAdapter{
	private List<LifeMonthsBean> list;
	private Context context;
    private int nowMonth;

	public YuShengMonthAdapter(Context context, List<LifeMonthsBean> list,int nowMonth) {
		this.context = context;
		this.list = list;
        this.nowMonth = nowMonth;
	}


    public void setList(List<LifeMonthsBean> list,int nowMonth) {
        if (list != null) {
            this.list =  list;
            this.nowMonth = nowMonth;
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
	public View getView(int position, View convertView,ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.othermonth_item, parent, false);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            holder.month = (TextView)convertView.findViewById(R.id.month);
            holder.text = (TextView)convertView.findViewById(R.id.text);
            convertView.setTag(holder);
		} else {
            holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).getMonth() == 0){
            holder.month.setText("");
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else{
            if (list.get(position).getMonth() > nowMonth){
                //过去月
                holder.month.setText(list.get(position).getMonth()+"");
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.yusheng1));
                holder.month.setTextColor(context.getResources().getColor(R.color.yusheng2));
                holder.text.setTextColor(context.getResources().getColor(R.color.yusheng2));
            }else{
                holder.month.setText(list.get(position).getMonth()+"");
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.yusheng3));
                holder.month.setTextColor(context.getResources().getColor(R.color.yusheng4));
                holder.text.setTextColor(context.getResources().getColor(R.color.yusheng4));
			}
		}
        SetLayoutParams.setAbsListView(holder.layout, (MyApplication.width-5)/6,MyApplication.width/7*27/20);
		return convertView;
	}
	class ViewHolder {
        RelativeLayout layout;
        TextView month,text;
	}
}
package com.cn.uca.adapter.home.sign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.sign.IntegralDetailedBean;
import com.cn.uca.bean.home.sign.IntegralPoolBean;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class IntegralDetailAdapter extends BaseAdapter {
    private List<IntegralDetailedBean> list;
    private Context context;

    public IntegralDetailAdapter(List<IntegralDetailedBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<IntegralDetailedBean> list) {
        if (list != null) {
            this.list =  list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.integral_detail_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.integral = (TextView) convertView.findViewById(R.id.integral);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.name.setText(list.get(position).getCommodity_name());
        holder.time.setText(list.get(position).getCreate_time());
        holder.integral.setText(list.get(position).getRemarks());
        return convertView;
    }
    class ViewHolder {
        TextView name,time,integral;
    }
}

package com.cn.uca.adapter.home.raiders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.raider.RaidersSenicSpotBean;
import com.cn.uca.impl.raider.RouteImpl;
import com.cn.uca.util.ToastXutil;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class SpotNameAdapter extends BaseAdapter {
    private List<RaidersSenicSpotBean> list;
    private Context context;
    private RouteImpl route;
    public SpotNameAdapter(List<RaidersSenicSpotBean> list, Context context, RouteImpl route) {
        this.list = list;
        this.context = context;
        this.route = route;
    }
    public void setList(List<RaidersSenicSpotBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.spot_name_item, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder = new ViewHolder();
        holder.item = (TextView) convertView.findViewById(R.id.item);
        holder.start = (TextView) convertView.findViewById(R.id.start);
        holder.start.setTag(position);
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                route.start(v);
            }
        });
        holder.item.setText(list.get(position).getScenic_spot_name());
        return convertView;
    }
    class ViewHolder {
        TextView item,start;
    }
}

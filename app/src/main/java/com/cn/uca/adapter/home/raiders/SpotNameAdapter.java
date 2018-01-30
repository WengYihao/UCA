package com.cn.uca.adapter.home.raiders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.raider.RaidersSenicSpotBean;
import com.cn.uca.impl.raider.RouteImpl;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class SpotNameAdapter extends BaseAdapter {
    private List<RaidersSenicSpotBean> list;
    private Context context;
    private RouteImpl route;
    public SpotNameAdapter(List<RaidersSenicSpotBean> list, Context context,RouteImpl route) {
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
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.spot_name_item, parent, false);
            holder.item = (TextView) convertView.findViewById(R.id.item);
            holder.start = (TextView)convertView.findViewById(R.id.start);
            holder.end = (TextView)convertView.findViewById(R.id.end);
            holder.start.setTag(position);
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    route.start(v);
                }
            });
            holder.end.setTag(position);
            holder.end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    route.end(v);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item.setText(list.get(position).getScenic_spot_name());
        if (list.get(position).isStart()){
            holder.start.setBackgroundResource(R.drawable.fifteen_circular_ori_background);
            holder.start.setText("起点");
            holder.start.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.start.setBackgroundResource(R.drawable.text_lable_gray_bg);
            holder.start.setText("设为起点");
            holder.start.setTextColor(context.getResources().getColor(R.color.grey2));
        }
        if (list.get(position).isEnd()){
            holder.end.setBackgroundResource(R.drawable.fifteen_circular_ori_background);
            holder.end.setText("终点");
            holder.end.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.end.setBackgroundResource(R.drawable.text_lable_gray_bg);
            holder.end.setText("设为终点");
            holder.end.setTextColor(context.getResources().getColor(R.color.grey2));
        }
        return convertView;
    }
    class ViewHolder {
        TextView item,start,end;
    }
}

package com.cn.uca.adapter.home.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.dateview.GridViewBean;
import com.cn.uca.bean.home.travel.ReserveDateBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SystemUtil;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class ReserveDateAdapter extends BaseAdapter {
    private List<ReserveDateBean> list;
    private Context context;

    public ReserveDateAdapter(List<ReserveDateBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    public void setList(List<ReserveDateBean> list) {
        if (list != null) {
            this.list = (List<ReserveDateBean>) list;
            this.notifyDataSetChanged();
        }
    }
    @Override
    public Object getItem(int position) {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.travel_reserve_date_item, parent, false);
            holder.surplus = (TextView)convertView.findViewById(R.id.surplus);
            holder.day = (TextView)convertView.findViewById(R.id.day);
            holder.price = (TextView)convertView.findViewById(R.id.price);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).getCount() != 0){
            holder.surplus.setText("余"+list.get(position).getCount());
        }else{
            holder.surplus.setText("");
        }
//        if (list.get(position).getDay().substring(0,1).equals("0")){
//            holder.day.setText(list.get(position).getDay().substring(1));
//        }else{
//
//        }
        holder.day.setText(list.get(position).getDay());
        if (list.get(position).getPrice() != 0){
            holder.price.setText("￥"+(int)list.get(position).getPrice());
        }else{
            holder.price.setText("");
        }
        SetLayoutParams.setLinearLayout(holder.layout,MyApplication.width/7,MyApplication.width/6);
        return convertView;
    }
    class ViewHolder {
        RelativeLayout layout;
        TextView surplus,day,price;
    }
}

package com.cn.uca.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.user.CouponBean;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 * 优惠券
 */

public class CouponAdapter extends BaseAdapter {
    private List<CouponBean> list;
    private Context context;
    public CouponAdapter(List<CouponBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<CouponBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.coupon_item, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.discount = (TextView)convertView.findViewById(R.id.discount);
        holder.price = (TextView)convertView.findViewById(R.id.price);
        holder.type = (TextView)convertView.findViewById(R.id.type);

        holder.name.setText(list.get(position).getSetting_coupon_name());
        holder.time.setText("有效期至 "+list.get(position).getCoupon_existence_time());
        holder.discount.setText(list.get(position).getSetting_coupon_introduce());
        holder.price.setText((int) list.get(position).getCoupon_price()+"");
        return convertView;
    }
    class ViewHolder {
        TextView name,time,discount,price,type;
    }
}

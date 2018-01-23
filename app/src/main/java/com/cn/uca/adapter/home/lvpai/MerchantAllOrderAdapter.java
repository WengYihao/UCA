package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.MerchantAllOrderBean;
import com.cn.uca.bean.home.lvpai.MerchantOrderBean;
import com.cn.uca.impl.lvpai.OrderCallback;
import com.cn.uca.impl.lvpai.OrderItemBack;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class MerchantAllOrderAdapter extends BaseAdapter {
    private List<MerchantAllOrderBean> list;
    private Context context;
    private OrderItemBack back;

    public MerchantAllOrderAdapter(List<MerchantAllOrderBean> list, Context context,OrderItemBack back) {
        this.list = list;
        this.context = context;
        this.back = back;
    }
    public void setList(List<MerchantAllOrderBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.merchant_order_item, parent, false);
            holder.pic = (ImageView)convertView.findViewById(R.id.pic);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.order = (TextView)convertView.findViewById(R.id.order);
            holder.user_pic = (CircleImageView)convertView.findViewById(R.id.user_pic);
            holder.name = (TextView) convertView.findViewById(R.id.username);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.layout = (LinearLayout)convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position > 0){
            if (list.get(position).getTrip_shoot_id() == list.get(position-1).getTrip_shoot_id()){
                holder.layout.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait(),holder.user_pic);
                holder.name.setText(list.get(position).getUser_nick_name());
                holder.date.setText(list.get(position).getBeg_date()+"~"+list.get(position).getEnd_date());
            }else{
                ImageLoader.getInstance().displayImage(list.get(position).getPictures(),holder.pic);
                ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait(),holder.user_pic);
                holder.title.setText(list.get(position).getTitle());
                holder.price.setText("￥"+(int) list.get(position).getPrice());
                holder.name.setText(list.get(position).getUser_nick_name());
                holder.date.setText(list.get(position).getBeg_date()+"~"+list.get(position).getEnd_date());
            }
        }else{
            ImageLoader.getInstance().displayImage(list.get(position).getPictures(),holder.pic);
            ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait(),holder.user_pic);
            holder.title.setText(list.get(position).getTitle());
            holder.price.setText("￥"+(int) list.get(position).getPrice());
            holder.name.setText(list.get(position).getUser_nick_name());
            holder.date.setText(list.get(position).getBeg_date()+"~"+list.get(position).getEnd_date());
        }
        holder.order.setTag(position);
        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.back(v);
            }
        });
        return convertView;
    }
    class ViewHolder {
        CircleImageView user_pic;
        ImageView pic;
        TextView title,name,order,date,price;
        LinearLayout layout;
    }
}

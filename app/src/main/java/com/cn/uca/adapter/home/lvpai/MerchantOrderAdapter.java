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
import com.cn.uca.bean.home.lvpai.MerchantCommodityBean;
import com.cn.uca.bean.home.lvpai.MerchantOrderBean;
import com.cn.uca.impl.lvpai.OrderCallback;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class MerchantOrderAdapter extends BaseAdapter {
    private List<MerchantOrderBean> list;
    private Context context;
    private OrderCallback callback;

    public MerchantOrderAdapter(List<MerchantOrderBean> list, Context context,OrderCallback callback) {
        this.list = list;
        this.context = context;
        this.callback = callback;
    }
    public void setList(List<MerchantOrderBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvpai_order_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.place = (TextView) convertView.findViewById(R.id.place);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.order = (TextView) convertView.findViewById(R.id.order);
            holder.timelong = (TextView) convertView.findViewById(R.id.timelong);
            holder.pic = (CircleImageView) convertView.findViewById(R.id.pic);
            holder.backorder = (TextView)convertView.findViewById(R.id.backorder);
            holder.settlement = (TextView)convertView.findViewById(R.id.settlement);
            holder.chat = (LinearLayout)convertView.findViewById(R.id.chat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getUser_head_portrait(),holder.pic);
        holder.name.setText(list.get(position).getUser_nick_name());
        holder.place.setText(list.get(position).getDestination());
        holder.title.setText(list.get(position).getTs_title());
        holder.order.setText(list.get(position).getOrder_number());
        holder.price.setText("￥"+(int) list.get(position).getPrice());
        holder.time.setText(list.get(position).getCreate_time());
        holder.timelong.setText(list.get(position).getBeg_date()+"~"+list.get(position).getEnd_date()+"("+list.get(position).getDays()+"天)");
        holder.backorder.setTag(position);
        holder.settlement.setTag(position);
        holder.chat.setTag(position);
        switch (list.get(position).getTsm_state_id()){
            case 1:
                holder.backorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.backOrder(v);
                    }
                });
                break;
            case 2:
                holder.backorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.backOrder(v);
                    }
                });
                holder.settlement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.settlement(v);
                    }
                });
                break;
            case 3:
                holder.settlement.setEnabled(false);
                holder.backorder.setEnabled(false);
                break;
            case 4:
                holder.settlement.setEnabled(false);
                holder.backorder.setEnabled(false);
                break;
        }
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.chat(v);
            }
        });
        return convertView;
    }
    class ViewHolder {
        CircleImageView pic;
        TextView name,title,order,place,price,time,timelong,backorder,settlement;
        LinearLayout chat;
    }
}

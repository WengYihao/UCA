package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.TeamBean;
import com.cn.uca.bean.home.lvpai.TripShootsBean;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.FluidLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class TripShootsAdapter extends BaseAdapter {
    private List<TripShootsBean> list;
    private Context context;

    public TripShootsAdapter(List<TripShootsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<TripShootsBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.merchant_trip_shoots_item, parent, false);
            holder.name  = (TextView)convertView.findViewById(R.id.name);
            holder.place = (TextView)convertView.findViewById(R.id.place);
            holder.price = (TextView)convertView.findViewById(R.id.price);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            holder.fluidLayout = (FluidLayout)convertView.findViewById(R.id.fluidLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getPicture_url(),holder.pic);
        holder.name.setText(list.get(position).getTitle());
        holder.price.setText("￥"+(int)list.get(position).getPrice());
        holder.fluidLayout.removeAllViews();
        holder.fluidLayout.setGravity(Gravity.TOP);
        for (int a = 0;a<list.get(position).getLable_names().size();a++){
            TextView tv = new TextView(context);
            tv.setText(list.get(position).getLable_names().get(a));
            tv.setTextSize(12);
            tv.setBackgroundResource(R.drawable.text_lable_white_border_bg);
            tv.setTextColor(context.getResources().getColor(R.color.white));
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(12, 12, 0, 12);
            holder.fluidLayout.addView(tv, params);
        }
        return convertView;
    }
    class ViewHolder {
        TextView name,place,price;
        ImageView pic;
        FluidLayout fluidLayout;
    }
}

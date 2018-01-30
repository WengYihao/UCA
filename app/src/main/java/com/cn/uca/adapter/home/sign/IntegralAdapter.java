package com.cn.uca.adapter.home.sign;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.footprint.CityBean;
import com.cn.uca.bean.home.sign.IntegralPoolBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.MyConfig;
import com.cn.uca.util.SetLayoutParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class IntegralAdapter extends BaseAdapter {
    private List<IntegralPoolBean> list;
    private Context context;
    private int type;

    public IntegralAdapter(List<IntegralPoolBean> list, Context context,int type) {
        this.list = list;
        this.context = context;
        this.type = type;
    }
    public void setList(List<IntegralPoolBean> list) {
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


//    @Override
//    public int getItemViewType(int position) {
//        if (type == 1){
//            return TypeOne;
//        }else{
//            return TypeTwo;
//        }
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ViewHolder2 holder2 = null;
        if (convertView == null) {
            switch (type){
                case 1:
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.integral_pools_item, parent, false);
                    holder.name = (TextView) convertView.findViewById(R.id.name);
                    holder.integral = (TextView) convertView.findViewById(R.id.integral);
                    convertView.setTag(holder);
                    break;
                case 2:
                    holder2 = new ViewHolder2();
                    convertView = LayoutInflater.from(context).inflate(R.layout.integral_pools_item2, parent, false);
                    holder2.pic = (ImageView)convertView.findViewById(R.id.pic);
                    holder2.name = (TextView) convertView.findViewById(R.id.name);
                    holder2.num = (TextView)convertView.findViewById(R.id.num);
                    holder2.integral = (TextView) convertView.findViewById(R.id.integral);
                    convertView.setTag(holder2);
                    break;
            }
        } else {
            switch (type){
                case 1:
                    holder = (ViewHolder) convertView.getTag();
                    break;
                case 2:
                    holder2 = (ViewHolder2)convertView.getTag();
                    break;
            }
        }
        switch (type){
            case 1:
                holder.name.setText(list.get(position).getCommodity_name());
                holder.integral.setText(list.get(position).getNeed_integral()+" 积分");
                break;
            case 2:
                if (list.get(position).getCommodity_picture_path() != ""){
                    ImageLoader.getInstance().displayImage(list.get(position).getCommodity_picture_path(),holder2.pic);
                }else{

                }
                holder2.name.setText(list.get(position).getCommodity_name());
                holder2.num.setText(list.get(position).getNeed_integral()+" 积分");
                break;
        }

        return convertView;
    }
    class ViewHolder {
        TextView name,integral;
    }
    class ViewHolder2{
        ImageView pic;
        TextView name,num,integral;
    }
}

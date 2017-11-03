package com.cn.uca.adapter.home.footprint;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.footprint.CityBean;
import com.cn.uca.bean.home.footprint.CityNameBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.MyConfig;
import com.cn.uca.util.SetLayoutParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class FootPrintAdapter extends BaseAdapter {
    private List<CityBean> list;
    private Context context;
    private int TypeOne = 0;// 注意这个不同布局的类型起始值必须从0开始
    private int TypeTwo = 1;

    public FootPrintAdapter(List<CityBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<CityBean> list) {
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
    public int getItemViewType(int position) {
        if (!list.get(position).getPicture_url().equals("")){
            return TypeOne;
        }else{
            return TypeTwo;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ViewHolder2 holder2 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type){
                case 0:
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.footprint_item, parent, false);
                    holder.layout = (LinearLayout)convertView.findViewById(R.id.layout);
                    holder.time = (TextView) convertView.findViewById(R.id.time);
                    holder.place = (TextView) convertView.findViewById(R.id.place);
                    holder.content = (TextView) convertView.findViewById(R.id.content);
                    holder.pic = (SimpleDraweeView)convertView.findViewById(R.id.pic);
                    convertView.setTag(holder);
                    break;
                case 1:
                    holder2 = new ViewHolder2();
                    convertView = LayoutInflater.from(context).inflate(R.layout.footprint_item2,parent,false);
                    holder2.layout = (LinearLayout)convertView.findViewById(R.id.layout);
                    holder2.time = (TextView) convertView.findViewById(R.id.time);
                    holder2.place = (TextView) convertView.findViewById(R.id.place);
                    holder2.content = (TextView) convertView.findViewById(R.id.content);
                    convertView.setTag(holder2);
                    break;
            }
        } else {
            switch (type){
                case 0:
                    holder = (ViewHolder) convertView.getTag();
                    break;
                case 1:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }
        switch (type){
            case 0:
                holder.time.setText(list.get(position).getTravel_time());
                holder.place.setText(list.get(position).getCity_name());
                Uri uri = Uri.parse(list.get(position).getPicture_url());
                holder.pic.setVisibility(View.VISIBLE);
                holder.pic.setImageURI(uri);
                holder.content.setText(list.get(position).getContent());
                SetLayoutParams.setLinearLayout(holder.pic, MyApplication.width*7/25,MyApplication.width*6/25);
                SetLayoutParams.setRelativeLayout(holder.layout,MyApplication.width*12/25,MyApplication.width*6/25);
                break;
            case 1:
                holder2.time.setText(list.get(position).getTravel_time());
                holder2.place.setText(list.get(position).getCity_name());
                holder2.content.setText(list.get(position).getContent());
                SetLayoutParams.setRelativeLayout(holder2.layout,MyApplication.width*6/25,MyApplication.width*6/25);
                break;
        }
        return convertView;
    }
    class ViewHolder {
        LinearLayout layout;
        TextView time,place,content;
        SimpleDraweeView pic;
    }
    class ViewHolder2{
        LinearLayout layout;
        TextView time,place,content;
    }
}

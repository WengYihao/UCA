package com.cn.uca.adapter.yueka;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.yueka.LineNameBean;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.impl.yueka.PointClickImpl;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class YuekaLineAdapter extends BaseAdapter {
    private List<PlacesBean> list;
    private Context context;
    private PointClickImpl click;

    public YuekaLineAdapter(){}
    public YuekaLineAdapter(List<PlacesBean> list, Context context,PointClickImpl click){
        this.list = list;
        this.context = context;
        this.click = click;
    }
    public void setList(List<PlacesBean> list) {
        if (list != null) {
            this.list = (List<PlacesBean>) list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.yueka_line_item, parent, false);
            holder.delete = (TextView) convertView.findViewById(R.id.delete);
            holder.city = (TextView) convertView.findViewById(R.id.city);
            holder.place = (TextView) convertView.findViewById(R.id.place);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.city.setText(list.get(position).getPlace_name());
        holder.place.setText(list.get(position).getDeparture_address());
        holder.delete.setTag(position);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.deleteBack(2,list.get(position).getPlace_id());//删除路线点
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView delete,city,place;
    }
}

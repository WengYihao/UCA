package com.cn.uca.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.SearchBean;
import com.cn.uca.bean.home.lvpai.setAddressBean;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class SearchAdapter extends BaseAdapter {
    private List<SearchBean> list;
    private Context context;

    public SearchAdapter(List<SearchBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<SearchBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_search_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.price.setText("￥"+(int)list.get(position).getPrice());
        return convertView;
    }
    class ViewHolder {
        TextView name,price;
    }
}

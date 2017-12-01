package com.cn.uca.adapter.home.footprint;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.footprint.CityNameBean;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class CityNameAdapter extends BaseAdapter {
    private List<CityNameBean> list;
    private Context context;

    public CityNameAdapter(){}
    public CityNameAdapter(List<CityNameBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<CityNameBean> list) {
        if (list != null) {
            this.list = (List<CityNameBean>) list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.letter_item, parent, false);
            holder.item = (TextView) convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item.setText(list.get(position).getCity_name());
        holder.item.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        return convertView;
    }
    class ViewHolder {
        TextView item;
    }
}

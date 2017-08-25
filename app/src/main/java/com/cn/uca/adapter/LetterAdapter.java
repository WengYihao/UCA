package com.cn.uca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;

/**
 * Created by asus on 2017/8/23.
 * 一元夺宝字母适配器
 */

public class LetterAdapter extends BaseAdapter{
    private String [] list;
    private Context context;

    public LetterAdapter(){}
    public LetterAdapter(String [] list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(String [] list) {
        if (list != null) {
            this.list =( String []) list;
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
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
            holder.item = (TextView)convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item.setText(list[position]);
        return convertView;
    }
    class ViewHolder {
        TextView item;
    }
}

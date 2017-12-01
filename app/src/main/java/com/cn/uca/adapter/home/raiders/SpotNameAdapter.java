package com.cn.uca.adapter.home.raiders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.raider.RaidersSenicSpotBean;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class SpotNameAdapter extends BaseAdapter {
    private List<RaidersSenicSpotBean> list;
    private Context context;

    public SpotNameAdapter(List<RaidersSenicSpotBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<RaidersSenicSpotBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.spot_name_item, parent, false);
            holder.item = (TextView) convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item.setText(list.get(position).getScenic_spot_name());
        return convertView;
    }
    class ViewHolder {
        TextView item;
    }
}

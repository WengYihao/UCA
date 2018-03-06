package com.cn.uca.adapter.rongim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.raider.RaidersSenicSpotBean;
import com.cn.uca.bean.rongim.ReportTypeBean;
import com.cn.uca.impl.raider.RouteImpl;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class ReportTypeAdapter extends BaseAdapter {
    private List<ReportTypeBean> list;
    private Context context;
    public ReportTypeAdapter(List<ReportTypeBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<ReportTypeBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.report_type_item, parent, false);
            holder.item = (TextView) convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item.setText(list.get(position).getReport_type_name());
        return convertView;
    }
    class ViewHolder {
        TextView item;
    }
}

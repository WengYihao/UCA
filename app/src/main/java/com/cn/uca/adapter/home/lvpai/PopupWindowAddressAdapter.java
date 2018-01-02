package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.setAddressBean;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class PopupWindowAddressAdapter extends BaseAdapter {
    private List<setAddressBean> list;
    private Context context;

    public PopupWindowAddressAdapter(List<setAddressBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<setAddressBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvpai_popupwindow_address_item, parent, false);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       holder.address.setText(list.get(position).getAddress());
        return convertView;
    }
    class ViewHolder {
        TextView address;
    }
}

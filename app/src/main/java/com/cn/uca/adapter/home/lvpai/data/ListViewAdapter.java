package com.cn.uca.adapter.home.lvpai.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.TeamBean;
import com.cn.uca.bean.home.lvpai.dateview.GridViewBean;
import com.cn.uca.bean.home.lvpai.dateview.ListViewBean;
import com.cn.uca.impl.ItemClick;
import com.cn.uca.impl.lvpai.CallBackDate;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.NoScrollGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class ListViewAdapter extends BaseAdapter{
    private List<ListViewBean> list;
    private Context context;
    private CallBackDate backDate;

    public ListViewAdapter(List<ListViewBean> list, Context context,CallBackDate backDate) {
        this.list = list;
        this.context = context;
        this.backDate = backDate;
    }
    public void setList(List<ListViewBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvpai_date_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.gridView = (NoScrollGridView) convertView.findViewById(R.id.gridView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       holder.name.setText(list.get(position).getMonthName());
        GridViewAdapter adapter = new GridViewAdapter(list.get(position).getBean(),context);
        holder.gridView.setAdapter(adapter);
        final int item = (position);
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                backDate.callBack(list.get(item).getBean().getViewBean().get(position).getDate());
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView name;
        NoScrollGridView gridView;
    }
}

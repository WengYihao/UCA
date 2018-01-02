package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.DescriptionBean;
import com.cn.uca.bean.home.lvpai.DetailContentBean;
import com.cn.uca.bean.home.lvpai.PhotoBean;
import com.cn.uca.bean.home.lvpai.TeamBean;
import com.cn.uca.util.StringXutil;
import com.cn.uca.view.CircleImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class TeamPicAdapter extends BaseAdapter {
    private List<TeamBean> list;
    private Context context;

    public TeamPicAdapter(List<TeamBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<TeamBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvpai_team_userpic_item, parent, false);
            holder.pic = (CircleImageView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getHead_portrait_url(),holder.pic);
        return convertView;
    }
    class ViewHolder {
        CircleImageView pic;
    }
}

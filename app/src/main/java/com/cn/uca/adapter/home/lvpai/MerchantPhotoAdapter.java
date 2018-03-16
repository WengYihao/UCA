package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.AlbumBean;
import com.cn.uca.bean.home.lvpai.CommodityBean;
import com.cn.uca.bean.home.lvpai.MerchantBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.MyConfig;
import com.cn.uca.util.SetLayoutParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class MerchantPhotoAdapter extends BaseAdapter {
    private List<AlbumBean> list;
    private Context context;

    public MerchantPhotoAdapter(List<AlbumBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<AlbumBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.merchant_photo_item, parent, false);
            holder.photo_num = (TextView) convertView.findViewById(R.id.photo_num);
            holder.photo_name = (TextView) convertView.findViewById(R.id.photo_name);
            holder.photo_num_text = (TextView) convertView.findViewById(R.id.photo_num_text);
            holder.pic = (SimpleDraweeView) convertView.findViewById(R.id.pic);
            holder.layout = (LinearLayout)convertView.findViewById(R.id.layout);
            holder.pic_layout = (RelativeLayout)convertView.findViewById(R.id.pic_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Uri uri = Uri.parse(list.get(position).getImg_url()+MyConfig.corpPhoto);
        holder.pic.setImageURI(uri);
        SetLayoutParams.setLinearLayout(holder.pic_layout, (MyApplication.width-20)*2/5,((MyApplication.width-20)*2/5)*3/4);
        holder.photo_name.setText(list.get(position).getAlbum_name());
        holder.photo_num.setText(list.get(position).getNumber()+"");
        holder.photo_num_text.setText(list.get(position).getNumber()+"个内容");
        holder.layout.getBackground().setAlpha(120);
        return convertView;
    }
    class ViewHolder {
        TextView photo_num,photo_name,photo_num_text;
        SimpleDraweeView pic;
        LinearLayout layout;
        RelativeLayout pic_layout;
    }
}

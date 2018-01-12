package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.MerchantPhotoBean;
import com.cn.uca.bean.home.lvpai.PhotoDetailBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.MyConfig;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.NoScrollGridView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class PhotoItemAdapter extends BaseAdapter {
    private List<MerchantPhotoBean> list;
    private Context context;

    public PhotoItemAdapter(List<MerchantPhotoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<MerchantPhotoBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.look_photo_pic_item, parent, false);
            holder.pic = (SimpleDraweeView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Uri uri = Uri.parse(MyConfig.photo+list.get(position).getPicture_url());
        holder.pic.setImageURI(uri);
        SetLayoutParams.setLinearLayout(holder.pic, (MyApplication.width- SystemUtil.dip2px(26))/3,(MyApplication.width- SystemUtil.dip2px(26))/3);
        return convertView;
    }
    class ViewHolder {
        SimpleDraweeView pic;
    }
}

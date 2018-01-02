package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.CommodityBean;
import com.cn.uca.bean.home.lvpai.DescriptionBean;
import com.cn.uca.bean.home.lvpai.DetailContentBean;
import com.cn.uca.bean.home.lvpai.MerchantBean;
import com.cn.uca.bean.home.lvpai.PhotoBean;
import com.cn.uca.config.MyConfig;
import com.cn.uca.util.StringXutil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class DetailAdapter extends BaseAdapter {
    private List list;
    private Context context;

    public DetailAdapter(List list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvpai_commodity_detail_item, parent, false);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.pic = (SimpleDraweeView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position) instanceof DetailContentBean){
            DetailContentBean contentBean = (DetailContentBean) list.get(position);
            holder.type.setText(contentBean.getType());
            if (StringXutil.isEmpty(contentBean.getBean().getContent())){
                holder.content.setVisibility(View.GONE);
            }else{
                holder.content.setText(contentBean.getBean().getContent());
            }
            if (StringXutil.isEmpty(contentBean.getBean().getImg_url())){
                holder.pic.setVisibility(View.GONE);
            }else{
                Uri uri = Uri.parse(contentBean.getBean().getImg_url());
                holder.pic.setImageURI(uri);
            }
        }else if(list.get(position) instanceof PhotoBean){
            PhotoBean photoBean = (PhotoBean) list.get(position);
            holder.type.setText("【客照欣赏】");
            holder.content.setVisibility(View.GONE);
            Uri uri = Uri.parse(photoBean.getImg_url());
            holder.pic.setImageURI(uri);
        }else if(list.get(position) instanceof DescriptionBean){
            DescriptionBean bean = (DescriptionBean)list.get(position);
            if (bean.getOrder() == 1){
                holder.type.setText("【费用说明】");
                holder.content.setText(bean.getContent());
                holder.pic.setVisibility(View.GONE);
            }else if(bean.getOrder() == 2){
                holder.type.setText("【预定须知】");
                holder.content.setText(bean.getContent());
                holder.pic.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
    class ViewHolder {
        TextView type,content;
        SimpleDraweeView pic;
    }
}

package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.CommodityBean;
import com.cn.uca.bean.home.lvpai.MerchantBean;
import com.cn.uca.bean.home.lvpai.setAddressBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class MerchantAdapter extends BaseAdapter {
    private List list;
    private Context context;

    public MerchantAdapter(List list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvpai_merchant_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.place = (TextView) convertView.findViewById(R.id.place);
            holder.score = (TextView) convertView.findViewById(R.id.score);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position) instanceof MerchantBean){
            MerchantBean merchantBean = (MerchantBean) list.get(position);
            holder.name.setText(merchantBean.getMerchant_name());
            holder.place.setText(merchantBean.getM_city_name());
            holder.score.setText(merchantBean.getScore()+"分");
            ImageLoader.getInstance().displayImage(merchantBean.getPictures().get(0),holder.pic);
        }else if(list.get(position) instanceof CommodityBean){
            CommodityBean commodityBean = (CommodityBean) list.get(position);
            holder.name.setText(commodityBean.getTitle());
            holder.place.setText(commodityBean.getM_city_name());
            holder.score.setText("￥"+(int) commodityBean.getPrice());
            ImageLoader.getInstance().displayImage(commodityBean.getPicture_url(),holder.pic);
        }
        return convertView;
    }
    class ViewHolder {
        TextView name,place,score;
        ImageView pic;
    }
}

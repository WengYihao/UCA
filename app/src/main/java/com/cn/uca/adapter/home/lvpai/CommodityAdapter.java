package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.MerchantCommodityBean;
import com.cn.uca.bean.home.lvpai.TeamBean;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class CommodityAdapter extends BaseAdapter {
    private List<MerchantCommodityBean> list;
    private Context context;

    public CommodityAdapter(List<MerchantCommodityBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<MerchantCommodityBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvpai_merchant_commodity_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.place = (TextView) convertView.findViewById(R.id.place);
            holder.collection_num = (TextView) convertView.findViewById(R.id.collection_num);
            holder.buy_num = (TextView) convertView.findViewById(R.id.buy_num);
            holder.evaluate_num = (TextView) convertView.findViewById(R.id.evaluate_num);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).getPicture_url(),holder.pic);
        holder.name.setText(list.get(position).getTitle());
        holder.place.setText(list.get(position).getDestination());
        holder.collection_num.setText("收藏"+list.get(position).getCollection_number());
        holder.buy_num.setText("已购"+list.get(position).getPurchase_number());
        holder.evaluate_num.setText("评论"+list.get(position).getComment_number());
        return convertView;
    }
    class ViewHolder {
        ImageView pic;
        TextView name,place,collection_num,buy_num,evaluate_num;
    }
}

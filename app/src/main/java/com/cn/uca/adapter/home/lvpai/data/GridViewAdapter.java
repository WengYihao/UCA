package com.cn.uca.adapter.home.lvpai.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.dateview.GridViewBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.dialog.LoadDialog;

/**
 * Created by asus on 2017/11/1.
 */

public class GridViewAdapter extends BaseAdapter {
    private GridViewBean bean;
    private Context context;

    public GridViewAdapter(GridViewBean bean, Context context) {
        this.bean = bean;
        this.context = context;
    }
    @Override
    public int getCount() {
        return bean.getViewBean().size();
    }

    @Override
    public Object getItem(int position) {
        return bean.getViewBean().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvpai_date_day_item, parent, false);
            holder.day = (TextView)convertView.findViewById(R.id.day);
            holder.text = (TextView)convertView.findViewById(R.id.text);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int week = bean.getWeekId();
        if (position<week-1){
            holder.layout.setBackgroundResource(R.color.grey2);
            holder.day.setVisibility(View.GONE);
        }else{
            if (bean.getSourceBeen().size() != 0){
                for (int a = 0;a<bean.getSourceBeen().size();a++){
                    if (bean.getViewBean().get(position).getDate().equals(bean.getSourceBeen().get(a).getDate())){
                        switch (bean.getSourceBeen().get(a).getType()){
                            case 1://行程被购买
                                holder.layout.setBackgroundResource(R.color.gray2);
                                holder.day.setText(bean.getViewBean().get(position).getDay()+"日");
                                holder.text.setVisibility(View.VISIBLE);
                                holder.layout.setEnabled(false);
                                break;
                            case 2://商家忙碌
                                holder.layout.setBackgroundResource(R.color.yellow);
                                holder.day.setText(bean.getViewBean().get(position).getDay()+"日");
                                holder.day.setTextColor(context.getResources().getColor(R.color.white));
                                holder.layout.setEnabled(false);
                                holder.text.setVisibility(View.VISIBLE);
                                holder.text.setText("商家忙碌");
                                holder.text.setTextColor(context.getResources().getColor(R.color.white));
                                break;
                        }
                        break;
                    }else{
                        switch (bean.getViewBean().get(position).getSelect()){
                            case "true":
                                holder.layout.setBackgroundResource(R.color.ori);
                                holder.day.setText(bean.getViewBean().get(position).getDay()+"日");
                                holder.day.setTextColor(context.getResources().getColor(R.color.white));
                                SetLayoutParams.setLinearLayout(holder.day,SystemUtil.dip2px(40),SystemUtil.dip2px(40));
                                holder.day.setBackgroundResource(R.drawable.circular_background2);
                                break;
                            case "ob":
                                holder.layout.setBackgroundResource(R.color.ori2);
                                holder.day.setText(bean.getViewBean().get(position).getDay()+"日");
                                holder.day.setTextColor(context.getResources().getColor(R.color.white));
                                break;
                            default:
                                holder.layout.setBackgroundResource(R.color.white);
                                holder.day.setText(bean.getViewBean().get(position).getDay()+"日");
                                break;
                        }
                    }
                }
            }else{
                switch (bean.getViewBean().get(position).getSelect()){
                    case "true":
                        holder.layout.setBackgroundResource(R.color.ori);
                        holder.day.setText(bean.getViewBean().get(position).getDay()+"日");
                        holder.day.setTextColor(context.getResources().getColor(R.color.white));
                        SetLayoutParams.setLinearLayout(holder.day,SystemUtil.dip2px(40),SystemUtil.dip2px(40));
                        holder.day.setBackgroundResource(R.drawable.circular_background2);
                        break;
                    case "ob":
                        holder.layout.setBackgroundResource(R.color.ori2);
                        holder.day.setText(bean.getViewBean().get(position).getDay()+"日");
                        holder.day.setTextColor(context.getResources().getColor(R.color.white));
                        break;
                    default:
                        holder.layout.setBackgroundResource(R.color.white);
                        holder.day.setText(bean.getViewBean().get(position).getDay()+"日");
                        break;
                }
            }
        }
        SetLayoutParams.setLinearLayout(holder.layout,MyApplication.width/7,MyApplication.width/6);
        return convertView;
    }
    class ViewHolder {
        LinearLayout layout;
        TextView day,text;
    }
}

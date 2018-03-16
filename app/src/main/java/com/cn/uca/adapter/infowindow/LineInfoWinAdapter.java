package com.cn.uca.adapter.infowindow;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.cn.uca.R;
import com.cn.uca.bean.home.raider.RaidersFoodBean;
import com.cn.uca.bean.home.raider.RaidersSenicSpotBean;
import com.cn.uca.bean.home.raider.RaidersUtilBean;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.raider.FindWayImpl;
import com.cn.uca.impl.yueka.PointClickImpl;
import com.cn.uca.ui.view.home.raider.SpotDetailActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Locale;


/**
 * Created by uca on 2017/9/23.
 * 地图上自定义的infowindow的适配器
 */
public class LineInfoWinAdapter implements AMap.InfoWindowAdapter{
    private Context context;
    private PointClickImpl click;
    public LineInfoWinAdapter(Context context,PointClickImpl click){
        this.context = context;
        this.click = click;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = null;
        RaidersUtilBean utilBean = (RaidersUtilBean) marker.getObject();
        switch (utilBean.getType()) {
            case "add":
                view = LayoutInflater.from(context).inflate(R.layout.yueka_point_infowindow, null);
                setViewAdd(marker,view);
                break;
            case "delete":
                view = LayoutInflater.from(context).inflate(R.layout.yueka_point_delete_infowindow, null);
                setViewDelete(marker,view);
                break;
        }
       return view;
    }
    @Override
    public View getInfoContents(Marker marker) {
        marker.hideInfoWindow();
        return null;
    }
    //这个方法根据自己的实体信息来进行相应控件的赋值
    private void setViewAdd(final Marker marker, View view) {
        RaidersUtilBean utilBean = (RaidersUtilBean) marker.getObject();
        TextView city = (TextView)view.findViewById(R.id.city);
        TextView place = (TextView)view.findViewById(R.id.place);
        TextView add = (TextView)view.findViewById(R.id.add);
        //实例
        final PlacesBean bean = (PlacesBean) utilBean.getObject();
        city.setText(bean.getPlace_name());
        place.setText(bean.getDeparture_address());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.addBack(bean.getPlace_name(),bean.getDeparture_address(),bean.getGaode_code(),bean.getLat(),bean.getLng());//添加路线点
            }
        });
    }

    private void setViewDelete(Marker marker,View view){
        RaidersUtilBean utilBean = (RaidersUtilBean) marker.getObject();
        TextView city = (TextView)view.findViewById(R.id.city);
        TextView place = (TextView)view.findViewById(R.id.place);
        TextView delete = (TextView)view.findViewById(R.id.delete);
        //实例
        final PlacesBean bean = (PlacesBean) utilBean.getObject();
        city.setText(bean.getPlace_name());
        place.setText(bean.getDeparture_address());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.deleteBack(2,bean.getPlace_id());//删除路线点
            }
        });
    }
}

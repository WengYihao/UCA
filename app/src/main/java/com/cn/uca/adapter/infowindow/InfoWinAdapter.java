package com.cn.uca.adapter.infowindow;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.cn.uca.R;
import com.cn.uca.bean.home.raider.RaidersFoodBean;
import com.cn.uca.bean.home.raider.RaidersUtilBean;
import com.cn.uca.bean.home.raider.RaidersSenicSpotBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.raider.FindWayImpl;
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
public class InfoWinAdapter implements AMap.InfoWindowAdapter{
    private Context context;
    TextToSpeech speech;
    private FindWayImpl findWay;
    public InfoWinAdapter(Context context,FindWayImpl findWay){
        this.context = context;
        this.findWay = findWay;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = null;
        RaidersUtilBean utilBean = (RaidersUtilBean) marker.getObject();
        switch (utilBean.getType()) {
            case "airport":
                ToastXutil.show("点我干嘛？");
                break;
            case "spot":
                view = LayoutInflater.from(context).inflate(R.layout.spot_infowindow, null);
                setViewSpot(marker,view);
                break;
            case "station":
                ToastXutil.show("没有啊！");
                break;
            case "food":
                view = LayoutInflater.from(context).inflate(R.layout.food_infowindow,null);
                setViewFood(marker,view);
                break;
        }
        Log.i("123","654321");
       return view;
    }
    @Override
    public View getInfoContents(Marker marker) {
        marker.hideInfoWindow();
        return null;
    }
    //这个方法根据自己的实体信息来进行相应控件的赋值
    private void setViewSpot(final Marker marker, View view) {
        RaidersUtilBean utilBean = (RaidersUtilBean) marker.getObject();
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView daohang = (TextView)view.findViewById(R.id.daohang);
        final TextView content = (TextView)view.findViewById(R.id.content);
        TextView details = (TextView)view.findViewById(R.id.detail);
        TextView commentary = (TextView)view.findViewById(R.id.commentary);
        ImageView pic = (ImageView) view.findViewById(R.id.pic);

        //实例
        final RaidersSenicSpotBean bean = (RaidersSenicSpotBean) utilBean.getObject();
        speech = new TextToSpeech(context,new MyOnInitialListener());
        name.setText(bean.getScenic_spot_name());
        content.setText(bean.getIntroduce());
        ImageLoader.getInstance().displayImage(bean.getPicture_url(),pic);
        SetLayoutParams.setLinearLayout(pic, MyApplication.width/3,MyApplication.width/5);
        SetLayoutParams.setLinearLayout(content,MyApplication.width/3,MyApplication.width/5-SystemUtil.dip2px(30));
        SetLayoutParams.setLinearLayout(commentary,(MyApplication.width/3-SystemUtil.dip2px(15))/2,SystemUtil.dip2px(20));
        SetLayoutParams.setLinearLayout(details,(MyApplication.width/3-SystemUtil.dip2px(15))/2,SystemUtil.dip2px(20));
        daohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findWay.click(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude));
            }
        });
        commentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speech.speak(bean.getIntroduce().toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastXutil.show("请求集合"+bean.getOrder());
                Intent intent = new Intent();
                intent.setClass(context, SpotDetailActivity.class);
                intent.putExtra("content",bean.getIntroduce());
                Log.i("123",bean.getIntroduce());
                intent.putExtra("name",bean.getScenic_spot_name());
                context.startActivity(intent);
            }
        });
    }
    class MyOnInitialListener implements TextToSpeech.OnInitListener{
        @Override
        public void onInit(int status) {
            speech.setLanguage(Locale.CHINESE);
        }
    }

    private void setViewFood(Marker marker,View view){
        RaidersUtilBean utilBean = (RaidersUtilBean) marker.getObject();
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView daohang = (TextView)view.findViewById(R.id.daohang);
        TextView content = (TextView)view.findViewById(R.id.content);

        //实例
        final RaidersFoodBean bean = (RaidersFoodBean) utilBean.getObject();
        name.setText(bean.getRaiders_food_name());
        content.setText(bean.getIntroduce());
        SetLayoutParams.setLinearLayout(content,MyApplication.width*2/3,MyApplication.width/3);
        daohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastXutil.show("待开发");
            }
        });
    }
}

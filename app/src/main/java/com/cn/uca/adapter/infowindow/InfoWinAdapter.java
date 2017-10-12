package com.cn.uca.adapter.infowindow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.cn.uca.R;
import com.cn.uca.bean.InfoWindowsBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.ToastXutil;
import android.support.annotation.NonNull;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by uca on 2017/9/23.
 * 地图上自定义的infowindow的适配器
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private Context mContext = MyApplication.getContext();
//    private InfoWindowsBean bean;
    private LatLng latLng;
    private ImageView pic;
    private TextView name,content,detail;
    private String agentName;
    private String snippet;

//    public InfoWinAdapter(Context context,InfoWindowsBean bean){
//        this.mContext = context;
//        this.bean = bean;
//    }

    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        Log.i("123","654321");
        return view;
    }
    @Override
    public View getInfoContents(Marker marker) {
        initData(marker);
        View view = initView();
        Log.i("123","123456");
        return view;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        snippet = marker.getSnippet();
        agentName = marker.getTitle();
    }

    @NonNull
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_infowindow, null);
        name = (TextView) view.findViewById(R.id.name);
        content = (TextView) view.findViewById(R.id.content);

        name.setText(agentName);
        content.setText(snippet);
//        ImageLoader.getInstance().displayImage(bean.getPic(),pic);

        detail.setOnClickListener(this);
//        call.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
//            case R.id.detail:  //点击导航
//                NavigationUtils.Navigation(latLng);
//                break;

            case R.id.detail:  //详情
                ToastXutil.show("哈哈");
                break;
        }
    }

}

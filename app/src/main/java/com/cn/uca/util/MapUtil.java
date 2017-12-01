package com.cn.uca.util;

import android.content.Context;
import android.graphics.Color;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cn.uca.R;

import java.util.List;

/**
 * Created by asus on 2017/10/17.
 */

public class MapUtil {
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    public static CircleOptions addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        return options;
    }
    public static MarkerOptions addMarker(LatLng latlng,Context context){
        BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        return options;
    }
    public static MarkerOptions addMarker(double lat, double lng, int id){
        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markOptiopns = new MarkerOptions().position(latLng).title("")
                .icon(BitmapDescriptorFactory.fromResource(id));
        return markOptiopns;
    }

    public static void removeMarker(List<Marker> list){
        if (list != null) {
            for (Marker marker : list) {
                marker.remove();
            }
        }
    }
}

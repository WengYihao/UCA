package com.cn.uca.util;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by asus on 2017/10/17.
 */

public class MapUtil {

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

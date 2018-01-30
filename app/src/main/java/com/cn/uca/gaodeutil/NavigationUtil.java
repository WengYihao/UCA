package com.cn.uca.gaodeutil;

import android.content.Context;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.maps.model.RoutePara;

/**
 * Created by asus on 2018/1/30.
 */

public class NavigationUtil {
    /**
     * 驾车导航
     * @param endLatlng
     * @param context
     */
    public static void driverNavi(LatLng endLatlng, Context context){
        NaviPara naviPara = new NaviPara();
        naviPara.setTargetPoint(endLatlng);//设置终点位置
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);//设置导航策略，这里是避免拥堵
        try {
            AMapUtils.openAMapNavi(naviPara, context);//调起高德地图驾车导航
        } catch (com.amap.api.maps.AMapException e) {
            AMapUtils.getLatestAMapApp(context);// 如果没安装会进入异常，调起下载页面
        }
    }

    /**
     * 步行导航
     * @param startLatlng
     * @param endLatlng
     * @param context
     */
    public static void walkNavi(LatLng startLatlng,LatLng endLatlng,Context context){
        RoutePara routePara = new RoutePara();
        routePara.setStartPoint(startLatlng);
//        routePara.setEndPoint(endLatlng);
        routePara.setStartName("起点");
        routePara.setEndName("终点");
        try {
            AMapUtils.openAMapWalkingRoute(routePara, context);//调起高德地图步行导航
//            AMapUtils.openAMapTransitRoute(routePara, context);
        } catch (com.amap.api.maps.AMapException e) {
            AMapUtils.getLatestAMapApp(context);// 如果没安装会进入异常，调起下载页面
        }
    }
}

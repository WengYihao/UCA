package com.cn.uca.ui.home;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cn.uca.R;
import com.cn.uca.adapter.infowindow.InfoWinAdapter;
import com.cn.uca.bean.InfoWindowsBean;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.ToastXutil;

import java.util.ArrayList;
import java.util.List;

public class RaidersDetailActivity extends AppCompatActivity implements AMap.OnMarkerClickListener{

    private AMap aMap;
    private MapView mapView;
    private boolean isShow = true;
    private List<Marker> list = new ArrayList<>();
    private InfoWinAdapter infoWinAdapter;
    Marker marker,marker1,marker2,marker3;
    private UiSettings uiSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raiders_detail);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initMap();
        aa();
    }

    private void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
            InfoWindowsBean bean4 = new InfoWindowsBean();
            bean4.setContent("美丽美丽，真的好美丽，不来看看嘛？");
            bean4.setName("宝安国际机场");
            bean4.setPic("http://www.szyouka.com/1.png");
//            infoWinAdapter = new InfoWinAdapter(RaidersDetailActivity.this,bean4);
            infoWinAdapter = new InfoWinAdapter();
            aMap.setOnMarkerClickListener(this);
            aMap.setInfoWindowAdapter(infoWinAdapter);

            addMarkerToMap(new LatLng(22.633144,113.814454),"成都","中国四川省成都市");
            uiSetting = aMap.getUiSettings();
            uiSetting.setZoomControlsEnabled(false);
            uiSetting.setTiltGesturesEnabled(false);
            uiSetting.setRotateGesturesEnabled(false);
//            List<LatLng> li = new ArrayList<>();
//            li.add(new LatLng(22.633144,113.814454));
//            li.add(new LatLng(22.463279,114.539602));
//            li.add(new LatLng(22.534607,113.972976));
//            li.add(new LatLng(22.48615,113.94683));
//            List<InfoWindowsBean> list = new ArrayList<>();
//            InfoWindowsBean bean = new InfoWindowsBean();
//            bean.setContent("美丽美丽，真的好美丽，不来看看嘛？");
//            bean.setName("宝安国际机场");
//            bean.setPic("http://www.szyouka.com/1.png");
//            list.add(bean);
//            InfoWindowsBean bean1 = new InfoWindowsBean();
//            bean1.setContent("eneneneneneneenenenene");
//            bean1.setName("aaaaaaaa");
//            bean1.setPic("http://www.szyouka.com/2.png");
//            list.add(bean1);
//            InfoWindowsBean bean2 = new InfoWindowsBean();
//            bean2.setContent("xxxxxxxxxxxxxxxxx");
//            bean2.setName("ccccccccc");
//            bean2.setPic("http://www.szyouka.com/3.png");
//            list.add(bean2);
//            InfoWindowsBean bean3 = new InfoWindowsBean();
//            bean3.setContent("bbbbbbbbbbbbbbb");
//            bean3.setName("rrrrrr");
//            bean3.setPic("http://www.szyouka.com/4.png");
//            list.add(bean3);
//            addMarkerToMap(li,list);
        }
    }
//    /**
//     * 隐藏
//     * @param list
//     */
//    public void Hide(List<Marker> list){
//        if (list != null) {
//            for (Marker marker : list) {
//                marker.setVisible(false);
//            }
//        }
//    }
//
//    public static void Show(List<Marker> list){
//        if (list != null) {
//            for (Marker marker : list) {
//                marker.setVisible(true);
//            }
//        }
//    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                    LatLng southwest = new LatLng(22.910333,114.706878);
                    LatLng northeast = new LatLng(22.403411,113.652191);
                    LatLngBounds bounds = new LatLngBounds.Builder().include(southwest).include(northeast).build();
                    aMap.addGroundOverlay(new GroundOverlayOptions().image(descriptor).positionFromBounds(bounds));
                    aMap.showMapText(false);
                    LatLngBounds latLngBounds = new LatLngBounds(northeast, southwest);
                    aMap.setMapStatusLimits(latLngBounds);
                    break;
                case 1:
                    ToastXutil.show("123");
                    break;
            }
        }
    };
    public void aa(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = GraphicsBitmapUtils.getbitmap("http://www.szyouka.com/shenzhen.png");
                if (bitmap != null) {
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

            }
        }).start();
//        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.map_sz);
//
//        LatLng southwest = new LatLng(22.910333,114.706878);
//        LatLng northeast = new LatLng(22.403411,113.652191);//锦绣中华
//        LatLngBounds bounds = new LatLngBounds.Builder().include(southwest).include(northeast).build();
//                    aMap.addGroundOverlay(new GroundOverlayOptions().image(descriptor).positionFromBounds(bounds)).setTransparency(0.5f);
//                    aMap.showMapText(false);

//         marker = (Marker) (aMap.addMarker(help_add_icon(22.633144,113.814454, R.mipmap.home_select)));
//         marker1 = (Marker)(aMap.addMarker(help_add_icon(22.463279,114.539602,R.mipmap.home_select)));
//         marker2 = (Marker)(aMap.addMarker(help_add_icon(22.534607,113.972976,R.mipmap.home_select)));
//         marker3 = (Marker)(aMap.addMarker(help_add_icon(22.48615,113.94683,R.mipmap.home_select)));
//        list.add(marker);
//        list.add(marker1);
//        list.add(marker2);
//        list.add(marker3);
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(8f));
    }
    public static MarkerOptions help_add_icon(double lat, double lng, int id){
        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markOptiopns = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(id));
        return markOptiopns;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ToastXutil.show("321");
//        if (marker.getPosition() == this.marker.getPosition()){
//            ShowPopupWindow.showSpot(mapView,RaidersDetailActivity.this,"宝安公园","漂亮哈哈哈哈","http://www.szyouka.com/1.png");
//        }else if (marker.getPosition() == this.marker1.getPosition()){
//            ShowPopupWindow.showSpot(mapView,RaidersDetailActivity.this,"灵芝公园","漂亮哈哈哈哈","http://www.szyouka.com/2.png");
//        }else if (marker.getPosition() == this.marker2.getPosition()){
//            ShowPopupWindow.showSpot(mapView,RaidersDetailActivity.this,"荔枝公园","漂亮哈哈哈哈","http://www.szyouka.com/3.png");
//        }else if (marker.getPosition() == this.marker3.getPosition()){
//            ShowPopupWindow.showSpot(mapView,RaidersDetailActivity.this,"嘻嘻公园","漂亮哈哈哈哈","http://www.szyouka.com/4.png");
//        }
        return false;
    }
    private void addMarkerToMap(List<LatLng> list, List<InfoWindowsBean> bean) {
        for (int i = 0;i<list.size();i++){
            aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(list.get(i))
                    .title(bean.get(i).getName())
                    .snippet(bean.get(i).getContent())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_select))
            );
        }
    }
    private void addMarkerToMap(LatLng latLng, String title, String snippet) {
        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_select))
        );
    }

}

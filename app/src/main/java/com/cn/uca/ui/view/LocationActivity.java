package com.cn.uca.ui.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.MapUtil;

public class LocationActivity extends BaseBackActivity implements  LocationSource,AMapLocationListener{

    private final static String TAG = "LocationActivity";
        private MapView mapView;
        private AMap aMap;
        private LocationSource.OnLocationChangedListener mListener;
        private AMapLocationClient mlocationClient;
        private AMapLocationClientOption mLocationOption;// 高德相关
        private static double lat,lng;
        private boolean isFirst = true;
        private Circle mCircle;
        private Marker mLocMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initMap();
    }

    private void initMap() {
        if (aMap == null){
            aMap = mapView.getMap();
        }
        setUpMap();
    }

    private void setUpMap() {
        aMap.setLocationSource(this);
//        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 跟随模式
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
//                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                if (isFirst){
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 15));//定位成功移到当前定位点
                    CircleOptions circleOptions = MapUtil.addCircle(
                            new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()),
                            aMapLocation.getAccuracy());
                    MarkerOptions options = MapUtil.addMarker(
                            new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), this);
                    mCircle = aMap.addCircle(circleOptions);// 添加定位精度圆
                    mLocMarker = aMap.addMarker(options);// 添加定位图标
                    isFirst = false;
                    lat = aMapLocation.getLatitude();
                    lng = aMapLocation.getLongitude();
                }
                Log.i("TAG",lat+"---"+lng);
            }else{
                Log.i("TAG",aMapLocation.getErrorCode()+"错误码"+aMapLocation.getErrorInfo()+"错误信息");
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null){
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);// 设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mlocationClient.setLocationOption(mLocationOption);// 设置为高精度定位模式
            mLocationOption.setInterval(1000);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        Log.i("TAG","123");
    }
}

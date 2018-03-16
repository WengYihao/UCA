package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.SearchResultAdapter;
import com.cn.uca.bean.home.lvpai.getAddressBean;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.MapUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.dialog.LoadDialog;

import java.util.ArrayList;
import java.util.List;

public class MapChoiceActivity extends BaseBackActivity implements View.OnClickListener,LocationSource,AMapLocationListener,
        AMap.OnMarkerDragListener,PoiSearch.OnPoiSearchListener,GeocodeSearch.OnGeocodeSearchListener {
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;// 高德相关
    private double lat,lng,selectLat,selectLng;
    private boolean isFirst = true;
    private TextView back;
    private Circle mCircle;
    private Marker mLocMarker;
    private RelativeLayout seachLayout;
    private String city;
    private Marker screenMarker = null;//屏幕中心点
    private LatLonPoint lp;//初始周边搜索点(定位点)
    private PoiSearch.Query query;// Poi查询条件类
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch poiSearch;//搜索结果
    private List<PoiItem> poiItems;// poi数据
    private GeocodeSearch geocoderSearch;
    private ListView listView;
    private SearchResultAdapter adapter;
    private TextView address;
    private TextView location;
    private TextView finish;
    private EditText supplement;
    private String code,type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_choice);
        com.amap.api.maps.MapsInitializer.loadWorldGridMap(true);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        getInfo();
        initView();

    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            type = intent.getStringExtra("type");
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);

        address = (TextView)findViewById(R.id.address);
        seachLayout = (RelativeLayout)findViewById(R.id.seachLayout);
        location = (TextView)findViewById(R.id.location);
        finish = (TextView)findViewById(R.id.finish);
        supplement = (EditText)findViewById(R.id.supplement);
        location.setOnClickListener(this);
        seachLayout.setOnClickListener(this);
        finish.setOnClickListener(this);
        poiItems = new ArrayList<>();
        listView = (ListView)findViewById(R.id.listView);
        adapter = new SearchResultAdapter(poiItems,this);
        listView.setAdapter(adapter);
        initMap();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double lat = poiItems.get(position).getLatLonPoint().getLatitude();
                double lng = poiItems.get(position).getLatLonPoint().getLongitude();
                getAddress(lat,lng);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 15));//定位成功移到当前定位点
            }
        });
    }

    private void initMap() {
        if (aMap == null){
            aMap = mapView.getMap();
        }
        setUpMap();
    }

    private void setUpMap() {
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.getUiSettings().setZoomControlsEnabled(false);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter();
            }
        });

        // 设置可视范围变化时的回调的接口方法
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {}

            @Override
            public void onCameraChangeFinish(CameraPosition postion) {
                //屏幕中心的Marker跳动
                selectLat = postion.target.latitude;
                selectLng = postion.target.longitude;
                doSearchQuery(selectLat,selectLng);
                getAddress(selectLat,selectLng);

            }
        });
    }
    /**
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        screenMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f,0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.purple_pin)));
        //设置Marker在屏幕上,不跟随地图移动
        screenMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
    }

    public void getAddress(double lat,double lng) {
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat,lng), 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(double lat,double lng) {
        LoadDialog.show(this);
        currentPage = 0;
        query = new PoiSearch.Query("", "地点", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        lp = new LatLonPoint(lat,lng);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
        // 设置搜索区域为以lp点为圆心，其周围5000米范围
        poiSearch.searchPOIAsyn();// 异步搜索
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.seachLayout:
                Intent intent = new Intent();
                intent.setClass(MapChoiceActivity.this,SearchResultActivity.class);
                intent.putExtra("city",city);
                startActivityForResult(intent,0);
                break;
            case R.id.location:
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));//定位成功移到当前定位点
                break;
            case R.id.finish:
//                ToastXutil.show("地址:"+address.getText().toString()+"-"+"坐标："+selectLat+","+selectLng);
                switch (type){
                    case "lvpai":
                        getAddressBean bean = new getAddressBean();
                        bean.setAddress(address.getText().toString()+supplement.getText().toString());
                        bean.setGaode_code(code);
                        Intent intent1 = new Intent();
                        intent1.putExtra("address", bean);
                        setResult(0,intent1);
                        this.finish();
                        break;
                    case "samecityka":
                        Intent intent2 = new Intent();
                        intent2.putExtra("lat",selectLat);
                        intent2.putExtra("lng",selectLng);
                        intent2.putExtra("code",code);
                        intent2.putExtra("address",address.getText().toString()+supplement.getText().toString());
                        setResult(7,intent2);
                        this.finish();
                        break;
                }

                break;
        }
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
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
                    city = aMapLocation.getCity().substring(0,aMapLocation.getCity().indexOf("市"));
                    lat = aMapLocation.getLatitude();
                    lng = aMapLocation.getLongitude();
                    doSearchQuery(lat,lng);
                    address.setText(aMapLocation.getAddress());
                }
            }else{
                mCircle.setCenter(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                mCircle.setRadius(aMapLocation.getAccuracy());
            }
        }else{
            Log.i("456",aMapLocation.getErrorCode()+"错误码"+aMapLocation.getErrorInfo()+"错误信息");
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
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        LoadDialog.dismiss(this);
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                poiItems.clear();
                poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                if (poiItems != null && poiItems.size() > 0) {
                    adapter.setList(poiItems);
                }else {
                    ToastXutil.show("对不起，没有搜索到相关数据！");
                }
            } else {
                ToastXutil.show("对不起，没有搜索到相关数据！");
            }
        } else  {
            ToastXutil.show(rcode+"");
        }
    }

    /**
     * 逆地理编码
     * @param poiItem
     * @param i
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 逆地理编码
     * @param result
     * @param rCode
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                Log.i("456","1111");
                address.setText(result.getRegeocodeAddress().getFormatAddress());
                code = result.getRegeocodeAddress().getCityCode();
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (data != null){
                double lat = data.getDoubleExtra("lat",0);
                double lng = data.getDoubleExtra("lng",0);
                getAddress(lat,lng);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));//定位成功移到当前定位点
            }
        }
    }
}

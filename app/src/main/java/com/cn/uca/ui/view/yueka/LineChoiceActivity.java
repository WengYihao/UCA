package com.cn.uca.ui.view.yueka;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.SearchResultAdapter;
import com.cn.uca.adapter.infowindow.LineInfoWinAdapter;
import com.cn.uca.adapter.yueka.YuekaLineAdapter;
import com.cn.uca.bean.home.raider.RaidersUtilBean;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.PointClickImpl;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.MapUtil;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LineChoiceActivity extends BaseBackActivity implements AMap.OnMarkerClickListener,AMap.OnMapClickListener,LocationSource,AMapLocationListener,GeocodeSearch.OnGeocodeSearchListener,View.OnClickListener,AMap.OnMarkerDragListener,PoiSearch.OnPoiSearchListener,PointClickImpl{

    private MapView mapView;
    private AMap aMap;
    private MyEditText search;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;// 高德相关
    private boolean isFirst = true;
    private static double lat,lng;
    private Marker screenMarker = null;
    private UiSettings mUiSettings;
    private List<PlacesBean> listShow;
    private TextView finish,back;
    private Circle mCircle;
    private Marker mLocMarker;
    private String keyWord = "";
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private SearchResultAdapter adapter;
    private List<PoiItem> list;
    private String city;//当前定位城市
    private ListView listView;
    private YuekaLineAdapter lineAdapter;
    private ListView lineList;
    private Marker curShowWindowMarker;
    private PolylineOptions polt;
    private Polyline polyline;
    private List<Marker> listMarker = new ArrayList<>();
    private int id;//路线id
    private Marker searchMarker;
    private GeocodeSearch geocoderSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_choice);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        initView();
        initMap();
        getInfo();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
            listShow  = intent.getParcelableArrayListExtra("places");
            if (listShow != null){
                if (listShow.size() > 0){
                    lineAdapter.setList(listShow);
                    refresh(listShow);
                }
            }
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        search = (MyEditText)findViewById(R.id.search);
        lineList = (ListView)findViewById(R.id.lineList);
        finish = (TextView)findViewById(R.id.finish);
        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new SearchResultAdapter(list,this);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
        finish.setOnClickListener(this);
        listMarker =  new ArrayList<>();
        listShow = new ArrayList<>();

        lineAdapter = new YuekaLineAdapter(listShow,this,this);
        lineList.setAdapter(lineAdapter);
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(LineChoiceActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    keyWord = StringXutil.checkEditText(search);
                    if (!"".equals(keyWord)) {
                        doSearchQuery("");
                    }
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchMarker != null){
                    searchMarker.remove();
                }
                double lat = list.get(position).getLatLonPoint().getLatitude();
                double lng = list.get(position).getLatLonPoint().getLongitude();
                searchMarker = aMap.addMarker(MapUtil.addMarker(lat,lng,R.mipmap.icon_point1));
                RaidersUtilBean utilBean = new RaidersUtilBean();
                utilBean.setType("add");
                PlacesBean bean = new PlacesBean();
                bean.setPlace_name(list.get(position).getCityName());
                bean.setDeparture_address(list.get(position).getTitle());
                bean.setLat(lat);
                bean.setLng(lng);
                utilBean.setObject(bean);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));//定位成功移到当前定位点
                searchMarker.setObject(utilBean);
                searchMarker.showInfoWindow();
                listView.setVisibility(View.GONE);
            }
        });
    }
    protected void doSearchQuery(String city) {
        Log.e("456","********"+keyWord);
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (curShowWindowMarker.isInfoWindowShown()){
            curShowWindowMarker.hideInfoWindow();
        }
        if (screenMarker.isInfoWindowShown()){
            screenMarker.hideInfoWindow();
        }
        if (searchMarker.isInfoWindowShown()){
            searchMarker.hideInfoWindow();
        }
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
    private void initMap() {
        if (aMap == null){
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
        }
        setUpMap();
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter();
            }
        });

        // 设置可视范围变化时的回调的接口方法
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition postion) {
                //屏幕中心的Marker跳动
//                Log.i("123",postion.target.latitude+"---"+postion.target.longitude);
                if (searchMarker != null){
                    searchMarker.remove();
                }
                //屏幕中心的Marker跳动
                double lat = postion.target.latitude;
                double lng = postion.target.longitude;
                searchMarker = aMap.addMarker(MapUtil.addMarker(lat,lng,R.mipmap.icon_point1));
                getAddressByLatlng(new LatLng(lat,lng));
            }
        });
    }

    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocoderSearch.getFromLocationAsyn(query);
    }

    private void setUpMap() {
        aMap.setLocationSource(this);
        mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);// 缩放按钮右中显示
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerDragListener(this);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(new LineInfoWinAdapter(this,this));
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
                if (isFirst){
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 10));//定位成功移到当前定位点
                    CircleOptions circleOptions = MapUtil.addCircle(
                            new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()),
                            aMapLocation.getAccuracy());
                    MarkerOptions options = MapUtil.addMarker(
                            new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), this);
                    mCircle = aMap.addCircle(circleOptions);// 添加定位精度圆
                    mLocMarker = aMap.addMarker(options);// 添加定位图标
                    isFirst = false;
                }
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
            }else{
                mCircle.setCenter(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                mCircle.setRadius(aMapLocation.getAccuracy());
                Log.i("457",aMapLocation.getErrorCode()+"错误码"+aMapLocation.getErrorInfo()+"错误信息");
            }
        }
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.finish:
                setResult(0,new Intent());
                this.finish();
                break;

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
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                poiResult = result;
                // 取得搜索到的poiitems有多少页
                list.clear();
                list = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                if (list.size() > 0){
                    listView.setVisibility(View.VISIBLE);
                    adapter.setList(list);
                }else{
                    doSearchQuery("深圳");
                }
            }
        } else {
            ToastXutil.show(rCode+"");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        curShowWindowMarker = marker;
        return true;
    }

    @Override
    public void deleteBack(int type, int id) {
        deletePlace(id);
    }

    @Override
    public void addBack(String city, String place,String code, double lat, double lng) {
        addPlace(city, place, code,lat, lng);
    }
    //删除路线点
    private void deletePlace(final int id){
        String token = SharePreferenceXutil.getAccountToken();
        YueKaHttp.deletePlace(token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                if ((int)response == 0){
                    ToastXutil.show("删除成功");
                    for (int i = 0;i <listShow.size();i++){
                        if (listShow.get(i).getPlace_id() == id){
                            listShow.remove(i);
                            lineAdapter.setList(listShow);
                            refresh(listShow);
                        }
                    }
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    //添加路线点
    private void addPlace(String city,String place,String code,double lat,double lng){
        String token = SharePreferenceXutil.getAccountToken();
        YueKaHttp.addPlace(token, city , id,code,place, lat, lng, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case  0:
                            ToastXutil.show("添加成功");
                            Gson gson = new Gson();
                            PlacesBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<PlacesBean>() {
                            }.getType());
                            listShow.add(bean);
                            lineAdapter.setList(listShow);
                            refresh(listShow);
                            if (searchMarker.isInfoWindowShown()){
                                searchMarker.hideInfoWindow();
                                searchMarker.remove();
                            }
                            break;
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                ToastXutil.show(errorMsg);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    //刷新地图点、线路
    private void refresh(List<PlacesBean> list){
        polt = new PolylineOptions();
        if (listMarker.size() != 0){//移除路线点marker
            MapUtil.removeMarker(listMarker);
            listMarker.clear();
        }
        if (polyline != null){
            polyline.remove();
        }
        for (int a = 0;a < list.size();a++){
            Marker marker = null;
            switch (a){
                case 0:
                    marker = aMap.addMarker(MapUtil.addMarker(list.get(a).getLat(),list.get(a).getLng(),R.mipmap.icon_point1));
                    break;
                case 1:
                    marker = aMap.addMarker(MapUtil.addMarker(list.get(a).getLat(),list.get(a).getLng(),R.mipmap.icon_point2));
                    break;
                case 2:
                    marker = aMap.addMarker(MapUtil.addMarker(list.get(a).getLat(),list.get(a).getLng(),R.mipmap.icon_point3));
                    break;
                case 3:
                    marker = aMap.addMarker(MapUtil.addMarker(list.get(a).getLat(),list.get(a).getLng(),R.mipmap.icon_point4));
                    break;
                case 4:
                    marker = aMap.addMarker(MapUtil.addMarker(list.get(a).getLat(),list.get(a).getLng(),R.mipmap.icon_point5));
                    break;
                case 5:
                    marker = aMap.addMarker(MapUtil.addMarker(list.get(a).getLat(),list.get(a).getLng(),R.mipmap.icon_point6));
                    break;
            }
            listMarker.add(marker);
            RaidersUtilBean utilBean = new RaidersUtilBean();
            utilBean.setType("delete");
            utilBean.setObject(list.get(a));
            marker.setObject(utilBean);
            polt.add(new LatLng(list.get(a).getLat(),list.get(a).getLng()));
        }
        polt.width(10).geodesic(true).color(Color.BLACK);
        polyline =  aMap.addPolyline(polt);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        RaidersUtilBean utilBean = new RaidersUtilBean();
        utilBean.setType("add");
        PlacesBean bean = new PlacesBean();
        bean.setGaode_code(regeocodeAddress.getCityCode());
        bean.setPlace_name(regeocodeAddress.getPois().get(0).getTitle());
        bean.setDeparture_address(regeocodeAddress.getPois().get(0).getSnippet());
        bean.setLat(searchMarker.getPosition().latitude);
        bean.setLng(searchMarker.getPosition().longitude);
        utilBean.setObject(bean);
        searchMarker.setObject(utilBean);
        searchMarker.showInfoWindow();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}

package com.cn.uca.ui.view.home.raider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.cn.uca.R;
import com.cn.uca.adapter.infowindow.InfoWinAdapter;
import com.cn.uca.bean.home.raider.RaidersUtilBean;
import com.cn.uca.bean.home.raider.RaidersAirportBean;
import com.cn.uca.bean.home.raider.RaidersDetailsBean;
import com.cn.uca.bean.home.raider.RaidersFoodBean;
import com.cn.uca.bean.home.raider.RaidersTrainStationBean;
import com.cn.uca.bean.home.raider.RaidersSenicSpotBean;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.MapUtil;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaidersDetailActivity extends BaseBackActivity implements View.OnClickListener,AMap.OnMarkerClickListener,AMap.OnMapClickListener{

    private AMap aMap;
    private MapView mapView;
    private UiSettings uiSetting;
    private TextView back,food,line,spot,raider,lixian,fankui;
    private int id;
    private String name;
    private List<RaidersAirportBean> airportList;
    private List<RaidersSenicSpotBean> senicSpotList;
    private List<RaidersFoodBean> foodList;
    private List<RaidersTrainStationBean> trainStationList;
    private LatLng southwest,northeast;
    private boolean isShowFood = true;
    private boolean isShowLine = true;
    private List<Marker> foodMarker;
    private Marker curShowWindowMarker;
    private LatLngBounds latLngBounds;
    private List<LatLng> lineList;
    private PolylineOptions polt = new PolylineOptions();
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_raiders_detail);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        getInfo();
        initView();
        initMap();
    }

    private void getInfo(){
        Intent intent  = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
            name = intent.getStringExtra("cityName");
        }
    }

    private void getCityRaidersInfo(String account_token,String time_stamp,int city_raiders_id){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("city_raiders_id",city_raiders_id);
        String sign = SignUtil.sign(map);

        HomeHttp.getCityRaidersInfo(account_token, sign, time_stamp, city_raiders_id, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200) {
                        JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                JSONObject object = jsonObject.getJSONObject("data");
                                Gson gson = new Gson();
                                RaidersDetailsBean bean = gson.fromJson(object.toString(),new TypeToken<RaidersDetailsBean>() {
                                }.getType());
                                southwest = new LatLng(bean.getLeft_lower_lat(),bean.getLeft_lower_lng());
                                northeast = new LatLng(bean.getUpper_right_lat(),bean.getUpper_right_lng());
                                airportList = bean.getAirportRets();
                                senicSpotList = bean.getScenicSpotRets();
                                foodList = bean.getFoodRets();
                                trainStationList = bean.getTrainStationRets();
                                for (int a = 0;a < airportList.size();a++){
                                    Marker marker = aMap.addMarker(MapUtil.addMarker(airportList.get(a).getLat(),airportList.get(a).getLng(),R.mipmap.airport_back));
                                    RaidersUtilBean utilBean = new RaidersUtilBean();
                                    utilBean.setType("airport");
                                    utilBean.setObject(airportList.get(a));
                                    marker.setObject(utilBean);
                                }
                                for (int a = 0;a < senicSpotList.size();a++){
                                    Marker marker = aMap.addMarker(MapUtil.addMarker(senicSpotList.get(a).getLat(),senicSpotList.get(a).getLng(),R.mipmap.spot_back));
                                    RaidersUtilBean utilBean = new RaidersUtilBean();
                                    utilBean.setType("spot");
                                    utilBean.setObject(senicSpotList.get(a));
                                    marker.setObject(utilBean);
                                }
                                for (int a = 0;a < trainStationList.size();a++){
                                    Marker marker = aMap.addMarker(MapUtil.addMarker(trainStationList.get(a).getLat(),trainStationList.get(a).getLng(),R.mipmap.station_back));
                                    RaidersUtilBean utilBean = new RaidersUtilBean();
                                    utilBean.setType("station");
                                    utilBean.setObject(trainStationList.get(a));
                                    marker.setObject(utilBean);
                                }
                                toBitmap(bean.getMap_picture_url());
                                break;
                        }
                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage());
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    Log.i("456",i+ new JSONObject(new String(bytes)).toString());
                }catch (Exception e){

                }

            }
        });
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        food = (TextView)findViewById(R.id.food);
        spot = (TextView)findViewById(R.id.spot);
        line = (TextView)findViewById(R.id.line);
        raider = (TextView)findViewById(R.id.raiders);
        lixian = (TextView)findViewById(R.id.lixian);
        fankui = (TextView)findViewById(R.id.fankui);

        back.setOnClickListener(this);
        food.setOnClickListener(this);
        spot.setOnClickListener(this);
        line.setOnClickListener(this);
        raider.setOnClickListener(this);
        lixian.setOnClickListener(this);
        fankui.setOnClickListener(this);
        airportList = new ArrayList<>();
        senicSpotList = new ArrayList<>();
        foodList = new ArrayList<>();
        trainStationList = new ArrayList<>();
        foodMarker = new ArrayList<>();
        lineList = new ArrayList<>();

        getCityRaidersInfo(SharePreferenceXutil.getAccountToken(), SystemUtil.getCurrentDate2(),id);
    }
    private void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(this);
            aMap.setOnMapClickListener(this);
            aMap.setInfoWindowAdapter(new InfoWinAdapter(RaidersDetailActivity.this));
            uiSetting = aMap.getUiSettings();
            uiSetting.setZoomControlsEnabled(false);//隐藏放大缩小控件、不允许缩小、旋转
            uiSetting.setTiltGesturesEnabled(false);//是否倾斜
            uiSetting.setRotateGesturesEnabled(false);//是否旋转
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                    LatLngBounds bounds = new LatLngBounds.Builder().include(southwest).include(northeast).build();
                    aMap.addGroundOverlay(new GroundOverlayOptions().image(descriptor).positionFromBounds(bounds));
                    aMap.showMapText(false);
                    latLngBounds = new LatLngBounds(southwest,northeast);//限制地图显示左下、右上点坐标，顺序不能换
                    aMap.setMapStatusLimits(latLngBounds);
                    break;
                case 1:
                    ToastXutil.show("加载失败");
                    break;
            }
        }
    };
    public void toBitmap(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = GraphicsBitmapUtils.getbitmap(url);
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
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        curShowWindowMarker = marker;
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.food:
                if (isShowFood){
                    food.setBackgroundResource(R.mipmap.food_select);
                    for (int a = 0;a < foodList.size();a++){
                        Marker marker = aMap.addMarker(MapUtil.addMarker(foodList.get(a).getLat(),foodList.get(a).getLng(),R.mipmap.food_back));
                        foodMarker.add(marker);
                        RaidersUtilBean utilBean = new RaidersUtilBean();
                        utilBean.setType("food");
                        utilBean.setObject(foodList.get(a));
                        marker.setObject(utilBean);
                    }
                    isShowFood = false;
                }else{
                    food.setBackgroundResource(R.mipmap.food_normal);
                    MapUtil.removeMarker(foodMarker);
                    foodMarker.clear();
                    isShowFood = true;
                }
                break;
            case R.id.spot:
                //景点列表
                ToastXutil.show("等等我，马上到");
                break;
            case R.id.raiders:
                //攻略
                ToastXutil.show("等等我，马上到");
                break;
            case R.id.line:
                if (isShowLine){
                    if (lineList.size() > 0){
                        polyline.setVisible(true);
                    }else{
                        for (RaidersSenicSpotBean in:senicSpotList) {
                            lineList.add(new LatLng(in.getLat(),in.getLng()));
                        }
                        for(int i=0;i<lineList.size();i++){
                            polt.add(lineList.get(i));
                        }
                        polt.width(10).geodesic(true).color(Color.RED);
                        polyline =  aMap.addPolyline(polt);
                    }
                    isShowLine = false;
                }else{
                    polyline.setVisible(false);
                    isShowLine = true;
                }
                break;
            case R.id.lixian:
                ToastXutil.show("离线");
                break;
            case R.id.fankui:
                ToastXutil.show("反馈");
                break;
        }
    }
    @Override
    public void onMapClick(LatLng latLng) {
        if (aMap != null && curShowWindowMarker != null) {
            if (curShowWindowMarker.isInfoWindowShown()){
                curShowWindowMarker.hideInfoWindow();
            }
        }
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
        aMap.setMapStatusLimits(latLngBounds);
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
    }
}
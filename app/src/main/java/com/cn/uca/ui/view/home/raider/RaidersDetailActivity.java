package com.cn.uca.ui.view.home.raider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
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
import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.raiders.SpotNameAdapter;
import com.cn.uca.adapter.infowindow.InfoWinAdapter;
import com.cn.uca.bean.home.raider.RaidersUtilBean;
import com.cn.uca.bean.home.raider.RaidersAirportBean;
import com.cn.uca.bean.home.raider.RaidersDetailsBean;
import com.cn.uca.bean.home.raider.RaidersFoodBean;
import com.cn.uca.bean.home.raider.RaidersTrainStationBean;
import com.cn.uca.bean.home.raider.RaidersSenicSpotBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.gaodeutil.NavigationUtil;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.raider.FindWayImpl;
import com.cn.uca.impl.raider.RouteImpl;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.home.yusheng.YuShengDetailsActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.GraphicsBitmapUtils;
import com.cn.uca.util.MapUtil;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
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

public class RaidersDetailActivity extends BaseBackActivity implements LocationSource,AMapLocationListener,View.OnClickListener,AMap.OnMarkerClickListener,AMap.OnMapClickListener,RouteImpl,FindWayImpl {

    private AMap aMap;
    private MapView mapView;
    private UiSettings uiSetting;
    private TextView back,share,food,line,spot,raider,lixian,fankui;
    private int id;
    private String name;
    private List<RaidersAirportBean> airportList;
    private List<RaidersSenicSpotBean> senicSpotList,list;
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
    private String url;
    private SpotNameAdapter adapter;
    private Dialog dialog;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;// 高德相关
    private static double lat,lng;
//    private PointBean pointBean,pointBean1;//弹窗
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
                                url = bean.getIntroduce_url();
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
        share = (TextView)findViewById(R.id.share);
        food = (TextView)findViewById(R.id.food);
        spot = (TextView)findViewById(R.id.spot);
        line = (TextView)findViewById(R.id.line);
        raider = (TextView)findViewById(R.id.raiders);
        lixian = (TextView)findViewById(R.id.lixian);
        fankui = (TextView)findViewById(R.id.fankui);

        back.setOnClickListener(this);
        share.setOnClickListener(this);
        food.setOnClickListener(this);
        spot.setOnClickListener(this);
        line.setOnClickListener(this);
        raider.setOnClickListener(this);
        lixian.setOnClickListener(this);
        fankui.setOnClickListener(this);
        airportList = new ArrayList<>();
        list = new ArrayList<>();
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
            aMap.setLocationSource(this);
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setOnMarkerClickListener(this);
            aMap.setOnMapClickListener(this);
            aMap.setInfoWindowAdapter(new InfoWinAdapter(RaidersDetailActivity.this,this));
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
            case R.id.share:
                getShare();
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
                showCityDialog();
                break;
            case R.id.raiders:
                //攻略
                Intent intent = new Intent();
                intent.setClass(RaidersDetailActivity.this,CityRaiderDetailActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
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
                        polt.width(20).geodesic(true).color(Color.RED);
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


    private void getShare(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("shareType","GL");
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("id",id);
        String sign = SignUtil.sign(map);
        UserHttp.getShare(account_token, time_stamp, sign, "GL",id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            String share_title = jsonObject.getJSONObject("data").getString("share_title");
                            String web_url = jsonObject.getJSONObject("data").getString("web_url");
                            WeChatManager.instance().sendWebPageToWX(RaidersDetailActivity.this,true,web_url,R.mipmap.logo,share_title,"快来围观一下我的余生呗！");
                            break;
                        default:
                            ToastXutil.show("分享失败");
                            break;
                    }
                }catch (Exception e){

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
    Dialog dialog2;
    View inflate;
    ListView listView;
    private void showCityDialog(){
        startName = null;
        endName = null;
        if (list != null){
            list.clear();
        }
        list.addAll(senicSpotList);
        for (RaidersSenicSpotBean bean :list){
            bean.setState(0);
        }
        dialog2 = new Dialog(this,R.style.dialog_style);
        inflate = LayoutInflater.from(this).inflate(R.layout.choose_spot_dialog, null);
        listView = (ListView)inflate.findViewById(R.id.listView);
        adapter = new SpotNameAdapter(senicSpotList,this,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowPopupWindow.spotDetail(RaidersDetailActivity.this,list.get(i).getIntroduce());
//                Intent intent = new Intent();
//                intent.setClass(RaidersDetailActivity.this,SpotDetailActivity.class);
//                intent.putExtra("content",senicSpotList.get(i).getIntroduce());
//                intent.putExtra("name",senicSpotList.get(i).getScenic_spot_name());
//                startActivity(intent);
                dialog2.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog2.setContentView(inflate);
//        dialog.setCanceledOnTouchOutside(true);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog2.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.dimAmount = 0f;
        params.width = MyApplication.width;
//        params.height = MyApplication.height;
        params.height = MyApplication.height*5/9;
        params.x = 0;
        params.y = SystemUtil.dip2px(45);
        dialog2.show();//显示对话框
    }
    @Override
    public void onMapClick(LatLng latLng) {
        if (aMap != null && curShowWindowMarker != null) {
            if (curShowWindowMarker.isInfoWindowShown()){
                curShowWindowMarker.hideInfoWindow();
            }
        }
    }
    //方法必须重写
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    //方法必须重写
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        aMap.setMapStatusLimits(latLngBounds);
    }


    //方法必须重写
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    //方法必须重写
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    String startName = null;
    String endName = null;
    LatLng start = null;
    LatLng end = null;
    @Override
    public void start(View v) {
        Log.e("456",(int)v.getTag()+"--");
        if (startName == null){
            for (RaidersSenicSpotBean bean : list){
                bean.setState(2);//选择了起点
            }
            list.get((int)v.getTag()).setState(1);//起点
            startName = list.get((int)v.getTag()).getScenic_spot_name();
            start = new LatLng(list.get((int)v.getTag()).getLat(),list.get((int)v.getTag()).getLng());
            adapter.setList(list);
        }else{
            //选择终点
            list.get((int)v.getTag()).setState(3);//终点
            adapter.setList(list);
            endName = list.get((int)v.getTag()).getScenic_spot_name();
            end = new LatLng(list.get((int)v.getTag()).getLat(),list.get((int)v.getTag()).getLng());
            show2(start,end);
            dialog2.dismiss();
//            ToastXutil.show(startName+"-"+endName);
//            Intent intent = new Intent();
//            intent.setClass(RaidersDetailActivity.this,RouteActivity.class);
//            intent.putExtra("start",startName);
//            intent.putExtra("end",endName);
//            startActivity(intent);
//            dialog2.dismiss();
        }
    }
    private void show(final LatLng latLng){
       dialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
       View inflate = LayoutInflater.from(this).inflate(R.layout.navi_type_dialog, null);
       LinearLayout walk = (LinearLayout)inflate.findViewById(R.id.walk);
        LinearLayout driver = (LinearLayout)inflate.findViewById(R.id.driver);
        LinearLayout bus = (LinearLayout)inflate.findViewById(R.id.bus);
        TextView cancel = (TextView)inflate.findViewById(R.id.cancel);
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NavigationUtil.walkNavi(new LatLng(lat,lng),latLng,RaidersDetailActivity.this);
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               NavigationUtil.driverNavi(latLng,RaidersDetailActivity.this);
            }
        });

        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NavigationUtil.busNavi(new LatLng(lat,lng),latLng,RaidersDetailActivity.this);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialogWindow.setAttributes(params);
        StatusMargin.setFrameLayoutBottom(RaidersDetailActivity.this,inflate,0);
        dialog.show();//显示对话框
    }

    private void show2(final LatLng start,final LatLng end){
        dialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.navi_type_dialog, null);
        LinearLayout walk = (LinearLayout)inflate.findViewById(R.id.walk);
        LinearLayout driver = (LinearLayout)inflate.findViewById(R.id.driver);
        LinearLayout bus = (LinearLayout)inflate.findViewById(R.id.bus);
        TextView cancel = (TextView)inflate.findViewById(R.id.cancel);
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NavigationUtil.walkNavi(start,end,RaidersDetailActivity.this);
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NavigationUtil.driverNavi(start,RaidersDetailActivity.this);
            }
        });

        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NavigationUtil.busNavi(start,end,RaidersDetailActivity.this);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialogWindow.setAttributes(params);
        StatusMargin.setFrameLayoutBottom(RaidersDetailActivity.this,inflate,0);
        dialog.show();//显示对话框
    }
    @Override
    public void click(LatLng latLng) {
        show(latLng);
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
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

}
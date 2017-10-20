package com.cn.uca.ui.view.yueka;

import android.content.Intent;
import android.graphics.Point;
import android.os.Parcelable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cn.uca.R;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LineChoiceActivity extends BaseBackActivity implements  LocationSource,AMapLocationListener,View.OnClickListener,AMap.OnMarkerDragListener{

    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;// 高德相关
    private boolean isFirst = true;
    private static double lat,lng;
    private RelativeLayout addView;
    private TextView add;
    private Map<Integer,List<TextView>> mapTextView;
    private Map<Integer,List<Marker>> mapMarker;
    private Map<Integer,List<EditText>> mapEditText;
    private List<TextView> listTextView;
    private List<Marker> listMarker;
    private List<EditText> listEditText;
    private Marker screenMarker = null;
    Marker markerC;
    private UiSettings mUiSettings;
    private List<PlacesBean> listAdd,listShow,listAll;
    private int id,clickViewId;
    private TextView finish,delete,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
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
            if (listShow.size() > 0){
                for (int i = 0; i < listShow.size();i++){
                    final  TextView textView = new TextView(this);
                    setAddView(textView,1);
                }
            }
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        addView = (RelativeLayout)findViewById(R.id.addView);
        add = (TextView)findViewById(R.id.add);
        finish = (TextView)findViewById(R.id.finish);
        delete = (TextView)findViewById(R.id.delete);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        finish.setOnClickListener(this);
        delete.setOnClickListener(this);
        RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) add.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.setMargins(SystemUtil.dipTopx(this,20),0,0,SystemUtil.dipTopx(this,200));
        add.setLayoutParams(linearParams);
        mapTextView = new HashMap<>();
        mapEditText = new HashMap<>();
        mapMarker = new HashMap<>();
        listTextView = new ArrayList<>();
        listMarker =  new ArrayList<>();
        listEditText = new ArrayList<>();
        listAdd = new ArrayList<>();
        listShow = new ArrayList<>();
        listAll = new ArrayList<>();
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
            }
        });
    }

    private void setUpMap() {
        aMap.setLocationSource(this);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 跟随模式
        mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);// 缩放按钮右中显示
        aMap.setOnMarkerDragListener(this);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                if (isFirst){
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 15));//定位成功移到当前定位点
                    isFirst = false;
                }
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
            }else{
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
            case R.id.add:
                TextView textView = new TextView(this);
                setAddView(textView,2);
                break;
            case R.id.finish:
                addLinePoint();
                break;
            case R.id.delete:
                ToastXutil.show(clickViewId+"---");
                deletePoint(clickViewId);
                break;

        }
    }

    /**
     * 添加按钮
     * @param view1
     * @param type
     */
    private void setAddView(final TextView view1,int type){
        if (mapTextView.size() <= 5){
            listTextView.add(view1);
            view1.setId(listTextView.size());
            switch (type){
                case 1:
                    view1.setText(listShow.get(listTextView.size()-1).getDeparture_address());
                    Marker maker = aMap.addMarker(new MarkerOptions().position(new LatLng(listShow.get(listTextView.size()-1).getLat(),listShow.get(listTextView.size()-1).getLng()))
                            .title("地点："+listTextView.size())
                            .snippet(listShow.get(listTextView.size()-1).getDeparture_address())
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    listMarker.add(maker);
                    mapMarker.put(listTextView.size(),listMarker);
                    break;
                case 2:
                    view1.setText(listTextView.size()+"");
                    break;
            }
            mapTextView.put(listTextView.size(),listTextView);
            view1.setGravity(Gravity.CENTER);
            view1.setBackgroundResource(R.color.ori);
            view1.setTextColor(getResources().getColor(R.color.white));
            RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(SystemUtil.dipTopx(this,40), SystemUtil.dipTopx(this,40));
            param1.setMargins(SystemUtil.dipTopx(this,20),0,0,SystemUtil.dipTopx(this,10));
            if (listTextView.size()-1 >0){
                param1.addRule(RelativeLayout.ABOVE,listTextView.size()-1);
            }else{
                param1.addRule(RelativeLayout.ABOVE, R.id.add);//此控件在id为1的控件的下边
            }
            addView.addView(view1,param1);
            final EditText editText = new EditText(this);
            listEditText.add(editText);
            editText.setId(listEditText.size()+10);
            mapEditText.put(listEditText.size()+10,listEditText);
            editText.setText(listTextView.get(listTextView.size()-1).getText());
            editText.setGravity(Gravity.CENTER);
            editText.setBackgroundResource(R.color.white);
            editText.setTextSize(14);
            editText.setTextColor(getResources().getColor(R.color.black));
            RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(SystemUtil.dipTopx(this,120), SystemUtil.dipTopx(this,40));
            param2.setMargins(0,0,0,SystemUtil.dipTopx(this,10));
            if (listTextView.size()-1 >0){
                param2.addRule(RelativeLayout.ABOVE,listTextView.size()-1);
            }else{
                param2.addRule(RelativeLayout.ABOVE, R.id.add);//此控件在id为1的控件的下边
            }
            param2.addRule(RelativeLayout.RIGHT_OF,listTextView.size());
            addView.addView(editText,param2);
            editText.setVisibility(View.GONE);
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view1.getText().equals("完成")){
                        delete.setVisibility(View.GONE);
                        view1.setText(editText.getText().toString());
                        view1.setBackgroundColor(getResources().getColor(R.color.ori));
                        editText.setVisibility(View.GONE);
                        if (listMarker.size() < listTextView.size()){
                            for (Integer idText :mapTextView.keySet()){
                                if (mapMarker.get(view.getId()) == null){
                                    //不存在
                                    Marker maker = aMap.addMarker(new MarkerOptions().position(new LatLng(screenMarker.getPosition().latitude,screenMarker.getPosition().longitude))
                                            .title("地点："+view.getId())
                                            .snippet(mapEditText.get(view.getId()+10).get(view.getId()-1).getText().toString())
                                            .draggable(true)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                    maker.showInfoWindow();
                                    listMarker.add(maker);
                                    mapMarker.put(view.getId(),listMarker);
                                    Log.i("123",maker.getPosition().latitude+"***"+maker.getPosition().longitude+"添加上去的");
                                }else{
                                    //存在

                                }
                            }
                        }else{
                            markerC.hideInfoWindow();
                        }
                    }else{
                        delete.setVisibility(View.VISIBLE);
                        clickViewId = view.getId();
                        for (Integer idMarker :mapMarker.keySet()){
                            if (mapMarker.get(view.getId()) != null){
                                markerC = (Marker)mapMarker.get(view.getId()).get(view.getId()-1);
                                markerC.showInfoWindow();
                                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(markerC.getPosition().latitude, markerC.getPosition().longitude), 15));
                                EditText ed = mapEditText.get(view.getId()+10).get(view.getId()-1);
                                ed.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        markerC.setSnippet(charSequence.toString());
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });
                            }else{
                                Log.i("123","null"+view.getId());
                            }
                        }
                        for (int i = 0;i<listTextView.size();i++){
                            listTextView.get(i).setText(listEditText.get(i).getText().toString());
                            listTextView.get(i).setBackgroundColor(getResources().getColor(R.color.ori));
                            listEditText.get(i).setVisibility(View.GONE);
                        }
                        view1.setText("完成");
                        view1.setBackgroundColor(getResources().getColor(R.color.blue));
                        editText.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else{
            ToastXutil.show("路线点最多只能添加6个");
        }
    }

    /**
     * 添加路线点
     */
    private void addLinePoint(){
        Set<Integer> set = mapTextView.keySet();
        Object [] objects = set.toArray();
        for (int i = listShow.size(); i < objects.length ; i++){
            PlacesBean bean = new PlacesBean();
            bean.setCity_id(2);
            bean.setDeparture_address(mapTextView.get(i+1).get(i).getText().toString());
            bean.setLat(mapMarker.get(i+1).get(i).getPosition().latitude);
            bean.setLng(mapMarker.get(i+1).get(i).getPosition().longitude);
            bean.setOrder(i+1);
            bean.setPlace_name("深圳");
            bean.setRoute_id(id);
            listAdd.add(bean);
        }
        try {
            Gson gson = new Gson();
            YueKaHttp.addLinePoint(gson.toJson(listAdd), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        if (i == 200){
                            JSONObject jsonObject =new JSONObject(new String(bytes,"UTF-8"));
//                            Log.i("123",jsonObject.toString()+"----");
                            int code  = jsonObject.getInt("code");
                            switch (code){
                                case 0:
                                    ToastXutil.show("添加成功");
                                    Log.i("123",listShow.toString()+"修改之后");
                                    for (Integer in :mapTextView.keySet()){
                                        Log.i("123",in + "--"+mapTextView.get(in).get(in-1).getText()+"文本框");
                                    }
                                    for (Integer in :mapEditText.keySet()){
                                        Log.i("123",in + "--"+mapEditText.get(in).get(in-11).getText()+"输入框");
                                    }
                                    for (Integer in :mapMarker.keySet()){
                                        Log.i("123 ",in + "--"+mapMarker.get(in).get(in-1).getPosition().latitude+"--"+mapMarker.get(in).get(in-1).getPosition().longitude+"坐标");
                                    }
                                    for (int a = 0;a < listAdd.size();a++){
                                        listAll.add(listAdd.get(a));
                                    }
                                    for (int b = 0;b < listShow.size();b++){
                                        listAll.add(listShow.get(b));
                                    }
//                                    Log.i("123",listAll.size()+listAll.toString());
                                    Intent intent = new Intent();
                                    intent.putParcelableArrayListExtra("list",(ArrayList<? extends Parcelable>) listAll);
                                    intent.putExtra("id",id);
                                    setResult(0,intent);
                                    LineChoiceActivity.this.finish();
                                    break;
                            }
                        }
                    }catch (Exception e){
                        Log.i("123",e.getMessage()+"/*/*");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.i("123",i+"----");
                }
            });
        }catch (Exception e){
            Log.i("456",e.getMessage()+"/*/*");
        }
    }

    /**
     * 删除路线点
     */
    private void deletePoint(int indext){
//        addView.removeAllViews();//先清除所有按钮
//        mapMarker.get(indext).get(0).remove();//清除弹出来的窗体
        for (Integer in :mapMarker.keySet()){
            if (in <indext){
                mapMarker.put(indext,mapMarker.get(in));
                mapMarker.remove(in);
            }
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
        Log.i("124",marker.getPosition().latitude+"**"+marker.getPosition().longitude+"拖完的坐标");
    }
}

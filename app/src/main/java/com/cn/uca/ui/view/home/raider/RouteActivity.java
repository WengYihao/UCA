package com.cn.uca.ui.view.home.raider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.cn.uca.R;
import com.cn.uca.adapter.gaode.BusResultListAdapter;
import com.cn.uca.config.Constant;
import com.cn.uca.gaodeutil.AMapUtil;
import com.cn.uca.gaodeutil.DrivingRouteOverlay;
import com.cn.uca.gaodeutil.WalkRouteOverlay;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ToastXutil;

public class RouteActivity extends BaseBackActivity implements View.OnClickListener,RouteSearch.OnRouteSearchListener{

    TextView back,start,end,walk,transit,drive;
    private ImageView line1,line2,line3;
    private AMap aMap;
    private MapView mapView;
    private ListView listView;
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;//步行
    private BusRouteResult mBusRouteResult;//公交
    private DriveRouteResult mDriveRouteResult;//驾车
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 116.335891);//起点，116.335891,39.942295
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//终点，116.481288,39.995576
    private String mCurrentCityName = "北京";
    private RelativeLayout mBottomLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private String startName = "";
    private String endName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        getInfo();
        initView();
        init();
        setfromandtoMarker();
        searchRouteResult(Constant.ROUTE_TYPE_WALK,RouteSearch.WALK_DEFAULT);
    }

    private void getInfo() {
        Intent intent = getIntent();
        if (intent != null){
            startName = intent.getStringExtra("start");
            endName = intent.getStringExtra("end");
        }
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        start = (TextView)findViewById(R.id.start);
        end = (TextView)findViewById(R.id.end);
        walk = (TextView)findViewById(R.id.walk);
        transit = (TextView)findViewById(R.id.transit);
        drive = (TextView)findViewById(R.id.drive);
        line1 = (ImageView)findViewById(R.id.line1);
        line2 = (ImageView)findViewById(R.id.line2);
        line3 = (ImageView)findViewById(R.id.line3);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
        listView = (ListView)findViewById(R.id.bus_result_list);
        back.setOnClickListener(this);
        walk.setOnClickListener(this);
        transit.setOnClickListener(this);
        drive.setOnClickListener(this);

        start.setText(startName);
        end.setText(endName);
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
//        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
//        mHeadLayout = (RelativeLayout) findViewById(R.id.routemap_header);
//        mHeadLayout.setVisibility(View.GONE);
//        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
//        mRouteDetailDes = (TextView) findViewById(R.id.secondline);

    }
    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_back)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end_back)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.walk:
                searchRouteResult(Constant.ROUTE_TYPE_WALK,RouteSearch.WALK_DEFAULT);
                walk.setTextColor(getResources().getColor(R.color.ori));
                line1.setVisibility(View.VISIBLE);

                transit.setTextColor(getResources().getColor(R.color.grey));
                line2.setVisibility(View.GONE);

                drive.setTextColor(getResources().getColor(R.color.grey));
                line3.setVisibility(View.GONE);

                mapView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                break;
            case R.id.transit:
                mBottomLayout.setVisibility(View.GONE);
                searchRouteResult(Constant.ROUTE_TYPE_BUS, RouteSearch.BusDefault);
                walk.setTextColor(getResources().getColor(R.color.grey));
                line1.setVisibility(View.GONE);

                transit.setTextColor(getResources().getColor(R.color.ori));
                line2.setVisibility(View.VISIBLE);

                drive.setTextColor(getResources().getColor(R.color.grey));
                line3.setVisibility(View.GONE);

                mapView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                break;
            case R.id.drive:
                searchRouteResult(Constant.ROUTE_TYPE_DRIVE,RouteSearch.DrivingDefault);
                walk.setTextColor(getResources().getColor(R.color.grey));
                line1.setVisibility(View.GONE);

                transit.setTextColor(getResources().getColor(R.color.grey));
                line2.setVisibility(View.GONE);

                drive.setTextColor(getResources().getColor(R.color.ori));
                line3.setVisibility(View.VISIBLE);

                mapView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastXutil.show("定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastXutil.show("终点未设置");
        }
        switch (routeType){
            case Constant.ROUTE_TYPE_WALK:
                final RouteSearch.FromAndTo fromAndToWalk = new RouteSearch.FromAndTo(
                        mStartPoint, mEndPoint);
                RouteSearch.WalkRouteQuery queryWalk = new RouteSearch.WalkRouteQuery(fromAndToWalk, mode);
                mRouteSearch.calculateWalkRouteAsyn(queryWalk);// 异步路径规划步行模式查询
                break;
            case Constant.ROUTE_TYPE_BUS:
                final RouteSearch.FromAndTo fromAndToBus = new RouteSearch.FromAndTo(
                        mStartPoint, mEndPoint);
               RouteSearch.BusRouteQuery queryBus = new RouteSearch.BusRouteQuery(fromAndToBus, mode,
                       mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
                mRouteSearch.calculateBusRouteAsyn(queryBus);// 异步路径规划公交模式查询
                break;
            case Constant.ROUTE_TYPE_DRIVE:
                final RouteSearch.FromAndTo fromAndToDrive = new RouteSearch.FromAndTo(
                        mStartPoint, mEndPoint);
                RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndToDrive, mode, null,
                        null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
                mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
                break;
        }
    }
    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(RouteActivity.this, mBusRouteResult);
                    listView.setAdapter(mBusResultListAdapter);
                } else if (result != null && result.getPaths() == null) {
                    ToastXutil.show("暂无结果");
                }
            } else {
                ToastXutil.show("暂无结果");
            }
        } else {
            ToastXutil.show(errorCode+"");
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约"+taxiCost+"元");
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           ToastXutil.show("驾车导航");
                        }
                    });

                } else if (result != null && result.getPaths() == null) {
                    ToastXutil.show("暂无结果");
                }

            } else {
                ToastXutil.show("暂无结果");
            }
        } else {
            ToastXutil.show(errorCode+"");
        }

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    Log.e("456",des+"--------");
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          ToastXutil.show("开始导航");
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastXutil.show("暂无结果");
                }
            } else {
                ToastXutil.show("暂无结果");
            }
        } else {
            ToastXutil.show(errorCode+"-");
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}

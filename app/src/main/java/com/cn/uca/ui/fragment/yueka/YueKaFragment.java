package com.cn.uca.ui.fragment.yueka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.yueka.YueKaAdapter;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.yueka.EscortRecordsBean;
import com.cn.uca.bean.yueka.GetEscortBean;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.datepicker.OnDoubleSureLisener;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.impl.yueka.SearchCallBack;
import com.cn.uca.popupwindows.YuekaSearchPopupWindow;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.rongim.ChatListActivity;
import com.cn.uca.ui.view.rongim.ConversationActivity;
import com.cn.uca.ui.view.util.CityActivity;
import com.cn.uca.ui.view.yueka.OrderYueActivity;
import com.cn.uca.ui.view.yueka.OtherYueActivity;
import com.cn.uca.ui.view.yueka.YueChatActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.datepicker.DoubleDatePickDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by asus on 2017/8/11.
 */

public class YueKaFragment extends Fragment implements AMapLocationListener,View.OnClickListener,OnDoubleSureLisener,CollectionClickListener,SearchCallBack{

    private View view;
    private ListView listView;
    private YueKaAdapter yueKaAdapter;
    private List<EscortRecordsBean> list;
    private TextView place,messageYue,orderYue,freeTime,all;
    private int page = 1;
    private int pageCount = 5;
    private RefreshLayout refreshLayout;

    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    public static String city,cityCode;
    private int sexId;
    private String begTime,endTime,beg_age,end_age,lable;
    private GetEscortBean bean = new GetEscortBean();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yueka, null);
        autoObtainLocationPermission();
        location();
        ininView();
        return view;
    }
    private void ininView() {
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        place = (TextView) view.findViewById(R.id.place);
        all = (TextView)view.findViewById(R.id.all);
        messageYue = (TextView) view.findViewById(R.id.messageYue);
        orderYue = (TextView) view.findViewById(R.id.orderYue);
        listView = (ListView) view.findViewById(R.id.listView);
        freeTime = (TextView) view.findViewById(R.id.freeTime);

        place.setOnClickListener(this);
        all.setOnClickListener(this);
        messageYue.setOnClickListener(this);
        orderYue.setOnClickListener(this);
        freeTime.setOnClickListener(this);

        list = new ArrayList<>();
        yueKaAdapter = new YueKaAdapter(list, getActivity(), this);
        listView.setAdapter(yueKaAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), YueChatActivity.class);
                int id = list.get(i).getEscort_record_id();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getEscortRecords(getBean());
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                       getEscortRecords(getBean());
                        refreshlayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();

    }

    private GetEscortBean getBean(){
        GetEscortBean bean = new GetEscortBean();
        bean.setBeg_age(beg_age);
        bean.setEnd_age(end_age);
        bean.setBeg_time(begTime);
        bean.setEnd_time(endTime);
        bean.setGaode_code(cityCode);
        bean.setAccount_token(SharePreferenceXutil.getAccountToken());
        bean.setPage(page);
        bean.setPageCount(pageCount);
        bean.setLabel(lable);
        bean.setSex_id(sexId);

        return bean;
    }
    /**
     * 自动获取定位权限
     */

    private void autoObtainLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.STORAGE_PERMISSIONS_REQUEST_CODE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.place:
                Intent intent = new Intent();
                intent.setClass(getActivity(),CityActivity.class);
                intent.putExtra("city",city);
                startActivityForResult(intent,0);
                break;
            case R.id.messageYue:
                connectRongServer(SharePreferenceXutil.getRongToken());
                break;
            case R.id.all:
                YuekaSearchPopupWindow yuekaSearchPopupWindow = new YuekaSearchPopupWindow(getActivity(),all,this);
                break;
            case R.id.orderYue:
                if (SharePreferenceXutil.isAuthentication()){
                    startActivity(new Intent(getActivity(), OrderYueActivity.class));
                }else{
                    startActivity(new Intent(getActivity(), OtherYueActivity.class));
                }
                break;
            case R.id.freeTime:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
        }
    }

    /**
     * 验证融云token
     * @param token
     */
    private void connectRongServer(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String userId) {
                startActivity(new Intent(getActivity(), ChatListActivity.class));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }

            @Override
            public void onTokenIncorrect() {
            }
        });
    }
    private void getEscortRecords(GetEscortBean bean){

        YueKaHttp.getEscortRecords(bean, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200){
                    try {
                        JSONObject jsonObject =new JSONObject(new String(bytes,"UTF-8"));
                        Log.i("123",jsonObject.toString());
                        int code  = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                JSONObject object = jsonObject.getJSONObject("data");
                                JSONArray array = object.getJSONArray("escortRecords");
                                Gson gson = new Gson();
                                List<EscortRecordsBean> bean = gson.fromJson(array.toString(),new TypeToken<List<EscortRecordsBean>>(){}.getType());
                                if (bean.size() > 0) {
                                    list.clear();
                                    list.addAll(bean);
                                    yueKaAdapter.setList(list);
                                } else {
                                    if (list.size() != 0) {
                                        ToastXutil.show("没有更多数据了");
                                    } else {
                                        yueKaAdapter.setList(list);
                                    }
                                }
                                break;
                        }
                    }catch (Exception e){
                        Log.i("456",e.getMessage()+"====");
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    JSONObject jsonObject =new JSONObject(new String(bytes,"UTF-8"));
                    Log.i("456",jsonObject.toString()+"====");
                }catch (Exception e){

                }

            }
        });
    }

    private void showDatePickDialog(DateType type) {
        DoubleDatePickDialog dialog = new DoubleDatePickDialog(getActivity());
        //设置上下年分限制
        dialog.setYearLimt(0);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnDoubleSureLisener(this);
        dialog.show();
    }

    @Override
    public void onCollectionClick(final View v) {
        LinearLayout layout = (LinearLayout)listView.getChildAt((int) v.getTag());// 获得子item的layout
        final TextView tv = (TextView) layout.findViewById(R.id.click);
        if (list.get((int) v.getTag()).isCollection()){
            //--发送请求
            YueKaHttp.collectionEscortRecord(list.get((int) v.getTag()).getEscort_record_id(), new CallBack() {
                @Override
                public void onResponse(Object response) {
                    if ((int) response == 0){
                        ToastXutil.show("取消收藏");
                        tv.setBackgroundResource(R.mipmap.nocollection);
                        list.get((int) v.getTag()).setCollection(false);
                        yueKaAdapter.setList(list);
                    }
                }

                @Override
                public void onErrorMsg(String errorMsg) {

                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        }else{
            //--发送请求
            YueKaHttp.collectionEscortRecord(list.get((int) v.getTag()).getEscort_record_id(), new CallBack() {
                @Override
                public void onResponse(Object response) {
                    if ((int) response == 0){
                        ToastXutil.show("收藏成功");
                        tv.setBackgroundResource(R.mipmap.collection);
                        list.get((int) v.getTag()).setCollection(true);
                        yueKaAdapter.setList(list);
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

    }

    @Override
    public void onSure(Date dateStart, Date dateEnd) {
        if (dateStart.after(dateEnd)){
            ToastXutil.show("开始时间不能大于结束时间");
        }else{
            begTime = SystemUtil.UtilDateToString(dateStart);
            endTime = SystemUtil.UtilDateToString(dateEnd);
            freeTime.setText(begTime+"-"+endTime);
            getEscortRecords(getBean());
        }
    }

    private  void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(200);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                double la= aMapLocation.getLatitude();//获取纬度
                double lo= aMapLocation.getLongitude();//获取经度
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc){
                    city = aMapLocation.getCity().substring(0,aMapLocation.getCity().indexOf("市"));
                    cityCode = aMapLocation.getCityCode();
                    Log.i("456",SystemUtil.getCurrentDate()+"定位");
                    getEscortRecords(getBean());
                    place.setText(city);
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.i("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (data != null){
                cityCode = data.getStringExtra("code");
                getEscortRecords(getBean());
                place.setText(data.getStringExtra("city"));
            }
        }
    }

    @Override
    public void onCallBack(int sexId, String begAge, String endAge, String lable) {
        beg_age = begAge.substring(0,begAge.indexOf("岁"));
        end_age = endAge.substring(0,endAge.indexOf("岁"));
        this.lable = lable;
        getEscortRecords(getBean());
    }
}

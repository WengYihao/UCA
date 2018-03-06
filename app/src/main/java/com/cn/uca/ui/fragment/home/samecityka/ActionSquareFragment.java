package com.cn.uca.ui.fragment.home.samecityka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.SameCityKaAdapter;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.home.samecityka.LableBean;
import com.cn.uca.bean.home.samecityka.SameCityKaBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.datepicker.OnDoubleSureLisener;
import com.cn.uca.impl.yueka.SearchCallBack;
import com.cn.uca.popupwindows.SameCityKaSearchPopupWindow;
import com.cn.uca.popupwindows.SameCityKaSearchPopupWindow2;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.samecityka.ActionDetailActivity;
import com.cn.uca.ui.view.home.samecityka.ActionSearchActivity;
import com.cn.uca.ui.view.util.CityActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.NoScrollListView;
import com.cn.uca.view.ObservableScrollView;
import com.cn.uca.view.datepicker.DoubleDatePickDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by asus on 2017/12/4.
 * 活动广场
 */

public class ActionSquareFragment extends Fragment implements AMapLocationListener,View.OnClickListener,OnDoubleSureLisener{

    private View view;
    private TextView back,place,search,num1,num2,num3,num4,num5,num6,type,time,price;
    private ObservableScrollView scrollView;
    private NoScrollListView listView;
    private LinearLayout locationLayout,layout_1,layout_2;
    private RelativeLayout layout1,layout2,layout3,layout4,layout5,layout6;
    private int page = Constant.PAGE;
    private int pageCount = Constant.PAGE_COUNT;
    private List<SameCityKaBean> list;
    private SameCityKaAdapter adapter;
    private List<String> listLable;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    public static String city,cityCode;
    private int actionTypeId = 2;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_action_square,null);

        location();
        initView();
        getCafeLabel();

        return view;
    }

    private void initView() {
        back = (TextView)view.findViewById(R.id.back);
        place = (TextView)view.findViewById(R.id.place);
        search = (TextView)view.findViewById(R.id.search);
        scrollView = (ObservableScrollView)view.findViewById(R.id.scrollView);
        type = (TextView)view.findViewById(R.id.type);
        time = (TextView)view.findViewById(R.id.time);
        price = (TextView)view.findViewById(R.id.price);
        locationLayout = (LinearLayout)view.findViewById(R.id.locationLayout);
        layout_1 = (LinearLayout)view.findViewById(R.id.layout_1);
        layout_2 = (LinearLayout)view.findViewById(R.id.layout_2);
        layout1 = (RelativeLayout)view.findViewById(R.id.layout1);
        layout2 = (RelativeLayout)view.findViewById(R.id.layout2);
        layout3 = (RelativeLayout)view.findViewById(R.id.layout3);
        layout4 = (RelativeLayout)view.findViewById(R.id.layout4);
        layout5 = (RelativeLayout)view.findViewById(R.id.layout5);
        layout6 = (RelativeLayout)view.findViewById(R.id.layout6);
        num1 = (TextView)view.findViewById(R.id.num1);
        num2 = (TextView)view.findViewById(R.id.num2);
        num3 = (TextView)view.findViewById(R.id.num3);
        num4 = (TextView)view.findViewById(R.id.num4);
        num5 = (TextView)view.findViewById(R.id.num5);
        num6 = (TextView)view.findViewById(R.id.num6);

        listView = (NoScrollListView)view.findViewById(R.id.listView);
        back.setOnClickListener(this);
        search.setOnClickListener(this);
        locationLayout.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        type.setOnClickListener(this);
        time.setOnClickListener(this);
        price.setOnClickListener(this);

        SetLayoutParams.setRelativeLayout(layout1, (MyApplication.width- SystemUtil.dip2px(30))/3,SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout2, (MyApplication.width- SystemUtil.dip2px(30))/3,SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout3, (MyApplication.width- SystemUtil.dip2px(30))/3,SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout4, (MyApplication.width- SystemUtil.dip2px(30))/3+SystemUtil.dip2px(12),SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout5, (MyApplication.width- SystemUtil.dip2px(30))/3+SystemUtil.dip2px(24),SystemUtil.dip2px(80));
        SetLayoutParams.setRelativeLayout(layout6, (MyApplication.width- SystemUtil.dip2px(30))/3+SystemUtil.dip2px(12),SystemUtil.dip2px(80));

        layout2.setVisibility(View.GONE);
        layout5.setVisibility(View.VISIBLE);

        scrollView.setOnScollChangedListener(new ObservableScrollView.OnScollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy){
                if (y>=220){
                    layout_1.setVisibility(View.VISIBLE);
                    layout_2.setVisibility(View.GONE);
                }else{
                    layout_1.setVisibility(View.GONE);
                    layout_2.setVisibility(View.VISIBLE);
                }
            }
        });

        list = new ArrayList<>();
        listLable = new ArrayList<>();
        adapter = new SameCityKaAdapter(list,getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ActionDetailActivity.class);
                intent.putExtra("id",list.get(position).getCity_cafe_id());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.search:
                startActivity(new Intent(getActivity(),ActionSearchActivity.class));
                break;
            case R.id.locationLayout:
                Intent intent = new Intent();
                intent.setClass(getActivity(), CityActivity.class);
                intent.putExtra("city",city);
                intent.putExtra("type","samecityka");
                startActivityForResult(intent,9);
                break ;
            case R.id.type:
                new SameCityKaSearchPopupWindow2(getActivity(), type, listLable, new SearchCallBack() {
                    @Override
                    public void onCallBack(int sexId, String begAge, String endAge, String lable) {

                    }
                });
                break;
            case R.id.time:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.price:
                new SameCityKaSearchPopupWindow(getActivity(), price, new SearchCallBack() {
                    @Override
                    public void onCallBack(int sexId, String begAge, String endAge, String lable) {

                    }
                });
                break;
            case R.id.layout1:
                actionTypeId = 1;
                getCityCafe(actionTypeId,"","","","");
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.GONE);
                break;
            case R.id.layout2:
                actionTypeId = 2;
                getCityCafe(actionTypeId,"","","","");
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.VISIBLE);
                layout6.setVisibility(View.GONE);
                break;
            case R.id.layout3:
                actionTypeId = 3;
                getCityCafe(actionTypeId,"","","","");
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void getCityCafe(int id,String beg_time,String end_time,String charge,String lable){
        Map<String,Object> map = new HashMap<>();
        map.put("user_card_type_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("gaode_code",cityCode);
        map.put("beg_time",beg_time);
        map.put("end_time",end_time);
        map.put("charge",charge);
        map.put("label_id",lable);
        String sign = SignUtil.sign(map);
        HomeHttp.getCityCafe(time_stamp, id, sign, page, pageCount,cityCode, beg_time, end_time, charge, lable, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONObject object = jsonObject.getJSONObject("data");
                            num1.setText(object.getString("ordinary_sum_count"));
                            num4.setText(object.getString("ordinary_sum_count"));
                            num2.setText(object.getString("enterprise_sum_count"));
                            num5.setText(object.getString("enterprise_sum_count"));
                            num3.setText(object.getString("school_sum_count"));
                            num6.setText(object.getString("school_sum_count"));
                            List<SameCityKaBean> bean = gson.fromJson(object.getJSONArray("cityCafes").toString(),new TypeToken<List<SameCityKaBean>>() {
                            }.getType());
                            list.clear();
                            if (bean.size() > 0){
                                list.addAll(bean);
                                adapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
                                }else{
                                    ToastXutil.show("暂无数据");
                                }
                                adapter.setList(list);
                            }
                            break;
                        default:
                            break;
                    }

                }catch (Exception e){
                    Log.e("456",e.getMessage());
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

    //分类
    private void getCafeLabel(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getCafeLabel(account_token, time_stamp, sign, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONArray("data");
                            List<LableBean> bean = gson.fromJson(array.toString(),new TypeToken<List<LableBean>>() {
                            }.getType());
                            for (int i = 0;i<bean.size();i++){
                                listLable.add(bean.get(i).getLable_name());
                            }
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

    //时间
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
    public void onSure(Date dateStart, Date dateEnd) {

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
                    getCityCafe(2,"","","","");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9){
            if (data != null){
                cityCode = data.getStringExtra("code");
                getCityCafe(actionTypeId,"","","","");
                place.setText(data.getStringExtra("city"));
            }
        }
    }
}

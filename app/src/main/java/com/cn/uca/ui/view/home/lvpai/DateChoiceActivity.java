package com.cn.uca.ui.view.home.lvpai;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.data.ListViewAdapter;
import com.cn.uca.bean.home.lvpai.OrderBean;
import com.cn.uca.bean.home.lvpai.dateview.DateBean;
import com.cn.uca.bean.home.lvpai.dateview.GridViewBean;
import com.cn.uca.bean.home.lvpai.dateview.ListViewBean;
import com.cn.uca.bean.home.lvpai.dateview.SourceDateBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.ServiceBack;
import com.cn.uca.impl.lvpai.CallBackDate;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.user.WalletPasswordActivity;
import com.cn.uca.ui.view.user.order.LvPaiOrderDetailActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行程选择
 */
public class DateChoiceActivity extends BaseBackActivity implements View.OnClickListener,CallBackDate,ServiceBack{

    private TextView back,dayLong,buy;
    private ListView listView;
    private ListViewAdapter adapter;
    private List<ListViewBean> list;
    private int id,day;
    private List<String> stringList = new ArrayList<>();//行程选择日期
    private OrderBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_choice);
        getInfo();
        initView();
        getTsDate();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        dayLong = (TextView)findViewById(R.id.dayLong);
        buy = (TextView)findViewById(R.id.buy);
        listView = (ListView)findViewById(R.id.listView);

        back.setOnClickListener(this);
        buy.setOnClickListener(this);
        list = new ArrayList<>();
        adapter = new ListViewAdapter(list,this,this);
        listView.setAdapter(adapter);
    }
    private List<ListViewBean> getDate(List<SourceDateBean> sourceBeen){
       List<ListViewBean> list = new ArrayList<>();
        for (int i=0;i<4;i++){
            if(i==0){
                //当前月
                ListViewBean listViewBean = new ListViewBean();
                listViewBean.setMonthName(SystemUtil.getCurrentDateOnly3());
                int day = new Date().getDate() ;//当前是当月的第几天
                int maxDate = Calendar.getInstance().getActualMaximum(Calendar.DATE);//当月有几天
                int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);//开始日期是星期几

                GridViewBean gridViewBean = new GridViewBean();
                gridViewBean.setWeekId(week);
                List<DateBean> listDate = new ArrayList<>();
                for (int a = 0;a<week-1;a++){
                    DateBean dateBean = new DateBean();
                    dateBean.setDate("");
                    dateBean.setDay("");
                    dateBean.setSelect("false");
                    listDate.add(dateBean);
                }
                for (int j = day;j<=maxDate;j++){
                    if (j >= 10){
                        DateBean dateBean = new DateBean();
                        dateBean.setDate(SystemUtil.getCurrentDateOnly3()+"-"+j);
                        dateBean.setDay(j+"");
                        dateBean.setSelect("false");
                        listDate.add(dateBean);
                    }else{
                        DateBean dateBean = new DateBean();
                        dateBean.setDate(SystemUtil.getCurrentDateOnly3()+"-0"+j);
                        dateBean.setDay(j+"");
                        dateBean.setSelect("false");
                        listDate.add(dateBean);
                    }
                }
                gridViewBean.setViewBean(listDate);
                gridViewBean.setSourceBeen(sourceBeen);
                listViewBean.setBean(gridViewBean);
                list.add(listViewBean);
            }else{
                //下三月
                Date start = getPerFirstDayOfMonth(i);
                ListViewBean listViewBean = new ListViewBean();
                listViewBean.setMonthName(SystemUtil.getCurrentDateOnly4(start));
                GridViewBean gridViewBean = new GridViewBean();
                Calendar cal = Calendar.getInstance();
                cal.setTime(start);
                int week = cal.get(Calendar.DAY_OF_WEEK);
                gridViewBean.setWeekId(week);
                int maxDate = cal.getActualMaximum(Calendar.DATE);
                List<DateBean> listDate = new ArrayList<>();
                for (int a = 0;a<week-1;a++){
                    DateBean dateBean = new DateBean();
                    dateBean.setDate("");
                    dateBean.setDay("");
                    dateBean.setSelect("false");
                    listDate.add(dateBean);
                }
                for (int j = 1;j<=maxDate;j++){
                    if (j >= 10){
                        DateBean dateBean = new DateBean();
                        dateBean.setDate(SystemUtil.getCurrentDateOnly4(start)+"-"+j);
                        dateBean.setDay(j+"");
                        dateBean.setSelect("false");
                        listDate.add(dateBean);
                    }else{
                        DateBean dateBean = new DateBean();
                        dateBean.setDate(SystemUtil.getCurrentDateOnly4(start)+"-0"+j);
                        dateBean.setDay(j+"");
                        dateBean.setSelect("false");
                        listDate.add(dateBean);
                    }
                }
                gridViewBean.setViewBean(listDate);
                gridViewBean.setSourceBeen(sourceBeen);
                listViewBean.setBean(gridViewBean);
                list.add(listViewBean);
            }
        }
        Log.e("456",list.toString());
        return list;
    }
   //描述:获取以后第几个月的第一天.
    public Date getPerFirstDayOfMonth(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, i);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case  R.id.back:
               this.finish();
            break;
            case R.id.buy:
                createTsOrder();
                break;
        }
    }
    private void getTsDate(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("trip_shoot_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.getTsDate(time_stamp, sign, account_token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONObject object = jsonObject.getJSONObject("data");
                            day = object.getInt("days");
                            dayLong.setText("商家单次服务时长为:"+day+"天");
                            JSONArray array = object.getJSONArray("tsDates");
                            List<SourceDateBean> beanList = gson.fromJson(array.toString(),new TypeToken<List<SourceDateBean>>() {
                            }.getType());
                            list = getDate(beanList);
                            if (list.size() != 0){
                                adapter.setList(list);
                            }
                            Log.e("456",list.toString());
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage()+"报错");
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
    private void createTsOrder(){
        if (stringList.size()>0){
            Map<String,Object> map = new HashMap<>();
            String account_token = SharePreferenceXutil.getAccountToken();
            map.put("account_token",account_token);
            String time_stamp = SystemUtil.getCurrentDate2();
            map.put("time_stamp",time_stamp);
            map.put("trip_shoot_id",id);
            map.put("dates",StringXutil.ListtoString(stringList));
            String sign = SignUtil.sign(map);
            HomeHttp.createTsOrder(time_stamp, sign, account_token, id, StringXutil.ListtoString(stringList), new CallBack() {
                @Override
                public void onResponse(Object response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response.toString());
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                Gson gson = new Gson();
                                bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<OrderBean>() {
                                }.getType());
                                String str = "http://www.szyouka.com:8080/youkatravel/agreement/tripShootUserProtocol.html";
                                ShowPopupWindow.seviceWindow(getWindow().getDecorView(),DateChoiceActivity.this,str,DateChoiceActivity.this);
                                break;
                            case 680:
                                ToastXutil.show("行程必须大于今天");
                                break;
                            case 740:
                                ToastXutil.show("请先设置支付密码");
                                Intent intent = new Intent();
                                intent.setClass(DateChoiceActivity.this, WalletPasswordActivity.class);
                                intent.putExtra("type",2);
                                startActivity(intent);
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
        }else{
            ToastXutil.show("请选择服务日期");
        }

    }
    @Override
    public void callBack(String date) {
        if (list.size() != 0){
            List<SourceDateBean> yList = list.get(0).getBean().getSourceBeen();
            List<String> li = SystemUtil.getBetweenDate(date,day-1);
            boolean aaa = false;
            if (yList.size() != 0){
                for (SourceDateBean y : yList) {
                    for (String str : li){
                        if (str.equals(y.getDate())){//如果选中日期等于忙碌日期中的一个
                            ToastXutil.show("该日期不可选择或商家忙碌");
                            aaa = false;//日期不可选
                            break;//跳出循环
                        }else{
                            aaa = true;//日期可选
                        }
                    }
                    if (!aaa){
                        break;
                    }
                }
            }else{
                Log.e("456","--------------");
                aaa = true;
            }

            if (aaa){//可选再执行
                for (int i = 0;i<list.size();i++){
                    ListViewBean bean = list.get(i);//一个月的日期对象
                    for (int a = 0;a<bean.getBean().getViewBean().size();a++){
                        for (int b = 0;b<li.size();b++){
                            bean.getBean().getViewBean().get(a).setSelect("false");//先重新赋值
                        }
                    }
                    for (int a = 0;a<bean.getBean().getViewBean().size();a++){
                        for (int b = 0;b<li.size();b++){//对比
                            if (li.get(b).equals(bean.getBean().getViewBean().get(a).getDate())){
                                if (li.get(b).equals(date)){
                                    bean.getBean().getViewBean().get(a).setSelect("true");
                                }else{
                                    bean.getBean().getViewBean().get(a).setSelect("ob");
                                }
                            }
                        }
                    }
                }
                adapter.setList(list);
                if (stringList.size() != 0){
                    stringList = li;
                }else{
                    stringList = li;
                }
            }
        }else{
            ToastXutil.show("123");
        }
    }

    @Override
    public void sure() {
        Intent intent = new Intent();
        intent.setClass(DateChoiceActivity.this, LvPaiOrderDetailActivity.class);
        intent.putExtra("type","order");
        intent.putExtra("order",bean.getOrder_number());
        startActivity(intent);
    }
}

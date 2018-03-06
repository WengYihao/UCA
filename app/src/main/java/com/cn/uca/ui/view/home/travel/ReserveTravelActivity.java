package com.cn.uca.ui.view.home.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.ReserveDateAdapter;
import com.cn.uca.adapter.home.travel.ReserveMonthAdapter;
import com.cn.uca.bean.home.travel.ReserveDateBean;
import com.cn.uca.bean.home.travel.ReserveMonthBean;
import com.cn.uca.bean.home.travel.ScheduleBean;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.HorizontalListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//旅游预订
public class ReserveTravelActivity extends BaseBackActivity implements View.OnClickListener{

    private HorizontalListView listView;
    private ReserveMonthAdapter monthAdapter;
    private List<ReserveMonthBean> listMonth = new ArrayList<>();
    private GridView gridView;
    private ReserveDateAdapter dateAdapter;
    private List<ReserveDateBean> list;
    private List<ScheduleBean> scheduleBeanList = new ArrayList<>();//传过来的团期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_travel);

        getInfo();
        initView();
    }

    private void initView() {
        listView = (HorizontalListView)findViewById(R.id.listView);
        monthAdapter = new ReserveMonthAdapter(listMonth,this);
        listView.setAdapter(monthAdapter);
//        aa();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (ReserveMonthBean typeBean :listMonth){
                    typeBean.setCheck(false);
                }
                listMonth.get(position).setCheck(true);
                monthAdapter.notifyDataSetChanged();
                if (list != null){
                    list.clear();
                    list = getDays("2018",listMonth.get(position).getName(),scheduleBeanList);
                    dateAdapter.setList(list);
                    Log.e("456",list.size()+"---"+list.toString());
                }
//                viewPager.setCurrentItem(position);
            }
        });

        gridView = (GridView)findViewById(R.id.gridView);
        list = new ArrayList<>();
        dateAdapter = new ReserveDateAdapter(list,this);
        gridView.setAdapter(dateAdapter);
        list = getDays("2018",listMonth.get(0).getName(),scheduleBeanList);
        dateAdapter.setList(list);
        Log.e("456",list.size()+"---"+list.toString());
    }

    private void getInfo() {
        Intent intent = getIntent();
        if (intent != null){
            scheduleBeanList = intent.getParcelableArrayListExtra("date");
            ReserveMonthBean bean = null;
            for (int i = 0;i<scheduleBeanList.size();i++){
                bean = new ReserveMonthBean();
                if (i==0){
                    bean.setCheck(true);
                    bean.setName(scheduleBeanList.get(i).getSchedule_date().substring(5).substring(0,2));
                }else{
                    bean.setName(scheduleBeanList.get(i).getSchedule_date().substring(5).substring(0,2));
                    bean.setCheck(false);
                }
                listMonth.add(bean);
            }
            for (int i = 0; i < listMonth.size() - 1; i++) {
                for (int j = listMonth.size() - 1; j > i; j--) {
                    if (listMonth.get(j).getName().equals(listMonth.get(i).getName())) {
                        listMonth.remove(j);
                    }
                }
            }
        }
    }

    private List<ReserveDateBean> getDays(String year,String month,List<ScheduleBean> list){
        int day = 0;//某月有几天
        int week = 0;
        List<ReserveDateBean> listDate = null;
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(SystemUtil.StringToUtilDate4(year+"-"+month));
            day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            week = calendar.get(Calendar.DAY_OF_WEEK);
            listDate = new ArrayList<>();
            ReserveDateBean dateBean = null;
            for (int a = 0;a<week-1;a++){
                dateBean = new ReserveDateBean();
                dateBean.setDate("");
                dateBean.setDay("");
                dateBean.setCount(0);
                dateBean.setPrice(0);
                dateBean.setSelect("false");
                listDate.add(dateBean);
            }
            List<ScheduleBean> beanList = new ArrayList<>();
            for (ScheduleBean bean : list){
                //日历上的团期
                Log.e("456",bean.getSchedule_date().substring(5,7)+"---"+month+"月份");
                if (bean.getSchedule_date().substring(5,7).equals(month)){
                    beanList.add(bean);
                }
            }
//            for (int j = 1;j<=day;j++){
//                dateBean = new ReserveDateBean();
//                dateBean.setDate(SystemUtil.UtilDateToString(calendar.getTime()));
//                dateBean.setDay(j+"");
//                dateBean.setSelect("false");
//                dateBean.setPrice(0);
//                dateBean.setCount(0);
//                listDate.add(dateBean);
//            }
            for (int i = 0;i <beanList.size();i++){
                for (int j = 1;j<=day;j++){
                    Log.e("456",beanList.get(i).getSchedule_date().substring(9)+"-"+j+"比较");
                    if (beanList.get(i).getSchedule_date().substring(9).equals(j+"")){
                        dateBean = new ReserveDateBean();
                        dateBean.setDate(beanList.get(i).getSchedule_date());
                        dateBean.setDay(j+"");
                        dateBean.setSelect("false");
                        dateBean.setPrice(beanList.get(i).getPerson_price());
                        dateBean.setCount(beanList.get(i).getStock_count());
                        listDate.add(dateBean);
                        Log.e("456","相等");
                        break;
                    }else{
                        dateBean = new ReserveDateBean();
                        dateBean.setDate(SystemUtil.UtilDateToString(calendar.getTime()));
                        dateBean.setDay(j+"");
                        dateBean.setSelect("false");
                        dateBean.setPrice(0);
                        dateBean.setCount(0);
                        listDate.add(dateBean);
                        Log.e("456","相等");
                        break;
                    }
                }
            }
        }catch (Exception e){
            return null;
        }
        return listDate;
    }

    private void aa(){
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("2");
        list.add("2");
        list.add("2");
        list.add("3");
        list.add("3");
        list.add("3");
        list.add("4");
        list.add("4");
        list.add("4");

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }

        Log.e("456",list.toString());
//        List<String> list = new ArrayList<>();
//        list.add("10-01");
//        list.add("10-12");
//        list.add("10-07");
//        list.add("10-11");
//        list.add("10-71");
//        list.add("10-22");
//        list.add("11-13");
//        list.add("11-14");
//        list.add("11-123");
//        list.add("11-114");
//        list.add("12-07");
//        list.add("12-08");
//        list.add("12-807");
//        list.add("12-708");
//
//        List<String> list1 = new ArrayList<>();
//        List<Map<String,List<String>>> list2 = new ArrayList<>();
//        Map<String,List<String>> map = new HashMap<>();
//        List<String> list3 = new ArrayList<>();
//        for (int i = 0; i < list.size() - 1; i++) {
//            for (int j = list.size() - 1; j > i; j--) {
//                if (list.get(j).substring(0,2).equals(list.get(i).substring(0,2))) {
//                    list1.add(list.get(j).substring(0,2));
//                    List<String> list4 = new ArrayList<>();
//                    list4.add(list.get(j));
//                    list4.add(list.get(i));
//                    map.put(list.get(j).substring(0,2),list4);
//                }
//            }
//        }
//        Log.e("456",list1.toString());
    }
    @Override
    public void onClick(View v) {

    }
}

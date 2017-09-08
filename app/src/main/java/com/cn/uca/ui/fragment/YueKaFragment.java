package com.cn.uca.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.YueKaAdapter;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.YueKaBean;
import com.cn.uca.impl.datepicker.OnDoubleSureLisener;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.ui.OrderYueActivity;
import com.cn.uca.ui.YueChatActivity;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.datepicker.DatePickDialog;
import com.cn.uca.view.datepicker.DoubleDatePickDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2017/8/11.
 */

public class YueKaFragment extends Fragment implements View.OnClickListener,OnDoubleSureLisener{

    private View view;
    private RelativeLayout llTitle;
    private ListView listView;
    private YueKaAdapter yueKaAdapter;
    private List<YueKaBean> list;
    private List<String> lable1,lable2,img1,img2;
    private TextView orderYue,stateTitle,startTime,endTime,freeTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yueka, null);

        ininView();
        return view;
    }

    private void ininView() {
        orderYue = (TextView)view.findViewById(R.id.orderYue);
        stateTitle = (TextView)view.findViewById(R.id.stateTitle);
        stateTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.getStatusHeight(getActivity())));
        listView  = (ListView)view.findViewById(R.id.listView);
        llTitle = (RelativeLayout) view.findViewById(R.id.llTitle);
        startTime = (TextView)view.findViewById(R.id.startTime);
        endTime = (TextView)view.findViewById(R.id.endTime);
        freeTime = (TextView)view.findViewById(R.id.freeTime);

        orderYue.setOnClickListener(this);
        freeTime.setOnClickListener(this);

        list = new ArrayList<>();
        lable1 = new ArrayList<>();
        img1 = new ArrayList<>();

        lable1.add("已认证");
        lable1.add("有车一族");
        lable1.add("包买票");

        img1 = new ArrayList<>();
        img1.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        img1.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        YueKaBean yueKaBean = new YueKaBean();
        yueKaBean.setName("小光光");
        yueKaBean.setAge(25);
        yueKaBean.setStart(4);
        yueKaBean.setPrice("￥200-340");
        yueKaBean.setSum(10);
        yueKaBean.setCount(32);
        yueKaBean.setEvaluate("非常不错的一次旅行");
        yueKaBean.setLable(lable1);
        yueKaBean.setImgList(img1);
        list.add(yueKaBean);

        lable2 = new ArrayList<>();
        img2 = new ArrayList<>();
        lable2.add("已认证");
        lable2.add("有车一族");
        lable2.add("包吃住");

        img2 = new ArrayList<>();
        img2.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        img2.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");

        YueKaBean yueKaBean1 = new YueKaBean();
        yueKaBean1.setName("小豪豪");
        yueKaBean1.setAge(24);
        yueKaBean1.setStart(4);
        yueKaBean1.setPrice("￥300-460");
        yueKaBean1.setSum(20);
        yueKaBean1.setCount(48);
        yueKaBean1.setEvaluate("下次还找你");
        yueKaBean1.setLable(lable2);
        yueKaBean1.setImgList(img2);
        list.add(yueKaBean1);
        yueKaAdapter = new YueKaAdapter(list,getActivity());

        listView.setAdapter(yueKaAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), YueChatActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.orderYue:
                startActivity(new Intent(getActivity(), OrderYueActivity.class));
                break;
            case R.id.freeTime:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
        }
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
    public void onSure(Date dateStart, Date dateEnd) {
        startTime.setText(SystemUtil.UtilDateToString(dateStart));
        endTime.setText(SystemUtil.UtilDateToString(dateEnd));
    }
}

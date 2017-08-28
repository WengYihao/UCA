package com.cn.uca.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Text;
import com.cn.uca.R;
import com.cn.uca.adapter.YueKaAdapter;
import com.cn.uca.bean.DateType;
import com.cn.uca.bean.YueKaBean;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.datepicker.DatePickDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2017/8/11.
 */

public class YueKaFragment extends Fragment implements View.OnClickListener,OnSureLisener{

    private View view;
    private ListView listView;
    private YueKaAdapter yueKaAdapter;
    private List<YueKaBean> list;
    private List<String> lable1,lable2,img1,img2;
    private TextView startTime,endTime;
    private boolean index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yueka, null);

        ininView();
        return view;
    }

    private void ininView() {
        listView  = (ListView)view.findViewById(R.id.listView);
        startTime = (TextView)view.findViewById(R.id.startTime);
        endTime = (TextView)view.findViewById(R.id.endTime);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

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
        yueKaBean.setPrice("0.4k-0.6k");
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
        yueKaBean1.setPrice("0.5k-0.6k");
        yueKaBean1.setSum(20);
        yueKaBean1.setCount(48);
        yueKaBean1.setEvaluate("下次还找你");
        yueKaBean1.setLable(lable2);
        yueKaBean1.setImgList(img2);
        list.add(yueKaBean1);
        yueKaAdapter = new YueKaAdapter(list,getActivity());

        listView.setAdapter(yueKaAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startTime:
                index = true;
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.endTime:
                index = false;
                showDatePickDialog(DateType.TYPE_YMD);
                break;
        }
    }
    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(getActivity());
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(this);
        dialog.show();
    }

    @Override
    public void onSure(Date date) {
        if (index){
            startTime.setText(SystemUtil.UtilDateToString(date));
        }else{
            endTime.setText(SystemUtil.UtilDateToString(date));
        }
//        ToastXutil.show(date.toString()+"--");
    }
}

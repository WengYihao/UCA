package com.cn.uca.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.travel.PlaceAdapter;
import com.cn.uca.bean.travel.TravelPlaceBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.HorizontalListView;
import com.cn.uca.view.MyEditText;

import java.util.ArrayList;
import java.util.List;

public class TourismActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView more;
    private ImageView back;
    private MyEditText seach;
    private HorizontalListView listView;
    private PlaceAdapter placeAdapter;
    private List<TravelPlaceBean> list;
    private LinearLayout llTitle,layout1,layout2,layout3,layout4,layout5,layout6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_tourism);
        initView();
    }

    private void initView() {
        back = (ImageView)findViewById(R.id.back);
        seach = (MyEditText) findViewById(R.id.seach);
        more = (TextView)findViewById(R.id.more);
        llTitle = (LinearLayout)findViewById(R.id.llTitle);
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        layout2 = (LinearLayout)findViewById(R.id.layout2);
        layout3 = (LinearLayout)findViewById(R.id.layout3);
        layout4 = (LinearLayout)findViewById(R.id.layout4);
        layout5 = (LinearLayout)findViewById(R.id.layout5);
        layout6 = (LinearLayout)findViewById(R.id.layout6);

        SetLayoutParams.setLinearLayout(layout1,MyApplication.width/3,MyApplication.width/3);
        SetLayoutParams.setLinearLayout(layout2,MyApplication.width/3,MyApplication.width/3);
        SetLayoutParams.setLinearLayout(layout3,MyApplication.width/3,MyApplication.width/3);
        SetLayoutParams.setLinearLayout(layout4,MyApplication.width/3,MyApplication.width/3);
        SetLayoutParams.setLinearLayout(layout5,MyApplication.width/3,MyApplication.width/3);
        SetLayoutParams.setLinearLayout(layout6,MyApplication.width/3,MyApplication.width/3);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);

        StatusMargin.setRelativeLayout(this,llTitle);


        listView = (HorizontalListView)findViewById(R.id.llName);
        back.setOnClickListener(this);
        more.setOnClickListener(this);
        seach.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //是否是回车键
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(TourismActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //搜索
//                    search();
                }
                return false;
            }
        });




        list = new ArrayList<>();
        TravelPlaceBean bean = new TravelPlaceBean();
        bean.setName("北京");
        bean.setUrl("http://www.szyouka.com/1.png");
        TravelPlaceBean bean1 = new TravelPlaceBean();
        bean1.setName("上海");
        bean1.setUrl("http://www.szyouka.com/2.png");
        TravelPlaceBean bean2 = new TravelPlaceBean();
        bean2.setName("广州");
        bean2.setUrl("http://www.szyouka.com/3.png");
        TravelPlaceBean bean3 = new TravelPlaceBean();
        bean3.setName("深圳");
        bean3.setUrl("http://www.szyouka.com/4.png");
        list.add(bean);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        placeAdapter = new PlaceAdapter(list,TourismActivity.this);
        listView.setAdapter(placeAdapter);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) listView.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = MyApplication.width*2/7;// 控件的高强制设成20
        listView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.more:
//                List<String> list = new ArrayList<>();
//                list.add("我的收藏");
//                list.add("我的订单");
//                list.add("消息中心");
//                ShowPopupWindow.show(MyApplication.getContext(),list,more);
                break;
            case  R.id.layout1:
                //周边游
                startActivity(new Intent(TourismActivity.this,PeripheryTravelActivity.class));
                break;
            case R.id.layout2:
                //国内游
                startActivity(new Intent(TourismActivity.this,DomesticTravelActivity.class));
                break;
            case R.id.layout3:
                //出境游
//                ToastXutil.show("敬请期待-出境游");
                startActivity(new Intent(TourismActivity.this,ExitTravelActivity.class));
                break;
            case R.id.layout4:
                //定制游
                ToastXutil.show("敬请期待-定制游");
                break;
            case R.id.layout5:
                //亲子游
                ToastXutil.show("敬请期待-亲子游");
                break;
            case R.id.layout6:
                //蜜月游
                startActivity(new Intent(TourismActivity.this,HoneymoonTravelActivity.class));
                break;
        }
    }
}

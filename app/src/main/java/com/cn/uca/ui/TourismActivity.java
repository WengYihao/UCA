package com.cn.uca.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.PlaceAdapter;
import com.cn.uca.config.MyApplication;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.view.HorizontalListView;
import com.cn.uca.view.MyEditText;

import java.util.ArrayList;
import java.util.List;

public class TourismActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,view,more;
    private MyEditText seach;
    private HorizontalListView listView;
    private PlaceAdapter placeAdapter;
    private RelativeLayout llTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_tourism);
        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        seach = (MyEditText) findViewById(R.id.seach);
        more = (TextView)findViewById(R.id.more);
        llTitle = (RelativeLayout)findViewById(R.id.llTitle);
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

        String [] data = getResources().getStringArray(R.array.hot_list);
        placeAdapter = new PlaceAdapter(data,getApplicationContext());
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
                List<String> list = new ArrayList<>();
                list.add("我的收藏");
                list.add("我的订单");
                list.add("消息中心");
                ShowPopupWindow.show(MyApplication.getContext(),list,more);
                break;
        }
    }
}

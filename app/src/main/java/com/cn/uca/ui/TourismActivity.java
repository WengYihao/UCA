package com.cn.uca.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.PlaceAdapter;
import com.cn.uca.adapter.ShowAdapter;
import com.cn.uca.config.MyApplication;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.HorizontalListView;
import com.cn.uca.view.MyEditText;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class TourismActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView view,more;
    private MyEditText seach;
    private HorizontalListView listView;
    private PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism);
        StatusBarUtil.setTranslucentForImageView(this, 0, view);//设置状态栏透明

        initView();
    }

    private void initView() {
        seach = (MyEditText) findViewById(R.id.seach);

        more = (TextView)findViewById(R.id.more);

        listView = (HorizontalListView)findViewById(R.id.llName);
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
                    ToastXutil.show("123");
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
            case R.id.more:
                Log.i("123","456");
                List<String> list = new ArrayList<>();
                list.add("我的收藏");
                list.add("我的订单");
                list.add("消息中心");
                ShowPopupWindow.show(MyApplication.getContext(),list,more);
                break;
        }
    }
}

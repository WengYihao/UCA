package com.cn.uca.ui.view.home.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.PlaceAdapter;
import com.cn.uca.bean.home.travel.TravelPlaceBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.HorizontalListView;
import com.cn.uca.view.MyEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourismActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView more;
    private ImageView back;
    private MyEditText seach;
    private HorizontalListView listView;
    private PlaceAdapter placeAdapter;
    private List<TravelPlaceBean> list;
    private RelativeLayout llList;
    private LinearLayout llTitle,layout1,layout2,layout3,layout4,layout5,layout6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism);
        initView();
        getHotTicket();
    }

    private void initView() {
        back = (ImageView)findViewById(R.id.back);
        seach = (MyEditText) findViewById(R.id.seach);
        more = (TextView)findViewById(R.id.more);
        llList = (RelativeLayout)findViewById(R.id.llList);
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
        llList.setOnClickListener(this);
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
        placeAdapter = new PlaceAdapter(list,TourismActivity.this);
        listView.setAdapter(placeAdapter);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) listView.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = MyApplication.width*2/7;// 控件的高强制设成20
        listView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(TourismActivity.this,SpotTicketActivity.class);
                intent.putExtra("id",list.get(i).getScenic_spot_id());
                startActivity(intent);
            }
        });
    }

    private void getHotTicket(){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getHotTicket(sign, time_stamp, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray array = jsonObject.getJSONArray("data");
                            Gson gson = new Gson();
                            List<TravelPlaceBean> bean = gson.fromJson(array.toString(),new TypeToken<List<TravelPlaceBean>>() {
                            }.getType());
                            list.addAll(bean);
                            placeAdapter.setList(list);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.more:

                break;
            case R.id.llList:
                startActivity(new Intent(TourismActivity .this,SpotListActivity.class));
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
                startActivity(new Intent(TourismActivity.this,ExitTravelActivity.class));
                break;
            case R.id.layout4:
                //定制游
                startActivity(new Intent(TourismActivity.this,CustomizTravelActivity.class));
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

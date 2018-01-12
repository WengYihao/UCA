package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActionManageActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,title,time,follow,signup,stop;
    private SimpleDraweeView pic;
    private LinearLayout layout1,layout2,layout3,layout4;
    private int id;
    private long midDate;
    private TimeCount timeStr;
    private int isStart = 0;//1售票中，2暂停售票

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_manage);

        getInfo();
        initView();
        cityCafeAdmin();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        pic = (SimpleDraweeView)findViewById(R.id.pic);
        title = (TextView)findViewById(R.id.title);
        time = (TextView)findViewById(R.id.time);
        follow = (TextView)findViewById(R.id.follow);
        signup = (TextView)findViewById(R.id.signup);
        stop = (TextView)findViewById(R.id.stop);
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        layout2 = (LinearLayout)findViewById(R.id.layout2);
        layout3 = (LinearLayout)findViewById(R.id.layout3);
        layout4 = (LinearLayout)findViewById(R.id.layout4);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        SetLayoutParams.setLinearLayout(layout1,MyApplication.width/2,MyApplication.width/3);
        SetLayoutParams.setLinearLayout(layout2,MyApplication.width/2,MyApplication.width/3);
        SetLayoutParams.setLinearLayout(layout3,MyApplication.width/2,MyApplication.width/3);
        SetLayoutParams.setLinearLayout(layout4,MyApplication.width/2,MyApplication.width/3);

        back.setOnClickListener(this);
        stop.setOnClickListener(this);

    }

    private void initTime(String str){
        Drawable drawable = null;
        try{
            long ti= SystemUtil.StringToUtilDate3(str).getTime();
            midDate = ti- System.currentTimeMillis();
            if (midDate < 0){
                time.setText("已结束");
                stop.setEnabled(false);
                drawable = getResources().getDrawable(R.mipmap.on_stop);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                stop.setCompoundDrawables(drawable, null, null, null);
                stop.setText("暂停报名");
            }else{
                timeStr = new TimeCount(midDate, 1000);// 构造CountDownTimer对象
                timeStr.start();
                switch (isStart){
                    case 1:
                        drawable = getResources().getDrawable(R.mipmap.on_now);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        stop.setCompoundDrawables(drawable, null, null, null);
                        stop.setText("正在报名");
                        break;
                    case 2:
                        drawable = getResources().getDrawable(R.mipmap.on_stop);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        stop.setCompoundDrawables(drawable, null, null, null);
                        stop.setText("暂停报名");
                        break;
                }
            }
        }catch (Exception e){
            Log.e("456",e.getMessage()+"转换报错");
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.stop:
                staSusCityCafe();
                break;
            case R.id.layout1:
                startActivity(new Intent(this, TicketManageActivity.class));//票券管理
                break;
            case R.id.layout2:
//                startActivity(new Intent(this, MyFollowActivity.class));//邀请
                break;
            case R.id.layout3:
                ToastXutil.show("暂不支持修改");
                break;
            case R.id.layout4:
                Intent intent = new Intent();
                intent.setClass(ActionManageActivity.this, SettlementActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);//结算
                break;
        }
    }

    //开始/暂停报名
    private void staSusCityCafe(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("city_cafe_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.staSusCityCafe(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                if ((int) response == 0) {
                    Drawable drawable = null;
                    switch (isStart) {
                        case 1:
                            drawable = getResources().getDrawable(R.mipmap.on_stop);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            stop.setCompoundDrawables(drawable, null, null, null);
                            stop.setText("暂停报名");
                            isStart = 2;
                            break;
                        case 2:
                            drawable = getResources().getDrawable(R.mipmap.on_now);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            stop.setCompoundDrawables(drawable, null, null, null);
                            stop.setText("正在报名");
                            isStart = 1;
                            break;
                    }
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
    private void cityCafeAdmin(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("city_cafe_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.cityCafeAdmin(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            pic.setImageURI(Uri.parse(object.getString("cover_url")));
                            title.setText(object.getString("title"));
                            time.setText("");
                            follow.setText(object.getInt("collection_number")+"关注");
                            signup.setText(object.getInt("enter_count")+"报名");
                            if (object.getBoolean("start_register")){
                                isStart = 1;
                            }else{
                                isStart = 2;
                            }
                            initTime(object.getString("beg_time"));
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage()+"解析报错");
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
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            time.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            long hh = millisUntilFinished / 60 / 60 /1000% 60;
            long mm = millisUntilFinished / 60/1000 % 60;
            long ss = millisUntilFinished /1000% 60;
            time.setText("剩余"+hh + " 时 " + mm + " 分 " + ss + " 秒");
        }
    }
}

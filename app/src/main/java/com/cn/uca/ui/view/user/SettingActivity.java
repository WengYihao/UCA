package com.cn.uca.ui.view.user;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.dialog.ToastDialog;

import org.json.JSONObject;

public class SettingActivity extends BaseBackActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private RelativeLayout layout1,layout5;
//    private RelativeLayout layout1,layout2,layout3,layout4,layout5;
    private Switch select;
    private TextView back,exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityCollector.addActivity(this);
        initView();
        isPush();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
//        layout2 = (RelativeLayout)findViewById(R.id.layout2);
//        layout3 = (RelativeLayout)findViewById(R.id.layout3);
//        layout4 = (RelativeLayout)findViewById(R.id.layout4);
        layout5 = (RelativeLayout)findViewById(R.id.layout5);

        select = (Switch)findViewById(R.id.select);
        exit = (TextView)findViewById(R.id.exit);

        back.setOnClickListener(this);
        layout1.setOnClickListener(this);
//        layout2.setOnClickListener(this);
//        layout3.setOnClickListener(this);
//        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        exit.setOnClickListener(this);
        select.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            setUserPush(true);
        }else {
            setUserPush(false);
        }
    }

    private void isPush(){
        UserHttp.getUserPush(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        JSONObject object = jsonObject.getJSONObject("data");
                        boolean open_push = object.getBoolean("open_push");
                        select.setChecked(open_push);
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

    private void setUserPush(boolean push){
        UserHttp.setUserPush(push, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    if ((int) response == 0){
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                if (!StringXutil.isEmpty(errorMsg)){
                    ToastXutil.show(errorMsg);
                }
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
            case R.id.layout1:
                goToMarket(this,"com.cn.uca");
                break;
//            case R.id.layout2:
//
//                break;
//            case R.id.layout3:
//
//                break;
//            case R.id.layout4:
//
//                break;
            case R.id.layout5:
                startActivity(new Intent(this,FeedBackActivity.class));
                break;
            case R.id.exit:
                ToastDialog.exit(this);
                break;
        }
    }

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}

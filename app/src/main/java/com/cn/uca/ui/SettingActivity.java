package com.cn.uca.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.dialog.ToastDialog;

import org.json.JSONObject;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private RelativeLayout layout1,layout2,layout3;
    private Switch select;
    private TextView exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        isPush();
    }

    private void initView() {
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        layout3 = (RelativeLayout)findViewById(R.id.layout3);

        select = (Switch)findViewById(R.id.select);
        exit = (TextView)findViewById(R.id.exit);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
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
        MyApplication.getServer().getUserPush(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    Log.i("123",response.toString()+"---");
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
        MyApplication.getServer().setUserPush(push, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString()+"---");
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0){
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
            case R.id.layout1:

                break;
            case R.id.layout2:

                break;
            case R.id.layout3:

                break;
            case R.id.exit:
                ToastDialog.exit(this);
                break;
        }
    }
}

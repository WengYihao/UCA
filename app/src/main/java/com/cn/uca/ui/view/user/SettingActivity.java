package com.cn.uca.ui.view.user;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cn.uca.R;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ActivityCollector;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.dialog.ToastDialog;

import org.json.JSONObject;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SettingActivity extends BaseBackActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private RelativeLayout layout1,layout2,layout3;
    private Switch select;
    private TextView exit;
    private ImageView aaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_setting);
        ActivityCollector.addActivity(this);
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
        aaa = (ImageView)findViewById(R.id.aaa);
//        Glide.with(this)
//                .load("http://www.szyouka.com/youkatravel/fileRresources/upload/ticketPicture/shenzhenhuanlegu111.jpg")
//                .crossFade(1000)
//                .bitmapTransform(new BlurTransformation(this,23,4)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
//                .into(aaa);
        Glide.with(this).load("http://www.szyouka.com/youkatravel/fileRresources/upload/ticketPicture/shenzhenhuanlegu111.jpg").bitmapTransform(new BlurTransformation(this, 18)).into(aaa);
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

package com.cn.uca.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.user.UserInfo;
import com.cn.uca.config.MyApplication;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.ToastXutil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class InfoSexActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,finish;
    private RadioButton man,woman,secrecy;
    private String sexType = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_info_sex);

        initView();
        getInfo();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        finish = (TextView)findViewById(R.id.finish);

        man = (RadioButton)findViewById(R.id.man);
        woman = (RadioButton)findViewById(R.id.woman);
        secrecy = (RadioButton)findViewById(R.id.secrecy);

        back.setOnClickListener(this);
        finish.setOnClickListener(this);
        man.setOnClickListener(this);
        woman.setOnClickListener(this);
        secrecy.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.finish:
                setSex();
                break;
            case R.id.man:
                sexType = "1";
                break;
            case R.id.woman:
                sexType = "2";
                break;
            case R.id.secrecy:
                sexType = "3";
                break;
        }
    }

    private void setSex(){
        UserInfo info = new UserInfo();
        switch (sexType){
            case "1":
                info.setSex_id(1);
                break;
            case "2":
                info.setSex_id(2);
                break;
            case "3":
                info.setSex_id(3);
                break;
        }
        UserHttp.updateUserInfo(info, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                        int code = jsonObject.getInt("code");
                        if (code == 0){
                            ToastXutil.show("修改成功");
                            Intent intent = new Intent();
                            intent.putExtra("type","sex");
                            intent.putExtra("sex",sexType);
                            setResult(0, intent);
                            InfoSexActivity.this.finish();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });
    }

    public void getInfo() {
        Intent intent = getIntent();
        if (intent != null){
            String sex = intent.getStringExtra("sex");
            switch (sex){
                case "男":
                    man.setChecked(true);
                    break;
                case "女":
                    woman.setChecked(true);
                    break;
                case "保密":
                    secrecy.setChecked(true);
                    break;
            }
        }
    }
}

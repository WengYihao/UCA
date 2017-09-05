package com.cn.uca.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.user.UserInfo;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.datepicker.DatePickDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener,OnSureLisener {

    private RelativeLayout layout1,layout2,layout3,layout4,layout5,layout6,layout7;
    private TextView nickName,sex,birthDate,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        initView();
    }

    private void initView() {
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        layout3 = (RelativeLayout)findViewById(R.id.layout3);
        layout4 = (RelativeLayout)findViewById(R.id.layout4);
        layout5 = (RelativeLayout)findViewById(R.id.layout5);
        layout6 = (RelativeLayout)findViewById(R.id.layout6);
        layout7 = (RelativeLayout)findViewById(R.id.layout7);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        layout7.setOnClickListener(this);

        nickName = (TextView)findViewById(R.id.nickName);
        sex = (TextView)findViewById(R.id.sex);
        birthDate = (TextView)findViewById(R.id.date);
        phone = (TextView)findViewById(R.id.phone);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(InformationActivity.this,InfoActivity.class);
        switch (view.getId()){
            case R.id.layout1:
                intent.putExtra("type","nickName");
                intent.putExtra("nickName","哈哈");
                startActivityForResult(intent,0);
                break;
            case R.id.layout2:
                Intent intent1 = new Intent();
                intent1.setClass(InformationActivity.this,InfoSexActivity.class);
                intent1.putExtra("sex","1");
                startActivityForResult(intent1,0);
                break;
            case R.id.layout3:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.layout4:
                intent.putExtra("type","phone");
                intent.putExtra("phone","15112360329");
                startActivityForResult(intent,0);
                break;
            case R.id.layout5:

                break;
            case R.id.layout6:

                break;
            case R.id.layout7:

                break;
        }
    }

    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(60);
        //设置标题
        dialog.setTitle(R.string.infoActivity_date_title_text);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (data != null){
                String type = data.getStringExtra("type");
                switch (type){
                    case "nickName":
                        nickName.setText(data.getStringExtra("nickName"));
                        break;
                    case "sex":
                        switch (data.getStringExtra("sex")){
                            case "1":
                                sex.setText("男");
                                break;
                            case "2":
                                sex.setText("女");
                                break;
                            case "3":
                                sex.setText("保密");
                                break;
                        }
                        break;
                    case "date":
                        birthDate.setText(data.getStringExtra("date"));
                        break;
                    case "phone":
                        phone.setText(data.getStringExtra("phone"));
                        break;
                }
            }
        }
    }

    @Override
    public void onSure(final Date date) {
        UserInfo info = new UserInfo();
        info.setUser_birth_date(SystemUtil.UtilDateToString(date));
        MyApplication.getServer().updateUserInfo(info, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                        int code = jsonObject.getInt("code");
                        if (code == 0){
                            ToastXutil.show("修改成功");
                            birthDate.setText(SystemUtil.UtilDateToString(date));
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
}

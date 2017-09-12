package com.cn.uca.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.user.UserInfo;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.datepicker.DatePickDialog;
import com.cn.uca.view.dialog.ToastDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class InformationActivity extends BaseBackActivity implements View.OnClickListener,OnSureLisener {

    private RelativeLayout layout1,layout2,layout3,layout4,layout5,layout6,layout7;
    private TextView nickName,sex,birthDate,phone;
    private String userName,userSex,userAge,userPhoneNumber;
    private int sexId;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_information);

        initView();
        getInfo();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        layout3 = (RelativeLayout)findViewById(R.id.layout3);
        layout4 = (RelativeLayout)findViewById(R.id.layout4);
        layout5 = (RelativeLayout)findViewById(R.id.layout5);
        layout6 = (RelativeLayout)findViewById(R.id.layout6);
        layout7 = (RelativeLayout)findViewById(R.id.layout7);

        back.setOnClickListener(this);
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
            case R.id.back:
                sendInfo();
                break;
            case R.id.layout1:
                intent.putExtra("type","nickName");
                intent.putExtra("nickName",userName);
                startActivityForResult(intent,0);
                break;
            case R.id.layout2:
                Intent intent1 = new Intent();
                intent1.setClass(InformationActivity.this,InfoSexActivity.class);
                intent1.putExtra("sex",userSex);
                startActivityForResult(intent1,0);
                break;
            case R.id.layout3:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.layout4:
                if (!StringXutil.isEmpty(SharePreferenceXutil.getPhoneNumber())){
                    intent.putExtra("type","phone");
                    intent.putExtra("phone", SharePreferenceXutil.getPhoneNumber());
                    startActivityForResult(intent,0);
                }else{
                    intent.putExtra("type","phone");
                    intent.putExtra("phone", "");
                    startActivityForResult(intent,0);
                }
                break;
            case R.id.layout5:

                break;
            case R.id.layout6:

                break;
            case R.id.layout7:
                Intent intent2 = new Intent();
                intent2.setClass(InformationActivity.this,UpdatePasswordActivity.class);
                startActivity(intent2);
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
                        userName = data.getStringExtra("nickName");
                        nickName.setText(userName);
                        break;
                    case "sex":
                        switch (data.getStringExtra("sex")){
                            case "1":
                                userSex = "男";
                                sex.setText(userSex);
                                break;
                            case "2":
                                userSex = "女";
                                sex.setText(userSex);
                                break;
                            case "3":
                                userSex = "保密";
                                sex.setText(userSex);
                                break;
                        }
                        break;
                    case "date":
                        userAge = data.getStringExtra("date");
                        birthDate.setText(userAge);
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
                            userAge = SystemUtil.UtilDateToString(date);
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
            userName = intent.getStringExtra("userName");
            userAge = intent.getStringExtra("userAge");
            userSex = intent.getStringExtra("userSex");
            nickName.setText(userName);
            sex.setText(userSex);
            birthDate.setText(userAge);
            if (!StringXutil.isEmpty(SharePreferenceXutil.getPhoneNumber())){
                phone.setText(SharePreferenceXutil.getPhoneNumber());
            }else{
                phone.setText("");
            }
        }
    }

    private void sendInfo(){
        Intent intent = new Intent();
        intent.putExtra("userName",userName);
        intent.putExtra("userAge",userAge);
        intent.putExtra("userSex",userSex);
        setResult(0, intent);
        this.finish();
    }

    /**
     * 按下返回键时调用
     */
    @Override
    public void onBackPressed() {
        sendInfo();
        super.onBackPressed();
    }
}

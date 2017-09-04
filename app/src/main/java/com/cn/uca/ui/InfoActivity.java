package com.cn.uca.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.user.UserInfo;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener{

    private MyEditText myEditText,codePassword;
    private TextView title,finish,next;
    private String type;
    private String content;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initView();
        getInfo();
    }

    private void initView() {
        myEditText = (MyEditText)findViewById(R.id.myEditText);
        codePassword = (MyEditText)findViewById(R.id.codePassword);
        title = (TextView)findViewById(R.id.title);
        finish = (TextView)findViewById(R.id.finish);
        next = (TextView)findViewById(R.id.next);

        finish.setOnClickListener(this);
        next.setOnClickListener(this);

//        myEditText.addTextChangedListener(this);
    }

    public void getInfo() {
        Intent intent = getIntent();
        if (intent != null){
            type = intent.getStringExtra("type");
            switch (type){
                case "nickName":
                    title.setText(R.string.infoActivity_nickName_title_text);
                    myEditText.setText(intent.getStringExtra("nickName"));
                    break;
                case "phone":
                    title.setText(R.string.infoActivity_phone_title_text);
                    myEditText.setText(intent.getStringExtra("phone"));
                    myEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                    myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    myEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            content = myEditText.getText().toString();
                            if (content.length() == 11){
                                next.setVisibility(View.VISIBLE);
                            }else{
                                next.setVisibility(View.GONE);
                                codePassword.setVisibility(View.GONE);
                            }
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        final Intent intent = new Intent();
        switch (view.getId()){
            case R.id.finish:
                switch (type){
                    case "nickName":
                        UserInfo info = new UserInfo();
                        info.setUser_nick_name(myEditText.getText().toString().trim());
                        MyApplication.getServer().updateUserInfo(info, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                if (i == 200){
                                    try {
                                        JSONObject jsonObject = new JSONObject(new String(bytes,"UTF-8"));
                                        int code = jsonObject.getInt("code");
                                        if (code == 0){
                                            ToastXutil.show("修改成功");
                                            intent.putExtra("type","nickName");
                                            intent.putExtra("nickName", myEditText.getText().toString());
                                            setResult(0, intent);
                                            InfoActivity.this.finish();
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

                        break;
                    case "phone":
//                        intent.putExtra("type","phone");
//                        intent.putExtra("phone", myEditText.getText().toString());
//                        setResult(0, intent);
//                        this.finish();
                        break;
                }
                break;
            case R.id.next:
                if (StringXutil.isPhoneNumberValid(content)){
                    ToastXutil.show("输入的是手机号");
                    codePassword.setVisibility(View.VISIBLE);

                }else{
                    ToastXutil.show("请输入正确的手机号");
                }
                break;
        }
    }
}

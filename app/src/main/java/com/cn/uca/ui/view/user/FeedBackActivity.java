package com.cn.uca.ui.view.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;

import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,submit;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        editText = (EditText)findViewById(R.id.editText);
        submit = (TextView)findViewById(R.id.submit);

        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void feedback(){
        String reason = editText.getText().toString();
        if (!StringXutil.isEmpty(reason)){
            Map<String,Object> map = new HashMap<>();
            String account_token = SharePreferenceXutil.getAccountToken();
            map.put("account_token", account_token);
            String time_stamp = SystemUtil.getCurrentDate2();
            map.put("time_stamp",time_stamp);
            map.put("reason",reason) ;
            String sign = SignUtil.sign(map);
            UserHttp.feedback(account_token,sign, time_stamp, reason, new CallBack() {
                @Override
                public void onResponse(Object response) {
                    if ((int)response == 0){
                        ToastXutil.show("发送成功");
                        FeedBackActivity.this.finish();
                    }
                }

                @Override
                public void onErrorMsg(String errorMsg) {

                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        }else{
            ToastXutil.show("投诉内容不能为空！");
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.submit:
                feedback();
                break;
        }
    }
}

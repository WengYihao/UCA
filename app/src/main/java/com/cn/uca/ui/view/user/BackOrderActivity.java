package com.cn.uca.ui.view.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BackOrderActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,oneself,other,submit;
    private EditText reason;
    private int typeId = 1;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_order);

        getInfo();
        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        oneself = (TextView)findViewById(R.id.oneself);
        other = (TextView)findViewById(R.id.other);
        reason = (EditText)findViewById(R.id.reason);
        submit = (TextView)findViewById(R.id.submit);

        back.setOnClickListener(this);
        oneself.setOnClickListener(this);
        other.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            orderId = intent.getIntExtra("orderId",0);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.oneself:
                oneself.setTextColor(getResources().getColor(R.color.white));
                oneself.setBackgroundResource(R.drawable.twenty_circular_ori_background);

                other.setTextColor(getResources().getColor(R.color.grey3));
                other.setBackgroundResource(R.drawable.twenty_circular_gray_background);
                typeId = 1;
                break;
            case R.id.other:
                other.setTextColor(getResources().getColor(R.color.white));
                other.setBackgroundResource(R.drawable.twenty_circular_ori_background);

                oneself.setTextColor(getResources().getColor(R.color.grey3));
                oneself.setBackgroundResource(R.drawable.twenty_circular_gray_background);
                typeId = 2;
                break;
            case R.id.submit:
                travleEscortBack();
                break;
        }
    }

    private void travleEscortBack(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("user_order_id",orderId);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("escort_back_type_id",typeId);
        String reason = this.reason.getText().toString();
        if (!StringXutil.isEmpty(reason)){
            map.put("reason",reason);
            String sign = SignUtil.sign(map);
            UserHttp.travleEscortBack(orderId, account_token, time_stamp, sign, reason, typeId, new CallBack() {
                @Override
                public void onResponse(Object response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response.toString());
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                ToastXutil.show("提交申请成功");
                                BackOrderActivity.this.finish();
                                break;
                            case 400:
                                ToastXutil.show("申请已提交，等待领咖响应");
                                BackOrderActivity.this.finish();
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
        }else{
            ToastXutil.show("原因描述不能为空");
        }



    }
}

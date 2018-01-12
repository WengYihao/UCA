package com.cn.uca.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;

import org.json.JSONObject;

public class WalletActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,count;
    private LinearLayout layout1,layout2;
    private RelativeLayout layout3,layout4,layout5,layout6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initView();
        getWallet();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        layout2 = (LinearLayout)findViewById(R.id.layout2);
        layout3 = (RelativeLayout) findViewById(R.id.layout3);
        layout4 = (RelativeLayout) findViewById(R.id.layout4);
        layout5 = (RelativeLayout)findViewById(R.id.layout5);
        layout6 = (RelativeLayout)findViewById(R.id.layout6);

        back.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);

        count = (TextView)findViewById(R.id.count);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.layout1:
                //明细
                startActivity(new Intent(WalletActivity.this,WalletDetailActivity.class));
                break;
            case R.id.layout2:
                //优惠券
                break;
            case R.id.layout3:
                startActivity(new Intent(WalletActivity.this,RechargeActivity.class));
                break;
            case R.id.layout4:
                //提现
                break;
            case R.id.layout5:
//                银行卡
                break;
            case R.id.layout6:
                startActivity(new Intent(WalletActivity.this,RechargeSettingActivity.class));
                break;
        }
    }

    private void getWallet(){
        UserHttp.getWallet(new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString()+"---787878");
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    double num = jsonObject.getDouble("user_balance");
                    count.setText(num+"");
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                Log.i("123",errorMsg+"---12346855");
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}

package com.cn.uca.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;

//支付设置
public class RechargeSettingActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private RelativeLayout layout1,layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_setting);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);

        back.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.layout1:
                startActivity(new Intent(RechargeSettingActivity.this,WalletPasswordActivity.class));
                break;
            case R.id.layout2:

                break;
        }
    }
}

package com.cn.uca.ui.view.home.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;

/**
 * 积分记录
 */
public class IntegralRecordActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,exchange;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_record);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        exchange =(TextView)findViewById(R.id.exchange);
        exchange.setOnClickListener(this);

        back.setOnClickListener(this);
        exchange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.exchange:
                startActivity(new Intent(IntegralRecordActivity.this,IntegralExchangeActivity.class));
                break;
        }
    }
}

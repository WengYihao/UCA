package com.cn.uca.ui.view.home.lvpai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;

/**
 * 商家订单
 */
public class MerchantOrderActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_order);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }
}

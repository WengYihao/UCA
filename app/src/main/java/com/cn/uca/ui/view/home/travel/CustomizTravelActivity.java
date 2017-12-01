package com.cn.uca.ui.view.home.travel;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SystemUtil;

/**
 * 定制游
 */
public class CustomizTravelActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customiz_travel);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);

        layout = (LinearLayout)findViewById(R.id.layout);
        SetLayoutParams.setRelativeLayout(layout, MyApplication.width - SystemUtil.dip2px(30),MyApplication.height/2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }
}

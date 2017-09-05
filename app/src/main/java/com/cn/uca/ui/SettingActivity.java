package com.cn.uca.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.cn.uca.R;
import com.cn.uca.util.ToastXutil;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private Switch select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        select = (Switch)findViewById(R.id.select);
        select.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            ToastXutil.show("开启推送");
        }else {
            ToastXutil.show("关闭推送");
        }
    }
}

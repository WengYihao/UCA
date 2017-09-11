package com.cn.uca.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cn.uca.R;
import com.cn.uca.swipeback.SwipeBackActivity;

public class BaseBackActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }
}

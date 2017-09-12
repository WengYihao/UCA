package com.cn.uca.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cn.uca.R;
import com.cn.uca.util.FitStateUI;

public class WalletActivity extends BaseBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_wallet);
    }
}

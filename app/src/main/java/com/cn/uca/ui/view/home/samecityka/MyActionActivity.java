package com.cn.uca.ui.view.home.samecityka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;

public class MyActionActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,title01,title02,title03,title04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_action);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        title03 = (TextView)findViewById(R.id.title03);
        title04 = (TextView)findViewById(R.id.title04);

        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);
        title03.setOnClickListener(this);
        title04.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.title01:
                title01.setTextColor(getResources().getColor(R.color.ori));
                title02.setTextColor(getResources().getColor(R.color.grey));
                title03.setTextColor(getResources().getColor(R.color.grey));
                title04.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.title02:
                title01.setTextColor(getResources().getColor(R.color.grey));
                title02.setTextColor(getResources().getColor(R.color.ori));
                title03.setTextColor(getResources().getColor(R.color.grey));
                title04.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.title03:
                title01.setTextColor(getResources().getColor(R.color.grey));
                title02.setTextColor(getResources().getColor(R.color.grey));
                title03.setTextColor(getResources().getColor(R.color.ori));
                title04.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.title04:
                title01.setTextColor(getResources().getColor(R.color.grey));
                title02.setTextColor(getResources().getColor(R.color.grey));
                title03.setTextColor(getResources().getColor(R.color.grey));
                title04.setTextColor(getResources().getColor(R.color.ori));
                break;
        }
    }
}

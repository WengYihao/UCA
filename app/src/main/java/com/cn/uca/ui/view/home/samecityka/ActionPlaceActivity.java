package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.view.MyEditText;

public class ActionPlaceActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,mapLayout,finish;
    private MyEditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_place);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        mapLayout = (TextView)findViewById(R.id.mapLayout);
        address = (MyEditText)findViewById(R.id.address);
        finish = (TextView)findViewById(R.id.finish);

        back.setOnClickListener(this);
        mapLayout.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.mapLayout:
                startActivity(new Intent(ActionPlaceActivity.this,MapChoiceActivity.class));
                break;
            case R.id.finish:

                break;
        }

    }
}

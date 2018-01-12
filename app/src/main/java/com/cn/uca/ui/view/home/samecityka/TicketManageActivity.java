package com.cn.uca.ui.view.home.samecityka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;

/**
 * 票券管理
 */
public class TicketManageActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_manage);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listView);
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

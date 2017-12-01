package com.cn.uca.ui.view.home.raider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;

import org.w3c.dom.Text;

public class SpotDetailActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,title,text;
    private String content,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
        getInfo();
        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        text = (TextView)findViewById(R.id.text);

        back.setOnClickListener(this);
        title.setText(name);
        text.setText(content);

    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            content = intent.getStringExtra("content");
            name = intent.getStringExtra("name");
        }
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

package com.cn.uca.ui.view.yueka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.ToastXutil;

public class ToBeCollarActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,link,isCollar;
    private CheckBox isCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_collar);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        isCheck = (CheckBox)findViewById(R.id.isCheck);
        link = (TextView)findViewById(R.id.link);
        isCollar  =(TextView)findViewById(R.id.tobeCollar);
        back.setOnClickListener(this);
        link.setOnClickListener(this);
        isCollar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.tobeCollar:
                if (isCheck.isChecked()){
                    SharePreferenceXutil.setCollar(true);
                    ToastXutil.show("恭喜你已成为大咖，赶紧去发布吧~~~");
                    Intent intent = new Intent();
                    intent.putExtra("result","ok");
                    setResult(0,intent);
                    this.finish();
                }else{
                    ToastXutil.show("请阅读大咖相关条例并且同意！");
                }
                break;
        }
    }
}

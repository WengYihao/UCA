package com.cn.uca.ui.view.home.samecityka;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.view.MyEditText;

public class ActionSearchActivity extends BaseBackActivity implements View.OnClickListener{

    private MyEditText search;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_search);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);
        search = (MyEditText)findViewById(R.id.search);
        SpannableString ss = new SpannableString("搜索活动关键字/时间/位置等");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(10,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        search.setHint(new SpannedString(ss));
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

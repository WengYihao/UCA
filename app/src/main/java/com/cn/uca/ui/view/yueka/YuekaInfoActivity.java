//package com.cn.uca.ui.view.yueka;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.cn.uca.R;
//import com.cn.uca.ui.view.util.BaseBackActivity;
//
//public class YuekaInfoActivity extends BaseBackActivity implements View.OnClickListener{
//
//    private RelativeLayout layout1,layout2;
//    private TextView back;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_yueka_info);
//
//        initView();
//    }
//
//    private void initView() {
//        back = (TextView)findViewById(R.id.back);
//        layout1 = (RelativeLayout)findViewById(R.id.layout1);
//        layout2 = (RelativeLayout)findViewById(R.id.layout2);
//
//        back.setOnClickListener(this);
//        layout1.setOnClickListener(this);
//        layout2.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.back:
//                this.finish();
//                break;
//            case R.id.layout1:
//
//                break;
//            case R.id.layout2:
//                startActivity(new Intent(YuekaInfoActivity.this,BackImageActivity.class));
//                break;
//        }
//    }
//}

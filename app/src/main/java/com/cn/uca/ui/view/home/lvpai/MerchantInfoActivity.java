package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.MerchantInfoBean;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商家信息（商家管理）
 */
public class MerchantInfoActivity extends BaseBackActivity implements View.OnClickListener{

    private MerchantInfoBean bean;
    private CircleImageView pic;
    private TextView back,merchantname,introduce,username,phone,wechat;
    private CheckBox abroad,domestic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_info);
        getInfo();
        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        pic = (CircleImageView)findViewById(R.id.pic);
        merchantname = (TextView)findViewById(R.id.merchantname);
        introduce = (TextView)findViewById(R.id.introduce);
        username = (TextView)findViewById(R.id.username);
        phone = (TextView)findViewById(R.id.phone);
        wechat = (TextView)findViewById(R.id.wechat);
        abroad = (CheckBox)findViewById(R.id.abroad);
        domestic = (CheckBox)findViewById(R.id.domestic);

        back.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(bean.getHead_portrait_url(),pic);
        merchantname.setText(bean.getMerchant_name());
        introduce.setText(bean.getIntroduce());
        username.setText(bean.getContacts());
        phone.setText(bean.getPhone());
        wechat.setText(bean.getWeixin());
        if (bean.isOverseas_tour()){
            abroad.setChecked(true);
        }
        if (bean.isDomestic_travel()){
            domestic.setChecked(true);
        }
    }


    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            bean = intent.getParcelableExtra("info");
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

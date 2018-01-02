package com.cn.uca.ui.view.home.lvpai;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetLayoutParams;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 商家相册
 */
public class MerchantAlbumActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private SimpleDraweeView pic1,pic2,pic3;
    private LinearLayout layout1,layout2,layout3;
    private RelativeLayout pic_layout1,pic_layout2,pic_layout3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_album);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        pic1 = (SimpleDraweeView)findViewById(R.id.pic1);
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        pic2 = (SimpleDraweeView)findViewById(R.id.pic2);
        layout2 = (LinearLayout)findViewById(R.id.layout2);
        pic3 = (SimpleDraweeView)findViewById(R.id.pic3);
        layout3 = (LinearLayout)findViewById(R.id.layout3);

        back.setOnClickListener(this);
        layout1.getBackground().setAlpha(120);
        pic_layout1 = (RelativeLayout)findViewById(R.id.pic_layout1);
        SetLayoutParams.setLinearLayout(pic_layout1, (MyApplication.width-20)*3/7,(MyApplication.width-20)*9/28);
        layout2.getBackground().setAlpha(120);
        pic_layout2 = (RelativeLayout)findViewById(R.id.pic_layout2);
        SetLayoutParams.setLinearLayout(pic_layout2, (MyApplication.width-20)*3/7,(MyApplication.width-20)*9/28);
        layout3.getBackground().setAlpha(120);
        pic_layout3 = (RelativeLayout)findViewById(R.id.pic_layout3);
        SetLayoutParams.setLinearLayout(pic_layout3, (MyApplication.width-20)*3/7,(MyApplication.width-20)*9/28);

        Uri uri1 = Uri.parse("res:///" + R.mipmap.hotel_back);
        DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                .setUri(uri1)
                .setTapToRetryEnabled(true)
                .build();

        Uri uri2 = Uri.parse("res:///" + R.mipmap.plant_ticket_back);
        DraweeController controller2 = Fresco.newDraweeControllerBuilder()
                .setUri(uri2)
                .setTapToRetryEnabled(true)
                .build();
        Uri uri3 = Uri.parse("res:///" + R.mipmap.travel_back);

        DraweeController controller3 = Fresco.newDraweeControllerBuilder()
                .setUri(uri3)
                .setTapToRetryEnabled(true)
                .build();
        pic1.setController(controller1);
        pic2.setController(controller2);
        pic3.setController(controller3);
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

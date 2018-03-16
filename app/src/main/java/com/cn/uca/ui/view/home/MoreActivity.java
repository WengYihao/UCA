package com.cn.uca.ui.view.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.home.footprint.FootPrintActivity;
import com.cn.uca.ui.view.home.lvpai.LvPaiActivity;
import com.cn.uca.ui.view.home.planeticket.PlaneTicketActivity;
import com.cn.uca.ui.view.home.raider.RaidersActivity;
import com.cn.uca.ui.view.home.samecityka.SameCityKaActivity;
import com.cn.uca.ui.view.home.sign.SignActivity;
import com.cn.uca.ui.view.home.translate.TranslateActivity;
import com.cn.uca.ui.view.home.travel.TourismActivity;
import com.cn.uca.ui.view.home.yusheng.YuShengActivity;
import com.cn.uca.ui.view.home.yusheng.YuShengDetailsActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.ui.view.yueka.YuekaActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.ToastXutil;

public class MoreActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private LinearLayout samecityka,yueka,yusheng,oneRaiders,chuxing,travel,lvpai,youhui,footprint,translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        inttView();
    }

    private void inttView() {
        back = (TextView)findViewById(R.id.back);
        samecityka = (LinearLayout)findViewById(R.id.samecityka);
        yueka = (LinearLayout)findViewById(R.id.yueka);
        yusheng = (LinearLayout)findViewById(R.id.yusheng);
        oneRaiders = (LinearLayout)findViewById(R.id.oneRaiders);
        chuxing = (LinearLayout)findViewById(R.id.chuxing);
        travel = (LinearLayout)findViewById(R.id.travel);
        lvpai = (LinearLayout)findViewById(R.id.lvpai);
        youhui = (LinearLayout)findViewById(R.id.youhui);
        footprint = (LinearLayout)findViewById(R.id.footprint);
        translate = (LinearLayout)findViewById(R.id.translate);

        back.setOnClickListener(this);
        samecityka.setOnClickListener(this);
        yueka.setOnClickListener(this);
        yusheng.setOnClickListener(this);
        oneRaiders.setOnClickListener(this);
        chuxing.setOnClickListener(this);
        travel.setOnClickListener(this);
        lvpai.setOnClickListener(this);
        youhui.setOnClickListener(this);
        footprint.setOnClickListener(this);
        translate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.sign:
                startActivity(new Intent(this, SignActivity.class));
                break;
            case R.id.samecityka://同城咖
                startActivity(new Intent(this, SameCityKaActivity.class));
                break;
            case R.id.yueka://约咖
                startActivity(new Intent(this, YuekaActivity.class));
                break;
            case R.id.yusheng://余生
                if (SharePreferenceXutil.isOpenYS()){
                    startActivity(new Intent(this, YuShengDetailsActivity.class));
                }else{
                    startActivity(new Intent(this,YuShengActivity.class));
                }
                break;
            case R.id.oneRaiders://攻略
                startActivity(new Intent(this, RaidersActivity.class));
                break;
            case R.id.chuxing://出行
                startActivity(new Intent(this, PlaneTicketActivity.class));
                break;
            case R.id.travel://旅游
                startActivity(new Intent(this, TourismActivity.class));
                break;
            case R.id.lvpai://旅拍
                startActivity(new Intent(this, LvPaiActivity.class));
                break;
            case R.id.youhui:
                ToastXutil.show("想什么优惠呢");
                break;
            case R.id.footprint:
                if(SharePreferenceXutil.isSuccess()){
                    startActivity(new Intent(this, FootPrintActivity.class));
                }else{
                    ToastXutil.show("请先登录");
                }
                break;
            case R.id.translate:
                startActivity(new Intent(this, TranslateActivity.class));
                break;
        }
    }
}

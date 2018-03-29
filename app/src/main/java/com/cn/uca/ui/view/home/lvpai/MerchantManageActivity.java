package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.MerchantInfoBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.ownerview.MyRatingBar;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.rongim.ChatListActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.FluidLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 旅拍商家管理
 */
public class MerchantManageActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,message,prompt,close,edit,score,username,introduce,merchantname,merchantphone,merchantwechat,gotoMerchant;
    private LinearLayout layout;
    private CircleImageView user_pic;
    private MyRatingBar star;
    private FluidLayout fluidLayout;
    private Switch personal_tailor;
    private LinearLayout layout1,layout2,layout3,layout4,layout5,layout6;
    private MerchantInfoBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_manage);

        initView();
        getUserInfo();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        message = (TextView)findViewById(R.id.message);
        prompt = (TextView)findViewById(R.id.prompt);
        layout = (LinearLayout)findViewById(R.id.layout);
        close = (TextView)findViewById(R.id.close);
        edit = (TextView)findViewById(R.id.edit);
        user_pic = (CircleImageView)findViewById(R.id.user_pic);
        username = (TextView)findViewById(R.id.username);
        introduce = (TextView)findViewById(R.id.introduce);
        star = (MyRatingBar)findViewById(R.id.star);
        score = (TextView)findViewById(R.id.score);
        merchantname = (TextView)findViewById(R.id.merchantname);
        merchantphone = (TextView)findViewById(R.id.merchantphone);
        merchantwechat = (TextView)findViewById(R.id.merchantwechat);
        fluidLayout = (FluidLayout)findViewById(R.id.fluidLayout);
        gotoMerchant = (TextView)findViewById(R.id.gotoMerchant);
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        layout2 = (LinearLayout)findViewById(R.id.layout2);
        layout3 = (LinearLayout)findViewById(R.id.layout3);
        layout4 = (LinearLayout)findViewById(R.id.layout4);
        layout5 = (LinearLayout)findViewById(R.id.layout5);
        layout6 = (LinearLayout)findViewById(R.id.layout6);
        personal_tailor = (Switch)findViewById(R.id.personal_tailor);
        personal_tailor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }else {

                }
            }
        });
        back.setOnClickListener(this);
        message.setOnClickListener(this);
        close.setOnClickListener(this);
        edit.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        gotoMerchant.setOnClickListener(this);
        if (SharePreferenceXutil.isSettled()){
            layout.setVisibility(View.GONE);
        }
        SharePreferenceXutil.setSettled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.message:
                if (SharePreferenceXutil.isSuccess()){
                    startActivity(new Intent(this, ChatListActivity.class));
                }else{
                    ToastXutil.show("请先登录");
                }
                break;
            case R.id.close:
                layout.setVisibility(View.GONE);
                break;
            case R.id.edit:
                Intent intent = new Intent();
                intent.setClass(MerchantManageActivity.this,MerchantInfoActivity.class);
                intent.putExtra("info",bean);
                startActivityForResult(intent,0);
                break;
            case R.id.layout1:
                startActivity(new Intent(this, MerchantAlbumActivity.class));
                break;
            case R.id.layout2:
                startActivity(new Intent(this, MerchantAddressActivity.class));
                break;
            case R.id.layout3:
                startActivity(new Intent(this, MerchantCommodityActivity.class));
                break;
            case R.id.layout4:

                break;
            case R.id.layout5:
                startActivity(new Intent(this,OrderManageActivity.class));
                break;
            case R.id.layout6:
                startActivity(new Intent(this, MerchantTeamActivity.class));
                break;
            case R.id.gotoMerchant:
                Intent intent1 = new Intent();
                intent1.setClass(this,MerchantDetailActivity.class);
                intent1.putExtra("id",bean.getTrip_shoot_merchant_id());
                startActivity(intent1);
                break;
        }
    }

    private void getUserInfo(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getMerchantInfo(time_stamp, sign, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<MerchantInfoBean>() {
                            }.getType());
                            prompt.setText("你的“"+bean.getMerchant_name()+"”已成功入驻，在旅拍界面可直接通过原入驻的按钮进入管理中心");
                            ImageLoader.getInstance().displayImage(bean.getHead_portrait_url(),user_pic);
                            username.setText(bean.getMerchant_name());
                            star.setRating((float) bean.getScore());
                            score.setText(bean.getScore()+"");
                            introduce.setText(bean.getIntroduce());
                            merchantname.setText(bean.getContacts());
                            merchantphone.setText(bean.getPhone());
                            merchantwechat.setText(bean.getWeixin());
                            if (bean.isPersonal_tailor()){
                                personal_tailor.setChecked(true);
                            }
                            if (bean.isDomestic_travel()){
                                TextView tv = new TextView(MerchantManageActivity.this);
                                tv.setText("国内");
                                tv.setTextColor(getResources().getColor(R.color.ori));
                                tv.setTextSize(10);
                                tv.setBackgroundResource(R.drawable.text_lable_bg);
                                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(12, 12, 12, 12);
                                fluidLayout.addView(tv, params);
                            }
                            if (bean.isOverseas_tour()){
                                TextView tv = new TextView(MerchantManageActivity.this);
                                tv.setText("国外");
                                tv.setTextColor(getResources().getColor(R.color.ori));
                                tv.setTextSize(10);
                                tv.setBackgroundResource(R.drawable.text_lable_bg);
                                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(12, 12, 12, 12);
                                fluidLayout.addView(tv, params);
                            }
                            break;
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}

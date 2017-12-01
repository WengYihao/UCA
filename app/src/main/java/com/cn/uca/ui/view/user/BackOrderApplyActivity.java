package com.cn.uca.ui.view.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 退单申请（领咖）
 */
public class BackOrderApplyActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,text1,text2,text3,text4,refuse,agree;
    private CircleImageView pic;
    private int userId,backId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_order_apply);

        getInfo();
        initView();
        getTravelEscortBack();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            userId = intent.getIntExtra("userId",0);
        }
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        pic = (CircleImageView)findViewById(R.id.pic);
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);
        text4 = (TextView)findViewById(R.id.text4);
        refuse = (TextView)findViewById(R.id.refuse);
        agree = (TextView)findViewById(R.id.agree);

        back.setOnClickListener(this);

        pic.getBackground().setAlpha(120);
        pic.setOnClickListener(this);
        refuse.setOnClickListener(this);
        agree.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.refuse:
                approvalTravelEscortBack("no");
                break;
            case R.id.agree:
                approvalTravelEscortBack("yes");
                break;
            case R.id.pic:
                ToastXutil.show("要聊天吗？");
                break;
        }
    }

    private void getTravelEscortBack(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("escort_user_id",userId);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        YueKaHttp.getTravelEscortBack(userId, account_token, time_stamp, sign, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            text2.setText("退单原因:"+object.getString("escort_back_type_name"));
                            text3.setText("退单时间:"+object.getString("back_time"));
                            text4.setText("描    述:"+object.getString("reason"));
                            backId = object.getInt("travel_escort_back_id");
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

    private void approvalTravelEscortBack(final String result){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("travel_escort_back_id",backId);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("result",result);
        String sign = SignUtil.sign(map);
        UserHttp.approvalTravelEscortBack(backId, account_token, time_stamp, sign, result, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            if (result.equals("yes")){
                                ToastXutil.show("同意退单");
                                BackOrderApplyActivity.this.finish();
                            }else{
                                ToastXutil.show("拒绝退单");
                                BackOrderApplyActivity.this.finish();
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

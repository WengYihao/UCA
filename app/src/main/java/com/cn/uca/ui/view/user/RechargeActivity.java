package com.cn.uca.ui.view.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.wechat.WeChatPayBean;
import com.cn.uca.config.Constant;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RechargeActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private RadioButton btn1,btn2,btn3;
    private EditText num;
    private LinearLayout wechatpay_layout,alipay_layout,unionpay_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        btn1 = (RadioButton)findViewById(R.id.btn1);
        btn2 = (RadioButton)findViewById(R.id.btn2);
        btn3 = (RadioButton)findViewById(R.id.btn3);
        num = (EditText)findViewById(R.id.num);
        wechatpay_layout = (LinearLayout)findViewById(R.id.wechatpay_layout);
        alipay_layout = (LinearLayout)findViewById(R.id.alipay_layout);
        unionpay_layout = (LinearLayout)findViewById(R.id.unionpay_layout);

        back.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        wechatpay_layout.setOnClickListener(this);
        alipay_layout.setOnClickListener(this);
        unionpay_layout.setOnClickListener(this);

        btn1.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.btn1:
                if (btn2.isChecked()){
                    btn2.setChecked(false);
                }
                if (btn3.isChecked()){
                    btn3.setChecked(false);
                }
                break;
            case R.id.btn2:
                if (btn1.isChecked()){
                    btn1.setChecked(false);
                }
                if (btn3.isChecked()){
                    btn3.setChecked(false);
                }
                break;
            case R.id. btn3:
                if (btn1.isChecked()){
                    btn1.setChecked(false);
                }
                if (btn2.isChecked()){
                    btn2.setChecked(false);
                }
                break;
            case R.id.wechatpay_layout:
//                String money = getMoney();
//                if (money == null){
//                    ToastXutil.show("请选择充值金额或输入充值金额");
//                }else{
////                    wechatOrderCreate(money);
//                }
                wechatOrderCreate();
                break;
            case R.id.alipay_layout:

                break;
            case R.id.unionpay_layout:

                break;
        }
    }

    private String getMoney(){
        if (btn1.isChecked()){
            return "100.00";
        }else if(btn2.isChecked()){
            return "300.00";
        }else if (btn3.isChecked()){
            if (StringXutil.isEmpty(num.getText().toString())){
                return null;
            }else{
                String price = num.getText().toString();
                return price;
            }
        }
        return null;
    }
    private void wechatOrderCreate(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("amount_money","0.01");
        final String sign = SignUtil.sign(map);
        UserHttp.createWeiXinOrder(sign, time_stamp, "0.01", account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString()+"--");
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            Map<String,Object> map = new HashMap<>();
                            map.put("time_stamp",object.getString("time_stamp"));;
                            map.put("wxPay",object.getString("wxPay"));
                            map.put("order_no",object.getString("order_no"));
                            String signStr = SignUtil.sign(map);
                            if (signStr.equals(object.getString("sign"))){
                                byte [] bytes = Base64.decode(object.getString("wxPay"));
                                JSONObject object1 = new JSONObject(new String(bytes,"UTF-8"));
                                Gson gson = new Gson();
                                WeChatPayBean bean = gson.fromJson(object1.toString(),new TypeToken<WeChatPayBean>() {
                                }.getType());
                                wechatPay(bean);
                            }else{
                                ToastXutil.show("创建订单失败，请重试");
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage());
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
    private void wechatPay(WeChatPayBean bean){
        if (WeChatManager.instance().isWXAppInstalled()) {
            PayReq payReq = new PayReq();
            payReq.appId = Constant.WX_APP_ID;
            payReq.partnerId = bean.getPartnerid();
            payReq.prepayId = bean.getPrepayid();
            payReq.packageValue = "Sign=WXPay";
            payReq.nonceStr = bean.getNoncestr();
            payReq.timeStamp = bean.getTimestamp();
            payReq.sign = bean.getSign();
            WeChatManager.instance().sendReq(payReq);
        }else {
            ToastXutil.show(R.string.wechat_not_installed);
        }
    }
}

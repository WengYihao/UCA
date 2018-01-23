package com.cn.uca.ui.view.user.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.user.order.YueKaOrderInfo;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.user.PayBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.secretkey.MD5;
import com.cn.uca.secretkey.RSAUtils;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.user.BackOrderActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.pay.PayUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

//约咖订单详情
public class YueKaOrderDetailActivity extends BaseBackActivity implements View.OnClickListener,PayBack{

    private TextView back,name,state,order,place,date,service,people,price,config;
    private ImageView pic;
    private int id;
    private YueKaOrderInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_ka_order_detail);

        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if(intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        pic = (ImageView) findViewById(R.id.pic);
        name = (TextView)findViewById(R.id.name);
        state = (TextView)findViewById(R.id.state);
        order = (TextView)findViewById(R.id.order);
        place = (TextView)findViewById(R.id.place);
        date = (TextView)findViewById(R.id.date);
        service = (TextView)findViewById(R.id.service);
        people = (TextView)findViewById(R.id.people);
        price = (TextView)findViewById(R.id.price);
        config = (TextView)findViewById(R.id.config);

        back.setOnClickListener(this);
        config.setOnClickListener(this);

        getUserOrderInfo();
    }

    private void getUserOrderInfo(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("user_order_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.getUserOrderInfo(account_token, sign, time_stamp, id, null, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            info = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YueKaOrderInfo>() {
                            }.getType());
                            ImageLoader.getInstance().displayImage(info.getPicture(),pic);
                            name.setText(info.getEscort_record_name());
                            switch (info.getEscort_user_state_id()){
                                case 1:
                                    state.setText("已接单");
                                    switch (info.getUser_order_state_id()){
                                        case 1:
                                            config.setText("申请退单");
                                            break;
                                        case 2:
                                            config.setText("确定支付");
                                            break;
                                    }
                                    break;
                                case 2:
                                    state.setText("已拒接");
                                    config.setText("暂无操作");
                                    config.setEnabled(false);
                                    break;
                                case 3:
                                    state.setText("待接单");
                                    config.setText("暂无操作");
                                    config.setEnabled(false);
                                    break;
                                case 4:
                                    state.setText("已失效");
                                    config.setText("暂无操作");
                                    config.setEnabled(false);
                                    break;
                                case 5:
                                    state.setText("待评价");
                                    config.setText("立即评价");
                                    break;
                                case 6:
                                    state.setText("已完成");
                                    config.setText("暂无操作");
                                    config.setEnabled(false);
                                    break;
                                case 7:
                                    state.setText("领咖退单");
                                    config.setText("暂无操作");
                                    config.setEnabled(false);
                                    break;
                                case 8:
                                    state.setText("游咖退单");
                                    config.setText("暂无操作");
                                    config.setEnabled(false);
                                    break;
                                case 9:
                                    state.setText("等待领咖审批退单");
                                    config.setText("待审批");
                                    break;
                                case 10:
                                    state.setText("请审批退单");
                                    config.setText("前往查看");
                                    config.setEnabled(false);
                                    break;
                            }
                            order.setText("订单编号:"+info.getOrder_number());
                            place.setText(info.getCity_name());
                            date.setText(info.getBeg_date()+"~"+info.getEnd_date());
                            if (info.getServiceFunctionPrices().size() == 0){
                                service.setText("暂无服务");
                            }
                            people.setText(info.getActual_number()+"");
                            price.setText("￥"+(int)info.getActual_payment());
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage());
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.config:
                switch (info.getEscort_user_state_id()){
                    case 1:
                        switch (info.getUser_order_state_id()){
                            case 1:
                                //申请退单
                                Intent intent = new Intent();
                                intent.setClass(YueKaOrderDetailActivity.this, BackOrderActivity.class);
                                intent.putExtra("orderId",info.getUser_order_id());
                                startActivity(intent);
                                break;
                            case 2:
                                //支付
                                ShowPopupWindow.paymentWindow(this,"约咖订单",info.getActual_payment(),1,this);
                                break;
                        }
                        break;
                    case 5:
                        //评价
                        break;
                    case 9:
                        //审批领咖退单
                        break;
                }
                break;
        }
    }
    @Override
    public void walletPay(String password) {
        try{
            PublicKey publicKey = RSAUtils.loadPublicKey(Constant.PUBLIC_KEY);
            byte[] encryptByte = RSAUtils.encryptData(MD5.getMD5(password).getBytes(), publicKey);
            String afterencrypt = Base64.encode(encryptByte);
            PayUtil.walletPay(info.getActual_payment(),0,info.getOrder_number(),afterencrypt);
        }catch (Exception e){

        }

    }

    @Override
    public void otherPay(int payType) {
        switch (payType){
            case 2:
                PayUtil.weChatPay("order",info.getActual_payment()+"",info.getOrder_number(),0);
                break;
            case 3:
                PayUtil.aliPay(this,"order",info.getActual_payment()+"",info.getOrder_number(),0);
                break;
        }
    }
}

package com.cn.uca.ui.view.user.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.user.order.LvPaiOrderInfo;
import com.cn.uca.bean.user.order.SpotTicketOrderInfo;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.user.PayBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.secretkey.MD5;
import com.cn.uca.secretkey.RSAUtils;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.util.pay.PayUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

//景点门票订单详情
public class SpotTicketOrderActivity extends BaseBackActivity implements View.OnClickListener,PayBack {

    private TextView back;
    private ImageView pic;
    private TextView name,state,order,num,date,people,phone,identity,price,config;
    private int id;
    private String type,orderNum;
    private SpotTicketOrderInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_ticket_order);

        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if(intent != null){
            type = intent.getStringExtra("type");
            switch (type){
                case "id":
                    id = intent.getIntExtra("id",0);
                    break;
                case "order":
                    orderNum = intent.getStringExtra("order");
                    break;
            }

        }
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        pic = (ImageView)findViewById(R.id.pic);
        name = (TextView)findViewById(R.id.name);
        state = (TextView)findViewById(R.id.state);
        order = (TextView)findViewById(R.id.order);
        num = (TextView)findViewById(R.id.num);
        date = (TextView)findViewById(R.id.date);
        people = (TextView)findViewById(R.id.people);
        phone = (TextView)findViewById(R.id.phone);
        identity = (TextView)findViewById(R.id.identity);
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
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        switch (type){
            case "id":
                map.put("user_order_id",id);
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
                                    info = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<SpotTicketOrderInfo>() {
                                    }.getType());
                                    ImageLoader.getInstance().displayImage(info.getPicture(),pic);
                                    name.setText(info.getScenic_spot_name());
                                    order.setText("订单编号:"+info.getOrder_number());
                                    switch (info.getUser_order_state_id()){
                                        case 1:
                                            state.setText("已支付");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 2:
                                            state.setText("待支付");
                                            config.setText("确定支付");
                                            break;
                                        case 3:
                                            state.setText("已失效");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 4:
                                            state.setText("待确认");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 5:
                                            state.setText("已取消");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 6:
                                            state.setText("已退单");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 7:
                                            state.setText("待评价");
                                            config.setText("立即评价");
                                            break;
                                        case 8:
                                            state.setText("已完成");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                    }
                                    order.setText("订单编号:"+info.getOrder_number());
                                    num.setText(info.getNumber()+"");
                                    date.setText(info.getPlay_date());
                                    people.setText(info.getUser_name());
                                    phone.setText(info.getMobile());
                                    identity.setText(info.getId_card());
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
                break;
            case "order":
                map.put("order_number",orderNum);
                String sign2 = SignUtil.sign(map);
                UserHttp.getUserOrderInfo(account_token, sign2, time_stamp, 0, orderNum, new CallBack() {
                    @Override
                    public void onResponse(Object response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response.toString());
                            int code = jsonObject.getInt("code");
                            switch (code){
                                case 0:
                                    Gson gson = new Gson();
                                    info = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<SpotTicketOrderInfo>() {
                                    }.getType());
                                    ImageLoader.getInstance().displayImage(info.getPicture(),pic);
                                    name.setText(info.getScenic_spot_name());
                                    order.setText("订单编号:"+info.getOrder_number());
                                    switch (info.getUser_order_state_id()){
                                        case 1:
                                            state.setText("已支付");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 2:
                                            state.setText("待支付");
                                            config.setText("确定支付");
                                            break;
                                        case 3:
                                            state.setText("已失效");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 4:
                                            state.setText("待确认");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 5:
                                            state.setText("已取消");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 6:
                                            state.setText("已退单");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 7:
                                            state.setText("待评价");
                                            config.setText("立即评价");
                                            break;
                                        case 8:
                                            state.setText("已完成");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                    }
                                    order.setText("订单编号:"+info.getOrder_number());
                                    num.setText(info.getNumber()+"");
                                    date.setText(info.getPlay_date());
                                    people.setText(info.getUser_name());
                                    phone.setText(info.getMobile());
                                    identity.setText(info.getId_card());
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
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.config:
                switch (info.getUser_order_state_id()){
                    case 2:
                        //支付
                        ShowPopupWindow.paymentWindow(this,"景点门票",info.getActual_payment(),1,this);
                        break;
                    case 7:
                        //评价
                        ToastXutil.show("马上评价");
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

    @Override
    protected void onResume() {
        super.onResume();
        getUserOrderInfo();
    }
}

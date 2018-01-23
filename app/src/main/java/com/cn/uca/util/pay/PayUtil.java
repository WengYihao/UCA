package com.cn.uca.util.pay;

import android.app.Activity;
import android.util.Log;

import com.android.volley.VolleyError;
import com.cn.uca.alipay.AliPayUtil;
import com.cn.uca.bean.wechat.WeChatPayBean;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2017/11/22.
 */

public class PayUtil {
    //钱包支付
    public static void walletPay(double actual_payment,int user_coupon_id,String order_number,String pay_pwd){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("actual_payment",actual_payment);
        if (user_coupon_id != 0){
            map.put("user_coupon_id",user_coupon_id);
        }
        map.put("order_number",order_number);
        map.put("pay_pwd",pay_pwd);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.orderPayment(account_token, actual_payment, user_coupon_id, order_number, sign, pay_pwd, time_stamp, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    switch ((int)response){
                        case 0:
                            ToastXutil.show("支付成功");
                            break;
                        case 172:
                            ToastXutil.show("余额不足");
                            break;
                        case 743:
                            ToastXutil.show("密码错误");
                            break;
                        default:
                            ToastXutil.show("支付失败");
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
    //微信支付
    public static void weChatPay(String pay_type, String amount_money, String order_number, int user_coupon_id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("amount_money",amount_money);
        if (pay_type != null){
            map.put("pay_type",pay_type);
        }
        if (order_number != null){
            map.put("order_number",order_number);
        }
        if (user_coupon_id != 0){
            map.put("user_coupon_id",user_coupon_id);
        }
        final String sign = SignUtil.sign(map);
        UserHttp.createWeiXinOrder(sign, time_stamp, amount_money, account_token,pay_type,order_number,user_coupon_id, new CallBack() {
            @Override
            public void onResponse(Object response) {
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
                                WeChatManager.instance().wechatPay(bean);
                            }else{
                                ToastXutil.show("订单异常");
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
    //支付宝支付
    public static void aliPay(final Activity activity, String pay_type, String amount_money, String order_number, int user_coupon_id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("amount_money",amount_money);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        if (pay_type != null){
            map.put("pay_type",pay_type);
        }
        if (order_number != null){
            map.put("order_number",order_number);
        }
        if (user_coupon_id != 0){
            map.put("user_coupon_id",user_coupon_id);
        }
        final String sign = SignUtil.sign(map);
        QueryHttp.createZfbOrder(account_token, sign, time_stamp, amount_money,pay_type,order_number,user_coupon_id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            Map<String,Object> map = new HashMap<>();
                            map.put("time_stamp",object.getString("time_stamp"));;
                            map.put("zfbPay",object.getString("zfbPay"));
                            map.put("order_no",object.getString("order_no"));
                            String signStr = SignUtil.sign(map);
                            if (signStr.equals(object.getString("sign"))){
                                byte [] bytes = Base64.decode(object.getString("zfbPay"));
                                String str = new String(bytes,"UTF-8");
                                AliPayUtil payUtil = new AliPayUtil();
                                payUtil.toALiPay(activity,str);
                            }else{
                                ToastXutil.show("订单异常");
                            }
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
}

package com.cn.uca.alipay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.QueryHttp;
import com.cn.uca.ui.view.MainActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.utils.StringUtils;

/**
 * Created by asus on 2018/1/12.
 */

public class AliPayUtil {
    private Context context;
    private Activity activity;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    switch (payResult.getResultStatus()) {
                        case "9000":
                            Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                            break;
                        case "8000":
                            Toast.makeText(context, "正在处理中", Toast.LENGTH_SHORT).show();
                            break;
                        case "4000":
                            Toast.makeText(context, "订单支付失败", Toast.LENGTH_SHORT).show();
                            break;
                        case "5000":
                            Toast.makeText(context, "重复请求", Toast.LENGTH_SHORT).show();
                            break;
                        case "6001":
                            Toast.makeText(context, "已取消支付", Toast.LENGTH_SHORT).show();
                            break;
                        case "6002":
                            Toast.makeText(context, "网络连接出错", Toast.LENGTH_SHORT).show();
                            break;
                        case "6004":
                            Toast.makeText(context, "正在处理中", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case SDK_AUTH_FLAG:
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
//                        ToastXutil.show( "授权成功\n" + String.format("authCode:%s", authResult.getResult()));
                        String str = authResult.getResult();
                        String userId = org.apache.commons.lang.StringUtils.substringBetween(str,"user_id=","&target_id");
                        startLogin(authResult.getAuthCode(),userId);
                        Log.e("456",authResult.getResult()+"---"+userId);
                    } else {
                        // 其他状态值则为授权失败
                        ToastXutil.show( "授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;
            }
        }
    };

    /**
     * 签名在服务端来做
     * @param context
     * @param orderInfo
     */
    public void toALiPay(final Activity context, final String orderInfo) {
        this.context = context;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2
                        (orderInfo, true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void toAliLogin(final Activity activity,final String authInfo){
        this.activity = activity;
        Runnable authRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);
                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    private void startLogin(String auth_code,String user_id){
        Map<String,Object> map = new HashMap<>();
        map.put("auth_code",auth_code);
        map.put("user_id",user_id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        QueryHttp.userLogin(auth_code, user_id, sign, time_stamp, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            SharePreferenceXutil.saveAccountToken(object.getString("account_token"));
                            SharePreferenceXutil.saveRongToken(object.getString("rongyun_token"));
                            SharePreferenceXutil.setSuccess(true);
                            SharePreferenceXutil.setExit(false);
                            Intent intent = new Intent();
                            intent.setClass(activity,MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
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

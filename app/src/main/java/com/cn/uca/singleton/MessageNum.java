package com.cn.uca.singleton;

import com.android.volley.VolleyError;
import com.cn.uca.bean.MessageNumBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2018/3/13.
 */

public class MessageNum {
    private static final MessageNum instance = new MessageNum();
;
    private MessageNumBean bean;


    public static MessageNum getInstance(){
        return instance;
    }
    //获取用户标记红点
    public MessageNumBean msgReminding(){
        Map<String ,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.msgReminding(account_token, time_stamp, sign, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<MessageNumBean>() {
                            }.getType());
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
        return bean;
    }

    public MessageNumBean getBean(){
        return bean;
    }
}

package com.cn.uca.server.util;

import android.app.Activity;

import com.android.volley.VolleyError;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.lvpai.ServiceBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2018/1/22.
 */

public class LvPaiUtil {
    public static void updateMerchantAlbum(final ServiceBack back, final String type, int merchant_album_id, final String album_name){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("type",type);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        if (merchant_album_id != 0){
            map.put("merchant_album_id",merchant_album_id);
        }
        if(album_name != null){
            map.put("album_name",album_name);
        }
        String sign = SignUtil.sign(map);
        HomeHttp.updateMerchantAlbum(time_stamp, sign, account_token, type, merchant_album_id, album_name, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            switch (type){
                                case "add":
                                    ToastXutil.show("添加成功");
                                    back.success("");
                                    break;
                                case "update":
                                    ToastXutil.show("修改成功");
                                    back.success(album_name);
                                    break;
                                case "-":

                                    break;
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

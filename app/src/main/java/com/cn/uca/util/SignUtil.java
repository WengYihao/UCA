package com.cn.uca.util;

import android.util.Log;

import com.cn.uca.config.Constant;
import com.cn.uca.config.MyConfig;
import com.cn.uca.secretkey.MD5;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/10/12.
 */

public class SignUtil {
    public static String sign(Map<String,Object> map){
        try {
            List<String> delKeys = new ArrayList<String>();
            for (String key : map.keySet()) {
                Log.i("789","---|"+ key + "|---");
                Log.i("789",key.equals("")+"hhh"+(key == null));
                if (StringXutil.isEmpty(map.get(key).toString())) {
                    Log.i("789",key+"++++");
                    delKeys.add(key);
                }
            }
            Log.i("123",delKeys.size()+"个数");
            for (String delKey : delKeys) {
                map.remove(delKey);
                Log.i("789",map.toString()+"---");
            }
            Log.i("789",map.toString());
            String[] mapKeyArrays = new String[map.size()];
            map.keySet().toArray(mapKeyArrays);// 赋值

            Arrays.sort(mapKeyArrays);// 排序

            StringBuilder stringBuilder = new StringBuilder();
            for (String key : mapKeyArrays) {
                stringBuilder.append(key).append("=").append(map.get(key)).append("&");
            }
            String sign = MD5.getMD5(Constant.PUBLIC_KEY).toUpperCase();
            stringBuilder.append("publicKey").append("=").append(sign);
            Log.i("123",stringBuilder.toString()+"**");
            sign = MD5.getMD5(stringBuilder.toString()).toUpperCase();
            Log.i("123",sign+"---");
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("456",e.getMessage());
            return null;
        }
    }
}

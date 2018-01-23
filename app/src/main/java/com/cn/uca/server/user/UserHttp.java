package com.cn.uca.server.user;

import android.telecom.Call;

import com.cn.uca.bean.user.UserInfo;
import com.cn.uca.config.MyConfig;
import com.cn.uca.config.base.BaseServer;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.SharePreferenceXutil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2017/9/29.
 */

public class UserHttp extends BaseServer {
    /**
     * 上传用户头像
     * @param file
     * @param handler
     */
    public static void uploadPic(File file, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try{
            params.put("user_head_portrait",file);
        }catch (Exception e){

        }
        params.put("account_token", SharePreferenceXutil.getAccountToken());
        client.post(MyConfig.uplodePic,params,handler);
    }

    /**
     * 获取j简单用户信息
     * @param callBack
     */
    public static void getUserBriefInfo(CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.getUserBriefInfo,map,callBack);
    }

    /**
     * 获取用户信息
     * @param callBack
     */
    public static void getUserInfo(CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.getUserInfo,map,callBack);
    }

    /**
     * 修改用户信息
     * @param handler
     */
    public static void updateUserInfo(UserInfo userInfo, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",SharePreferenceXutil.getAccountToken());
        params.put("user_nick_name",userInfo.getUser_nick_name());
        params.put("sex_id",userInfo.getSex_id());
        params.put("user_birth_date",userInfo.getUser_birth_date());
        params.put("position_id",1);
        client.post(MyConfig.setUserInfo,params,handler);
    }

    /**
     * 获取用户推送
     * @param callBack
     */
    public static void getUserPush(CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        get(MyConfig.getUserPush,map,callBack);
    }

    /**
     * 设置用户是否推送
     * @param open_push
     * @param callBack
     */
    public static void setUserPush(boolean open_push,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("open_push",open_push+"");
        post4(MyConfig.setUserPush,map,callBack);
    }

    /**
     * 修改密码
     * @param encryption_password
     * @param encryption_new_password
     * @param callBack
     */
    public static void updatePassword(String encryption_password,String encryption_new_password,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("encryption_password",encryption_password);
        map.put("encryption_new_password",encryption_new_password);
        post4(MyConfig.updatePassword,map,callBack);
    }

    /**
     * 身份认证
     * @param user_identiry_name
     * @param user_identity_number
     * @param user_identiry_photo
     * @param user_identiry_photo_back
     * @param handler
     */
    public static void submitIdentity(String user_identiry_name, String user_identity_number, File user_identiry_photo, File user_identiry_photo_back, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",SharePreferenceXutil.getAccountToken());
        params.put("user_identiry_name",user_identiry_name);
        params.put("user_identity_number",user_identity_number);
        try {
            params.put("user_identiry_photo",user_identiry_photo);
            params.put("user_identiry_photo_back",user_identiry_photo_back);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(MyConfig.submitIdentity,params,handler);
    }

    /**
     * 获取用户余额
     * @param callBack
     */
    public static void getWallet(CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post3(MyConfig.getWallet,map,callBack);
    }

    /**
     * 获取芝麻认证url
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param callBack
     */
    public static void getIdCardUrl(String sign,String time_stamp,String account_token,CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("account_token",account_token);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        get(MyConfig.getIdCardUrl,map,callBack);
    }

    /**
     * app反馈
     * @param sign
     * @param time_stamp
     * @param reason
     * @param callBack
     */
    public static void feedback(String account_token,String sign,String time_stamp,String reason,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("reason",reason);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        post4(MyConfig.feedback,map,callBack);
    }

    /**
     * 获取用户订单
     * @param page
     * @param pageCount
     * @param commodity_id
     * @param user_order_satet
     * @param callBack
     */
    public static void getUserOrder(int page,int pageCount,int commodity_id,String user_order_satet,CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("page",page+"");
        map.put("pageCount",pageCount+"");
        map.put("commodity_id",commodity_id+"");
        map.put("user_order_satet",user_order_satet);
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        get(MyConfig.getUserOrder,map,callBack);
    }

    /**
     * 获取订单信息
     * @param account_token
     * @param sign
     * @param time_stamp
     * @param user_order_id
     * @param order_number
     * @param callBack
     */
    public static void getUserOrderInfo(String account_token,String sign,String time_stamp,int user_order_id,String order_number,CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("account_token",account_token);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        if (user_order_id != 0){
            map.put("user_order_id",user_order_id);
        }
        if (order_number != null){
            map.put("order_number",order_number);
        }
        get(MyConfig.getUserOrderInfo,map,callBack);
    }


    /**
     * 创建微信订单
     * @param sign
     * @param time_stamp
     * @param amount_money
     * @param account_token
     * @param callBack
     */
    public static void createWeiXinOrder(String sign,String time_stamp,String amount_money,String account_token,String pay_type,String order_number,int user_coupon_id,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("amount_money",amount_money);
        map.put("account_token",account_token);
        if (pay_type != null){
            map.put("pay_type",pay_type);
        }
        if (order_number != null){
            map.put("order_number",order_number);
        }
        if (user_coupon_id != 0){
            map.put("user_coupon_id",user_coupon_id+"");
        }
        post5(MyConfig.createWeiXinOrder,map,callBack);
    }

    /**
     * 验证微信支付是否成功
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param order_no
     * @param callBack
     */
    public static void queryWeiXinOrderState(String sign, String time_stamp, String account_token, String order_no, CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("order_no",order_no);
        map.put("account_token",account_token);
        get(MyConfig.queryWeiXinOrderState,map,callBack);
    }

    /**
     * 获取用户明细
     * @param account_token
     * @param page
     * @param pageCount
     * @param date
     * @param callBack
     */
    public static void getPurseRecords(String account_token,int page, int pageCount,String date,CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("account_token",account_token);
        map.put("date",date);
        get(MyConfig.getPurseRecords,map,callBack);
    }

    /**
     * 获取我的接单
     * @param account_token
     * @param page
     * @param pageCount
     * @param sign
     * @param time_stamp
     * @param escort_record_state_id
     * @param callBack
     */
    public static void getMyEscortOrder(String account_token,int page, int pageCount,String sign,String time_stamp,String escort_record_state_id,CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("account_token",account_token);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("escort_record_state_id",escort_record_state_id);
        get(MyConfig.getMyEscortOrder,map,callBack);
    }

    /**
     * 游咖用户申请退单
     * @param user_order_id
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param reason
     * @param escort_back_type_id
     * @param callBack
     */
    public static void travleEscortBack(int user_order_id,String account_token,String time_stamp,String sign,String reason,int escort_back_type_id,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("user_order_id",user_order_id+"");
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("reason",reason);
        map.put("escort_back_type_id",escort_back_type_id+"");
        post5(MyConfig.travleEscortBack,map,callBack);
    }

    /**
     * 领咖端审批游咖退单
     * @param travel_escort_back_id
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param result
     * @param callBack
     */
    public static void approvalTravelEscortBack(int travel_escort_back_id,String account_token,String time_stamp,String sign,String result,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("travel_escort_back_id",travel_escort_back_id+"");
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("result",result);
        post5(MyConfig.approvalTravelEscortBack,map,callBack);
    }

    /**
     * 获取分享的内容
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param shareType
     * @param callBack
     */
    public static void getShare(String account_token,String time_stamp,String sign,String shareType,int id,CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("shareType",shareType);
        if (shareType != "ZUJI"){
            map.put("id",id);
        }
        get(MyConfig.getShare,map,callBack);
    }

    /**
     * 设置支付密码
     * @param pay_pwd
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param callBack
     */
    public static void setPayPwd(String pay_pwd,String sign,String time_stamp,String account_token,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("pay_pwd",pay_pwd);
        post5(MyConfig.setPayPwd,map,callBack);
    }

    /**
     * 修改支付密码
     * @param new_pay_pwd
     * @param pay_pwd
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param callBack
     */
    public static void updatePayPwd(String new_pay_pwd,String pay_pwd,String sign,String time_stamp,String account_token,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("new_pay_pwd",new_pay_pwd);
        map.put("pay_pwd",pay_pwd);
        post5(MyConfig.updatePayPwd,map,callBack);
    }

    /**
     * 绑定手机号
     * @param phone_number
     * @param code
     * @param encryption_password
     * @param callBack
     */
    public static void bindPhoneNumber(String phone_number,String code,String encryption_password,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("phone_number",phone_number);
        map.put("code",code);
        map.put("encryption_password",encryption_password);
        post4(MyConfig.bindPhoneNumber,map,callBack);
    }


    /**
     * 绑定支付宝
     * @param auth_code
     * @param user_id
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param callBack
     */
    public static void bindZfb(String auth_code,String user_id,String sign,String time_stamp,String account_token,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("auth_code",auth_code);
        map.put("user_id",user_id);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        post5(MyConfig.bindZfb,map,callBack);
    }

    /**
     * 绑定微信
     * @param access_token
     * @param openid
     * @param callBack
     */
    public static void bindWeixin(String access_token,String openid,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("access_token",access_token);
        map.put("openid",openid);
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        post5(MyConfig.bindWeixin,map,callBack);
    }

    /**
     * 获取用户推送消息
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param page
     * @param pageCount
     * @param push_type
     * @param callBack
     */
    public static void getPushUser(String sign,String time_stamp,String account_token,int page,int pageCount,String push_type,CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("push_type",push_type);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getPushUser,map,callBack);
    }
}

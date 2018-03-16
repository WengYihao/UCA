package com.cn.uca.server.yueka;

import android.telecom.Call;

import com.cn.uca.bean.home.samecityka.SendImgFileBean;
import com.cn.uca.bean.yueka.GetEscortBean;
import com.cn.uca.bean.yueka.ReleaseEscortRecordBean;
import com.cn.uca.config.MyConfig;
import com.cn.uca.config.base.BaseServer;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StringXutil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/9/26.
 */

public class YueKaHttp extends BaseServer{

    /**
     * 收藏/取消伴游
     * @param escort_record_id
     * @param callBack
     */
    public static void collectionEscortRecord(int escort_record_id, CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token", SharePreferenceXutil.getAccountToken());
        map.put("escort_record_id",escort_record_id+"");
        post4(MyConfig.collectionEscortRecord,map,callBack);
    }
    /**
     * 获取伴游
     * @param bean
     * @param handler
     */
    public static void  getEscortRecords(GetEscortBean bean, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",bean.getAccount_token());
        params.put("gaode_code",bean.getGaode_code());
        params.put("page",bean.getPage());
        params.put("pageCount",bean.getPageCount());
        params.put("beg_time",bean.getBeg_time());
        params.put("end_time",bean.getEnd_time());
        if (bean.getSex_id() != 0){
            params.put("sex_id",bean.getSex_id());
        }
        params.put("beg_age",bean.getBeg_age());
        params.put("end_age",bean.getEnd_age());
        params.put("label",bean.getLabel());
        client.get(MyConfig.getEscortRecords,params,handler);
    }

    /**
     * 发布伴游
     * @param bean
     * @param handler
     */
    public static void releaseEscortRecord(ReleaseEscortRecordBean bean, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("escort_record_name",bean.getEscort_record_name());
        params.put("account_token",SharePreferenceXutil.getAccountToken());
        params.put("beg_time",bean.getBeg_time());
        params.put("end_time",bean.getEnd_time());
        params.put("escort_details_id",bean.getEscort_details_id());
        params.put("route_id",bean.getRoute_id());
        params.put("requirement_people_number",bean.getRequirement_people_number());
        params.put("releaseServices",bean.getReleaseServices());
        client.post(MyConfig.releaseEscortRecord,params,handler);
    }

    /**
     * 获取伴游详情
     * @param escort_record_id
     * @param callBack
     */
    public static void getEscortRecordInfo(int escort_record_id,CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        if (!StringXutil.isEmpty(SharePreferenceXutil.getAccountToken())){
            map.put("account_token",SharePreferenceXutil.getAccountToken());
            map.put("escort_record_id",escort_record_id);
        }else{
            map.put("escort_record_id",escort_record_id);
        }
        get(MyConfig.getEscortRecordInfo,map,callBack);
    }

    /**
     * 添加路线
     * @param name
     * @param callBack
     */
    public static void addLine(String name,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("route_name",name);
        post5(MyConfig.addLine,map,callBack);
    }

    /**
     * 获取所有伴游路线
     * @param callBack
     */
    public static void getAllLine(CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        get(MyConfig.getAllLine,map,callBack);
    }

    /**
     * 删除路线
     * @param route_id
     * @param callBack
     */
    public static void deleteLine(int route_id,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("route_id",route_id+"");
        post4(MyConfig.deleteLine,map,callBack);
    }

    /**
     * 添加路线点
     * @param array
     * @param handler
     */
    public static void addLinePoint(String array, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",SharePreferenceXutil.getAccountToken());
        params.put("places",array);
        client.post(MyConfig.addLinePoint,params,handler);
    }

    /**
     * 修改路线名
     * @param route_id
     * @param route_name
     * @param callBack
     */
    public static void updateLineName(int route_id,String route_name,CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("route_id",route_id+"");
        map.put("route_name",route_name);
        post4(MyConfig.updateLineName,map,callBack);
    }
    /**
     * 获取发布页面数据
     * @param callBack
     */
    public static void getReleaseInfo(CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        get(MyConfig.getReleaseInfo,map,callBack);
    }

    /**
     * 获取约咖用户信息
     * @param pageCount
     * @param callBack
     */
    public static void getEscortInfo(int pageCount,CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("pageCount",pageCount);
        get(MyConfig.getEscortInfo,map,callBack);
    }

    /**
     * 上传/修改游约咖面图片(约咖)
     * @param file
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param cover_photo_id
     * @param handler
     */
    public static void uploadCoverPicture(File file,String account_token,String time_stamp,String sign,int cover_photo_id,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("file",file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("sign",sign);
        params.put("time_stamp", time_stamp);
        params.put("account_token",account_token);
        if (cover_photo_id != 0){
            params.put("cover_photo_id",cover_photo_id);
        }
        client.post(MyConfig.uploadCoverPicture,params,handler);
    }

    /**
     * 获取用户游咖所有背景图片
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param callBack
     */
    public static void getCoverPicture(String account_token,String time_stamp,String sign,CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        get(MyConfig.getCoverPicture,map,callBack);
    }

    /**
     * 删除用户图片
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param cover_photo_id
     * @param callBack
     */
    public static void deleteCoverPicture(String account_token,String time_stamp,String sign,int cover_photo_id,CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("cover_photo_id",cover_photo_id+"");
        post4(MyConfig.deleteCoverPicture,map,callBack);
    }

    /**
     * 伴游确认订单页面数据
     * @param escort_record_id
     * @param callBack
     */
    public static void getConfirmCoffee(int escort_record_id,CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("escort_record_id",escort_record_id);
        get(MyConfig.getConfirmCoffee,map,callBack);
    }

    /**
     * 订购伴游
     * @param account_token
     * @param actual_number
     * @param beg_time
     * @param end_time
     * @param escort_record_id
     * @param sign
     * @param time_stamp
     * @param escort_service_ids
     * @param callBack
     */
    public static void purchaseEscor(String account_token,int actual_number,String beg_time,String end_time,int escort_record_id,String sign,String time_stamp,String escort_service_ids,CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("actual_number",actual_number+"");
        map.put("beg_time",beg_time);
        map.put("end_time",end_time);
        map.put("escort_record_id",escort_record_id+"");
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("escort_service_ids",escort_service_ids);
        post5(MyConfig.purchaseEscor,map,callBack);
    }

    /**
     * 计算游咖价格
     * @param account_token
     * @param actual_number
     * @param beg_time
     * @param end_time
     * @param escort_record_id
     * @param sign
     * @param time_stamp
     * @param escort_service_ids
     * @param callBack
     */
    public static void calculationPrice(String account_token,int actual_number,String beg_time,String end_time,int escort_record_id,String sign,String time_stamp,String escort_service_ids,CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("actual_number",actual_number+"");
        map.put("beg_time",beg_time);
        map.put("end_time",end_time);
        map.put("escort_record_id",escort_record_id+"");
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("escort_service_ids",escort_service_ids);
        get(MyConfig.calculationPrice,map,callBack);
    }

    /**
     * 获取领咖我的发布
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getReleaseEscortRecords(int page, int pageCount,CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("page",page);
        map.put("pageCount",pageCount   );
        get(MyConfig.getReleaseEscortRecords,map,callBack);
    }

    /**
     * 获取陪游的所有用户订单
     * @param escort_record_id
     * @param callBack
     */
    public static void getEscprtRecordUsers(int escort_record_id,CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("escort_record_id",escort_record_id);
        get(MyConfig.getEscprtRecordUsers,map,callBack);
    }

    /**
     * 伴游接单/拒接
     * @param escort_user_id
     * @param orders
     * @param callBack
     */
    public static void acceptanceEscortUser(int escort_user_id,int orders,CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("escort_user_id",escort_user_id+"");
        map.put("orders",orders+"");
        post5(MyConfig.acceptanceEscortUser,map,callBack);
    }

    /**
     * 领咖获取游咖退单信息
     * @param escort_user_id
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param callBack
     */
    public static void getTravelEscortBack(int escort_user_id ,String account_token,String time_stamp,String sign,CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("escort_user_id",escort_user_id);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        get(MyConfig.getTravelEscortBack,map,callBack);
    }

    /**
     * 发布约咖-详情
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param details
     * @param list
     * @param handler
     */
    public static void saveDetails(String account_token, String time_stamp, String sign, String details, List<SendImgFileBean> list, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("time_stamp",time_stamp);
        params.put("sign",sign);
        params.put("account_token",account_token);
        params.put("details",details);
        for (int i = 0;i<list.size();i++){
            try {
                params.put(list.get(i).getImgName(), PhotoCompress.comp(list.get(i).getFile()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        client.post(MyConfig.saveDetails,params,handler);
    }

    /**
     * 获取游咖收藏
     * @param time_stamp
     * @param sign
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getEscortCollection(String account_token,String time_stamp,String sign,int page,int pageCount,CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getEscortCollection,map,callBack);
    }

    /**
     * 约咖-收藏-取消关注
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param escort_collection_id
     * @param callBack
     */
    public static void cancelCollection(String account_token,String time_stamp,String sign,int escort_collection_id,CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("escort_collection_id",escort_collection_id+"");
        post4(MyConfig.cancelCollection,map,callBack);
    }

    /**
     * 删除伴游路线点
     * @param account_token
     * @param place_id
     * @param callBack
     */
    public static void deletePlace(String account_token,int place_id,CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("place_id",place_id+"");
        post4(MyConfig.deletePlace,map,callBack);
    }

    /**
     * 添加伴游路线点
     * @param account_token
     * @param place_name
     * @param route_id
     * @param city_id
     * @param departure_address
     * @param lng
     * @param lat
     * @param callBack
     */
    public static void addPlace(String account_token,String place_name,int route_id,String gaode_code,String departure_address,double lat,double lng,CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("place_name",place_name);
        map.put("route_id",route_id+"");
        map.put("gaode_code",gaode_code);
        map.put("departure_address",departure_address);
        map.put("lat",lat+"");
        map.put("lng",lng+"");
        post5(MyConfig.addPlace,map,callBack);
    }

    /**
     * 约咖-撤销
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param escort_record_id
     * @param callBack
     */
    public static void revokeEscortOrder(String account_token,String time_stamp,String sign,int escort_record_id,CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("escort_record_id",escort_record_id+"");
        post4(MyConfig.revokeEscortOrder,map,callBack);
    }

    /**
     * 约咖-删除
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param escort_record_id
     * @param callBack
     */
    public static void deleteEscortRecords(String account_token,String time_stamp,String sign,int escort_record_id,CallBack callBack){
        Map<String ,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("escort_record_id",escort_record_id+"");
        post4(MyConfig.deleteEscortRecords,map,callBack);
    }

    /**
     * 获取发布的记录(并重新发布)
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param escort_record_id
     * @param callBack
     */
    public static void getERInfo(String account_token,String time_stamp,String sign,int escort_record_id,CallBack callBack){
        Map<String ,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("escort_record_id",escort_record_id);
        get(MyConfig.getERInfo,map,callBack);
    }
}

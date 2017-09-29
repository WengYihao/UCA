package com.cn.uca.server.yueka;

import com.cn.uca.bean.yueka.GetEscortBean;
import com.cn.uca.bean.yueka.ReleaseEscortRecordBean;
import com.cn.uca.config.MyConfig;
import com.cn.uca.config.base.BaseServer;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StringXutil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
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
    public static void getEscortRecords(GetEscortBean bean, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",bean.getAccount_token());
        params.put("gaode_code",bean.getGaode_code());
        params.put("page",bean.getPage());
        params.put("pageCount",bean.getPageCount());
        params.put("beg_time",bean.getBeg_time());
        params.put("end_time",bean.getEnd_time());
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
     * @param callBack
     */
    public static void addLine(CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("route_name","自定义路线");
        post3(MyConfig.addLine,map,callBack);
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
}

package com.cn.uca.server.home;

import com.cn.uca.config.MyConfig;
import com.cn.uca.config.base.BaseServer;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.SharePreferenceXutil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2017/9/27.
 */

public class HomeHttp extends BaseServer {



    /**
     * 获取轮播图
     * @param callBack
     */
    public static void getCarouselFigures(CallBack callBack){
        get(MyConfig.getCarouselFigures,null,callBack);
    }

    /**
     * 获取周边游数据
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getZhouBianTravel(int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getZhouBianTravel,map,callBack);
    }

    /**
     * 获取周边游酒店
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getJiuDian(int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getJuiDian,map,callBack);
    }

    /**
     * 获取国内游
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getGuoNeiYou(int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getGuoNeiYou,map,callBack);
    }
    /**
     * 获取蜜月游
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getMiYeuYou(int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getMiYeuYou,map,callBack);
    }
    /**
     * 获取出境游
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getChuJingYou(int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getChuJingYou,map,callBack);
    }

    /**
     * 获取景区门票
     * @param scenic_spot_id
     * @param sign
     * @param time_stamp
     * @param callBack
     */
    public static void getTicket(int scenic_spot_id ,String sign,String time_stamp,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("scenic_spot_id",scenic_spot_id);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        get(MyConfig.getTicket,map,callBack);
    }

    /**
     * 获取热门景区门票
     * @param sign
     * @param time_stamp
     * @param callBack
     */
    public static void getHotTicket(String sign,String time_stamp,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        get(MyConfig.getHotTicket,map,callBack);
    }

    /**
     * 获取热门城市景区门票
     * @param page
     * @param pageCount
     * @param sign
     * @param time_stamp
     * @param city_pinyin
     * @param callBack
     */
    public static void getScenicSpotCity(int page,int pageCount,String sign,String time_stamp,String city_pinyin,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("city_pinyin",city_pinyin);
        get(MyConfig.getScenicSpotCity,map,callBack);
    }

    /**
     * 获取城市景区门票
     * @param page
     * @param pageCount
     * @param sign
     * @param time_stamp
     * @param city_id
     * @param callBack
     */
    public static void getScenicSpot(int page,int pageCount,String sign,String time_stamp,int city_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("city_id",city_id);
        get(MyConfig.getScenicSpot,map,callBack);
    }

    /**城市攻略
     * @param page
     * @param pageCount
     * @param city_pinyin
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param callBack
     */
    public static void cpyGetCityRaiders(int page,int pageCount,String city_pinyin,String sign,String time_stamp,String account_token,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("city_pinyin",city_pinyin);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        get(MyConfig.cpyGetCityRaiders,map,callBack);
    }

    /**
     * 城市攻略详情
     * @param account_token
     * @param sign
     * @param time_stamp
     * @param city_raiders_id
     * @param handler
     */
    public static void getCityRaidersInfo(String account_token,String sign,String time_stamp,int city_raiders_id ,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",account_token);
        params.put("sign",sign);
        params.put("time_stamp",time_stamp);
        params.put("city_raiders_id",city_raiders_id);
        client.get(MyConfig.getCityRaidersInfo,params,handler);
    }
}

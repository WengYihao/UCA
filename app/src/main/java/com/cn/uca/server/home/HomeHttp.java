package com.cn.uca.server.home;

import com.cn.uca.config.MyConfig;
import com.cn.uca.config.base.BaseServer;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.SharePreferenceXutil;

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
}

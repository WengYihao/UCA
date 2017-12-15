package com.cn.uca.server.home;

import android.telecom.Call;

import com.cn.uca.bean.home.samecityka.AddCardBean;
import com.cn.uca.config.MyConfig;
import com.cn.uca.config.base.BaseServer;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.SharePreferenceXutil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
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

    /**
     * 开启余生
     * @param sign
     * @param time_stamp
     * @param date_birth
     * @param sex_id
     * @param callBack
     */
    public static void openLife(String sign,String time_stamp,String date_birth,String sex_id,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",SharePreferenceXutil.getAccountToken());
        map.put("date_birth",date_birth);
        map.put("sex_id",sex_id);
        post5(MyConfig.openLife,map,callBack);
    }

    /**
     * 获取余生天数的记录
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param page
     * @param direction
     * @param callBack
     */
    public static void getLifeDays(String sign,String time_stamp,String account_token,int page,String direction,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("page",page);
        map.put("direction",direction);
        get(MyConfig.getLifeDays,map,callBack);
    }

    /**
     * 获取余生月数的记录
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param page
     * @param direction
     * @param callBack
     */
    public static void getLifeMonths(String sign,String time_stamp,String account_token,int page,String direction,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("page",page);
        map.put("direction",direction);
        get(MyConfig.getLifeMonths,map,callBack);
    }

    /**
     * 获取余生痕迹
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param page
     * @param callBack
     */
    public static void getLifeHistorical(String sign,String time_stamp,String account_token,int page,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("page",page);
        get(MyConfig.getLifeHistorical,map,callBack);
    }

    /**
     * 添加余生天记录
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param content
     * @param day
     * @param callBack
     */
    public static void addLifeDayEvent(String sign,String time_stamp,String account_token,String content,int day,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("content",content);
        map.put("day",day+"");
        post4(MyConfig.addLifeDayEvent,map,callBack);
    }

    /**
     * 添加余生月记录
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param content
     * @param month
     * @param callBack
     */
    public static void addLifeMonthEvent(String sign,String time_stamp,String account_token,String content,int month,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("content",content);
        map.put("month",month+"");
        post4(MyConfig.addLifeMonthEvent,map,callBack);
    }

    /**
     * 获取用户足迹以及用户城市记录
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param pageCount
     * @param callBack
     */
    public static void getFootprintChina(String sign,String time_stamp,String account_token,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("pageCount",pageCount);
        map.put("account_token",account_token);
        get(MyConfig.getFootprintChina,map,callBack);
    }

    /**
     * 获取足迹省份下的城市
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param code
     * @param callBack
     */
    public static void getCityName(String sign,String time_stamp,String account_token,String code,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("code",code);
        map.put("account_token",account_token);
        get(MyConfig.getCityName,map,callBack);
    }

    /**
     * 添加用户足迹
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param city_id
     * @param travel_time
     * @param content
     * @param file
     * @param handler
     */
    public static void addFootprintChina(String sign, String time_stamp, String account_token, int city_id, String travel_time, String content, File file, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sign",sign);
        params.put("time_stamp", time_stamp);
        params.put("account_token",account_token);
        params.put("city_id", city_id);
        params.put("travel_time",travel_time);
        params.put("content", content);
        try {
            if (file != null){
                params.put("file", file);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(MyConfig.addFootprintChina,params,handler);
    }

    /**
     * 加载用户足迹
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void loadMoreFootprintChina(String sign, String time_stamp, String account_token, int page, int pageCount, CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.loadMoreFootprintChina,map,callBack);
    }

    public static void getFootprintWorld(String sign,String time_stamp,String account_token,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("pageCount",pageCount);
        map.put("account_token",account_token);
        get(MyConfig.getFootprintWorld,map,callBack);
    }

    public static void addFootprintWorld(String sign, String time_stamp, String account_token, String country_id, String travel_time, String content, File file, AsyncHttpResponseHandler handle){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sign",sign);
        params.put("time_stamp", time_stamp);
        params.put("account_token",account_token);
        params.put("country_id", country_id);
        params.put("travel_time",travel_time);
        params.put("content", content);
        try {
            if (file != null){
                params.put("file", file);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(MyConfig.addFootprintWorld,params,handle);
    }

    public static void loadMoreFoodPrintWorld(String sign, String time_stamp, String account_token, int page, int pageCount, CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.loadMoreFoodPrintWorld,map,callBack);
    }

    /**
     * 购买一元攻略
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param city_raiders_id
     * @param callBack
     */
    public static void purchaseCityRaiders(String sign,String time_stamp,String account_token,int city_raiders_id,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("city_raiders_id",city_raiders_id+"");
        post5(MyConfig.purchaseCityRaiders,map,callBack);
    }

    /**
     * 支付订单
     * @param account_token
     * @param actual_payment
     * @param user_coupon_id
     * @param order_number
     * @param sign
     * @param time_stamp
     * @param callBack
     */
    public static void orderPayment(String account_token,double actual_payment,int user_coupon_id,String order_number,String sign,String time_stamp,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("actual_payment",actual_payment+"");
       if (user_coupon_id != 0){
           map.put("user_coupon_id",user_coupon_id+"");
       }
        map.put("order_number",order_number+"");
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        post4(MyConfig.orderPayment,map,callBack);
    }

    /**
     * 用户签到
     * @param sign
     * @param time_stamp
     * @param callBack
     */
    public static void userClock(String account_token,String sign, String time_stamp,CallBack callBack){
        Map<String,String> map =new HashMap<>();
        map.put("sign",sign);
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        post5(MyConfig.userClock,map,callBack);
    }

    /**
     * 获取用户签到信息
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param callBack
     */
    public static void getUserClock(String account_token,String time_stamp,String sign,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        get(MyConfig.getUserClock,map,callBack);
    }

    /**
     * 收藏/取消攻略
     * @param account_token
     * @param city_raiders_id
     * @param sign
     * @param time_stamp
     * @param callBack
     */
    public static void collectionRaiders(String account_token,int city_raiders_id ,String sign,String time_stamp, CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token", account_token);
        map.put("city_raiders_id",city_raiders_id +"");
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        post4(MyConfig.collectionRaiders,map,callBack);
    }

    /**
     * 获取攻略
     * @param page
     * @param pageCount
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param type
     * @param callBack
     */
    public static void getMyRaiders(int page,int pageCount,String sign,String time_stamp,String account_token,int type,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page", page);
        map.put("pageCount",pageCount);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("type",type);
        get(MyConfig.getMyRaiders,map,callBack);
    }

    /**
     * 获取同城咖发布时的标签
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param callBack
     */
    public  static void getCafeLabel(String account_token, String time_stamp, String sign, CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token", account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        get(MyConfig.getCafeLabel,map,callBack);
    }

    /**
     * 添加用户名片
     * @param bean
     * @param handler
     */
    public static void addUserCard(AddCardBean bean,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",bean.getAccount_token());
        params.put("time_stamp", bean.getTime_stamp());
        params.put("sign",bean.getSign());
        params.put("corporate_name", bean.getCorporate_name());
        params.put("hand_phone",bean.getHand_phone());
        params.put("introduce", bean.getIntroduce());
        params.put("learning_name", bean.getLearning_name());
        params.put("user_card_name",bean.getUser_card_name());
        params.put("user_card_type_id", bean.getUser_card_type_id());
        params.put("weixin",bean.getWeixin());
        try{
            params.put("file", bean.getFile());
        }catch (Exception e){

        }
        client.post(MyConfig.addUserCard,params,handler);
    }

    /**
     * 获取用户名片
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param callBack
     */
    public static void getUserCard(String account_token,String time_stamp,String sign,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token", account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        get(MyConfig.getUserCard,map,callBack);
    }

    /**
     * 获取同城咖2
     * @param time_stamp
     * @param user_card_type_id
     * @param sign
     * @param page
     * @param pageCount
     * @param city_id
     * @param beg_time
     * @param end_time
     * @param charge
     * @param label_id
     * @param callBack
     */
    public static void getCityCafe(String time_stamp,int user_card_type_id,String sign,int page,int pageCount,int city_id,String beg_time,String end_time,String charge,String label_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("time_stamp",time_stamp);
        map.put("user_card_type_id",user_card_type_id);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("city_id",city_id);
        map.put("beg_time",beg_time);
        map.put("end_time",end_time);
        map.put("charge",charge);
        map.put("sign",sign);
        map.put("label_id",label_id);
        get(MyConfig.getCityCafe,map,callBack);
    }

    /**
     * 获取同城咖详情
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param city_cafe_id
     * @param callBack
     */
    public static void getCityCafeInfo(String account_token,String time_stamp,String sign,int city_cafe_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("city_cafe_id",city_cafe_id);
        get(MyConfig.getCityCafeInfo,map,callBack);
    }

    /**
     * .获取同城咖购票
     * @param time_stamp
     * @param sign
     * @param city_cafe_id
     * @param callBack
     */
    public static void getTickets(String time_stamp,String sign,int city_cafe_id, CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("city_cafe_id",city_cafe_id);
        get(MyConfig.getTickets,map,callBack);
    }

    /**
     * 购买同城咖
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param city_cafe_id
     * @param tickets
     * @param fill_infos
     * @param callBack
     */
    public static void orderTicket(String time_stamp,String sign,String account_token,int city_cafe_id,String tickets,String fill_infos,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token", account_token);
        map.put("city_cafe_id",city_cafe_id +"");
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("tickets",tickets);
        map.put("fill_infos",fill_infos);
        post4(MyConfig.orderTicket,map,callBack);
    }
}


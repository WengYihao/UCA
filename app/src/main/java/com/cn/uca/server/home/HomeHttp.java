package com.cn.uca.server.home;

import android.util.Log;

import com.cn.uca.bean.home.lvpai.BecomeMerchantBean;
import com.cn.uca.bean.home.samecityka.AddCardBean;
import com.cn.uca.bean.home.samecityka.SendActionBean;
import com.cn.uca.bean.home.samecityka.SendImgFileBean;
import com.cn.uca.config.MyConfig;
import com.cn.uca.config.base.BaseServer;
import com.cn.uca.impl.CallBack;
import com.cn.uca.util.PhotoCompress;
import com.cn.uca.util.SharePreferenceXutil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
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
     * 周边必玩
     * @param sign
     * @param time_stamp
     * @param gaode_code
     * @param callBack
     */
    public static void getMustPlayAround(String sign,String time_stamp,String gaode_code,CallBack callBack){
        Map<String,Object> map =new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("gaode_code",gaode_code);
        get(MyConfig.getMustPlayAround,map,callBack);
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
    public static void cpyGetCityRaiders(int page,int pageCount,String city_pinyin,int province_id,String sign,String time_stamp,String account_token,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("city_pinyin",city_pinyin);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        if (province_id != 0){
            map.put("province_id",province_id);
        }
        get(MyConfig.cpyGetCityRaiders,map,callBack);
    }

    /**
     * ssehngfen
     * @param province_pinyin
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param callBack
     */
    public static void getProvinceRaiders(String province_pinyin,String sign,String time_stamp,String account_token,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("province_pinyin",province_pinyin);
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        get(MyConfig.getProvinceRaiders,map,callBack);
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
     * 购取景区门票
     * @param sign
     * @param time_stamp
     * @param account_token
     * @param admission_ticket_id
     * @param id_card
     * @param mobile
     * @param number
     * @param play_date
     * @param user_name
     * @param callBack
     */
    public static void createTicketOrder(String sign,String time_stamp,String account_token,int admission_ticket_id,String
            id_card,String mobile,String number,String play_date,String user_name,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("sign",sign);
        map.put("time_stamp",time_stamp);
        map.put("account_token",account_token);
        map.put("admission_ticket_id",admission_ticket_id+"");
        map.put("id_card",id_card);
        map.put("mobile",mobile);
        map.put("number",number);
        map.put("play_date",play_date);
        map.put("user_name",user_name);
        post5(MyConfig.createTicketOrder,map,callBack);

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
     * @param pay_pwd
     * @param time_stamp
     * @param callBack
     */
    public static void orderPayment(String account_token,double actual_payment,int user_coupon_id,String order_number,String sign,String pay_pwd,String time_stamp,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("actual_payment",actual_payment+"");
       if (user_coupon_id != 0){
           map.put("user_coupon_id",user_coupon_id+"");
       }
        map.put("order_number",order_number+"");
        map.put("sign",sign);
        map.put("pay_pwd",pay_pwd);
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
     * 获取积分税换物品
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getIntegralPool(String account_token,String time_stamp,String sign,int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getIntegralPool,map,callBack);
    }

    /**
     * 获取用户明细
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param gain_loss
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getIntegralDetailed(String account_token,String time_stamp,String sign,int gain_loss,int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("gain_loss",gain_loss);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getIntegralDetailed,map,callBack);
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
        post5(MyConfig.orderTicket,map,callBack);
    }

    /**
     * 发布同城咖
     * @param bean
     * @param list
     * @param handler
     */
    public static void releaseCityCafe(SendActionBean bean, List<SendImgFileBean> list,AsyncHttpResponseHandler handler){
       try{
           AsyncHttpClient client = new AsyncHttpClient();
           RequestParams params = new RequestParams();
           params.put("account_token",bean.getAccount_token());
           params.put("time_stamp", bean.getTime_stamp());
           params.put("sign",bean.getSign());
           params.put("charge",bean.isCharge());
           params.put("activity_type_id", bean.getActivity_type_id());
           params.put("beg_time",bean.getBeg_time());
           params.put("end_time", bean.getEnd_time());
           params.put("labels", bean.getLabels());
           params.put("purchase_ticket",bean.isPurchase_ticket());
           params.put("title", bean.getTitle());
           params.put("details",bean.getDetails());
           params.put("user_card_id", bean.getUser_card_id());
           params.put("cover",bean.getCover());
           params.put("position", bean.getPosition());
           params.put("tickets",bean.getTickets());
           params.put("fill_infos",bean.getFill_infos());
           if (list.size() > 0){
               for (int i = 0;i<list.size();i++){
                   params.put(list.get(i).getImgName(), PhotoCompress.comp(list.get(i).getFile()));
               }
           }
           client.post(MyConfig.releaseCityCafe,params,handler);
       }catch (Exception e){

       }
    }

    /**
     * 获取我的门票
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getMyTicket(String account_token,String time_stamp,String sign,int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getMyTicket,map,callBack);
    }

    /**
     * 获取门票二维码
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param city_cafe_order_id
     * @param callBack
     */
    public static void getUserTickets(String account_token,String time_stamp,String sign,int city_cafe_order_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("city_cafe_order_id",city_cafe_order_id);
        get(MyConfig.getUserTickets,map,callBack);
    }

    /**
     * 获取用户收藏的同城咖
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getCollectionCityCafe(String account_token,String time_stamp,String sign,int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getCollectionCityCafe,map,callBack);
    }

    /**
     * 获取我的活动
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param page
     * @param pageCount
     * @param state
     * @param callBack
     */
    public static void getMyCityCafe(String account_token,String time_stamp,String sign,int page,int pageCount,String state,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("state",state);
        get(MyConfig.getMyCityCafe,map,callBack);
    }

    /**
     * 获取报名审批
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getTicketApproval(String account_token,String time_stamp,String sign,int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getTicketApproval,map,callBack);
    }

    /**
     * 获取退票审核
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getRefundTicket(String account_token,String time_stamp,String sign,int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getRefundTicket,map,callBack);
    }

    /**
     * 报名审核
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param ticket_approval_content_id
     * @param state
     * @param callBack
     */
    public static void approvalTicket(String account_token,String time_stamp,String sign,int ticket_approval_content_id,String state,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("ticket_approval_content_id",ticket_approval_content_id+"");
        map.put("state",state);
        post5(MyConfig.approvalTicket,map,callBack);
    }

    /**
     * 退票审核
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param refund_ticket_id
     * @param state
     * @param callBack
     */
    public static void approvalRefundTicket(String account_token,String time_stamp,String sign,int refund_ticket_id,String state,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("refund_ticket_id",refund_ticket_id+"");
        map.put("state",state);
        post5(MyConfig.approvalRefundTicket,map,callBack);
    }

    /**
     * 获取同城咖结算
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getSettlement(String account_token,String time_stamp,String sign,int page,int pageCount,int city_cafe_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        if (city_cafe_id != 0){
            map.put("city_cafe_id",city_cafe_id);
        }
        get(MyConfig.getSettlement,map,callBack);
    }

    /**
     * 同城咖结算
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param city_cafe_id
     * @param callBack
     */
    public static void settlement(String account_token,String time_stamp,String sign,int city_cafe_id,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("city_cafe_id",city_cafe_id+"");
        post5(MyConfig.settlement,map,callBack);
    }

    /**
     * 同城咖活动管理
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param city_cafe_id
     * @param callBack
     */
    public static void cityCafeAdmin(String account_token,String time_stamp,String sign,int city_cafe_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("city_cafe_id",city_cafe_id);
        get(MyConfig.cityCafeAdmin,map,callBack);
    }

    /**
     * 开始或暂停同城咖售票
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param city_cafe_id
     * @param callBack
     */
    public static void staSusCityCafe(String account_token,String time_stamp,String sign,int city_cafe_id,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("city_cafe_id",city_cafe_id+"");
        post4(MyConfig.staSusCityCafe,map,callBack);
    }

    /**
     * 同城咖收藏/取消
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param city_cafe_id
     * @param callBack
     */
    public static void collectionCityCafe (String account_token,String time_stamp,String sign,int city_cafe_id,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("city_cafe_id",city_cafe_id+"");
        post4(MyConfig.collectionCityCafe,map,callBack);
    }


    /**
     * 获取同城咖发布者卡票信息
     * @param time_stamp
     * @param sign
     * @param city_cafe_id
     * @param callBack
     */
    public static void getCafeUser(String time_stamp,String sign,int city_cafe_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("city_cafe_id",city_cafe_id);
        get(MyConfig.getCafeUser,map,callBack);
    }

    /**
     * 发起者验票
     * @param account_token
     * @param time_stamp
     * @param sign
     * @param code
     * @param callBack
     */
    public static void checkTicket(String account_token,String time_stamp,String sign,String code,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("code",code);
        post4(MyConfig.checkTicket,map,callBack);
    }
    /**
     * 旅拍商家入驻
     * @param bean
     * @param stream
     * @param handler
     */
    public static void becomeMerchant(BecomeMerchantBean bean, InputStream stream,List<SendImgFileBean> list, AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",bean.getAccount_token());
        params.put("time_stamp",bean.getTime_stamp());
        params.put("sign",bean.getSign());
        params.put("contacts",bean.getContacts());
        params.put("domestic_travel",bean.getDomestic_travel());
        params.put("introduce",bean.getIntroduce());
        params.put("merchant_name",bean.getMerchant_name());
        params.put("overseas_tour",bean.getOverseas_tour());
        params.put("weixin",bean.getWeixin());
        params.put("position",bean.getPosition());
        params.put("head_portrait",stream);
        params.put("phone",bean.getPhone());
        params.put("pictures",bean.getPictures());
        for (int i = 0;i<list.size();i++){
            try {
                params.put(list.get(i).getImgName(), PhotoCompress.comp(list.get(i).getFile()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("456",e.getMessage()+"网络请求");
            }
        }
        Log.e("456",params.toString());
        client.post(MyConfig.becomeMerchant,params,handler);
    }

    /**
     * 获取商家信息
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param callBack
     */
    public static void getMerchantInfo(String time_stamp,String sign,String account_token,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        get(MyConfig.getMerchantInfo,map,callBack);
    }

    /**
     * 获取旅拍商家地址
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoopt_merchant_address_id
     * @param callBack
     */
    public static void gerAddress(String time_stamp,String sign,String account_token,int trip_shoopt_merchant_address_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        if (trip_shoopt_merchant_address_id != 0){
            map.put("trip_shoopt_merchant_address_id",trip_shoopt_merchant_address_id);
        }
        get(MyConfig.gerAddress,map,callBack);
    }

    /**
     * 获取旅拍样式标签
     * @param time_stamp
     * @param sign
     * @param callBack
     */
    public static void getStyleLable(String time_stamp,String sign,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        get(MyConfig.getStyleLable,map,callBack);
    }

    /**
     * 获取旅拍商品 包括关键字搜索 (-旅拍-)
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param page
     * @param pageCount
     * @param labels
     * @param type
     * @param sortType
     * @param sortMode
     * @param trip_shoot_merchant_id
     * @param keyword
     * @param handler
     */
    public static void getTs(String time_stamp,String sign,String account_token,int page,int pageCount,
                             String labels,String type,String sortType,String sortMode,int trip_shoot_merchant_id,
                             String keyword,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("time_stamp",time_stamp);
        params.put("sign",sign);
        params.put("account_token",account_token);
        params.put("page",page);
        params.put("pageCount",pageCount);
        params.put("labels",labels);
        params.put("type",type);
        params.put("sortType",sortType);
        params.put("sortMode",sortMode);
        if (trip_shoot_merchant_id != 0){
            params.put("trip_shoot_merchant_id",trip_shoot_merchant_id);
        }
        params.put("keyword",keyword);
        client.get(MyConfig.getTs,params,handler);
    }

    /**
     * 获取旅拍商户 (-旅拍-商家-)
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param page
     * @param pageCount
     * @param gaode_code
     * @param sortType
     * @param sortMode
     * @param keyword
     * @param handler
     */
    public static void getMerchants(String time_stamp,String sign,String account_token,int page,int pageCount,String gaode_code,String sortType,String sortMode,String keyword,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("time_stamp",time_stamp);
        params.put("sign",sign);
        params.put("account_token",account_token);
        params.put("page",page);
        params.put("pageCount",pageCount);
        params.put("gaode_code",gaode_code);
        params.put("sortType",sortType);
        params.put("sortMode",sortMode);
        params.put("keyword",keyword);
        client.get(MyConfig.getMerchants,params,handler);
    }

    /**
     * 发布旅拍
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_type_id
     * @param days
     * @param rest_day
     * @param title
     * @param price
     * @param style_label_ids
     * @param summary
     * @param trip
     * @param photo
     * @param notice
     * @param pictures
     * @param gaode_code
     * @param country_id
     * @param list
     * @param handler
     */
    public static void releaseTripShoot(String time_stamp,String sign,
            String account_token,int trip_shoot_type_id,
            int days,String rest_day,String title,String price,
            String style_label_ids,String summary,String trip,
            String photo,String notice,String pictures,
            String gaode_code,int country_id,List<SendImgFileBean> list,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("time_stamp",time_stamp);
        params.put("sign",sign);
        params.put("account_token",account_token);
        params.put("trip_shoot_type_id",trip_shoot_type_id);
        params.put("days",days);
        params.put("rest_day",rest_day);
        params.put("title",title);
        params.put("price",price);
        params.put("style_label_ids",style_label_ids);
        params.put("summary",summary);
        params.put("trip",trip);
        params.put("photo",photo);
        params.put("notice",notice);
        params.put("pictures",pictures);
        params.put("gaode_code",gaode_code);
        params.put("country_id",country_id);
        for (int i = 0;i<list.size();i++){
            try {
                params.put(list.get(i).getImgName(), PhotoCompress.comp(list.get(i).getFile()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        client.post(MyConfig.releaseTripShoot,params,handler);
    }

    /**
     * 获取商品详细信息
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_id
     * @param callBack
     */
    public static void getCommodityInfo(String time_stamp,String sign,String account_token,int trip_shoot_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_id",trip_shoot_id);
        get(MyConfig.getCommodityInfo,map,callBack);
    }

    /**
     * 获取商家详细信息
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_merchant_id
     * @param callBack
     */
    public static void getMerInfo(String time_stamp,String sign,String account_token,int trip_shoot_merchant_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_merchant_id",trip_shoot_merchant_id);
        get(MyConfig.getMerInfo,map,callBack);
    }

    /**
     * 获取旅拍商户相册图片
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_merchant_id
     * @param callBack
     */
    public static void getMerAllPic(String time_stamp,String sign,String account_token,int trip_shoot_merchant_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_merchant_id",trip_shoot_merchant_id);
        get(MyConfig.getMerAllPic,map,callBack);
    }

    /**
     * .获取旅拍商品行程
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_id
     * @param callBack
     */
    public static void getTsDate(String time_stamp,String sign,String account_token,int trip_shoot_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_id",trip_shoot_id);
        get(MyConfig.getTsDate,map,callBack);
    }

    /**
     * 收藏或取消收藏旅拍商品
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_id
     * @param callBack
     */
    public static void collectionTs(String time_stamp,String sign,String account_token,int trip_shoot_id,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_id",trip_shoot_id+"");
        post4(MyConfig.collectionTs,map,callBack);
    }

    /**
     * 关注/取消关注旅拍商户
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_merchant_id
     * @param callBack
     */
    public static void followMerchant(String time_stamp,String sign,String account_token,int trip_shoot_merchant_id,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_merchant_id",trip_shoot_merchant_id+"");
        post4(MyConfig.followMerchant,map,callBack);
    }

    /**
     * 行程选择
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_id
     * @param dates
     * @param callBack
     */
    public static void createTsOrder(String time_stamp,String sign,String account_token,int trip_shoot_id,String dates,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_id",trip_shoot_id+"");
        map.put("dates",dates);
        post5(MyConfig.createTsOrder,map,callBack);
    }

    /**
     * 旅拍团队增删改
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param type
     * @param introduce
     * @param head_portrait
     * @param trip_shoot_team_id
     * @param handler
     */
    public static void updateTeam(String time_stamp,String sign,String account_token,String type,String introduce,File head_portrait,int trip_shoot_team_id,AsyncHttpResponseHandler handler ){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sign",sign);
        params.put("time_stamp", time_stamp);
        params.put("account_token",account_token);
        params.put("type",type);
       if (introduce != null){
           params.put("introduce",introduce);
       }
        try {
            if (head_portrait != null){
                params.put("head_portrait",head_portrait);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (trip_shoot_team_id != 0){
            params.put("trip_shoot_team_id",trip_shoot_team_id);
        }
        client.post(MyConfig.updateTeam,params,handler);
    }

    /**
     * 获取旅拍团队
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param callBack
     */
    public static void getTeam(String time_stamp,String sign,String account_token,CallBack callBack) {
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        get(MyConfig.getTeam,map,callBack);
    }

    /**
     * 获取用户收藏的旅拍商户
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getFollowMers(String time_stamp,String sign,String account_token,int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getFollowMers,map,callBack);
    }

    /**
     * 获取用户收藏的旅拍商户
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getCollTs(String time_stamp,String sign,String account_token,int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getCollTs,map,callBack);
    }

    /**
     * 获取商家相册
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param callBack
     */
    public static void getMerchantAlbum(String time_stamp,String sign,String account_token,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        get(MyConfig.getMerchantAlbum,map,callBack);
    }

    /**
     * 获取相册中的图片
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param merchant_album_id
     * @param callBack
     */
    public static void getAlbumPicture(String time_stamp,String sign,String account_token,int merchant_album_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("merchant_album_id",merchant_album_id);
        get(MyConfig.getAlbumPicture,map,callBack);
    }

    /**
     * 获取旅拍商户发布的商品
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param page
     * @param pageCount
     * @param state
     * @param callBack
     */
    public static void getMyTs(String time_stamp,String sign,String account_token,int page,int pageCount,String state,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("state",state);
        get(MyConfig.getMyTs,map,callBack);
    }

    /**
     * 取旅拍商家订单
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param page
     * @param pageCount
     * @param trip_shoot_id
     * @param callBack
     */
    public static void getTsOrders(String time_stamp,String sign,String account_token,int page,int pageCount,int trip_shoot_id,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("trip_shoot_id",trip_shoot_id);
        map.put("trip_shoot_state","orders");
        get(MyConfig.getTsOrders,map,callBack);
    }

    /**
     * 设置旅拍商品推荐
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_id
     * @param callBack
     */
    public static void hotspot(String time_stamp,String sign,String account_token,int trip_shoot_id,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_id",trip_shoot_id+"");
        post5(MyConfig.hotspot,map,callBack);
    }

    /**
     * 设置旅拍商品上/下架
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_id
     * @param callBack
     */
    public static void upLoShelves(String time_stamp,String sign,String account_token,int trip_shoot_id,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_id",trip_shoot_id+"");
        post5(MyConfig.upLoShelves,map,callBack);
    }

    /**
     * 获取旅拍商家所有订单
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param trip_shoot_state
     * @param page
     * @param pageCount
     * @param callBack
     */
    public static void getTsOrder(String time_stamp,String sign,String account_token,String trip_shoot_state,int page,int pageCount,CallBack callBack){
        Map<String,Object> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("trip_shoot_state",trip_shoot_state);
        map.put("page",page);
        map.put("pageCount",pageCount);
        get(MyConfig.getTsOrder,map,callBack);
    }

    /**
     * 商家增删改相册
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param type
     * @param merchant_album_id
     * @param album_name
     * @param callBack
     */
    public static void updateMerchantAlbum(String time_stamp,String sign,String account_token,String type,int merchant_album_id,String album_name,CallBack callBack){
        Map<String,String> map = new HashMap<>();
        map.put("account_token",account_token);
        map.put("time_stamp",time_stamp);
        map.put("sign",sign);
        map.put("type",type);
        if (merchant_album_id != 0){
            map.put("merchant_album_id",merchant_album_id+"");
        }
        if (album_name != null){
            map.put("album_name",album_name);
        }
        post5(MyConfig.updateMerchantAlbum,map,callBack);
    }

    /**
     * 添加照片
     * @param time_stamp
     * @param sign
     * @param account_token
     * @param merchant_album_id
     * @param file
     * @param handler
     */
    public static void addAlbumPicture(String time_stamp,String sign,String account_token,int merchant_album_id,File file,AsyncHttpResponseHandler handler){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_token",account_token);
        params.put("sign",sign);
        params.put("time_stamp",time_stamp);
        params.put("merchant_album_id",merchant_album_id);
        try{
            params.put("picture",file);
        }catch (Exception e){

        }
        client.post(MyConfig.addAlbumPicture,params,handler);
    }
}


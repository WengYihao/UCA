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


}

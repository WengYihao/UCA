package com.cn.uca.bean.home.travel;

import java.util.List;

/**
 * Created by asus on 2018/3/5.
 */

public class TravelInfoBean {
    private int tourism_id;// 旅游id
    private String product_name;// 产品名称
    private List<String> compress_pictures;// 缩略图
    private List<String> pictures;// 图片
    private double min_price;// 最低价格

    private List<ScheduleBean> schedules;// 获取班期
//    private List<CabinTypeRet> cabinTypes;// 获取旅游舱型
//    private List<TourismTripRet> tourismTrips;// 获取旅游行程

    private String price_include;// 费用包含
    private String price_not_include;// 费用不包含
    private String book_must_know;// 预定须知
    private String visa_info;// 签证须知
    private String the_guest_limit;// 收客限制
    private String default_clause;// 违款条约

    private int journey_days;// 旅行天数
    private int check_in_days;// 入住天数
    private int sign_up_end_days;// 报名结束日前
    private int purchase_quantity;// 购买量

    private String product_type_name;// 项目类型
    private String tourism_type_name;// 旅游类型
    private String traffic_type_name;// 交通类型

    private double score;// 评分
    private int comment_number;// 评论数
    private boolean assess_picure;// 有图点评 true或false
    private boolean satisfied;// 是否满意
    private boolean buy_insurance;// 是否买保险
    private boolean five_star_hotel;// 五星酒店

    public int getTourism_id() {
        return tourism_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public List<String> getCompress_pictures() {
        return compress_pictures;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public double getMin_price() {
        return min_price;
    }

    public String getPrice_include() {
        return price_include;
    }

    public String getPrice_not_include() {
        return price_not_include;
    }

    public String getBook_must_know() {
        return book_must_know;
    }

    public String getVisa_info() {
        return visa_info;
    }

    public String getThe_guest_limit() {
        return the_guest_limit;
    }

    public String getDefault_clause() {
        return default_clause;
    }

    public int getJourney_days() {
        return journey_days;
    }

    public int getCheck_in_days() {
        return check_in_days;
    }

    public int getSign_up_end_days() {
        return sign_up_end_days;
    }

    public int getPurchase_quantity() {
        return purchase_quantity;
    }

    public String getProduct_type_name() {
        return product_type_name;
    }

    public String getTourism_type_name() {
        return tourism_type_name;
    }

    public String getTraffic_type_name() {
        return traffic_type_name;
    }

    public double getScore() {
        return score;
    }

    public int getComment_number() {
        return comment_number;
    }

    public boolean isAssess_picure() {
        return assess_picure;
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public boolean isBuy_insurance() {
        return buy_insurance;
    }

    public boolean isFive_star_hotel() {
        return five_star_hotel;
    }

    public List<ScheduleBean> getSchedules() {
        return schedules;
    }
}

package com.cn.uca.bean.yueka;

import com.cn.uca.bean.home.samecityka.ActionDescribeBean;

import java.util.List;

/**
 * Created by asus on 2018/3/12.
 */

public class SendYueka {
    private int escort_record_id;
    private String escort_record_name;
    private List<ActionDescribeBean> paragraphs;
    private int requirement_people_number;
    private List<ServicesBean> serviceFunctions;

    public int getEscort_record_id() {
        return escort_record_id;
    }

    public String getEscort_record_name() {
        return escort_record_name;
    }

    public List<ActionDescribeBean> getParagraphs() {
        return paragraphs;
    }

    public int getRequirement_people_number() {
        return requirement_people_number;
    }

    public List<ServicesBean> getServiceFunctions() {
        return serviceFunctions;
    }
}

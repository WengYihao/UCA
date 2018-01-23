package com.cn.uca.bean.user.message;

/**
 * Created by asus on 2018/1/18.
 */

public class MessageBean {
    private String content;
    private String extras;
    private int system_push_data_id;
    private String system_push_time;
    private int system_push_user_id;
    private String title;

    public String getContent() {
        return content;
    }

    public String getExtras() {
        return extras;
    }

    public int getSystem_push_data_id() {
        return system_push_data_id;
    }

    public String getSystem_push_time() {
        return system_push_time;
    }

    public int getSystem_push_user_id() {
        return system_push_user_id;
    }

    public String getTitle() {
        return title;
    }
}

package com.cn.uca.bean.home.samecityka;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/13.
 * 活动描述
 */

public class ActionDescribeBean implements Parcelable{
    private String img_url;
    private String paragraph_type;
    private String content;
    private int paragraph_id;

    public String getImg_url() {
        return img_url;
    }

    public String getParagraph_type() {
        return paragraph_type;
    }

    public String getContent() {
        return content;
    }

    public int getParagraph_id() {
        return paragraph_id;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setParagraph_type(String paragraph_type) {
        this.paragraph_type = paragraph_type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setParagraph_id(int paragraph_id) {
        this.paragraph_id = paragraph_id;
    }

    @Override
    public String toString() {
        return "ActionDescribeBean{" +
                "img_url='" + img_url + '\'' +
                ", paragraph_type='" + paragraph_type + '\'' +
                ", content='" + content + '\'' +
                ", paragraph_id=" + paragraph_id +
                '}';
    }

    public static final Parcelable.Creator<ActionDescribeBean> CREATOR = new Creator<ActionDescribeBean>() {
        @Override
        public ActionDescribeBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            ActionDescribeBean bean = new ActionDescribeBean();
            bean.setImg_url(source.readString());
            bean.setParagraph_type(source.readString());
            bean.setContent(source.readString());
            bean.setParagraph_id(source.readInt());
            return bean;
        }
        @Override
        public ActionDescribeBean[] newArray(int size) {
            return new ActionDescribeBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img_url);
        dest.writeString(paragraph_type);
        dest.writeString(content);
        dest.writeInt(paragraph_id);
    }
}

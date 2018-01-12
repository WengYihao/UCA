package com.cn.uca.bean.home.samecityka;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.InputStream;

/**
 * Created by asus on 2017/12/12.
 */

public class SendContentBean implements Parcelable{
    private String paragraph_type;
    private String content;

    public String getParagraph_type() {
        return paragraph_type;
    }

    public String getContent() {
        return content;
    }


    public void setParagraph_type(String paragraph_type) {
        this.paragraph_type = paragraph_type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "{" +
                "paragraph_type='" + paragraph_type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
    public static final Parcelable.Creator<SendContentBean> CREATOR = new Creator<SendContentBean>() {
        @Override
        public SendContentBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            SendContentBean bean = new SendContentBean();
            bean.setParagraph_type(source.readString());
            bean.setContent(source.readString());
            return bean;
        }
        @Override
        public SendContentBean[] newArray(int size) {
            return new SendContentBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paragraph_type);
        dest.writeString(content);
    }
}

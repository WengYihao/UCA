package com.cn.uca.bean.home.lvpai;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/26.
 */

public class SendContentbean implements Parcelable{
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

    public static final Parcelable.Creator<SendContentbean> CREATOR = new Creator<SendContentbean>() {
        @Override
        public SendContentbean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            SendContentbean bean = new SendContentbean();
            bean.setParagraph_type(source.readString());
            bean.setContent(source.readString());
            return bean;
        }
        @Override
        public SendContentbean[] newArray(int size) {
            return new SendContentbean[size];
        }
    };
    @Override
    public String toString() {
        return "{" +
                "paragraph_type='" + paragraph_type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

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

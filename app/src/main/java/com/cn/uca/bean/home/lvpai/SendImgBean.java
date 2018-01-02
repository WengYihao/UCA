package com.cn.uca.bean.home.lvpai;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/26.
 */

public class SendImgBean implements Parcelable {
    private String paragraph_type;
    private String img_url;

    public String getParagraph_type() {
        return paragraph_type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setParagraph_type(String paragraph_type) {
        this.paragraph_type = paragraph_type;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public static final Parcelable.Creator<SendImgBean> CREATOR = new Creator<SendImgBean>() {
        @Override
        public SendImgBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            SendImgBean bean = new SendImgBean();
            bean.setParagraph_type(source.readString());
            bean.setImg_url(source.readString());
            return bean;
        }
        @Override
        public SendImgBean[] newArray(int size) {
            return new SendImgBean[size];
        }
    };
    @Override
    public String toString() {
        return "{" +
                "paragraph_type='" + paragraph_type + '\'' +
                ", img_url='" + img_url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paragraph_type);
        dest.writeString(img_url);
    }
}

package com.cn.uca.bean.home.lvpai;

import android.os.Parcel;
import android.os.Parcelable;

import com.cn.uca.bean.home.samecityka.SendImgFileBean;

import java.util.List;

/**
 * Created by asus on 2017/12/26.
 */

public class ImgAndContentBean implements Parcelable{
    private String content;
    private List<SendImgFileBean> been;

    public void setContent(String content) {
        this.content = content;
    }

    public void setBeen(List<SendImgFileBean> been) {
        this.been = been;
    }

    public String getContent() {
        return content;
    }

    public List<SendImgFileBean> getBeen() {
        return been;
    }

    @Override
    public String toString() {
        return "{" +
                "content='" + content + '\'' +
                ", been=" + been +
                '}';
    }
    public static final Parcelable.Creator<ImgAndContentBean> CREATOR = new Creator<ImgAndContentBean>() {
        @Override
        public ImgAndContentBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            ImgAndContentBean bean = new ImgAndContentBean();
            bean.setContent(source.readString());
            bean.setBeen(source.readArrayList(SendImgFileBean.class.getClassLoader()));
            return bean;
        }
        @Override
        public ImgAndContentBean[] newArray(int size) {
            return new ImgAndContentBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeList(been);
    }
}

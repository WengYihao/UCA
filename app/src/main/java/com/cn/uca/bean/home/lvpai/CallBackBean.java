package com.cn.uca.bean.home.lvpai;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/12/26.
 */

public class CallBackBean implements Parcelable {
    private ImgAndContentBean service;
    private ImgAndContentBean recommended;
    private ImgAndContentBean photo;
    private String description;

    public ImgAndContentBean getService() {
        return service;
    }

    public ImgAndContentBean getRecommended() {
        return recommended;
    }

    public ImgAndContentBean getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public void setService(ImgAndContentBean service) {
        this.service = service;
    }

    public void setRecommended(ImgAndContentBean recommended) {
        this.recommended = recommended;
    }

    public void setPhoto(ImgAndContentBean photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final Parcelable.Creator<CallBackBean> CREATOR = new Creator<CallBackBean>() {
        @Override
        public CallBackBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            CallBackBean bean = new CallBackBean();
            bean.service = source.readParcelable(ImgAndContentBean.class.getClassLoader());
            bean.recommended = source.readParcelable(ImgAndContentBean.class.getClassLoader());
            bean.photo = source.readParcelable(ImgAndContentBean.class.getClassLoader());
            bean.description = source.readString();
            return bean;
        }
        @Override
        public CallBackBean[] newArray(int size) {
            return new CallBackBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(service,0);
        dest.writeParcelable(recommended,1);
        dest.writeParcelable(photo,2);
        dest.writeString(description);
    }

    @Override
    public String toString() {
        return "CallBackBean{" +
                "service=" + service +
                ", recommended=" + recommended +
                ", photo=" + photo +
                ", description='" + description + '\'' +
                '}';
    }
}

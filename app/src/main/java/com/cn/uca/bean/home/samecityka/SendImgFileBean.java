package com.cn.uca.bean.home.samecityka;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.cn.uca.util.StringXutil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by asus on 2017/12/15.
 */

public class SendImgFileBean implements Parcelable{
    private String imgName;
    private Bitmap file;

    public String getImgName() {
        return imgName;
    }

    public Bitmap getFile() {
        return file;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setFile(Bitmap file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "{" +
                "imgName='" + imgName + '\'' +
                ", file=" + file +
                '}';
    }

    public static final Parcelable.Creator<SendImgFileBean> CREATOR = new Creator<SendImgFileBean>() {
        @Override
        public SendImgFileBean createFromParcel(Parcel source) {
            // 必须按成员变量的顺序读取数据，不然会出现获取数据报错
            SendImgFileBean bean = new SendImgFileBean();
            bean.setImgName(source.readString());
            bean.setFile((Bitmap) source.readParcelable(Bitmap.class.getClassLoader()));
            return bean;
        }
        @Override
        public SendImgFileBean[] newArray(int size) {
            return new SendImgFileBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeString(imgName);
            dest.writeParcelable(file,0);
        }catch (Exception e){
            Log.i("456",e.getMessage());
        }

    }
}

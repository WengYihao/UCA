package com.cn.uca.bean.home.yusheng;

import android.os.Parcel;
import android.os.Parcelable;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

/**
 * Created by asus on 2017/10/24.
 */

public class YuShengDayBean implements AsymmetricItem {
    private int columnSpan;
    private int rowSpan;
    private int position;
    private YuShengDayDetailsBean dayBean;

    public YuShengDayBean(int columnSpan, int rowSpan, int position, YuShengDayDetailsBean dayBean){
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
        this.dayBean = dayBean;
    }

    public YuShengDayBean(Parcel in) {
        readFromParcel(in);
    }

    @Override public int getColumnSpan() {
        return columnSpan;
    }

    @Override public int getRowSpan() {
        return rowSpan;
    }

    public int getPosition() {
        return position;
    }

    public YuShengDayDetailsBean getDayBean() {
        return dayBean;
    }

    @Override
    public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan,dayBean);
    }

       @Override public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
        dayBean = in.readParcelable(YuShengDayDetailsBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(columnSpan);
        parcel.writeInt(rowSpan);
        parcel.writeInt(position);
        parcel.writeParcelable((Parcelable) dayBean,i);
    }

    /* Parcelable interface implementation */
    public static final Parcelable.Creator<YuShengDayBean> CREATOR = new Parcelable.Creator<YuShengDayBean>() {
        @Override public YuShengDayBean createFromParcel(Parcel in) {
            return new YuShengDayBean(in);
        }

        @Override public YuShengDayBean[] newArray(int size) {
            return new YuShengDayBean[size];
        }
    };
}

package com.cn.uca.bean.home.yusheng;

import android.os.Parcel;
import android.os.Parcelable;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

/**
 * Created by asus on 2017/10/26.
 */

public class YuShengMonthBean implements AsymmetricItem {
    private int columnSpan;
    private int rowSpan;
    private int position;
    private YuShengMonthDetailsBean monthBean;

    public YuShengMonthBean(int columnSpan, int rowSpan, int position, YuShengMonthDetailsBean monthBean){
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
        this.monthBean = monthBean;
    }

    public YuShengMonthBean(Parcel in) {
        readFromParcel(in);
    }
    @Override
    public int getColumnSpan() {
        return columnSpan;
    }

    @Override
    public int getRowSpan() {
        return rowSpan;
    }

    public int getPosition() {
        return position;
    }

    public YuShengMonthDetailsBean getMonthBean() {
        return monthBean;
    }
    @Override
    public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan,monthBean);
    }

    @Override public int describeContents() {
        return 0;
    }
    private void readFromParcel(Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
        monthBean = in.readParcelable(YuShengMonthBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(columnSpan);
        parcel.writeInt(rowSpan);
        parcel.writeInt(position);
        parcel.writeParcelable((Parcelable) monthBean,i);
    }

    /* Parcelable interface implementation */
    public static final Parcelable.Creator<YuShengMonthBean> CREATOR = new Parcelable.Creator<YuShengMonthBean>() {
        @Override public YuShengMonthBean createFromParcel(Parcel in) {
            return new YuShengMonthBean(in);
        }

        @Override public YuShengMonthBean[] newArray(int size) {
            return new YuShengMonthBean[size];
        }
    };
}

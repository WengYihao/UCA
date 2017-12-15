package com.cn.uca.bean.home.samecityka;

/**
 * Created by asus on 2017/12/13.
 */

public class SendImgBean {

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

    @Override
    public String toString() {
        return "SendImgBean{" +
                "paragraph_type='" + paragraph_type + '\'' +
                ", img_url='" + img_url + '\'' +
                '}';
    }
}

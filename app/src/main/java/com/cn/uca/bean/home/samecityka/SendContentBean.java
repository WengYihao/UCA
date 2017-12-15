package com.cn.uca.bean.home.samecityka;

import java.io.InputStream;

/**
 * Created by asus on 2017/12/12.
 */

public class SendContentBean {
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
        return "SendContentBean{" +
                "paragraph_type='" + paragraph_type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

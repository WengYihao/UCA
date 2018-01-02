package com.cn.uca.bean.home.lvpai;

import java.util.List;

/**
 * Created by asus on 2017/12/28.
 */

public class PhotoDetailBean {
    private List<MerchantPhotoBean> albumPictures;
    private String album_name;
    private String create_time;
    private String img_url;
    private int merchant_album_id;
    private int number;

    public List<MerchantPhotoBean> getAlbumPictures() {
        return albumPictures;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getImg_url() {
        return img_url;
    }

    public int getMerchant_album_id() {
        return merchant_album_id;
    }

    public int getNumber() {
        return number;
    }
}

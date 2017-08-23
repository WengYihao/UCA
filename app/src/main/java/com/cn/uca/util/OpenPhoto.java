package com.cn.uca.util;

import android.content.Context;
import android.content.Intent;

import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.ImagePagerActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by asus on 2017/8/4.
 */

public class OpenPhoto {
    /**
     * 打开图片查看器
     * @param count
     * @param urls
     */
    public static void imageUrl(Context context,int count, ArrayList<String> urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, count);
        context.startActivity(intent);
    }
}

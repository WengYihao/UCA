package com.cn.uca.loader;

import android.content.Context;
import android.widget.ImageView;

import com.cn.uca.impl.ImageLoaderInterface;


public abstract class ImageLoader implements ImageLoaderInterface<ImageView> {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

}

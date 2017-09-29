package com.cn.uca.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.util.BaseHideActivity;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.view.FluidLayout;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends BaseHideActivity implements View.OnClickListener{

    private FluidLayout fluidLayout;
    private int gravity = Gravity.TOP;
    private List<String> images=new ArrayList<>();//图片地址集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
    }

    private void initView() {
        fluidLayout = (FluidLayout) findViewById(R.id.fluid_layout);

        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        images.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        genTag();
    }
    @Override
    public void onClick(View v) {

    }

    private void genTag() {
        fluidLayout.removeAllViews();
        fluidLayout.setGravity(gravity);

        for (int i = 0; i < images.size(); i++) {
            SimpleDraweeView tv = new SimpleDraweeView(this);
            Uri uri = Uri.parse(images.get(i));
            tv.setImageURI(uri);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OpenPhoto.imageUrl(Main2Activity.this,images.size(),(ArrayList<String>) images);
                }
            });
            GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(getResources()).
                    setRoundingParams(RoundingParams.fromCornersRadius(20)).build();
            tv.setHierarchy(hierarchy);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(12, 12, 12, 12);
            fluidLayout.addView(tv, params);
            FluidLayout.LayoutParams layoutParams =(FluidLayout.LayoutParams) tv.getLayoutParams(); //取控件textView当前的布局参数
            layoutParams.height =MyApplication.width/5*3/4;// 控件的高强制设成20
            layoutParams.width = MyApplication.width/5;
            tv.setLayoutParams(layoutParams); //使设置好的布局参数应用到控件

        }
    }
}

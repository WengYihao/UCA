package com.cn.uca.popupwindows;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.util.SystemUtil;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by asus on 2017/11/29.
 */

public class LoadingPopupWindow extends Dialog{
    private TextView mTextView;
    private GifDrawable gifFromAssets;
    private GifImageView imageView;
    public LoadingPopupWindow(Context context) {

        super(context, R.style.WinDialog);
        setContentView(R.layout.loading_popupwindow);
        mTextView = (TextView) findViewById(android.R.id.message);
        try {
            gifFromAssets = new GifDrawable(context.getAssets(), "loading.gif");
            imageView = (GifImageView)findViewById(R.id.load);
            imageView.setImageDrawable(gifFromAssets);
            final MediaController mediaController = new MediaController(context);
            mediaController.setMediaPlayer((GifDrawable) imageView.getDrawable());
            /**
             * 也许你会像我一样，当看到上面一行代码时会纳闷，为什么setMediaPalyer传入的参数会是一个
             * GifDrawable对象呢，它需要的参数类型是MediaPlayerControl。。。
             * 还永德我们前面提到GifDrawable实现了MediaPlayerControl接口吗？
             * 嗯。。。哦，，，恍然大明白了
             */
            mediaController.setAnchorView(imageView);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void show() {
        super.show();
        Log.i("123", SystemUtil.getCurrentDate()+"开始");
    }

    @Override
    public void dismiss() {
        gifFromAssets.stop();
        gifFromAssets.recycle();
        super.dismiss();
        Log.i("123", SystemUtil.getCurrentDate()+"结束");
    }
    public void setText(String s) {
        if (mTextView != null) {
            mTextView.setText(s);
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    public void setText(int res) {
        if (mTextView != null) {
            mTextView.setText(res);
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return false;
        }
        return super.onTouchEvent(event);
    }

}

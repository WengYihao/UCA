package com.cn.uca.ui.view.home.samecityka;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.home.samecityka.ReplyNewFragment;
import com.cn.uca.ui.fragment.home.samecityka.WaitNewFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;

/**
 * 回复留言--参与者
 */
public class ReplyNewActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,title01,title02;
    private ReplyNewFragment replyNewFragment;
    private WaitNewFragment waitNewFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_new);

        initView();
    }

    private void initView() {
        back =  (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);

        back.setOnClickListener(this);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);

        show(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.title01:
                show(0);
                break;
            case R.id.title02:
                show(1);
                break;
        }
    }
    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (index) {
            case 0:
                if (replyNewFragment == null) {
                    replyNewFragment = new ReplyNewFragment();
                    fragmentTransaction.add(R.id.container, replyNewFragment);
                }
                fragmentTransaction.show(replyNewFragment);
                title01.setTextColor(getResources().getColor(R.color.ori));
                title02.setTextColor(getResources().getColor(R.color.grey));
                break;
            case 1:
                if (waitNewFragment == null) {
                    waitNewFragment = new WaitNewFragment();
                    fragmentTransaction.add(R.id.container, waitNewFragment);
                }
                fragmentTransaction.show(waitNewFragment);
                title01.setTextColor(getResources().getColor(R.color.grey));
                title02.setTextColor(getResources().getColor(R.color.ori));
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(replyNewFragment);
                break;
            case 1:
                fragmentTransaction.hide(waitNewFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }
}

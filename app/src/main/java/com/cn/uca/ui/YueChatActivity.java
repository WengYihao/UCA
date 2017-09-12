package com.cn.uca.ui;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.CourseEscortFragment;
import com.cn.uca.ui.fragment.HotleFragment;
import com.cn.uca.ui.fragment.YueDetailsFragment;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SystemUtil;

public class YueChatActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView stateTitle,title01,title02;
    private int currentIndex = -1;
    private YueDetailsFragment yueDetailsFragment;
    private CourseEscortFragment courseEscortFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_yue_chat);

        initView();
    }

    private void initView() {
        stateTitle  =(TextView)findViewById(R.id.stateTitle);
        stateTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.getStatusHeight(this)));
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);

        title01.setOnClickListener(this);
        title02.setOnClickListener(this);

        show(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
                if (yueDetailsFragment == null) {
                    yueDetailsFragment = new YueDetailsFragment();
                    fragmentTransaction.add(R.id.container, yueDetailsFragment);
                }
                fragmentTransaction.show(yueDetailsFragment);
                title01.setTextColor(getResources().getColor(R.color.black));
                title01.setBackgroundResource(R.color.white);

                title02.setTextColor(getResources().getColor(R.color.white));
                title02.setBackgroundResource(R.color.grey2);

                break;
            case 1:
                if (courseEscortFragment == null) {
                    courseEscortFragment = new CourseEscortFragment();
                    fragmentTransaction.add(R.id.container, courseEscortFragment);
                }
                fragmentTransaction.show(courseEscortFragment);
                title01.setTextColor(getResources().getColor(R.color.white));
                title01.setBackgroundResource(R.color.grey2);

                title02.setTextColor(getResources().getColor(R.color.black));
                title02.setBackgroundResource(R.color.white);
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(yueDetailsFragment);
                break;
            case 1:
                fragmentTransaction.hide(courseEscortFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }
}

package com.cn.uca.ui.view.home.samecityka;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.home.samecityka.BackTicketFragment;
import com.cn.uca.ui.fragment.home.samecityka.SignUpFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;

/**
 * 审核
 */
public class ExamineActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,title01,title02;
    private SignUpFragment signUpFragment;
    private BackTicketFragment backTicketFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine);

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
                if (signUpFragment == null) {
                    signUpFragment = new SignUpFragment();
                    fragmentTransaction.add(R.id.container, signUpFragment);
                }
                fragmentTransaction.show(signUpFragment);
                title01.setTextColor(getResources().getColor(R.color.ori));
                title02.setTextColor(getResources().getColor(R.color.grey));
                break;
            case 1:
                if (backTicketFragment == null) {
                    backTicketFragment = new BackTicketFragment();
                    fragmentTransaction.add(R.id.container, backTicketFragment);
                }
                fragmentTransaction.show(backTicketFragment);
                title01.setTextColor(getResources().getColor(R.color.grey));
                title02.setTextColor(getResources().getColor(R.color.ori));
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(signUpFragment);
                break;
            case 1:
                fragmentTransaction.hide(backTicketFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }
}

package com.cn.uca.ui.view.home.travel;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.home.travel.DomesticFragment;
import com.cn.uca.ui.fragment.home.travel.ExitFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
//国内游---更多
public class MoreTravelActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,title,choice;
    private DomesticFragment domesticFragment;//国内
    private ExitFragment exitFragment;//出境
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_travel);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        choice = (TextView)findViewById(R.id.choice);

        back.setOnClickListener(this);
        choice.setOnClickListener(this);

        show(0);
    }

    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (index) {
            case 0:
                if (domesticFragment == null) {
                    domesticFragment = new DomesticFragment();
                    fragmentTransaction.add(R.id.container, domesticFragment);
                }
                fragmentTransaction.show(domesticFragment);
                break;
            case 1:
                if (exitFragment == null) {
                    exitFragment = new ExitFragment();
                    fragmentTransaction.add(R.id.container, exitFragment);
                }
                fragmentTransaction.show(exitFragment);
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(domesticFragment);
                break;
            case 1:
                fragmentTransaction.hide(exitFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.choice:
                switch (choice.getText().toString()){
                    case "出境":
                        choice.setText("国内");
                        title.setText("出境");
                        show(1);
                        break;
                    case "国内":
                        choice.setText("出境");
                        title.setText("国内");
                        show(0);
                        break;
                }
                break;
        }
    }
}

package com.cn.uca.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.fragment.CourseEscortFragment;
import com.cn.uca.ui.fragment.DetailsPresetFragment;
import com.cn.uca.ui.fragment.LinePresetFragment;
import com.cn.uca.ui.fragment.YueDetailsFragment;
import com.cn.uca.util.FitStateUI;

public class PresetManagerActivity extends FragmentActivity implements View.OnClickListener{

    private TextView back,line,details;
    private LinePresetFragment linePresetFragment;
    private DetailsPresetFragment detailsPresetFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FitStateUI.setImmersionStateMode(this);

        setContentView(R.layout.activity_preset_manager);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        line = (TextView)findViewById(R.id.line);
        details  =  (TextView)findViewById(R.id.details);

        back.setOnClickListener(this);
        line.setOnClickListener(this);
        details.setOnClickListener(this);

        show(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.line:
                show(0);
                break;
            case R.id.details:
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
                if (linePresetFragment == null) {
                    linePresetFragment = new LinePresetFragment();
                    fragmentTransaction.add(R.id.container, linePresetFragment);
                }
                fragmentTransaction.show(linePresetFragment);
                line.setTextColor(getResources().getColor(R.color.black));
                line.setBackgroundResource(R.color.white);

                details.setTextColor(getResources().getColor(R.color.white));
                details.setBackgroundResource(R.color.grey2);

                break;
            case 1:
                if (detailsPresetFragment == null) {
                    detailsPresetFragment = new DetailsPresetFragment();
                    fragmentTransaction.add(R.id.container,detailsPresetFragment);
                }
                fragmentTransaction.show(detailsPresetFragment);
                line.setTextColor(getResources().getColor(R.color.white));
                line.setBackgroundResource(R.color.grey2);

                details.setTextColor(getResources().getColor(R.color.black));
                details.setBackgroundResource(R.color.white);
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(linePresetFragment);
                break;
            case 1:
                fragmentTransaction.hide(detailsPresetFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }
}

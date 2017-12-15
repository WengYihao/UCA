package com.cn.uca.ui.fragment.home.samecityka;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.view.home.samecityka.CardManageActivity;
import com.cn.uca.ui.view.home.samecityka.ExamineActivity;
import com.cn.uca.ui.view.home.samecityka.MyActionActivity;
import com.cn.uca.ui.view.home.samecityka.MyNewActivity;
import com.cn.uca.ui.view.home.samecityka.SettlementActivity;
import com.cn.uca.util.SetLayoutParams;
import com.google.zxing.client.android.CaptureActivity;

/**
 * Created by asus on 2017/12/5.
 * 发起者
 */

public class InitiatorFragment extends Fragment implements View.OnClickListener{

    private View view;
    private LinearLayout layout1,layout2,layout3,layout4,layout5,layout6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_initiator,null);

        initView();
        return view;
    }

    private void initView() {
        layout1 = (LinearLayout)view.findViewById(R.id.layout1);
        layout2 = (LinearLayout)view.findViewById(R.id.layout2);
        layout3 = (LinearLayout)view.findViewById(R.id.layout3);
        layout4 = (LinearLayout)view.findViewById(R.id.layout4);
        layout5 = (LinearLayout)view.findViewById(R.id.layout5);
        layout6 = (LinearLayout)view.findViewById(R.id.layout6);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        SetLayoutParams.setLinearLayout(layout1, MyApplication.width/2,MyApplication.width*3/8);
        SetLayoutParams.setLinearLayout(layout2, MyApplication.width/2,MyApplication.width*3/8);
        SetLayoutParams.setLinearLayout(layout3, MyApplication.width/2,MyApplication.width*3/8);
        SetLayoutParams.setLinearLayout(layout4, MyApplication.width/2,MyApplication.width*3/8);
        SetLayoutParams.setLinearLayout(layout5, MyApplication.width/2,MyApplication.width*3/8);
        SetLayoutParams.setLinearLayout(layout6, MyApplication.width/2,MyApplication.width*3/8);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout1:
                startActivity(new Intent(getActivity(), MyActionActivity.class));
                break;
            case R.id.layout2:
                startActivity(new Intent(getActivity(), MyNewActivity.class));
                break;
            case R.id.layout3:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.layout4:
                startActivity(new Intent(getActivity(), ExamineActivity.class));
                break;
            case R.id.layout5:
                startActivity(new Intent(getActivity(), SettlementActivity.class));
                break;
            case R.id.layout6:
                startActivity(new Intent(getActivity(), CardManageActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== -1){
            String result = data.getStringExtra("result");
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        }
    }
}

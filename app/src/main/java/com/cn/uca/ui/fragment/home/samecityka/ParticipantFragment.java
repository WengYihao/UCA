package com.cn.uca.ui.fragment.home.samecityka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.view.home.samecityka.MyFollowActivity;
import com.cn.uca.ui.view.home.samecityka.MyTicketActivity;
import com.cn.uca.util.SetLayoutParams;

/**
 * Created by asus on 2017/12/5.
 * 参与者
 */

public class ParticipantFragment extends Fragment implements View.OnClickListener{

    private View view;
    private LinearLayout layout1,layout2,layout3,layout4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_participan,null);

        initView();
        return view;
    }

    private void initView() {
        layout1 = (LinearLayout)view.findViewById(R.id.layout1);
        layout2 = (LinearLayout)view.findViewById(R.id.layout2);
        layout3 = (LinearLayout)view.findViewById(R.id.layout3);
        layout4 = (LinearLayout)view.findViewById(R.id.layout4);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        SetLayoutParams.setLinearLayout(layout1,MyApplication.width/2,MyApplication.width/2);
        SetLayoutParams.setLinearLayout(layout2,MyApplication.width/2,MyApplication.width/2);
        SetLayoutParams.setLinearLayout(layout3,MyApplication.width/2,MyApplication.width/2);
        SetLayoutParams.setLinearLayout(layout4,MyApplication.width/2,MyApplication.width/2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout1:
                startActivity(new Intent(getActivity(), MyTicketActivity.class));
                break;
            case R.id.layout2:
                startActivity(new Intent(getActivity(), MyFollowActivity.class));
                break;
            case R.id.layout3:
//                startActivity(new Intent(getActivity(), ReplyNewActivity.class));
                break;
        }
    }
}

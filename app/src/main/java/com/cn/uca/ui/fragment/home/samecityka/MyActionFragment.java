package com.cn.uca.ui.fragment.home.samecityka;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.uca.R;

/**
 * Created by asus on 2017/12/4.
 * 我的活动-同城咖
 */

public class MyActionFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TextView back, participant,initiator,name;
    private ParticipantFragment participantFragment;
    private InitiatorFragment initiatorFragment;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex = -1;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_action,null);

        initView();
        return view;
    }

    private void initView() {
        back = (TextView)view.findViewById(R.id.back);
        participant = (TextView)view.findViewById(R.id.participant);
        initiator = (TextView)view.findViewById(R.id.initiator);
        name = (TextView)view.findViewById(R.id.name);

        back.setOnClickListener(this);
        participant.setOnClickListener(this);
        initiator.setOnClickListener(this);
        show(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.participant://参与者
                show(0);
                initiator.setBackgroundResource(R.mipmap.initiator_normal);
                participant.setBackgroundResource(R.mipmap.participant_select);
                name.setText("参与者");
                break;
            case R.id.initiator://发起者
                show(1);
                initiator.setBackgroundResource(R.mipmap.participant_normal);
                participant.setBackgroundResource(R.mipmap.initiator_select);
                name.setText("发起者");
                break;
        }
    }
    private void show(int index) {
        if (currentIndex == index) {
            return;
        }
        fragmentTransaction = getFragmentManager()
                .beginTransaction();
        switch (index) {
            case 0:
                if (participantFragment == null) {
                    participantFragment = new ParticipantFragment();
                    fragmentTransaction.add(R.id.container2, participantFragment);
                }
                fragmentTransaction.show(participantFragment);
                break;
            case 1:
                if (initiatorFragment == null) {
                    initiatorFragment = new InitiatorFragment();
                    fragmentTransaction.add(R.id.container2, initiatorFragment);
                }
                fragmentTransaction.show(initiatorFragment);
                break;
        }
        switch (currentIndex) {
            case 0:
                fragmentTransaction.hide(participantFragment);
                break;
            case 1:
                fragmentTransaction.hide(initiatorFragment);
                break;
        }
        fragmentTransaction.commit();
        currentIndex = index;
    }
}

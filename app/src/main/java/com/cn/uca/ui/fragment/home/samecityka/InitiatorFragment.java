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

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.samecityka.CardManageActivity;
import com.cn.uca.ui.view.home.samecityka.ExamineActivity;
import com.cn.uca.ui.view.home.samecityka.MyActionActivity;
import com.cn.uca.ui.view.home.samecityka.SettlementActivity;
import com.cn.uca.ui.view.rongim.ChatListActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.zxing.CaptureActivity;

import java.util.HashMap;
import java.util.Map;

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
                if (SharePreferenceXutil.isSuccess()){
                    startActivity(new Intent(getActivity(), ChatListActivity.class));
                }else{
                    ToastXutil.show("请先登录");
                }
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
            checkTicket(result);
        }
    }

    private void checkTicket(String code){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("code",code);
        String sign = SignUtil.sign(map);
        HomeHttp.checkTicket(account_token, time_stamp, sign, code, new CallBack() {
            @Override
            public void onResponse(Object response) {
                switch ((int)response){
                    case 0:
                        ToastXutil.show("验票成功！");
                        break;
                    case 532:
                        ToastXutil.show("改门票已过期");
                        break;
                    case 534:
                        ToastXutil.show("票已使用");
                        break;
                    case 536:
                        ToastXutil.show("停止验票");
                        break;
                    case 537:
                        ToastXutil.show("活动当天开始验票");
                        break;
                    case 538:
                        ToastXutil.show("活动已关闭验票");
                        break;
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}

package com.cn.uca.ui.fragment.home.lvpai;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.SendContentbean;
import com.cn.uca.bean.home.lvpai.SendImgBean;
import com.cn.uca.impl.CallBackValue;
import com.cn.uca.ui.view.home.lvpai.LvPaiDetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/12/25.
 * 说明须知
 */

public class DetailedDescriptionFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TextView back,finish;
    private CallBackValue callBackValue;
    private LvPaiDetailActivity activity;
    private EditText content1,content2;
    private List<Object> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detailed_description,null);

        initView();
        return view;
    }

    private void initView() {
        back = (TextView)view.findViewById(R.id.back);
        content1 = (EditText)view.findViewById(R.id.content1);
        content2 = (EditText)view.findViewById(R.id.content2);
        finish = (TextView)view.findViewById(R.id.finish);

        back.setOnClickListener(this);
        finish.setOnClickListener(this);

        activity = (LvPaiDetailActivity)getActivity();
        list = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getFragmentManager().popBackStack();
                activity.setBack(2);
                break;
            case R.id.finish:
                SendContentbean bean = new SendContentbean();
                bean.setContent(content1.getText().toString());
                bean.setParagraph_type("p");
                SendContentbean bean2 = new SendContentbean();
                bean2.setContent(content2.getText().toString());
                bean2.setParagraph_type("p");
                list.add(bean);
                list.add(bean2);
                Gson gson = new Gson();
                callBackValue.sendMessage(4,gson.toJson(list));
                break;
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }
    protected void onAttachToContext(Context context) {
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();
    }
}

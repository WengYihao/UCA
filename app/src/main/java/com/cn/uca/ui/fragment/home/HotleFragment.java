package com.cn.uca.ui.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cn.uca.R;

/**
 * Created by asus on 2017/8/17.
 * 酒店
 */

public class HotleFragment extends Fragment{

    private View view;
    private EditText name,price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hotle,null);

        initView();
        return view;
    }

    private void initView() {
        name = (EditText)view.findViewById(R.id.name);
        price = (EditText)view.findViewById(R.id.price);
        SpannableString ss = new SpannableString("关键字/地名/品牌/酒店名");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        name.setHint(new SpannedString(ss));

        SpannableString aa = new SpannableString("价格/星级");//定义hint的值
        AbsoluteSizeSpan aass = new AbsoluteSizeSpan(14,true);//设置字体大小 true表示单位是sp
        ss.setSpan(aass, 0, aa.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        price.setHint(new SpannedString(ss));
    }

}

package com.cn.uca.popupwindows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.yueka.SearchCallBack;
import com.cn.uca.view.PickerScrollView;
import com.cn.uca.view.PickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/13.
 */

public class YuekaSearchPopupWindow implements View.OnClickListener{
    private PopupWindow popupWindow;
    private Context context;
    private View view;
    private SearchCallBack searchCallBack;

    public YuekaSearchPopupWindow(Context context,View view,SearchCallBack searchCallBack){
        this.context = context;
        this.view = view;
        this.searchCallBack = searchCallBack;
        yuekaSearchPopupwindow();
    }

    public  void yuekaSearchPopupwindow(){
        View show = LayoutInflater.from(context).inflate(R.layout.yueka_seach_popupwindow,null);
        final RadioButton man = (RadioButton)show.findViewById(R.id.man);
        final RadioButton woman = (RadioButton)show.findViewById(R.id.woman);
        final EditText search = (EditText)show.findViewById(R.id.search);
        TextView lable = (TextView) show.findViewById(R.id.lable);
        TextView submit = (TextView) show.findViewById(R.id.submit);
        final PickerScrollView minute_pv = (PickerScrollView)show.findViewById(R.id.minute_pv);
        final PickerScrollView second_pv = (PickerScrollView) show.findViewById(R.id.second_pv);
        minute_pv.setColor(context.getResources().getColor(R.color.white));
        second_pv.setColor(context.getResources().getColor(R.color.white));
        List<String> data = new ArrayList<>();
        for (int i = 18;i<56;i++){
            data.add(i+"岁");
        }
        minute_pv.setData(data,0);
        second_pv.setData(data);

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                man.setBackgroundResource(R.drawable.circular_ori_background);
                woman.setBackgroundResource(R.drawable.circular_gray_background);
            }
        });
        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                woman.setBackgroundResource(R.drawable.circular_ori_background);
                man.setBackgroundResource(R.drawable.circular_gray_background);
            }
        });

        lable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setVisibility(View.VISIBLE);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sexId = 0;
                String begAge,endAge,lable;
                if (man.isChecked()){
                    sexId = 1;
                } else if (woman.isChecked()){
                    sexId = 2;
                }
                if (search.getText().toString() != null){
                    lable = search.getText().toString();
                }else{
                    lable = "-";
                }
                begAge = minute_pv.getBegAge();
                endAge = second_pv.getEndAge();
                searchCallBack.onCallBack(sexId,begAge,endAge,lable);
                popupWindow.dismiss();
            }
        });

        minute_pv.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(String pickers) {
                minute_pv.setBegAge(pickers);
            }
        });
        second_pv.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(String pickers) {
                second_pv.setEndAge(pickers);
            }
        });
        popupWindow = new PopupWindow(show, MyApplication.width,MyApplication.height*4/7);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
        popupWindow.showAsDropDown(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
//                Observable.just(1).subscribe(MainActivity.action1);
                break;
        }
    }
}

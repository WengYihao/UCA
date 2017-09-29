package com.cn.uca.popupwindows;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cn.uca.R;
import com.cn.uca.adapter.ShowAdapter;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.yueka.OnSelectItemListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/9/28.
 */

public class LinePopupWindow {

    private PopupWindow popupWindow;
    private Context context;
    private OnSelectItemListener listener;
    private Map<Integer,String> map;
    private List<String> lineName = new ArrayList<>();
    private List<Integer> lineid = new ArrayList<>();
    private View view;

    public void setOnSelectItemListener(OnSelectItemListener listener){
            this.listener = listener;
}

    public LinePopupWindow(Context context, Map<Integer,String> map, View view){
        this.context = context;
        this.map = map;
        this.view = view;
        initPopupWindow();
    }

    public void initPopupWindow(){
        for (String name :map.values()){
            lineName.add(name);
        }
        for (Integer id :map.keySet()){
            lineid.add(id);
        }
        View show = LayoutInflater.from(context).inflate(R.layout.show_list, null);
        ListView listView = (ListView)show.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listener != null){
                    listener.selectItem(lineName.get(i),lineid.get(i));
                }
            }
        });

        ShowAdapter showAdapter = new ShowAdapter(lineName,context);

        listView.setAdapter(showAdapter);

        popupWindow = new PopupWindow(show, MyApplication.width/3,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAsDropDown(view);
    }
}

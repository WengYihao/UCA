package com.cn.uca.ui.fragment.home.raider;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.raiders.RaidersCollectionAdapter;
import com.cn.uca.adapter.home.raiders.RaidersLookAdapter;
import com.cn.uca.bean.home.raider.RaidersBean;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.db.DatabaseHelper;
import com.cn.uca.impl.raider.LookBack;
import com.cn.uca.impl.yueka.CollectionClickListener;
import com.cn.uca.ui.fragment.home.BaseFragment;
import com.cn.uca.ui.view.home.raider.RaidersDetailActivity;
import com.cn.uca.util.ToastXutil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/3/8.
 * 攻略-收藏
 */

public class GLLookFragment extends BaseFragment implements LookBack,View.OnClickListener{

    private View view;
    private GridView gridView;
    private List<RaidersBean> list;
    private RaidersLookAdapter adapter;
    private RefreshLayout refreshLayout;
    private DatabaseHelper helper;
    private boolean isPrepared;
    private LinearLayout layout;
    private TextView title01,title02;
    private List<String> listStr;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_look_gl,null);

        isPrepared = true;
        setlazyLoad();
        return view;
    }

    @Override
    protected void setlazyLoad() {
        super.setlazyLoad();
        if(!isPrepared || !isVisible) {
            return;
        }
        initView();
    }

    public void aa(){
        for (RaidersBean bean : list){
            bean.setSelect(1);
        }
        adapter.setList(list);
        layout.setVisibility(View.VISIBLE);
    }
    public void bb(){
        for (RaidersBean bean : list){
            bean.setSelect(3);
        }
        adapter.setList(list);
        layout.setVisibility(View.GONE);
    }
    public void cc(){
        layout.setVisibility(View.GONE);
    }
    private void initView() {
        helper = new DatabaseHelper(getActivity());
        listStr = new ArrayList<>();
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        gridView = (GridView)view.findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new RaidersLookAdapter(list,getActivity(),this);
        gridView.setAdapter(adapter);

        layout = (LinearLayout)view.findViewById(R.id.layout);
        title01 = (TextView)view.findViewById(R.id.title01);
        title02 = (TextView)view.findViewById(R.id.title02);

        title01.setOnClickListener(this);
        title02.setOnClickListener(this);

        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list = getCityList();
                        adapter.setList(list);
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 500);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshlayout.finishLoadmore();
                    }
                }, 500);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),RaidersDetailActivity.class);
                intent.putExtra("id",list.get(position).getCity_raiders_id());
                intent.putExtra("cityName",list.get(position).getCity_name());
                startActivity(intent);
            }
        });
    }

    /**
     * 获取历史查看的城市
     * @return
     */
    private ArrayList<RaidersBean> getCityList() {
        ArrayList<RaidersBean> list = new ArrayList<>();
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from lookHistory", null);
            RaidersBean bean;
            while (cursor.moveToNext()) {
                bean = new RaidersBean();
                bean.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
                bean.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                bean.setCity_raiders_id(cursor.getInt(cursor.getColumnIndex("city_raiders_id")));
                if (cursor.getInt(cursor.getColumnIndex("collection")) == 0){
                    bean.setCollection(false);
                }else{
                    bean.setCollection(true);
                }
                if (cursor.getInt(cursor.getColumnIndex("lock")) == 0){
                    bean.setLock(false);
                }else{
                    bean.setLock(true);
                }
                bean.setPacture_url(cursor.getString(cursor.getColumnIndex("pacture_url")));
                bean.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                list.add(bean);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("456",e.getMessage()+"报错了");
        }
        return list;
    }

    //删除查看记录
    private void deleteList(List<String> list){
        try{
            SQLiteDatabase db = helper.getReadableDatabase();
            for (String str : list){
                db.delete("lookHistory", "city_name = ?", new String[] { str});
            }
            this.list.clear();
            this.list = getCityList();
            if (list.size() != 0){
                for (RaidersBean bean : this.list){
                    bean.setSelect(1);
                }
                adapter.setList(this.list);
            }
        }catch (Exception e){
           ToastXutil.show("操作失败");
        }

    }
    @Override
    public void lookBack(View v) {
        if (list.get((int) v.getTag()).getSelect() == 2){
            list.get((int) v.getTag()).setSelect(1);
//            adapter.setList(list);
        }else{
            list.get((int) v.getTag()).setSelect(2);
        }
        for (RaidersBean bean : list){
            Log.e("456",bean.getSelect()+"-");
        }
        adapter.setList(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title01:
                if (list.size() > 0){
                    if (listStr.size() > 0){
                        listStr.clear();
                    }
                    for (RaidersBean bean : list){
                        listStr.add(bean.getCity_name());
                    }
                    if (listStr.size() != 0){
                        deleteList(listStr);
                    }else{
                        ToastXutil.show("暂无操作记录");
                    }
                }else{
                    ToastXutil.show("暂无查看记录");
                }

                break;
            case R.id.title02:
                if (list.size() > 0){
                    if (listStr.size() > 0){
                        listStr.clear();
                    }
                    for (RaidersBean bean : list){
                        if (bean.getSelect() == 2){
                            listStr.add(bean.getCity_name());
                        }
                    }
                    if (listStr.size() != 0){
                        deleteList(listStr);
                    }else{
                        ToastXutil.show("请选择要删除的记录");
                    }
                }else{
                    ToastXutil.show("暂无查看记录");
                }
                break;
        }
    }
}

package com.cn.uca.ui.yueka;

import android.content.Intent;
import android.os.Parcelable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.EditLineAdapter;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.bean.yueka.YueKaLineBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.yueka.ItemClickListener;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;
import com.cn.uca.view.RdListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PresetManagerActivity extends BaseBackActivity implements View.OnClickListener,ItemClickListener {

    private TextView back,add;
    private RdListView listView;
    private List<YueKaLineBean> listData;
    private EditLineAdapter editLineAdapter;
    private int index;
    private List<PlacesBean> listPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_preset_manager);

        initView();
        getAllLine();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        listView = (RdListView) findViewById(R.id.listView);
        add = (TextView) findViewById(R.id.add);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        listData = new ArrayList<>();
        listPlaces = new ArrayList<>();
        editLineAdapter = new EditLineAdapter(listData,PresetManagerActivity.this,this);
        listView.setAdapter(editLineAdapter);
        SetListView.setListViewHeightBasedOnChildren(listView);
        listView.setDelButtonClickListener(new RdListView.DelButtonClickListener() {
            @Override
            public void clickHappend(final int position) {
                index = position;
                deleteLine(listData.get(position).getRoute_id());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastXutil.show(editLineAdapter.getItem(position) + "");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.add:
                addLine();
                break;
        }
    }

    /**
     * 获取所有路线
     */
    private void getAllLine(){
        YueKaHttp.getAllLine(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<YueKaLineBean> bean = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<YueKaLineBean>>() {
                            }.getType());
                            listData.addAll(bean);
                            editLineAdapter.setList(listData);
                            break;
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                ToastXutil.show(errorMsg.toString());
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    /**
     * 添加路线
     */
    private void addLine() {
        YueKaHttp.addLine(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    YueKaLineBean bean = new YueKaLineBean();
                    bean.setRoute_id(jsonObject.getInt("route_id"));
                    bean.setRoute_name(jsonObject.getString("route_name"));
                    bean.setEscort_id(jsonObject.getInt("escort_id"));
                    ArrayList<PlacesBean> list = new ArrayList<PlacesBean>();
                    bean.setPlaces(list);
                    listData.add(bean);
                    editLineAdapter.setList(listData);
                    ToastXutil.show("添加路线成功");
                }catch (Exception e){
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                ToastXutil.show(errorMsg.toString());
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    /**
     * 删除路线
     * @param id
     */
    private void deleteLine(int id){
        YueKaHttp.deleteLine(id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                if ((int)response == 0){
                    listData.remove(index);
                    editLineAdapter.setList(listData);
                    ToastXutil.show("删除路线成功");
                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                ToastXutil.show(errorMsg);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    /**
     * 显示/隐藏路线点
     * @param v
     */
    @Override
    public void clickShow(View v) {
        LinearLayout layout = (LinearLayout)listView.getChildAt((int) v.getTag());// 获得子item的layout
        ListView et = (ListView) layout.findViewById(R.id.listView);// 从layout中获得控件,根据其id
        TextView te = (TextView) layout.findViewById(R.id.arrowView);
        if (listData.get((int) v.getTag()).getPlaces() != null){
            if (listData.get((int) v.getTag()).getPlaces().size() != 0){
                switch (et.getVisibility()){
                    case 0://visible
                        et.setVisibility(View.GONE);
                        te.setBackgroundResource(R.mipmap.right_gray);
                        break;
                    case 8://gone
                        et.setVisibility(View.VISIBLE);
                        te.setBackgroundResource(R.mipmap.down);
                        break;
                }
            }else{
                ToastXutil.show("没有更多的数据，赶紧去添加吧！");
            }
        }else{
            ToastXutil.show("没有更多的数据，赶紧去添加吧！");
        }
    }

    /**
     * 修改路线名
     * @param v
     */
    @Override
    public void clickTrue(View v) {
        LinearLayout layout = (LinearLayout)listView.getChildAt((int) v.getTag());// 获得子item的layout
        MyEditText editText = (MyEditText)layout.findViewById(R.id.lineName);

        final String routeName = editText.getText().toString().trim();
        final int id = listData.get((int) v.getTag()).getRoute_id();
        YueKaHttp.updateLineName(id,routeName, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString());
                try {
                    if ((int)response == 0){
                        ToastXutil.show("修改成功");
                        for (int i = 0;i<listData.size();i++){
                            if (listData.get(i).getRoute_id() == id){
                                listData.get(i).setRoute_name(routeName);
                                editLineAdapter.setList(listData);
                            }
                        }
                    }
                }
                catch (Exception e){
                    Log.i("456",e.getMessage()+"//*/");
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

    /**
     * 编辑路线点
     * @param v
     */
    @Override
    public void clickEdit(View v) {
        Intent intent = new Intent();
        intent.setClass(this, LineChoiceActivity.class);
        intent.putExtra("id",listData.get((int) v.getTag()).getRoute_id());
//        intent.putExtra("size",listData.get((int) v.getTag()).getPlaces().size());
        intent.putParcelableArrayListExtra("places",(ArrayList<? extends Parcelable>) listData.get((int) v.getTag()).getPlaces());
        startActivityForResult(intent,0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (data != null){
                List<PlacesBean> list;
                listPlaces = data.getParcelableArrayListExtra("list");
                int route_id = data.getIntExtra("id",0);
                for (YueKaLineBean yueKaLineBean : listData) {
                    if(yueKaLineBean.getRoute_id() == route_id){
                        // 原数据
                        list = yueKaLineBean.getPlaces();
                        for (PlacesBean placesBean : listPlaces) {
                            list.add(placesBean);
                        }
                        editLineAdapter.setList(listData);
                        break;
                    }
                }
            }
        }
    }
}

package com.cn.uca.ui.view.yueka;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.yueka.EditLineAdapter;
import com.cn.uca.adapter.yueka.LinePointAdapter;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.bean.yueka.YueKaLineBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.lvpai.AddAlbumBack;
import com.cn.uca.impl.yueka.ItemClickListener;
import com.cn.uca.impl.yueka.LinePointCallBack;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.util.lmxListviewHelper;
import com.cn.uca.view.MyEditText;
import com.cn.uca.view.RdListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PresetManagerActivity extends BaseBackActivity implements View.OnClickListener,ItemClickListener,LinePointCallBack,AddAlbumBack{

    private TextView back,add;
    private RdListView listView;
    private List<YueKaLineBean> listData;
    private EditLineAdapter editLineAdapter;
    private int index;
    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_manager);

        initView();
    }

    private void initView() {
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        back = (TextView)findViewById(R.id.back);
        listView = (RdListView) findViewById(R.id.listView);
        add = (TextView) findViewById(R.id.add);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        listData = new ArrayList<>();
        editLineAdapter = new EditLineAdapter(listData,PresetManagerActivity.this,this,this);
        listView.setAdapter(editLineAdapter);
        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                        getAllLine();
                    }
                }, 1000);
            }
        });
       refreshLayout.setEnableLoadmore(false);
        //触发自动刷新
        refreshLayout.autoRefresh();

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
                addresspopuWindow(this);
                break;
        }
    }

    private void addresspopuWindow(final AddAlbumBack back){
        View inflate = LayoutInflater.from(this).inflate(R.layout.lvpai_addpic_dialog, null);
        TextView title = (TextView)inflate.findViewById(R.id.title);
        final EditText albumname = (EditText)inflate.findViewById(R.id.albumname);
        TextView positiveButton = (TextView)inflate.findViewById(R.id.positiveButton);//确定
        TextView negativeButton = (TextView)inflate.findViewById(R.id.negativeButton);//取消
        title.setText("添加路线");
        albumname.setHint("请输入路线方案名称");
        final PopupWindow popupWindow = new PopupWindow(inflate, MyApplication.width,
                MyApplication.height/3, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (albumname.getText().toString() != null){
                    back.determine(albumname.getText().toString());
                    popupWindow.dismiss();
                }else{
                    ToastXutil.show("相册名不能为空");
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void updateresspopuWindow(final int id,final String name){
        View inflate = LayoutInflater.from(this).inflate(R.layout.lvpai_addpic_dialog, null);
        TextView title = (TextView)inflate.findViewById(R.id.title);
        final EditText albumname = (EditText)inflate.findViewById(R.id.albumname);
        albumname.setText(name);
        TextView positiveButton = (TextView)inflate.findViewById(R.id.positiveButton);//确定
        TextView negativeButton = (TextView)inflate.findViewById(R.id.negativeButton);//取消
        title.setText("修改路线名");
        final PopupWindow popupWindow = new PopupWindow(inflate, MyApplication.width,
                MyApplication.height/3, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (albumname.getText().toString() != null){
                    YueKaHttp.updateLineName(id,albumname.getText().toString(), new CallBack() {
                        @Override
                        public void onResponse(Object response) {
                            Log.i("123",response.toString());
                            try {
                                if ((int)response == 0){
                                    ToastXutil.show("修改成功");
                                    popupWindow.dismiss();
                                    refreshLayout.autoRefresh();
                                }
                            }
                            catch (Exception e){
                                Log.i("456",e.getMessage()+"//*/");
                            }
                        }

                        @Override
                        public void onErrorMsg(String errorMsg) {
                            popupWindow.dismiss();
                            ToastXutil.show(errorMsg);
                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });

                }else{
                    ToastXutil.show("路线名不能为空");
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
                            if (bean.size() > 0){
                                listData.clear();
                                listData.addAll(bean);
                                editLineAdapter.setList(listData);
                            }
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
    private void addLine(String name) {
        YueKaHttp.addLine(name,new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            YueKaLineBean bean = new YueKaLineBean();
                            bean.setRoute_id(jsonObject.getJSONObject("data").getInt("route_id"));
                            bean.setRoute_name(jsonObject.getJSONObject("data").getString("route_name"));
                            bean.setEscort_id(jsonObject.getJSONObject("data").getInt("escort_id"));
                            ArrayList<PlacesBean> list = new ArrayList<PlacesBean>();
                            bean.setPlaces(list);
                            listData.add(bean);
                            editLineAdapter.setList(listData);
                            ToastXutil.show("添加路线成功");
                            break;
                    }

                }catch (Exception e){
                    Log.e("456",e.getMessage());
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
        String routeName = listData.get((int) v.getTag()).getRoute_name();
        int id = listData.get((int) v.getTag()).getRoute_id();
        updateresspopuWindow(id,routeName);
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
           refreshLayout.autoRefresh();
        }
    }

    @Override
    public void changPoint(ListView listView, final LinePointAdapter adapter, final List<PlacesBean> list) {
        lmxListviewHelper helper = new lmxListviewHelper(listView) {
            @Override
            public void changeItemPosition(int p1, int p2) {
//                adapter = new LinePointAdapter()
                PlacesBean bean = list.get(p2);
                list.set(p2,list.get(p1));
                list.set(p1,bean);
                adapter.setList(list);
            }

            @Override
            public void resetListview() {
                adapter.setList(list);
            }
        };
        helper.enableChangeItems(true);
    }

    @Override
    public void determine(String name) {
        addLine(name);
    }
}

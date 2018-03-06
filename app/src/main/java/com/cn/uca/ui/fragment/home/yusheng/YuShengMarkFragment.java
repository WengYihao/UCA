package com.cn.uca.ui.fragment.home.yusheng;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.yusheng.YuShengMarkAdapter;
import com.cn.uca.bean.home.yusheng.LifeHistoricalsBean;
import com.cn.uca.bean.home.yusheng.YushengMarkBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/10/25.
 */

public class YuShengMarkFragment extends Fragment {
    private View view;
    private SmartRefreshLayout refreshLayout;
    private ListView listView;
    private YuShengMarkAdapter markAdapter;
    private List<LifeHistoricalsBean> list;
    private int page = 1;
    private Dialog dialog;
    private View inflate;
    private TextView update,look,delete,btn_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mark_yusheng,null);
        
        initView();
        return view;
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        listView = (ListView)view.findViewById(R.id.listView);

        list = new ArrayList<>();
        markAdapter = new YuShengMarkAdapter(list,getActivity());
        listView.setAdapter(markAdapter);

//        getLifeHistorical(1);

        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLifeHistorical(page);
                        refreshlayout.finishRefresh();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                }, 1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getLifeHistorical(page);
                        refreshlayout.finishLoadmore();
                    }
                }, 1000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                show(list.get(position).getHistorical_id(),list.get(position).getRemaining_time(),list.get(position).getType(),list.get(position).getContent());

            }
        });
    }

    private void delete(int id,String type){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("historical_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("type",type);
        String sign = SignUtil.sign(map);
        HomeHttp.deleteLife(sign,time_stamp, account_token, id,type, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    switch ((int)response){
                        case 0:
                            ToastXutil.show("删除成功");
                            getLifeHistorical(page);
                            break;
                        default:
                            ToastXutil.show("删除失败");
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage()+"--");
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

    private void show(final int id,final String time,final String type,final String content){
        dialog = new Dialog(getActivity(),R.style.dialog_style);
        //填充对话框的布局
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.update_yusheng_dialog, null);
        //初始化控件
        update = (TextView) inflate.findViewById(R.id.update);
        look = (TextView)inflate.findViewById(R.id.look);
        delete = (TextView) inflate.findViewById(R.id.detele);
        btn_cancel = (TextView)inflate.findViewById(R.id.btn_cancel);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switch (type){
                    case "day":
                        ShowPopupWindow.dayPopupwindow(view,getActivity(),Integer.parseInt(time),"day",2,content);
                        break;
                    case "systemDay":

                        break;
                    case "month":
                        ShowPopupWindow.dayPopupwindow(view,getActivity(),Integer.parseInt(time),"month",2,content);
                        break;
                }

            }
        });
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switch (type){
                    case "day":
                        ShowPopupWindow.dayPopupwindow(view,getActivity(),Integer.parseInt(time),"day",3,content);
                        break;
                    case "systemDay":

                        break;
                    case "month":
                        ShowPopupWindow.dayPopupwindow(view,getActivity(),Integer.parseInt(time),"month",3,content);
                        break;
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(id, type);
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialogWindow.setAttributes(params);
        StatusMargin.setFrameLayoutBottom(getActivity(),inflate,0);
        dialog.show();//显示对话框
    }
    private void getLifeHistorical(int page){
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String sign = SignUtil.sign(map);
        HomeHttp.getLifeHistorical(sign, time_stamp, account_token, page, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            YushengMarkBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<YushengMarkBean>() {
                            }.getType());
                            if (bean.getLifeHistoricals().size() > 0) {
                                list.clear();
                                list.addAll(bean.getLifeHistoricals());
                                markAdapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
                                }else{
                                    markAdapter.setList(list);
                                }
                            }
                            break;
                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage()+"--");
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

package com.cn.uca.ui.view.home.lvpai;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.CommodityAdapter;
import com.cn.uca.adapter.home.raiders.SpotNameAdapter;
import com.cn.uca.bean.home.lvpai.MerchantCommodityBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.ServiceBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.samecityka.SameCityKaActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantCommodityActivity extends BaseBackActivity implements View.OnClickListener,ServiceBack{

    private TextView title01,title02,back,more,add_new_commodity,more_operation,isHotspot;
    private LinearLayout layout1,layout2,layout3,layout4,layout5,layout6,layout7,layout8;
    private PopupWindow popupWindow;
    private Dialog dialog,dialog2;
    private ListView listView;
    private CommodityAdapter adapter;
    private RefreshLayout refreshLayout;
    private List<MerchantCommodityBean> list;
    private int pageSale = 1;
    private int pageCountSale = 5;
    private int pageUp = 1;
    private int pageCountUp = 5;
    private int type = 1;
    private int shoodId;
    private boolean hotspot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_commodity);

        initView();
        getMyTs("sale",pageSale,pageCountSale);
    }

    private void initView() {
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        back = (TextView)findViewById(R.id.back);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        more = (TextView)findViewById(R.id.more);
        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new CommodityAdapter(list,this);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
        more.setOnClickListener(this);
        title01.setOnClickListener(this);
        title02.setOnClickListener(this);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (type){
                            case 1:
                                getMyTs("sale",pageSale,pageCountSale);
                                refreshlayout.finishRefresh();
                                refreshlayout.setLoadmoreFinished(false);
                                break;
                            case 2:
                                getMyTs("unsold",pageUp,pageCountUp);
                                refreshlayout.finishRefresh();
                                refreshlayout.setLoadmoreFinished(false);
                                break;
                        }

                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (type){
                            case 1:
                                pageSale ++ ;
                                getMyTs("sale",pageSale,pageCountSale);
                                refreshlayout.finishRefresh();
                                refreshlayout.setLoadmoreFinished(false);
                                break;
                            case 2:
                                pageUp ++;
                                getMyTs("unsold",pageUp,pageCountUp);
                                refreshlayout.finishRefresh();
                                refreshlayout.setLoadmoreFinished(false);
                                break;
                        }
                        refreshlayout.finishLoadmore();
                    }
                }, 2000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
                    case 1:
                        shoodId = list.get(position).getTrip_shoot_id();
                        hotspot = list.get(position).isHotspot();
                        showMore();
                        break;
                    case 2:
                        showUpsold();
                        shoodId = list.get(position).getTrip_shoot_id();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.more:
                show();
                break;
            case R.id.title01:
                type = 1;
                title01.setTextColor(getResources().getColor(R.color.ori));
                title02.setTextColor(getResources().getColor(R.color.black));
                list.clear();
                getMyTs("sale",1,pageCountSale*pageSale);
                break;
            case R.id.title02:
                type = 2;
                title01.setTextColor(getResources().getColor(R.color.black));
                title02.setTextColor(getResources().getColor(R.color.ori));
                list.clear();
                getMyTs("unsold",1,pageCountUp*pageUp);

                break;
            case R.id.add_new_commodity:
                popupWindow.dismiss();
                String str = "http://www.szyouka.com:8080/youkatravel/agreement/tripShootMerchantProtocol.html";
                ShowPopupWindow.seviceWindow(getWindow().getDecorView(),MerchantCommodityActivity.this,str,MerchantCommodityActivity.this);
                break;
            case R.id.more_operation:
                popupWindow.dismiss();
                ToastXutil.show("还有啥骚操作？");
                break;
            case R.id.layout1:
                Intent intent = new Intent();
                intent.setClass(MerchantCommodityActivity.this,MerchantOrderActivity.class);
                intent.putExtra("id",shoodId);
                startActivity(intent);
                dialog.dismiss();
                break;
            case R.id.layout2:
                hotspot();
                dialog.dismiss();
                break;
            case R.id.layout3:
                ToastXutil.show("编辑");
                dialog.dismiss();
                break;
            case R.id.layout4:
                upLoShelves();
                dialog.dismiss();
                break;
            case R.id.layout5:
                ToastXutil.show("下架的订单");
                dialog2.dismiss();
                break;
            case R.id.layout6:
                ToastXutil.show("删除");
                dialog2.dismiss();
                break;
            case R.id.layout7:
                ToastXutil.show("编辑");
                dialog2.dismiss();
                break;
            case R.id.layout8:
                ToastXutil.show("重新上架");
                dialog2.dismiss();
                upLoShelves();
                break;
        }
    }
    private void showMore(){
        dialog = new Dialog(this,R.style.dialog_style);
        View inflate = LayoutInflater.from(this).inflate(R.layout.lvpai_manager_dialog, null);
        isHotspot = (TextView)inflate.findViewById(R.id.isHotspot);
        if (hotspot){
            isHotspot.setBackgroundResource(R.mipmap.lvpai_recommend_red_back);
        }else{
            isHotspot.setBackgroundResource(R.mipmap.lvpai_recommend_ori_back);
        }
        layout1 = (LinearLayout)inflate.findViewById(R.id.layout1);
        layout1.setOnClickListener(this);
        layout2 = (LinearLayout)inflate.findViewById(R.id.layout2);
        layout2.setOnClickListener(this);
        layout3 = (LinearLayout)inflate.findViewById(R.id.layout3);
        layout3.setOnClickListener(this);
        layout4 = (LinearLayout)inflate.findViewById(R.id.layout4);
        layout4.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
//        params.dimAmount = 0f;
        params.width = MyApplication.width;
        params.height = MyApplication.width/2;
        dialog.show();//显示对话框
    }
    private void showUpsold(){
        dialog2 = new Dialog(this,R.style.dialog_style);
        View inflate = LayoutInflater.from(this).inflate(R.layout.lvpai_manager_dialog2, null);
        layout5 = (LinearLayout)inflate.findViewById(R.id.layout5);
        layout5.setOnClickListener(this);
        layout6 = (LinearLayout)inflate.findViewById(R.id.layout6);
        layout6.setOnClickListener(this);
        layout7 = (LinearLayout)inflate.findViewById(R.id.layout7);
        layout7.setOnClickListener(this);
        layout8 = (LinearLayout)inflate.findViewById(R.id.layout8);
        layout8.setOnClickListener(this);
        //将布局设置给Dialog
        dialog2.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog2.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        params.height = MyApplication.width/2;
        dialog2.show();//显示对话框
    }
    private void show(){
		View show = LayoutInflater.from(this).inflate(R.layout.lvpai_commodity_dialog, null);
        add_new_commodity = (TextView)show.findViewById(R.id.add_new_commodity);//添加新品
        more_operation = (TextView)show.findViewById(R.id.more_operation);//批量操作
        add_new_commodity.setOnClickListener(this);
        more_operation.setOnClickListener(this);
		popupWindow = new PopupWindow(show, MyApplication.width/3,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.RIGHT|Gravity.TOP,20,50);
	}
	private void getMyTs(String state,int page,int pageCount){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("page",page);
        map.put("pageCount",pageCount);
        map.put("state",state);
        String sign = SignUtil.sign(map);
        HomeHttp.getMyTs(time_stamp, sign, account_token, page, pageCount, state, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<MerchantCommodityBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("tripShoot").toString(),new TypeToken<List<MerchantCommodityBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.clear();
                                list.addAll(bean);
                                adapter.setList(list);
                            }else{
                                if (list.size() != 0){
                                    ToastXutil.show("没有更多数据了");
                                }else{
                                    adapter.setList(list);
                                }
                            }
                            break;
                    }
                }catch (Exception e){

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
     * 推荐/取消推荐
     */
    private void hotspot(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("trip_shoot_id",shoodId);
        String sign = SignUtil.sign(map);
        HomeHttp.hotspot(time_stamp, sign, account_token, shoodId, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            if (hotspot){
                                hotspot = false;
                                isHotspot.setBackgroundResource(R.mipmap.lvpai_recommend_ori_back);
                                for (int i= 0;i<list.size();i++){
                                    if (list.get(i).getTrip_shoot_id() == shoodId){
                                        list.get(i).setHotspot(false);
                                    }
                                }
                                ToastXutil.show("取消推荐");
                            }else {
                                hotspot = true;
                                isHotspot.setBackgroundResource(R.mipmap.lvpai_recommend_red_back);
                                for (int i= 0;i<list.size();i++){
                                    if (list.get(i).getTrip_shoot_id() == shoodId){
                                        list.get(i).setHotspot(true);
                                    }
                                }
                                ToastXutil.show("推荐成功");
                            }
                            break;
                        case 660:
                            ToastXutil.show("最多只能设置两种商品");
                            break;
                    }
                }catch (Exception e){

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
     * 上/下架
     */
    private void upLoShelves(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("trip_shoot_id",shoodId);
        String sign = SignUtil.sign(map);
        HomeHttp.upLoShelves(time_stamp, sign, account_token, shoodId, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            switch (type){
                                case 1:
                                    ToastXutil.show("下架成功");
                                    getMyTs("sale",1,pageCountSale*pageCountSale);
                                    break;
                                case 2:
                                    ToastXutil.show("上架成功");
                                    getMyTs("unsold",1,pageCountUp*pageUp);
                                    break;
                            }

                            break;
                    }
                }catch (Exception e){

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

    @Override
    public void sure() {
        startActivity(new Intent(MerchantCommodityActivity.this,AddMerchantActivity.class));
    }
}

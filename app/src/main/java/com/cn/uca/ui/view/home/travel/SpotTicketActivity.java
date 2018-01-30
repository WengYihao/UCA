package com.cn.uca.ui.view.home.travel;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.SpotTicketAdapter;
import com.cn.uca.bean.home.travel.SpotTicketBean;
import com.cn.uca.bean.home.travel.TicketBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.travel.SpotTicketItem;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.WebViewActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.NoScrollListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SpotTicketActivity extends BaseBackActivity implements View.OnClickListener,SpotTicketItem{

    private int id;
    private TextView back,name,address,introduce,content_num;
    private ImageView pic,spot_pic;
    private RelativeLayout spot_content;
    private List<TicketBean> list;
    private SpotTicketAdapter adapter;
    private NoScrollListView ticket_listview;
    private SpotTicketBean bean;
    private Dialog orderDialog;//推购票弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_ticket);
        initView();
        getInfo();
        getTicket();
    }

    private void getInfo(){
        Intent in = getIntent();
        if (in != null){
            id = in.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        name = (TextView)findViewById(R.id.name);
        address = (TextView)findViewById(R.id.address);
        introduce = (TextView)findViewById(R.id.introduce);
        content_num = (TextView)findViewById(R.id.content_num);
        spot_content = (RelativeLayout)findViewById(R.id.spot_content);
        pic = (ImageView)findViewById(R.id.pic);
        spot_pic = (ImageView)findViewById(R.id.spot_pic);
        ticket_listview = (NoScrollListView)findViewById(R.id.ticket_listview);
        back.setOnClickListener(this);
        address.setOnClickListener(this);
        introduce.setOnClickListener(this);
        spot_content.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new SpotTicketAdapter(list,getApplicationContext(),this);
        ticket_listview.setAdapter(adapter);
    }

    private void getTicket(){
        Map<String,Object> map = new HashMap<>();
        map.put("scenic_spot_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getTicket(id, sign, time_stamp, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            Gson gson = new Gson();
                            bean = gson.fromJson(object.toString(),new TypeToken<SpotTicketBean>(){}.getType());
                            Glide.with(SpotTicketActivity.this)
                                    .load(bean.getPicture_url())
                                    .into(spot_pic);
                            Glide.with(SpotTicketActivity.this).load(bean.getPicture_url()).bitmapTransform(new BlurTransformation(SpotTicketActivity.this, 20)).into(pic);
                            name.setText(bean.getScenic_spot_name());
                            address.setText(bean.getAddress());
                            content_num.setText(bean.getEvaluation_quantity()+"条评论");
                            JSONArray array = object.getJSONArray("ticketRets");
                            List<TicketBean> listBean = gson.fromJson(array.toString(),new TypeToken<List<TicketBean>>() {
                            }.getType());
                            list.addAll(listBean);
                            adapter.setList(list);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.introduce:
                Intent intent = new Intent();
                intent.setClass(SpotTicketActivity.this, WebViewActivity.class);
                intent.putExtra("type","spotintroduction");
                intent.putExtra("url",bean.getIntroduce_url());
                startActivity(intent);
                break;
            case R.id.spot_content:
                ToastXutil.show("看什么简介，看我不好吗？");
                break;
        }
    }
//String name,String time,String url,double price
    private void showWindow(final int id, final String name, String time, String url, final double price){
        orderDialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
        View inflateLable = LayoutInflater.from(this).inflate(R.layout.travel_order_dialog, null);
        TextView nameTv = (TextView)inflateLable.findViewById(R.id.name);
        TextView closeTv = (TextView)inflateLable.findViewById(R.id.close);
        TextView timeTv = (TextView)inflateLable.findViewById(R.id.time);
        WebView webView = (WebView)inflateLable.findViewById(R.id.webView);
        TextView priceTv = (TextView)inflateLable.findViewById(R.id.price);
        TextView config = (TextView)inflateLable.findViewById(R.id.config);
        nameTv.setText(name);
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDialog.dismiss();
            }
        });
        timeTv.setText(time);
        priceTv.setText("￥"+(int)price);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SpotTicketActivity.this,TravelOrderActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("price",(int) price+"");
                startActivity(intent);
                orderDialog.dismiss();
            }
        });
        orderDialog.setContentView(inflateLable);
        //获取当前Activity所在的窗体
        Window dialogWindow = orderDialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        params.height = MyApplication.height*4/5;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialogWindow.setAttributes(params);
        StatusMargin.setFrameLayoutBottom(SpotTicketActivity.this,inflateLable,0);
        orderDialog.show();//显示对话框
    }

    //预定须知
    @Override
    public void ReserveBack(View v) {
        Intent intent = new Intent();
        intent.setClass(SpotTicketActivity.this, WebViewActivity.class);
        intent.putExtra("type","reserveintroduction");
        intent.putExtra("url",list.get((int)v.getTag()).getOrder_notice_url());
        startActivity(intent);
    }

    //订票
    @Override
    public void OrderBack(View v) {
        int id = list.get((int)v.getTag()).getAdmission_ticket_id();
        String name = list.get((int)v.getTag()).getScenic_name();
        String url = list.get((int)v.getTag()).getOrder_notice_url();
        String time = list.get((int)v.getTag()).getScheduled_time();
        double price = list.get((int)v.getTag()).getPrice_customer();
        showWindow(id,name,time,url,price);
    }
}

package com.cn.uca.ui.view.user.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.user.SameCityKaOrderTicketAdapter;
import com.cn.uca.bean.home.samecityka.MyTicketCodeBean;
import com.cn.uca.bean.home.samecityka.SameCityKaOrderBean;
import com.cn.uca.bean.user.order.SameCityKaOrderInfo;
import com.cn.uca.bean.user.order.YueKaOrderInfo;
import com.cn.uca.config.Constant;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.user.PayBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.secretkey.Base64;
import com.cn.uca.secretkey.MD5;
import com.cn.uca.secretkey.RSAUtils;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.util.pay.PayUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//同城咖订单详情
public class ActionOrderDetailActivity extends BaseBackActivity implements View.OnClickListener,PayBack{

    private TextView back, title, state, order, place, date, name, price, config;
    private ImageView pic;
    private SameCityKaOrderInfo info;
    private int id;
    private String type,orderNum;
    private ListView listView;
    private List<MyTicketCodeBean> list;
    private SameCityKaOrderTicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_order_detail);

        getInfo();
        initView();
    }

    private void getInfo() {
        Intent intent = getIntent();
        if(intent != null){
            type = intent.getStringExtra("type");
            switch (type){
                case "id":
                    id = intent.getIntExtra("id",0);
                    break;
                case "order":
                    orderNum = intent.getStringExtra("order");
                    break;
            }
        }
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        pic = (ImageView) findViewById(R.id.pic);
        title = (TextView) findViewById(R.id.title);
        state = (TextView) findViewById(R.id.state);
        order = (TextView) findViewById(R.id.order);
        place = (TextView) findViewById(R.id.place);
        date = (TextView) findViewById(R.id.date);
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        config = (TextView) findViewById(R.id.config);
        listView = (ListView)findViewById(R.id.listView);
        back.setOnClickListener(this);
        config.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new SameCityKaOrderTicketAdapter(list,this);
        listView.setAdapter(adapter);
        SetListView.setListViewHeightBasedOnChildren(listView);
        getUserOrderInfo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.config:
                switch (info.getUser_order_state_id()){
                    case 2:
                        //支付
                        ShowPopupWindow.paymentWindow(this,"同城咖订单",info.getActual_payment(),1,this);
                        break;
                }
                break;
        }
    }

    private void getUserOrderInfo(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        switch (type){
            case "id":
                map.put("user_order_id",id);
                String sign = SignUtil.sign(map);
                UserHttp.getUserOrderInfo(account_token, sign, time_stamp, id, null, new CallBack() {
                    @Override
                    public void onResponse(Object response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response.toString());
                            int code = jsonObject.getInt("code");
                            switch (code){
                                case 0:
                                    Gson gson = new Gson();
                                    info = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<SameCityKaOrderInfo>() {
                                    }.getType());
                                    ImageLoader.getInstance().displayImage(info.getPicture(),pic);
                                    title.setText(info.getTitle());
                                    list.addAll(info.getTickets());
                                    adapter.setList(list);
                                    switch (info.getUser_order_state_id()){
                                        case 1:
                                            state.setText("已支付");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 2:
                                            state.setText("待支付");
                                            config.setText("确定支付");
                                            break;
                                        case 3:
                                            state.setText("已失效");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 4:
                                            state.setText("待确认");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 5:
                                            state.setText("已取消");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 6:
                                            state.setText("已退单");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 7:
                                            state.setText("待评价");
                                            config.setText("立即评价");
                                            break;
                                        case 8:
                                            state.setText("已完成");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                    }
                                    order.setText("订单编号:"+info.getOrder_number());
                                    switch (info.getActivity_type_id()){
                                        case 1:
                                            place.setText("线上活动");
                                            break;
                                        case 2:
                                            place.setText(info.getCity_name());
                                            break;
                                    }
                                    date.setText(info.getBeg_date()+"~"+info.getEnd_date());
                                    name.setText(info.getUser_card_name());
                                    price.setText("￥"+(int)info.getActual_payment());
                                    break;
                            }
                        }catch (Exception e){
                            Log.e("456",e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorMsg(String errorMsg) {

                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
                break;
            case "order":
                map.put("order_number",orderNum);
                String sign2 = SignUtil.sign(map);
                UserHttp.getUserOrderInfo(account_token, sign2, time_stamp, 0, orderNum, new CallBack() {
                    @Override
                    public void onResponse(Object response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response.toString());
                            int code = jsonObject.getInt("code");
                            switch (code){
                                case 0:
                                    Gson gson = new Gson();
                                    info = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<SameCityKaOrderInfo>() {
                                    }.getType());
                                    ImageLoader.getInstance().displayImage(info.getPicture(),pic);
                                    title.setText(info.getTitle());
                                    list.addAll(info.getTickets());
                                    adapter.setList(list);
                                    switch (info.getUser_order_state_id()){
                                        case 1:
                                            state.setText("已支付");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 2:
                                            state.setText("待支付");
                                            config.setText("确定支付");
                                            break;
                                        case 3:
                                            state.setText("已失效");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 4:
                                            state.setText("待确认");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 5:
                                            state.setText("已取消");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 6:
                                            state.setText("已退单");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                        case 7:
                                            state.setText("待评价");
                                            config.setText("立即评价");
                                            break;
                                        case 8:
                                            state.setText("已完成");
                                            config.setText("暂无操作");
                                            config.setEnabled(false);
                                            break;
                                    }
                                    order.setText("订单编号:"+info.getOrder_number());
                                    switch (info.getActivity_type_id()){
                                        case 1:
                                            place.setText("线上活动");
                                            break;
                                        case 2:
                                            place.setText(info.getCity_name());
                                            break;
                                    }
                                    date.setText(info.getBeg_date()+"~"+info.getEnd_date());
                                    name.setText(info.getUser_card_name());
                                    price.setText("￥"+(int)info.getActual_payment());
                                    break;
                            }
                        }catch (Exception e){
                            Log.e("456",e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorMsg(String errorMsg) {

                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
                break;
        }
    }

    @Override
    public void walletPay(String password) {
        try{
            PublicKey publicKey = RSAUtils.loadPublicKey(Constant.PUBLIC_KEY);
            byte[] encryptByte = RSAUtils.encryptData(MD5.getMD5(password).getBytes(), publicKey);
            String afterencrypt = Base64.encode(encryptByte);
            PayUtil.walletPay(info.getActual_payment(),0,info.getOrder_number(),afterencrypt);
        }catch (Exception e){

        }

    }

    @Override
    public void otherPay(int payType) {
        switch (payType){
            case 2:
                PayUtil.weChatPay("order",info.getActual_payment()+"",info.getOrder_number(),0);
                break;
            case 3:
                PayUtil.aliPay(this,"order",info.getActual_payment()+"",info.getOrder_number(),0);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserOrderInfo();
    }
}
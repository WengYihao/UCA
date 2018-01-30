package com.cn.uca.ui.view.home.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.home.lvpai.OrderBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.user.WalletPasswordActivity;
import com.cn.uca.ui.view.user.order.SpotTicketOrderActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.datepicker.DatePickDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TravelOrderActivity extends BaseBackActivity implements View.OnClickListener,OnSureLisener {

    private TextView back,ticket_name,ticket_price,date,allPrice,config;
    private EditText ticket_num,people_name,phone,identity;
    private int id;
    private String name,price,dataStr,numStr,nameStr,phoneStr,cardStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_order);

        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
            name = intent.getStringExtra("name");
            price = intent.getStringExtra("price");

        }
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        ticket_name = (TextView)findViewById(R.id.ticket_name);
        ticket_price = (TextView)findViewById(R.id.ticket_price);
        date = (TextView)findViewById(R.id.date);
        ticket_num = ( EditText) findViewById(R.id.ticket_num);
        people_name = (EditText) findViewById(R.id.people_name);
        phone = (EditText)findViewById(R.id.phone);
        identity = (EditText)findViewById(R.id.identity);
        allPrice = (TextView)findViewById(R.id.price);
        config = (TextView)findViewById(R.id.config);

        back.setOnClickListener(this);
        config.setOnClickListener(this);
        date.setOnClickListener(this);

        ticket_name.setText(name);
        ticket_price.setText("￥" + price +"/张");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.date:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.config:
                createTicketOrder();
                break;

        }
    }

    private void createTicketOrder(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("admission_ticket_id",id);
        cardStr = identity.getText().toString();
        if (StringXutil.isEmpty(cardStr)){
            ToastXutil.show("身份证不能为空");
        }else{
            map.put("id_card",cardStr);
        }
        phoneStr = phone.getText().toString();
        if (StringXutil.isEmpty(phoneStr)){
            ToastXutil.show("手机号不能为空");
        }else{
            map.put("mobile",phoneStr);
        }
        numStr = ticket_num.getText().toString();
        if (StringXutil.isEmpty(numStr)){
            ToastXutil.show("数量不能为空");
        }else{
            map.put("number",numStr);
        }
        if (StringXutil.isEmpty(dataStr)){
            ToastXutil.show("出行日期不能为空");
        }else{
            map.put("play_date",dataStr);
        }
        nameStr = people_name.getText().toString();
        if (StringXutil.isEmpty(nameStr)){
            ToastXutil.show("出行日期不能为空");
        }else{
            map.put("user_name",nameStr);
        }
        String sign = SignUtil.sign(map);
        HomeHttp.createTicketOrder(sign, time_stamp, account_token, id, cardStr, phoneStr, numStr, dataStr, nameStr, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.e("789",response.toString());
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            String order = jsonObject.getJSONObject("data").getString("order_number");
                            Intent intent = new Intent();
                            intent.setClass(TravelOrderActivity.this, SpotTicketOrderActivity.class);
                            intent.putExtra("type","order");
                            intent.putExtra("order",order);
                            startActivity(intent);
                        case 740:
                            ToastXutil.show("请先设置支付密码");
                            Intent intent2 = new Intent();
                            intent2.setClass(TravelOrderActivity.this, WalletPasswordActivity.class);
                            intent2.putExtra("type",2);
                            startActivity(intent2);
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage());
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

    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(0);
        //设置标题
        dialog.setTitle(R.string.infoActivity_date_title_text);
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(this);
        dialog.show();
    }

    @Override
    public void onSure(Date date) {
        dataStr = SystemUtil.UtilDateToString(date);
        this.date.setText(dataStr);
    }
}

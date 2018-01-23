package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.FillInfoAdapter;
import com.cn.uca.bean.home.samecityka.AddTicketBean;
import com.cn.uca.bean.home.samecityka.FillInfoBean;
import com.cn.uca.bean.home.samecityka.SameCityKaOrderBean;
import com.cn.uca.bean.home.samecityka.SetInfoBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.home.lvpai.DateChoiceActivity;
import com.cn.uca.ui.view.user.WalletPasswordActivity;
import com.cn.uca.ui.view.user.order.ActionOrderDetailActivity;
import com.cn.uca.ui.view.user.order.LvPaiOrderDetailActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.NoScrollListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillInfoActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,submit;
    private NoScrollListView listView;
    private List<AddTicketBean> ticketList;
    private List<FillInfoBean> infoList;
    private List<SetInfoBean> setInfoBeen;
    private FillInfoAdapter adapter;
    private int id;
    private SameCityKaOrderBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);

        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            ticketList = intent.getParcelableArrayListExtra("ticketList");
            infoList = intent.getParcelableArrayListExtra("infoList");
            id = intent.getIntExtra("id",0);
            Log.e("456",ticketList.toString()+"--"+infoList.toString());
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        submit = (TextView)findViewById(R.id.submit);
        listView = (NoScrollListView)findViewById(R.id.listView);

        back.setOnClickListener(this);
        submit.setOnClickListener(this);

        setInfoBeen  = new ArrayList<>();
        adapter = new FillInfoAdapter(infoList,this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.submit:
                if (ticketList.size() == 0){
                    ToastXutil.show("该活动无需报名");
                }else{
                    for (int i = 0; i < infoList.size();i++){
                        LinearLayout layout = (LinearLayout)listView.getChildAt(i);// 获得子item的layout
                        EditText editText = (EditText)layout.findViewById(R.id.info);
                        if (StringXutil.isEmpty(editText.getText().toString())){
                            ToastXutil.show(infoList.get(i).getInfo_name()+"不能为空！");
                            break;
                        }else{
                            SetInfoBean bean = new SetInfoBean();
                            bean.setCafe_fill_user_info_id(infoList.get(i).getCafe_fill_user_info_id());
                            bean.setFill_info_content(editText.getText().toString());
                            setInfoBeen.add(bean);
                        }
                    }
                    orderTicket(setInfoBeen,ticketList);
                }
                break;
        }
    }

    private void orderTicket(List<SetInfoBean> list, List<AddTicketBean> list2){
        final Gson gson = new Gson();
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("city_cafe_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("tickets",gson.toJson(list2));
        map.put("fill_infos",gson.toJson(list));
        String sign = SignUtil.sign(map);
        HomeHttp.orderTicket(time_stamp, sign, account_token, id, gson.toJson(list2), gson.toJson(list), new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONObject array = jsonObject.optJSONObject("data");
                            bean = gson.fromJson(array.toString(),new TypeToken<SameCityKaOrderBean>() {
                            }.getType());
                            Intent intent = new Intent();
                            intent.setClass(FillInfoActivity.this, ActionOrderDetailActivity.class);
                            intent.putExtra("type","order");
                            intent.putExtra("order",bean.getOrder_number());
                            startActivity(intent);
                            FillInfoActivity.this.finish();
                            break;
                        case 740:
                            ToastXutil.show("请先设置支付密码");
                            Intent intent2 = new Intent();
                            intent2.setClass(FillInfoActivity.this, WalletPasswordActivity.class);
                            intent2.putExtra("type",2);
                            startActivity(intent2);
                            break;
                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage());
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

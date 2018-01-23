package com.cn.uca.ui.view.home.samecityka;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.adapter.home.samecityka.TicketCodeAdapter;
import com.cn.uca.bean.home.samecityka.MyTicketCodeBean;
import com.cn.uca.bean.home.samecityka.TicketCodeBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.CallBackValue;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.fragment.home.samecityka.TicketFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.L;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketCodeActivity extends BaseBackActivity implements View.OnClickListener,CallBackValue{

    private TextView back;
    private ViewPager mPager;
    private int id;
    private ListView listView;
    private List<Fragment> fragmentList;
    private List<TicketCodeBean> listCode;
    private List<MyTicketCodeBean> listTicket;
    private Dialog lableDialog;
    private EditText reason;//退票弹窗原因
    private TextView ticket_name,config;//票名、确定


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_code);

        getInfo();
        initView();
    }
    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
            listTicket = intent.getParcelableArrayListExtra("listTicket");
            if (id != 0){
                getUserTickets(id);
            }
        }
    }
    private void initFragment(List<TicketCodeBean> list) {
        for (int i= 0;i<list.size();i++){
            TicketFragment fragment = TicketFragment.newInstance(list.get(i).getUser_cafe_ticket_id(),list.get(i).getTicket_code(),list.get(i).getUser_ticket_state_id(),list.get(i).getTicket_name());
            fragmentList.add(fragment);
        }
        mPager.setAdapter( new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        listView = (ListView)findViewById(R.id.listView);
        TicketCodeAdapter adapter = new TicketCodeAdapter(listTicket,this);
        listView.setAdapter(adapter);
        SetListView.setListViewHeightBasedOnChildren(listView);
        mPager = (ViewPager) findViewById(R.id.container);
        SetLayoutParams.setLinearLayout(mPager, MyApplication.width- SystemUtil.dip2px(20),MyApplication.width- SystemUtil.dip2px(20));
        back.setOnClickListener(this);

        fragmentList = new ArrayList<>();
    }
    private void getUserTickets(int id){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("city_cafe_order_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getUserTickets(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONArray("data");
                            listCode = gson.fromJson(array.toString(),new TypeToken<List<TicketCodeBean>>() {
                            }.getType());
                            if (listCode.size() != 0 && listCode != null){
                                initFragment(listCode);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.config:
                ToastXutil.show("正在退票");
                break;
        }
    }

    @Override
    public void sendMessage(int type, Object obj) {
        switch (type){
            case 1:
                MyTicketCodeBean bean = (MyTicketCodeBean)obj;
                showLable(bean.getTicket_name(),bean.getNumber());
                break;
        }
    }

    private void showLable(String name,int count){
        lableDialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
        View inflateLable = LayoutInflater.from(this).inflate(R.layout.back_ticket_dialog, null);
        //初始化控件
        ticket_name = (TextView)inflateLable.findViewById(R.id.ticket_name);
        reason = (EditText)inflateLable.findViewById(R.id.reason);
        config = (TextView)inflateLable.findViewById(R.id.config);
        config.setOnClickListener(this);
        ticket_name.setText(name+"x"+count);
        //将布局设置给Dialog
        lableDialog.setContentView(inflateLable);
        //获取当前Activity所在的窗体
        Window dialogWindow = lableDialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        params.height = MyApplication.height/3;
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialogWindow.setAttributes(params);
        StatusMargin.setFrameLayoutBottom(this,inflateLable,0);
        lableDialog.show();//显示对话框
    }
}

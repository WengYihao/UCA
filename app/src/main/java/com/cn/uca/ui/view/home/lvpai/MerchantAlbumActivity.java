package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.MerchantPhotoAdapter;
import com.cn.uca.bean.home.lvpai.AlbumBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家相册
 */
public class MerchantAlbumActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,more;
    private ListView listView;
    private MerchantPhotoAdapter adapter;
    private List<AlbumBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_album);

        initView();
        getMerchantAlbum();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        more = (TextView)findViewById(R.id.more);

        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new MerchantPhotoAdapter(list,this);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
        more.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastXutil.show(list.get(position).getAlbum_name());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MerchantAlbumActivity.this,PhotoManageActivity.class);
                intent.putExtra("id",list.get(position).getMerchant_album_id());
                startActivity(intent);
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
                addresspopuWindow();
                break;
        }
    }
    private void addresspopuWindow(){
        View inflate = LayoutInflater.from(this).inflate(R.layout.lvpai_addpic_dialog, null);

        PopupWindow popupWindow = new PopupWindow(inflate, MyApplication.width,
                MyApplication.height/3, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
    }
    private void getMerchantAlbum(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getMerchantAlbum(time_stamp, sign, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<AlbumBean> bean = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<AlbumBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);
                                adapter.setList(list);
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
}

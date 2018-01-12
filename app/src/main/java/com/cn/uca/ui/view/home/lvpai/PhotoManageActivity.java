package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.PhotoItemAdapter;
import com.cn.uca.bean.home.lvpai.MerchantPhotoBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.NoScrollGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoManageActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,name;
    private NoScrollGridView gridView;
    private PhotoItemAdapter adapter;
    private List<MerchantPhotoBean> list;
    private int id;
    private List<String> listUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_manage);

        getInfo();
        initView();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        name = (TextView)findViewById(R.id.name);
        gridView = (NoScrollGridView)findViewById(R.id.gridView);

        back.setOnClickListener(this);

        list = new ArrayList<>();
        listUrl = new ArrayList<>();
        adapter = new PhotoItemAdapter(list,this);
        gridView.setAdapter(adapter);
        getAlbumPicture();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPhoto.imageUrl(PhotoManageActivity.this,position,(ArrayList<String>) listUrl);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getAlbumPicture(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("merchant_album_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.getAlbumPicture(time_stamp, sign, account_token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            name.setText(jsonObject.getJSONObject("data").getString("album_name"));
                            Gson gson = new Gson();
                            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("albumPictures");
                            List<MerchantPhotoBean> bean = gson.fromJson(array.toString(),new TypeToken<List<MerchantPhotoBean>>() {
                            }.getType());
                            if (bean.size() > 0){
                                list.addAll(bean);
                                adapter.setList(list);
                                for (int i = 0;i<list.size();i++){
                                    listUrl.add(list.get(i).getPicture_url());
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
}

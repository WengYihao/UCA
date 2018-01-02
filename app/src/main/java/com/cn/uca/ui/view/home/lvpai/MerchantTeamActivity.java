package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.TeamAdapter;
import com.cn.uca.bean.home.lvpai.TeamBean;
import com.cn.uca.config.Constant;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantTeamActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,more;
    private ListView listView;
    private TeamAdapter adapter;
    private List<TeamBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_team);

        initView();
        getTeam();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        more = (TextView)findViewById(R.id.more);
        listView = (ListView)findViewById(R.id.listView);

        list = new ArrayList<>();
        adapter = new TeamAdapter(list,this);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
        more.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MerchantTeamActivity.this,AddTeamActivity.class);
                intent.putExtra("type","update");
                intent.putExtra("id",list.get(position).getTrip_shoot_team_id());
                intent.putExtra("url",list.get(position).getHead_portrait_url());
                intent.putExtra("content",list.get(position).getIntroduce());
                startActivityForResult(intent,1);
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
                Intent intent = new Intent();
                intent.setClass(MerchantTeamActivity.this,AddTeamActivity.class);
                intent.putExtra("type","add");
                intent.putExtra("id",0);
                startActivityForResult(intent,0);
                break;
        }
    }

    private void getTeam(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getTeam(time_stamp, sign, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            List<TeamBean> beanList = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<TeamBean>>() {
                            }.getType());
                            if (beanList.size() > 0){
                                list.addAll(beanList);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0:
                //添加
                ToastXutil.show("---");
                break;
            case 1:
                //修改
                break;
            case 2:
                //删除
                break;
        }
    }
}

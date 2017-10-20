package com.cn.uca.ui.view.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;

import org.json.JSONObject;

public class WalletActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_wallet);

        initView();
        getWallet();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(this);

        count = (TextView)findViewById(R.id.count);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void getWallet(){
        UserHttp.getWallet(new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString()+"---787878");
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    double num = jsonObject.getDouble("user_balance");
                    count.setText(num+"");
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                Log.i("123",errorMsg+"---12346855");
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}

package com.cn.uca.ui.yueka;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.Date;

public class OtherYueActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,name,age,sex,state;
    private CircleImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_other_yue);

        initView();
        getEscortInfo();
    }

    private void getEscortInfo(){
        YueKaHttp.getEscortInfo(10, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString()+"---");
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    if (code == 0){
                        JSONObject object = jsonObject.getJSONObject("data");
                        int sexId = object.getInt("sex_id");
                        switch (sexId){
                            case 1:
                                sex.setBackgroundResource(R.mipmap.man);
                                break;
                            case 2:
                                sex.setBackgroundResource(R.mipmap.woman);
                                break;
                            case 3:
                                sex.setVisibility(View.GONE);
                                break;
                        }
                        name.setText(object.getString("user_nick_name"));
                        ImageLoader.getInstance().displayImage(object.getString("user_head_portrait"), pic);
                        if (!StringXutil.isEmpty(object.getString("user_birth_date"))){
                            Date date = SystemUtil.StringToUtilDate(object.getString("user_birth_date"));
                            age.setText(SystemUtil.getAge(date)+"岁");
                        }else{
                            age.setVisibility(View.GONE);
                        }
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
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        pic = (CircleImageView)findViewById(R.id.pic);
        name = (TextView)findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        sex = (TextView)findViewById(R.id.sex);
        state = (TextView)findViewById(R.id.state);

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }
}

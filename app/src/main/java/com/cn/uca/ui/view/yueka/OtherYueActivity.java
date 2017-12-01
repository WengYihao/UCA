package com.cn.uca.ui.view.yueka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherYueActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,name,age,sex,state;
    private CircleImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        state.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.state:
                getIdCardUrl();
                break;
        }
    }
    private void getIdCardUrl(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        UserHttp.getIdCardUrl(sign, time_stamp, account_token, new CallBack() {
            @Override
            public void onResponse(Object response) {
                Log.i("123",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            String url = jsonObject.getJSONObject("data").getString("url");
                           doVerify(url);
                            break;
                        case 20:
                            ToastXutil.show("请先登录");
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
    /**
     * 启动支付宝进行认证
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            // 这里使用固定appid 20000067
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
            builder.append(URLEncoder.encode(url));
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
        } else {
            // 处理没有安装支付宝的情况
            new AlertDialog.Builder(this)
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     * @return true 为已经安装
     */
    private  boolean hasApplication() {
        PackageManager manager = getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }
}

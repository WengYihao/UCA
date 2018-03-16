package com.cn.uca.ui.view.yueka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.user.UserInfo;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherYueActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,name,age,sex;
    private CircleImageView pic;
    private LinearLayout tobeCollar;
    private RelativeLayout layout1,layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_yue);

        initView();
        getUserInfo();
    }

    private void getUserInfo(){
        UserHttp.getUserBriefInfo(new CallBack() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                UserInfo info = gson.fromJson(response.toString(),new TypeToken<UserInfo>(){}.getType());
                Log.e("456",info.getUser_head_portrait());
                try{
                    ImageLoader.getInstance().displayImage(info.getUser_head_portrait(), pic);
                    name.setText(info.getUser_nick_name());
                    if (info.getUser_birth_date()== null){
                        age.setText("年龄: 未知");
                    }else{
                        Date date = SystemUtil.StringToUtilDate(info.getUser_birth_date());
                        age.setText("年龄: "+SystemUtil.getAge(date)+"岁");
                    }

                    switch (info.getSex_id()){
                        case 1:
                            sex.setText("性别: 男");
                            break;
                        case 2:
                            sex.setText("性别: 女");
                            break;
                        case 3:
                            sex.setText("性别: 保密");
                            break;
                    }

                }catch (Exception e){
                    Log.i("456",e.getMessage()+"报错");
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

        tobeCollar = (LinearLayout)findViewById(R.id.tobeCollar);

        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);

        back.setOnClickListener(this);
        tobeCollar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.tobeCollar:
                getIdCardUrl();
                break;
            case R.id.layout1:
                startActivity(new Intent(OtherYueActivity.this,YueKaCollectionActivity.class));
                break;
            case R.id.layout2:
                ToastXutil.show("暂无数据哦，请先认证");
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

package com.cn.uca.ui.view.yueka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.yueka.CommentAdapter;
import com.cn.uca.adapter.yueka.OrderCourseAdapter;
import com.cn.uca.bean.OrderCourseBean;
import com.cn.uca.bean.yueka.CommentBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.user.AcceptOrderActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.HorizontalListView;
import com.cn.uca.view.NoScrollListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderYueActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,state,name,age,sex;
    private CircleImageView pic;
//    private HorizontalListView orderCourse;
//    private NoScrollListView comment;
//    private List<OrderCourseBean> list1;//领咖约咖历程
//    private List<CommentBean> list2;//领咖用户评论
    private OrderCourseAdapter orderCourseAdapter;
    private CommentAdapter commentAdapter;
    private RelativeLayout layout,layout1,layout2,layout3,layout4,layout5,layout6,layout7,layout8,layout9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_yue);

        initView();
        getEscortInfo();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        state = (TextView)findViewById(R.id.state);
        name = (TextView)findViewById(R.id.name);
        sex = (TextView)findViewById(R.id.sex);
        age = (TextView)findViewById(R.id.age);
        pic = (CircleImageView)findViewById(R.id.pic);

        layout = (RelativeLayout)findViewById(R.id.layout);
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        layout3 = (RelativeLayout)findViewById(R.id.layout3);
        layout4 = (RelativeLayout)findViewById(R.id.layout4);
        layout5 = (RelativeLayout)findViewById(R.id.layout5);
        layout6 = (RelativeLayout)findViewById(R.id.layout6);
        layout7 = (RelativeLayout)findViewById(R.id.layout7);
        layout8 = (RelativeLayout)findViewById(R.id.layout8);
        layout9 = (RelativeLayout)findViewById(R.id.layout9);

        back.setOnClickListener(this);
        layout.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        layout7.setOnClickListener(this);
        layout8.setOnClickListener(this);
        layout9.setOnClickListener(this);

        if (SharePreferenceXutil.isAuthentication() && SharePreferenceXutil.isCollar()){
            state.setText("已认证");
        }else{
            state.setText("成为领咖");
        }
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
//                                sex.setBackgroundResource(R.mipmap.man);
                                sex.setText("性别: 男");
                                break;
                            case 2:
//                                sex.setBackgroundResource(R.mipmap.woman);
                                sex.setText("性别: 女");
                                break;
                            case 3:
                                sex.setText("性别: 保密");
                                break;
                        }
                        name.setText(object.getString("user_nick_name"));
                        ImageLoader.getInstance().displayImage(object.getString("user_head_portrait"), pic);
                        Date date = SystemUtil.StringToUtilDate(object.getString("user_birth_date"));
                        age.setText("年龄: "+SystemUtil.getAge(date)+"岁");
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.layout:
//                startActivity(new Intent(OrderYueActivity.this,YuekaInfoActivity.class));
                switch (state.getText().toString()){
                    case "已认证":

                        break;
                    case "成为领咖":
                        startActivityForResult(new Intent(OrderYueActivity.this,ToBeCollarActivity.class),0);
                        break;
                }
                break;
            case R.id.layout1://游咖-我的收藏
//                startActivity(new Intent(OrderYueActivity.this,PresetManagerActivity.class));
                break;
            case R.id.layout2://游咖-我的约单
//                startActivity(new Intent(OrderYueActivity.this,SendYueKaActivity.class));
                break;
            case R.id.layout3://领咖-发布约咖
               startActivity(new Intent(OrderYueActivity.this,SendYueKaActivity.class));
                break;
            case R.id.layout4://领咖-我的发布
                if (SharePreferenceXutil.isCollar()){
                    startActivity(new Intent(OrderYueActivity.this,SendListActivity.class));
                }else{
                    ToastXutil.show("请先成为大咖");
                }
                break;
            case R.id.layout5://领咖-我的接单
                if (SharePreferenceXutil.isCollar()){
                    startActivity(new Intent(OrderYueActivity.this,AcceptOrderActivity.class));
                }else{
                    ToastXutil.show("请先成为大咖");
                }
                break;
            case R.id.layout6://领咖-我的评价

                break;
            case R.id.layout7://领咖-路线管理
                if (SharePreferenceXutil.isCollar()){
                    startActivity(new Intent(OrderYueActivity.this,PresetManagerActivity.class));
                }else{
                    ToastXutil.show("请先成为大咖");
                }
                break;
            case R.id.layout8://领咖-标签管理

                break;
            case R.id.layout9://领咖-设置背景图
                if (SharePreferenceXutil.isCollar()){
                    startActivity(new Intent(OrderYueActivity.this,BackImageActivity.class));
                }else{
                    ToastXutil.show("请先成为大咖");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            switch (resultCode){
                case 0:
                    String result = data.getStringExtra("result");
                    if (result.equals("ok")){
                        state.setText("已认证");
                    }
                    break;
            }
        }
    }
}

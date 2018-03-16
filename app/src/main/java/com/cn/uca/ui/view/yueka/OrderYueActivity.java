package com.cn.uca.ui.view.yueka;

 import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.yueka.CommentAdapter;
import com.cn.uca.adapter.yueka.OrderCourseAdapter;
 import com.cn.uca.bean.MessageNumBean;
 import com.cn.uca.impl.CallBack;
 import com.cn.uca.impl.ServiceBack;
 import com.cn.uca.popupwindows.ShowPopupWindow;
 import com.cn.uca.server.yueka.YueKaHttp;
 import com.cn.uca.ui.view.rongim.ChatListActivity;
 import com.cn.uca.ui.view.user.AcceptOrderActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
 import com.cn.uca.view.RatingStarView;
 import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.Date;

 import io.rong.imkit.RongIM;
 import io.rong.imkit.manager.IUnReadMessageObserver;
 import io.rong.imlib.model.Conversation;

public class OrderYueActivity extends BaseBackActivity implements View.OnClickListener,ServiceBack,IUnReadMessageObserver{

    private TextView back,message,state,name,age,sex;
    private CircleImageView pic;
    private RatingStarView start;
    private OrderCourseAdapter orderCourseAdapter;
    private CommentAdapter commentAdapter;
    private RelativeLayout layout,layout1,layout2,layout3,layout4,layout5,layout6,layout8,layout9;
    private TextView yueka_message_num,yueka_order_num,send_yueka_num,yueka_my_order_num;//消息红点
    final Conversation.ConversationType[] conversationTypes = {
            Conversation.ConversationType.PRIVATE,
            Conversation.ConversationType.SYSTEM,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_yue);

        initView();
        getEscortInfo();
    }

    private void initView() {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);//未读消息
        back = (TextView)findViewById(R.id.back);
        message = (TextView)findViewById(R.id.message);
        state = (TextView)findViewById(R.id.state);
        start = (RatingStarView)findViewById(R.id.start);
        name = (TextView)findViewById(R.id.name);
        sex = (TextView)findViewById(R.id.sex);
        age = (TextView)findViewById(R.id.age);
        pic = (CircleImageView)findViewById(R.id.pic);

        yueka_message_num = (TextView)findViewById(R.id.yueka_message_num);
        yueka_order_num = (TextView)findViewById(R.id.yueka_order_num);
        send_yueka_num = (TextView)findViewById(R.id.send_yueka_num);
        yueka_my_order_num = (TextView)findViewById(R.id.yueka_my_order_num);

        layout1 = (RelativeLayout)findViewById(R.id.layout1);//约咖-领咖收藏
        layout2 = (RelativeLayout)findViewById(R.id.layout2);//约咖-我的约单
        layout3 = (RelativeLayout)findViewById(R.id.layout3);//领咖-发布约咖
        layout4 = (RelativeLayout)findViewById(R.id.layout4);//领咖-我的发布
        layout5 = (RelativeLayout)findViewById(R.id.layout5);//领咖-我的接单
        layout6 = (RelativeLayout)findViewById(R.id.layout6);//领咖-我的评价
        layout8 = (RelativeLayout)findViewById(R.id.layout8);//领咖-标签管理
        layout9 = (RelativeLayout)findViewById(R.id.layout9);//领咖-设置背景图

        back.setOnClickListener(this);
        message.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        layout8.setOnClickListener(this);
        layout9.setOnClickListener(this);

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
                                sex.setText("性别: 男");
                                break;
                            case 2:
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
                        start.setRating((float)object.getDouble("average_score"));
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
            case R.id.message:
                startActivity(new Intent(OrderYueActivity.this, ChatListActivity.class));
                break;
            case R.id.layout1://游咖-领咖收藏
                startActivity(new Intent(OrderYueActivity.this,YueKaCollectionActivity.class));
                break;
            case R.id.layout2://游咖-我的约单
                MessageNumBean.getInstens().setEu_agree_purchase(0);
//                MessageNumBean.getInstens().setEu_back_request();
                MessageNumBean.getInstens().setEu_agree_back(0);
                MessageNumBean.getInstens().setEu_disagree_back(0);
                MessageNumBean.getInstens().setEu_disagree_purchase(0);
                startActivity(new Intent(OrderYueActivity.this,YueKaYueActivity.class));
                break;
            case R.id.layout3://领咖-发布约咖
                String str = "http://www.szyouka.com:8080/youkatravel/agreement/escortProtocol.html";
                ShowPopupWindow.seviceWindow(getWindow().getDecorView(),this,str,this);
                break;
            case R.id.layout4://领咖-我的发布
                startActivity(new Intent(OrderYueActivity.this,SendListActivity.class));
                break;
            case R.id.layout5://领咖-我的接单
                MessageNumBean.getInstens().setE_payment(0);
                MessageNumBean.getInstens().setE_agree_back(0);
                MessageNumBean.getInstens().setE_disagree_back(0);
                startActivity(new Intent(OrderYueActivity.this,AcceptOrderActivity.class));
                break;
            case R.id.layout6://领咖-我的评价
                startActivity(new Intent(OrderYueActivity.this,YuekaCommentActivity.class));
                break;
            case R.id.layout8://领咖-标签管理

                break;
            case R.id.layout9://领咖-设置背景图
                startActivity(new Intent(OrderYueActivity.this,BackImageActivity.class));
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

    @Override
    public void sure() {
        startActivity(new Intent(OrderYueActivity.this,SendYueKaActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //消息红点
        Log.e("456",MessageNumBean.getInstens().toString()+"/*-");
        //消息红点
        if (MessageNumBean.getInstens().getE_purchase() != 0){
            send_yueka_num.setVisibility(View.VISIBLE);
        }else{
            send_yueka_num.setVisibility(View.GONE);
        }
        if (MessageNumBean.getInstens().getE_payment() != 0||
                MessageNumBean.getInstens().getE_agree_back() != 0 ||
                MessageNumBean.getInstens().getE_disagree_back() != 0 ||
                MessageNumBean.getInstens().getE_back_request() != 0 ||
                MessageNumBean.getInstens().getE_settlement() != 0){
            yueka_my_order_num.setVisibility(View.VISIBLE);
        }else{
            yueka_my_order_num.setVisibility(View.GONE);
        }
        if ( MessageNumBean.getInstens().getEu_agree_back() != 0 ||
                MessageNumBean.getInstens().getEu_agree_purchase() != 0 ||
                MessageNumBean.getInstens().getEu_disagree_back() != 0 ||
                MessageNumBean.getInstens().getEu_disagree_purchase() != 0 ||
                MessageNumBean.getInstens().getEu_back_request() != 0){
            yueka_order_num.setVisibility(View.VISIBLE);
        }else{
            yueka_order_num.setVisibility(View.GONE);
        }
    }



    @Override
    public void onCountChanged(int i) {//即时通讯红点
        if (i > 0){
            yueka_message_num.setVisibility(View.VISIBLE);
        }else{
            yueka_message_num.setVisibility(View.GONE);
        }
    }
}

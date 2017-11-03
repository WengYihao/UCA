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
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.HorizontalListView;
import com.cn.uca.view.NoScrollListView;
import com.cn.uca.view.RdListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderYueActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back,name,age,sex;
    private CircleImageView pic;
    private HorizontalListView orderCourse;
    private NoScrollListView comment;
    private List<OrderCourseBean> list1;
    private List<CommentBean> list2;
    private OrderCourseAdapter orderCourseAdapter;
    private CommentAdapter commentAdapter;
    private RelativeLayout layout1,layout2,layout3,layout4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_order_yue);

        initView();
        getEscortInfo();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        name = (TextView)findViewById(R.id.name);
        sex = (TextView)findViewById(R.id.sex);
        age = (TextView)findViewById(R.id.age);
        pic = (CircleImageView)findViewById(R.id.pic);
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        layout3 = (RelativeLayout)findViewById(R.id.layout3);
        layout4 = (RelativeLayout)findViewById(R.id.layout4);

        back.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);

        orderCourse = (HorizontalListView)findViewById(R.id.orderCourse);
        comment = (NoScrollListView) findViewById(R.id.comment);



        list1 = new ArrayList<>();
        OrderCourseBean bean = new OrderCourseBean();
        bean.setImg("http://www.szyouka.com/1.png");
        bean.setNum(9);
        bean.setTime("2017.4.5-2017.4.8");
        bean.setPlace("北京");
        bean.setText("如果数至里圈的什么图上，则到外圈去找，退回");
        list1.add(bean);
        OrderCourseBean bean1 = new OrderCourseBean();
        bean1.setImg("http://www.szyouka.com/2.png");
        bean1.setNum(8);
        bean1.setTime("2017.6.5-2.17.6.9");
        bean1.setPlace("广州");
        bean1.setText("我的祖母在后园辟小片地，种乌青菜，经霜，菜叶边缘作紫红色，味道苦中泛甜。");
        list1.add(bean1);
        OrderCourseBean bean2 = new OrderCourseBean();
        bean2.setImg("http://www.szyouka.com/3.png");
        bean2.setNum(7);
        bean2.setTime("2.17.8.8-2017.8.10");
        bean2.setPlace("深圳");
        bean2.setText("两道之间印出八仙、马、兔子、鲤鱼、虾……；每样都是两个");
        list1.add(bean2);
        orderCourseAdapter = new OrderCourseAdapter(list1,this);
        orderCourse.setAdapter(orderCourseAdapter);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) orderCourse.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = MyApplication.height/3;// 控件的高强制设成20
        orderCourse.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

        list2 = new ArrayList<>();
        CommentBean commentBean = new CommentBean();
        commentBean.setName("Action丶");
        commentBean.setText("我们跟刘哥去了很多地方，确实可以，基本上深圳都逛遍了，特色小吃也吃到了，这次和女朋友出来旅行都非常开心--");
        commentBean.setTime("2017-08-29");
        list2.add(commentBean);

        CommentBean commentBean1 = new CommentBean();
        commentBean1.setName("谭雅");
        commentBean1.setText("真的好评，约了几个大咖还是这个最聊得来，人也好看");
        commentBean1.setTime("2017-07-19");
        list2.add(commentBean1);

        CommentBean commentBean2 = new CommentBean();
        commentBean2.setName("我想静静");
        commentBean2.setText("努力有用的话还要天才干嘛？所以这个陪游还是不错的，很专业。");
        commentBean2.setTime("2017-06-16");
        list2.add(commentBean2);

        CommentBean commentBean3 = new CommentBean();
        commentBean3.setName("小光仔");
        commentBean3.setText("这一次意味着什么呢？感觉倍棒，这feel真爽");
        commentBean3.setTime("2017-08-14");
        list2.add(commentBean3);

        CommentBean commentBean4 = new CommentBean();
        commentBean4.setName("哔哩哔哩");
        commentBean4.setText("一次真正的专业体验，如果还有下次，你猜我会不会找你？你先猜猜");
        commentBean4.setTime("2017-07-27");
        list2.add(commentBean4);

        commentAdapter = new CommentAdapter(list2,this);
        comment.setAdapter(commentAdapter);
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
                        Date date = SystemUtil.StringToUtilDate(object.getString("user_birth_date"));
                        age.setText(SystemUtil.getAge(date)+"岁");
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
            case R.id.layout1:
                startActivity(new Intent(OrderYueActivity.this,PresetManagerActivity.class));
                break;
            case R.id.layout2:
                startActivity(new Intent(OrderYueActivity.this,SendYueKaActivity.class));
                break;
            case R.id.layout3:

                break;
            case R.id.layout4:

                break;
        }
    }
}

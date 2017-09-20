package com.cn.uca.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cn.uca.R;
import com.cn.uca.adapter.CommentAdapter;
import com.cn.uca.adapter.OrderCourseAdapter;
import com.cn.uca.bean.CommentBean;
import com.cn.uca.bean.OrderCourseBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SetListView;
import com.cn.uca.view.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class OrderYueActivity extends BaseBackActivity implements View.OnClickListener{

    private HorizontalListView orderCourse;
    private ListView comment;
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
    }

    private void initView() {
        layout1 = (RelativeLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        layout3 = (RelativeLayout)findViewById(R.id.layout3);
        layout4 = (RelativeLayout)findViewById(R.id.layout4);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);

        orderCourse = (HorizontalListView)findViewById(R.id.orderCourse);
        comment = (ListView)findViewById(R.id.comment);

        list1 = new ArrayList<>();
        OrderCourseBean bean = new OrderCourseBean();
        bean.setImg("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        bean.setNum(9);
        bean.setTime("2017.4.5-2017.4.8");
        bean.setPlace("北京");
        bean.setText("如果数至里圈的什么图上，则到外圈去找，退回");
        list1.add(bean);
        OrderCourseBean bean1 = new OrderCourseBean();
        bean1.setImg("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        bean1.setNum(8);
        bean1.setTime("2017.6.5-2.17.6.9");
        bean1.setPlace("广东-广州");
        bean1.setText("我的祖母在后园辟小片地，种乌青菜，经霜，菜叶边缘作紫红色，味道苦中泛甜。");
        list1.add(bean1);
        OrderCourseBean bean2 = new OrderCourseBean();
        bean2.setImg("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        bean2.setNum(7);
        bean2.setTime("2.17.8.8-2017.8.10");
        bean2.setPlace("广东-深圳");
        bean2.setText("两道之间印出八仙、马、兔子、鲤鱼、虾……；每样都是两个");
        list1.add(bean2);
        orderCourseAdapter = new OrderCourseAdapter(list1,this);
        orderCourse.setAdapter(orderCourseAdapter);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) orderCourse.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = MyApplication.height*2/5;// 控件的高强制设成20
        orderCourse.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

        list2 = new ArrayList<>();
        CommentBean commentBean = new CommentBean();
        commentBean.setText("哈哈哈哈哈哈哈哈哈哈");
        list2.add(commentBean);

        CommentBean commentBean1 = new CommentBean();
        commentBean1.setText("噢噢噢噢噢");
        list2.add(commentBean1);

        CommentBean commentBean2 = new CommentBean();
        commentBean2.setText("嘻嘻嘻嘻");
        list2.add(commentBean2);

        CommentBean commentBean3 = new CommentBean();
        commentBean3.setText("哦哦哦哦哦");
        list2.add(commentBean3);

        CommentBean commentBean4 = new CommentBean();
        commentBean4.setText("啊啊啊啊啊");
        list2.add(commentBean4);

        commentAdapter = new CommentAdapter(list2,this);
        comment.setAdapter(commentAdapter);
        SetListView.setListViewHeightBasedOnChildren(comment);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout1:
                startActivity(new Intent(OrderYueActivity.this,PresetManagerActivity.class));
                break;
            case R.id.layout2:

                break;
            case R.id.layout3:

                break;
            case R.id.layout4:

                break;
        }
    }
}

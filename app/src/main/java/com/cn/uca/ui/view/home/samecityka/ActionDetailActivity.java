package com.cn.uca.ui.view.home.samecityka;

import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.ActionDetailAdapter;
import com.cn.uca.adapter.home.samecityka.BuyTicketAdapter;
import com.cn.uca.bean.home.samecityka.ActionDescribeBean;
import com.cn.uca.bean.home.samecityka.ActionDetailBean;
import com.cn.uca.bean.home.samecityka.AddTicketBean;
import com.cn.uca.bean.home.samecityka.BuyTicketInfoBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.samecityka.DialogListener;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.NoScrollListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动详情-同城咖
 */
public class ActionDetailActivity extends BaseBackActivity implements View.OnClickListener,DialogListener{

    private RelativeLayout user_info,layout;
    private ImageView pic;
    private TextView back,surplustime,share,action_name,username,price,time,place,collection_icon,collection_text,join_action;
    private LinearLayout collection,leaving_news;//收藏/留言
    private int id;
    private int isCollection = 0;
    private BuyTicketInfoBean bean;
    private ListView listView;//门票信息
    private TextView submit;
    private List<AddTicketBean> ticketList;
    private Dialog dialog;
    private long midDate;
    private TimeCount timeStr;
    private ListView detail;
    private List<ActionDescribeBean> list;
    private ActionDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_detail);

        getInfo();
        initView();
        getCityCafeInfo();
        getTickets();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }
    private void initView() {
        back = (TextView)findViewById(R.id.back);
        share = (TextView)findViewById(R.id.share);
        layout = (RelativeLayout)findViewById(R.id.layout);
        surplustime = (TextView)findViewById(R.id.surplustime);
        pic = (ImageView)findViewById(R.id.pic);
        action_name = (TextView)findViewById(R.id.action_name);
        username = (TextView)findViewById(R.id.username);
        price = (TextView) findViewById(R.id.price);
        time = (TextView)findViewById(R.id.time);
        place  = (TextView)findViewById(R.id.place);
        detail = (ListView)findViewById(R.id.detail);
        user_info = (RelativeLayout)findViewById(R.id.user_info);
        collection = (LinearLayout)findViewById(R.id.collection);
        collection_icon = (TextView)findViewById(R.id.collection_icon);
        collection_text = (TextView)findViewById(R.id.collection_text);
        leaving_news = (LinearLayout)findViewById(R.id.leaving_news);
        join_action = (TextView)findViewById(R.id.join_action);

        layout.getBackground().setAlpha(120);
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        user_info.setOnClickListener(this);
        collection.setOnClickListener(this);
        leaving_news.setOnClickListener(this);
        join_action.setOnClickListener(this);

        ticketList = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new ActionDetailAdapter(list,this);
        detail.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.share:
                getShare();
                break;
            case R.id.user_info:
                Intent intent1 = new Intent();
                intent1.setClass(ActionDetailActivity.this,InitiatorInfoActivity.class);
                intent1.putExtra("id",id);
                startActivity(intent1);
                break;
            case R.id.collection:
                collectionCityCafe();
                break;
            case R.id.leaving_news:
                ToastXutil.show("哈哈...");
                break;
            case R.id.join_action:
                showCityDialog();
                break;
            case R.id.submit:
                if (ticketList.size() != 0){
                    ticketList.clear();
                }
                for (int i = 0; i < bean.getTickets().size();i++){
                    LinearLayout layout = (LinearLayout)listView.getChildAt(i);// 获得子item的layout
                    EditText editText = (EditText)layout.findViewById(R.id.num);
                    AddTicketBean bean = new AddTicketBean();
                    bean.setCity_cafe_ticket_id(this.bean.getTickets().get(i).getCity_cafe_ticket_id());
                    bean.setNumber(Integer.parseInt(editText.getText().toString()));
                    ticketList.add(bean);
                }
                Intent intent = new Intent();
                intent.setClass(ActionDetailActivity.this,FillInfoActivity.class);
                intent.putExtra("id",id);
                intent.putParcelableArrayListExtra("ticketList",(ArrayList<? extends Parcelable>) ticketList);
                intent.putParcelableArrayListExtra("infoList",(ArrayList<? extends Parcelable>) bean.getFillUserInfos());
                startActivity(intent);
                dialog.dismiss();
                break;

        }
    }

    private void collectionCityCafe(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("city_cafe_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.collectionCityCafe(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                if ((int) response == 0) {
                    switch (isCollection) {
                        case 1:
                            ToastXutil.show("取消收藏");
                            collection_icon.setBackgroundResource(R.mipmap.nocollection);
                            collection_text.setText("收藏");
                            isCollection = 2;
                            break;
                        case 2:
                            ToastXutil.show("收藏成功");
                            collection_icon.setBackgroundResource(R.mipmap.collection);
                            collection_text.setText("已收藏");
                            isCollection = 1;
                            break;
                    }
                }else{
                    ToastXutil.show("收藏失败");
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
    //分享
    private void getShare(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("shareType","TCK");
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("id",id);
        String sign = SignUtil.sign(map);
        UserHttp.getShare(account_token, time_stamp, sign, "TCK",id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            String share_title = jsonObject.getJSONObject("data").getString("share_title");
                            String web_url = jsonObject.getJSONObject("data").getString("web_url");
                            WeChatManager.instance().sendWebPageToWX(ActionDetailActivity.this,true,web_url,R.mipmap.logo,share_title,"快来参加同城活动吧！");
                            break;
                        default:
                            ToastXutil.show("分享失败");
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
    private void getCityCafeInfo(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("city_cafe_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getCityCafeInfo(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            ActionDetailBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<ActionDetailBean>() {
                            }.getType());
                            ImageLoader.getInstance().displayImage(bean.getCover_url(),pic);
                            action_name.setText(bean.getTitle());
                            username.setText(bean.getUser_card_name());
                            if (bean.isCharge()){
                                price.setText("收费");
                            }else{
                                price.setText("免费");
                            }
                            initTime(bean.getBeg_time());
                            time.setText(bean.getBeg_time()+"至"+bean.getEnd_time());;
                            if (bean.getPosition() != null){
                                place.setText(bean.getPosition().getAddress());
                            }else{
                                place.setText("线上活动");
                            }
                            if (bean.isCollection()){
                                collection_icon.setBackgroundResource(R.mipmap.collection);
                                collection_text.setText("已收藏");
                                isCollection = 1;
                            }else{
                                collection_icon.setBackgroundResource(R.mipmap.nocollection);
                                collection_text.setText("收藏");
                                isCollection = 2;
                            }
                            list = bean.getParagraphs();
                            Log.e("456",list.toString());
                            adapter.setList(list);
                            SetListView.setListViewHeightBasedOnChildren(detail);
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage());
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
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            layout.setVisibility(View.GONE);
            surplustime.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            long hh = millisUntilFinished / 60 / 60 /1000% 60;
            long mm = millisUntilFinished / 60/1000 % 60;
            long ss = millisUntilFinished /1000% 60;
            surplustime.setText(hh + " 时 " + mm + " 分 " + ss + " 秒");
        }
    }
    private void initTime(String time){
        try{
            long ti= SystemUtil.StringToUtilDate3(time).getTime();
            midDate = ti- System.currentTimeMillis();
            timeStr = new TimeCount(midDate, 1000);// 构造CountDownTimer对象
            timeStr.start();
        }catch (Exception e){}

    }
    /**
     * 参与活动门票弹窗
     */
    private void showCityDialog(){
       dialog = new Dialog(this,R.style.dialog_style);
        View inflate = LayoutInflater.from(this).inflate(R.layout.join_action_dialog, null);
        listView = (ListView)inflate.findViewById(R.id.listView);
        submit = (TextView)inflate.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        BuyTicketAdapter adapter = new BuyTicketAdapter(bean.getTickets(),this,this);
        listView.setAdapter(adapter);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
//        dialog.setCanceledOnTouchOutside(true);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        params.height = MyApplication.height*5/9;
        dialog.show();//显示对话框
    }
    private void getTickets(){
        Map<String,Object> map = new HashMap<>();
        map.put("city_cafe_id",id);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getTickets(time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<BuyTicketInfoBean>() {
                            }.getType());
                            break;
                    }
                }catch (Exception e){
                    Log.e("456",e.getMessage());
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
    public void add(View v) {
        LinearLayout layout = (LinearLayout)listView.getChildAt((int) v.getTag());// 获得子item的layout
        EditText editText = (EditText)layout.findViewById(R.id.num);
        if (!StringXutil.isEmpty(editText.getText().toString())){
            int num = Integer.parseInt(editText.getText().toString());
            num++;
            editText.setText(num+"");
        }else{
            int num = 1;
            num++;
            editText.setText(num+"");
        }
    }

    @Override
    public void reduce(View v) {
        LinearLayout layout = (LinearLayout)listView.getChildAt((int) v.getTag());// 获得子item的layout
        EditText editText = (EditText)layout.findViewById(R.id.num);
        if (!StringXutil.isEmpty(editText.getText().toString())){
            int num = Integer.parseInt(editText.getText().toString());
            if (num > 1){
                num--;
                editText.setText(num+"");
            }
        }
    }
}

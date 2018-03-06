package com.cn.uca.ui.view.home.travel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.travel.ScheduleAdapter;
import com.cn.uca.animate.ScaleInOutTransformer;
import com.cn.uca.bean.home.travel.ScheduleBean;
import com.cn.uca.bean.home.travel.TravelInfoBean;
import com.cn.uca.config.BannerConfig;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.banner.OnBannerListener;
import com.cn.uca.loader.GlideImageLoader;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.Banner;
import com.cn.uca.view.HorizontalListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravelDetailActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,more,name,price,photo_num,score,date,alldate,satisfied,assess_picure,buy_insurance,five_star_hotel,icon1,icon2,yuding;
    private Banner banner;
    private WebView webView;
    private HorizontalListView listView;
    private ScheduleAdapter scheduleAdapter;
    private List<ScheduleBean> list;
    private TravelInfoBean bean;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);

        getInfo();
        initView();
        getTourismInfo();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        more = (TextView)findViewById(R.id.more);
        name = (TextView)findViewById(R.id.name);
        price = (TextView)findViewById(R.id.price);
        photo_num = (TextView)findViewById(R.id.photo_num);
        score = (TextView)findViewById(R.id.score);
        banner = (Banner)findViewById(R.id.banner);
        webView = (WebView)findViewById(R.id.webView);
        date = (TextView)findViewById(R.id.date);
        listView = (HorizontalListView)findViewById(R.id.listView);
        alldate = (TextView)findViewById(R.id.alldate);
        satisfied = (TextView)findViewById(R.id.satisfied);
        assess_picure = (TextView)findViewById(R.id.assess_picure);
        yuding = (TextView)findViewById(R.id.yuding);
        back.setOnClickListener(this);
        more.setOnClickListener(this);
        yuding.setOnClickListener(this);

        list = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(list,this);
        listView.setAdapter(scheduleAdapter);
        alldate.setText("全部"+"\n"+"团期");
        buy_insurance = (TextView)findViewById(R.id.buy_insurance);
        five_star_hotel = (TextView)findViewById(R.id.five_star_hotel);
        icon1 = (TextView)findViewById(R.id.icon1);
        icon2 = (TextView)findViewById(R.id.icon2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(ScheduleBean bean : list){
                    bean.setCheck(0);
                }
                list.get(position).setCheck(1);
                scheduleAdapter.setList(list);
            }
        });
    }
    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.more:

                break;
            case R.id.yuding:
                Intent intent = new Intent();
                intent.setClass(this,ReserveTravelActivity.class);
                intent.putParcelableArrayListExtra("date",(ArrayList<? extends Parcelable>) bean.getSchedules());
                startActivity(intent);
                break;
        }
    }
    private void getTourismInfo(){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("tourism_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.getTourismInfo(sign, time_stamp, account_token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<TravelInfoBean>() {
                            }.getType());
                            banner.setImages(bean.getPictures())
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(new OnBannerListener() {
                                        @Override
                                        public void OnBannerClick(int position) {
                                            OpenPhoto.imageUrl(TravelDetailActivity.this,position,(ArrayList<String>) bean.getPictures());
                                        }
                                    })
                                    .setBannerAnimation(ScaleInOutTransformer.class)//翻转动画
                                    .start();
                            banner.updateBannerStyle(BannerConfig.NOT_INDICATOR);
                            price.setText((int) bean.getMin_price()+"");
                            name.setText(bean.getProduct_name());
                            photo_num.setText(bean.getPictures().size()+"张");
                            score.setText(bean.getScore()+"");
                            // 启用支持javascript
                            WebSettings settings = webView.getSettings();
                            settings.setJavaScriptEnabled(true);
                            settings.setAllowFileAccess(true);
                            // WebView加载web资源
                            String url = "http://www.szyouka.com:8080/youkatravel/api/travel/page/tourismIntr.do?tourism_id="+bean.getTourism_id();
                            webView.loadUrl(url);
                            // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
                            webView.setWebViewClient(new WebViewClient() {
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                                    view.loadUrl(url);
                                    return true;
                                }
                            });
                            date.setText(bean.getSchedules().get(0).getSchedule_date()+"("+SystemUtil.dateToWeek(bean.getSchedules().get(0).getSchedule_date())+")出发");
                            list.addAll(bean.getSchedules());
                            list.get(0).setCheck(1);
                            scheduleAdapter.setList(list);
                            if (bean.isAssess_picure()){
                                assess_picure.setText("有图点评");
                            }else{
                                assess_picure.setText("无图无评");
                            }
                            if (bean.isSatisfied()){
                                satisfied.setText("非常满意");
                            }else{
                                satisfied.setText("不满意");
                            }
                            if (bean.isBuy_insurance()){
                                buy_insurance.setTextColor(getResources().getColor(R.color.ori));
                                icon1.setBackgroundResource(R.drawable.checkbox_select);
                            }else{
                                buy_insurance.setTextColor(getResources().getColor(R.color.milky));
                                icon1.setBackgroundResource(R.drawable.checkbox_normal);
                            }
                            if (bean.isFive_star_hotel()){
                                five_star_hotel.setTextColor(getResources().getColor(R.color.ori));
                                icon2.setBackgroundResource(R.drawable.checkbox_select);
                            }else{
                                five_star_hotel.setTextColor(getResources().getColor(R.color.milky));
                                icon2.setBackgroundResource(R.drawable.checkbox_normal);
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
}

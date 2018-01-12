package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.DetailAdapter;
import com.cn.uca.adapter.home.lvpai.TeamPicAdapter;
import com.cn.uca.animate.ScaleInOutTransformer;
import com.cn.uca.bean.home.lvpai.CommodityDetailBean;
import com.cn.uca.bean.home.lvpai.DetailContentBean;
import com.cn.uca.bean.home.lvpai.TeamBean;
import com.cn.uca.config.BannerConfig;
import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.banner.OnBannerListener;
import com.cn.uca.loader.GlideImageLoader;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.server.user.UserHttp;
import com.cn.uca.ui.view.home.samecityka.ActionDetailActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.Banner;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.HorizontalListView;
import com.cn.uca.view.NoScrollListView;
import com.cn.uca.view.RatingStarView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;

/**
 * 商品详情
 */
public class CommodityDetailActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,collection,share,commodityname,price,username,score,place,photo_num,address,purchase_number,call,contentnum,teamcontent,yuding;
    private Banner banner;
    private CircleImageView user_pic;
    private RatingStarView star;
    private ListView listView;
    private DetailAdapter detailAdapter;
    private FluidLayout fluidLayout;
    private int id,chatId,merchantId;
    private String phone,title;
    private List list;
    private List<String> listLable;
    private List<TeamBean> listTeam;
    private TeamPicAdapter teamPicAdapter;
    private HorizontalListView teamPic;
    private LinearLayout chat;
    private LinearLayout gotoMerchant;
    private int isCollection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_detail);

        getInfo();
        initView();
        getCommodityInfo();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }

    }
    private void initView() {
        list = new ArrayList();
        listTeam = new ArrayList<>();
        listLable = new ArrayList<>();
        back = (TextView)findViewById(R.id.back);
        share = (TextView)findViewById(R.id.share);
        collection = (TextView)findViewById(R.id.collection);
        banner = (Banner)findViewById(R.id.banner);
        commodityname = (TextView)findViewById(R.id.commodityname);
        price = (TextView)findViewById(R.id.price);
        username = (TextView)findViewById(R.id.username);
        score = (TextView)findViewById(R.id.score);
        place = (TextView)findViewById(R.id.place);
        photo_num = (TextView)findViewById(R.id.photo_num);
        star = (RatingStarView)findViewById(R.id.star);
        user_pic = (CircleImageView)findViewById(R.id.user_pic);
        address = (TextView)findViewById(R.id.address);
        purchase_number = (TextView)findViewById(R.id.purchase_number);
        call = (TextView)findViewById(R.id.call);
        listView = (ListView)findViewById(R.id.listView);
        contentnum = (TextView)findViewById(R.id.contentnum);
        fluidLayout = (FluidLayout)findViewById(R.id.fluidLayout);
        detailAdapter = new DetailAdapter(list,this);
        listView.setAdapter(detailAdapter);
        gotoMerchant = (LinearLayout)findViewById(R.id.gotoMerchant);
        teamPic = (HorizontalListView)findViewById(R.id.teamPic);
        teamPicAdapter = new TeamPicAdapter(listTeam,this);
        teamPic.setAdapter(teamPicAdapter);
        yuding = (TextView)findViewById(R.id.yuding);

        teamcontent = (TextView)findViewById(R.id.teamcontent);
        chat = (LinearLayout)findViewById(R.id.chat);
        teamPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                teamcontent.setText(listTeam.get(position).getIntroduce());
            }
        });

        back.setOnClickListener(this);
        share.setOnClickListener(this);
        call.setOnClickListener(this);
        chat.setOnClickListener(this);
        collection.setOnClickListener(this);
        gotoMerchant.setOnClickListener(this);
        yuding.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.collection:
                //收藏
                collectionTs();
                break;
            case R.id.share:
                getShare();
                break;
            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.chat:
                if (RongIM.getInstance() != null) {
                      RongIM.getInstance().startPrivateChat(this,chatId+"",title);
                }
                break;
            case R.id.gotoMerchant:
                Intent intent1 = new Intent();
                intent1.setClass(this,MerchantDetailActivity.class);
                intent1.putExtra("id",merchantId);
                startActivity(intent1);
                break;
            case R.id.yuding:
                Intent intent2 = new Intent();
                intent2.setClass(this,DateChoiceActivity.class);
                intent2.putExtra("id",id);
                startActivity(intent2);
                break;
        }
    }

    private void getShare(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        map.put("shareType","LP");
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("id",id);
        String sign = SignUtil.sign(map);
        UserHttp.getShare(account_token, time_stamp, sign, "LP",id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            String share_title = jsonObject.getJSONObject("data").getString("share_title");
                            String web_url = jsonObject.getJSONObject("data").getString("web_url");
                            WeChatManager.instance().sendWebPageToWX(CommodityDetailActivity.this,true,web_url,R.mipmap.logo,share_title,"唯美的旅拍套餐吧！");
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

     //获取商品信息
    private void getCommodityInfo(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("trip_shoot_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.getCommodityInfo(time_stamp, sign, account_token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            final CommodityDetailBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<CommodityDetailBean>() {
                            }.getType());
                            banner.setImages(bean.getPictures())
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(new OnBannerListener() {
                                        @Override
                                        public void OnBannerClick(int position) {
                                            OpenPhoto.imageUrl(CommodityDetailActivity.this,position,(ArrayList<String>) bean.getPictures());
                                        }
                                    })
                                    .setBannerAnimation(ScaleInOutTransformer.class)//翻转动画
                                    .start();
                            banner.updateBannerStyle(BannerConfig.NOT_INDICATOR);
                            phone = bean.getPhone();
                            merchantId= bean.getTrip_shoot_merchant_id();
                            chatId = bean.getAccount_number_id();
                            title = bean.getMerchant_name();
                            if (bean.isCollection()){//已收藏
                                collection.setBackgroundResource(R.mipmap.collection_back_white);
                                isCollection = 1;
                            }else{//未收藏
                                collection.setBackgroundResource(R.mipmap.collection_back_gray);
                                isCollection = 2;
                            }
                            contentnum.setText(bean.getComment_number()+"");
                            commodityname.setText(bean.getTitle());
                            price.setText("￥"+(int)bean.getPrice());
                            place.setText(bean.getDestination());
                            username.setText(bean.getMerchant_name());
                            ImageLoader.getInstance().displayImage(bean.getHead_portrait_url(),user_pic);
                            star.setRating((float)bean.getScore());
                            score.setText(bean.getScore()+"分");
                            address.setText(bean.getTsmAddress().getAddress());
                            purchase_number.setText("已售"+bean.getPurchase_number());
                            fluidLayout.setGravity(Gravity.TOP);
                            listLable = bean.getLable_names();
                            if (bean.getTrip_shoot_type_id() == 1){
                                listLable.add("国内");
                            }else{
                                listLable.add("国外");
                            }
                            for (int a = 0;a<listLable.size();a++){
                                TextView tv = new TextView(CommodityDetailActivity.this);
                                tv.setText(listLable.get(a));
                                tv.setTextSize(12);
                                tv.setBackgroundResource(R.drawable.text_lable_green_border_bg);
                                tv.setTextColor(getResources().getColor(R.color.green4));
                                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(12, 12, 0, 12);
                               fluidLayout.addView(tv, params);
                            }
                            List<DetailContentBean> listDe = new ArrayList<DetailContentBean>();
                            for (int a = 0;a<bean.getSummarys().size();a++){
                                DetailContentBean contentBean = new DetailContentBean();
                                contentBean.setType("【服务概述】");
                                contentBean.setBean(bean.getSummarys().get(a));
                                listDe.add(contentBean);
                            }
                            for (int a = 0;a<bean.getTrips().size();a++){
                                DetailContentBean contentBean = new DetailContentBean();
                                contentBean.setType("【推荐行程】");
                                contentBean.setBean(bean.getTrips().get(a));
                                listDe.add(contentBean);
                            }
                            list.addAll(listDe);
                            list.addAll(bean.getPhotos());
                            list.addAll(bean.getNotices());
                            detailAdapter.setList(list);
                            SetListView.setListViewHeightBasedOnChildren(listView);
                            listTeam = bean.getTeams();
                            if (listTeam.size() < 0){
                                teamPic.setVisibility(View.GONE);
                                teamcontent.setText("暂无团队介绍");
                            }else{
                                teamPicAdapter.setList(listTeam);
                                teamcontent.setText(listTeam.get(0).getIntroduce());
                            }
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

    private void collectionTs(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("trip_shoot_id",id);
        String sign = SignUtil.sign(map);
       HomeHttp.collectionTs(time_stamp, sign, account_token, id, new CallBack() {
           @Override
           public void onResponse(Object response) {
               if ((int) response == 0) {
                   switch (isCollection) {
                       case 1:
                           ToastXutil.show("取消收藏");
                           collection.setBackgroundResource(R.mipmap.collection_back_gray);
                           isCollection = 2;
                           break;
                       case 2:
                           ToastXutil.show("收藏成功");
                           collection.setBackgroundResource(R.mipmap.collection_back_white);
                           isCollection = 1;
                           break;
                   }
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
    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
}

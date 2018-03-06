package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.lvpai.TeamPicAdapter;
import com.cn.uca.adapter.home.lvpai.TripShootsAdapter;
import com.cn.uca.animate.ScaleInOutTransformer;
import com.cn.uca.bean.home.lvpai.MerchantDetailBean;
import com.cn.uca.bean.home.lvpai.TeamBean;
import com.cn.uca.bean.home.lvpai.TripShootsBean;
import com.cn.uca.bean.home.lvpai.setAddressBean;
import com.cn.uca.config.BannerConfig;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.banner.OnBannerListener;
import com.cn.uca.loader.GlideImageLoader;
import com.cn.uca.popupwindows.ShowPopupWindow;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.Banner;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.HorizontalListView;
import com.cn.uca.view.RatingStarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家详情
 */
public class MerchantDetailActivity extends BaseBackActivity implements View.OnClickListener{

    private Banner banner;
    private TextView back,pic_num,username,score,follow,address,phone,call,teamcontent;
    private RatingStarView star;
    private LinearLayout pic_layout,address_layout,personal_tailor,all_commodity;
    private CircleImageView user_pic;
    private int id;
    private String phoneNum;
    private TripShootsAdapter tripShootsAdapter;
    private List<TripShootsBean> list;
    private ListView listView;
    private List<TeamBean> listTeam;
    private TeamPicAdapter teamPicAdapter;
    private HorizontalListView teamPic;
    private List<setAddressBean> listAddress;
    private int isFollow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);
        getInfo();
        initView();
        getMerInfo();
    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getIntExtra("id",0);
        }
    }

    private void initView() {
        banner = (Banner)findViewById(R.id.banner);
        back = (TextView)findViewById(R.id.back);//返回
        pic_num = (TextView)findViewById(R.id.pic_num);//相册数量
        username = (TextView)findViewById(R.id.username);//商家名字
        score = (TextView)findViewById(R.id.score);//评分数字
        follow = (TextView)findViewById(R.id.follow);//关注按钮
        address = (TextView)findViewById(R.id.address);//商家地址
        phone = (TextView)findViewById(R.id.phone);//商家手机号
        call = (TextView)findViewById(R.id.call);//拨打商家手机号
        star = (RatingStarView)findViewById(R.id.star);//星级评分
        pic_layout = (LinearLayout)findViewById(R.id.pic_layout);//相册
        address_layout = (LinearLayout)findViewById(R.id.address_layout);//其他分址
        personal_tailor = (LinearLayout)findViewById(R.id.personal_tailor);//私人订制
        all_commodity = (LinearLayout)findViewById(R.id.all_commodity);//全部商品
        user_pic = (CircleImageView)findViewById(R.id.user_pic);//商家头像
        listView = (ListView)findViewById(R.id.listView);
        back.setOnClickListener(this);
        follow.setOnClickListener(this);
        call.setOnClickListener(this);
        pic_layout.setOnClickListener(this);
        address_layout.setOnClickListener(this);
        personal_tailor.setOnClickListener(this);
        all_commodity.setOnClickListener(this);

        pic_layout.getBackground().setAlpha(120);
        list = new ArrayList<>();//商家推荐
        listTeam = new ArrayList<>();//团队介绍
        listAddress =  new ArrayList<>();
        tripShootsAdapter = new TripShootsAdapter(list,this);
        listView.setAdapter(tripShootsAdapter);
        teamPic = (HorizontalListView)findViewById(R.id.teamPic);
        teamPicAdapter = new TeamPicAdapter(listTeam,this);
        teamPic.setAdapter(teamPicAdapter);

        teamcontent = (TextView)findViewById(R.id.teamcontent);
        teamPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                teamcontent.setText(listTeam.get(position).getIntroduce());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.pic_layout:
                //相册
                Intent intent = new Intent();
                intent.setClass(this,LookPhotoActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
            case R.id.follow:
                //关注
                followMerchant();
                break;
            case R.id.call:
                //拨打电话
                //用intent启动拨打电话
                Intent intent2 = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneNum);
                intent2.setData(data);
                startActivity(intent2);
                break;
            case R.id.address_layout:
                //其他分址
                if (listAddress.size()!= 0){
                    ShowPopupWindow.addresspopuWindow(getWindow().getDecorView(),this,listAddress);
                }else{
                    ToastXutil.show("暂无其他分址");
                }
                break;
            case R.id.personal_tailor:
                //私人订制
                break;
            case R.id.all_commodity:
                //全部商品
                startActivity(new Intent(this,AllCommodityActivity.class));
                break;
        }
    }

    private void getMerInfo(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("trip_shoot_merchant_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.getMerInfo(time_stamp, sign, account_token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code) {
                        case 0:
                            Gson gson = new Gson();
                            final MerchantDetailBean bean = gson.fromJson(jsonObject.getJSONObject("data").toString(), new TypeToken<MerchantDetailBean>() {
                            }.getType());
                            banner.setImages(bean.getPictures())
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(new OnBannerListener() {
                                        @Override
                                        public void OnBannerClick(int position) {
                                            OpenPhoto.imageUrl(MerchantDetailActivity.this,position,(ArrayList<String>) bean.getPictures());
                                        }
                                    })
                                    .setBannerAnimation(ScaleInOutTransformer.class)//翻转动画
                                    .start();
                            banner.updateBannerStyle(BannerConfig.NOT_INDICATOR);
                            phoneNum = bean.getPhone();
                            ImageLoader.getInstance().displayImage(bean.getHead_portrait_url(),user_pic);
                            star.setRating((float)bean.getScore());
                            score.setText(bean.getScore()+"分");
                            username.setText(bean.getMerchant_name());
                            listAddress = bean.getAddress();
                            if (bean.isFollow()){
                                follow.setText("已关注");
                                isFollow = 1;
                            }else{
                                follow.setText("+关注");
                                isFollow = 2;
                            }
                            for (int i = 0;i<bean.getAddress().size();i++){
                                if (bean.getAddress().get(i).isDefault_ars()){
                                    address.setText(bean.getAddress().get(i).getAddress());
                                    break;
                                }
                            }
                            pic_num.setText(bean.getPicture_number()+"");
                            phone.setText(bean.getPhone());
                            list = bean.getTripShoots();
                            if (list.size()>0){
                                tripShootsAdapter.setList(list);
                                SetListView.setListViewHeightBasedOnChildren(listView);
                            }
                            listTeam = bean.getTeams();
                            if (listTeam.size() < 0){
                                teamPic.setVisibility(View.GONE);
                                teamcontent.setText("暂无团队介绍");
                            }else{
                                teamPicAdapter.setList(listTeam);
                                teamcontent.setText(listTeam.get(0).getIntroduce());
                            }
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

    private void followMerchant(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("trip_shoot_merchant_id",id);
        String sign = SignUtil.sign(map);
        HomeHttp.followMerchant(time_stamp, sign, account_token, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                if ((int) response == 0) {
                    switch (isFollow) {
                        case 1:
                            ToastXutil.show("取消关注");
                            follow.setText("+关注");
                            isFollow = 2;
                            break;
                        case 2:
                            ToastXutil.show("关注成功");
                            follow.setText("已关注");
                            isFollow = 1;
                            break;
                    }
                }else if ((int) response == 673){
                    ToastXutil.show("不能关注自己");
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

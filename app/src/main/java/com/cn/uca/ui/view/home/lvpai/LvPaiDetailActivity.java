package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.CallBackBean;
import com.cn.uca.bean.home.lvpai.ImgAndContentBean;
import com.cn.uca.impl.CallBackValue;
import com.cn.uca.ui.fragment.home.lvpai.ServiceOverviewFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;

public class LvPaiDetailActivity extends BaseBackActivity implements CallBackValue{

    private TextView title01,back1,title02,back2,title03,back3,title04;
    private ImgAndContentBean service,recommended,photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv_pai_detail);

        initView();
    }

    private void initView() {
        title01 = (TextView)findViewById(R.id.title01);
        back1 = (TextView)findViewById(R.id.back1);
        title02 = (TextView)findViewById(R.id.title02);
        back2 = (TextView)findViewById(R.id.back2);
        title03 = (TextView)findViewById(R.id.title03);
        back3 = (TextView)findViewById(R.id.back3);
        title04 = (TextView)findViewById(R.id.title04);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new ServiceOverviewFragment()).commit();
    }

    public void setBack(int index){
        switch (index){
            case 0:
                title02.setTextColor(getResources().getColor(R.color.grey2));
                back2.setBackgroundResource(R.mipmap.double_arrow_gray);
                break;
            case 1:
                title02.setTextColor(getResources().getColor(R.color.ori));
                back2.setBackgroundResource(R.mipmap.double_arrow_ori);
                title03.setTextColor(getResources().getColor(R.color.grey2));
                back3.setBackgroundResource(R.mipmap.double_arrow_gray);
                break;
            case 2:
                title03.setTextColor(getResources().getColor(R.color.ori));
                back3.setBackgroundResource(R.mipmap.double_arrow_ori);
                title04.setTextColor(getResources().getColor(R.color.grey2));
                break;
            case 3:
                title04.setTextColor(getResources().getColor(R.color.ori));
                break;
        }
    }
    @Override
    public void sendMessage(int type,Object obj) {
        switch (type){
            case 1:
                service = (ImgAndContentBean) obj;
                Log.e("456",service.toString()+"服务概述");
                break;
            case 2:
                recommended = (ImgAndContentBean) obj;
                Log.e("456",recommended.toString()+"推荐行程");
                break;
            case 3:
                photo = (ImgAndContentBean) obj;
                Log.e("456",photo.toString()+"客照欣赏");
                break;
            case 4:
                Log.e("456",obj.toString()+"说明须知");
                CallBackBean  bean = new CallBackBean();
                bean.setService(service);
                bean.setRecommended(recommended);
                bean.setPhoto(photo);
                bean.setDescription(obj.toString());
//                AddMerchantActivity activity = new AddMerchantActivity();
//                activity.sendMessage(5,bean);
                AddMerchantActivity.getMessage(bean);
                LvPaiDetailActivity.this.finish();
                break;
        }
    }
}

package com.cn.uca.ui.view.home.raider;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.ui.fragment.home.raider.GLCollectionFragment;
import com.cn.uca.ui.fragment.home.raider.GLLookFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ToastXutil;

import java.util.ArrayList;

public class RaiderCollectionActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private GLCollectionFragment collectionFragment;
    private GLLookFragment lookFragment;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private TextView title01,title02,edit;
    private int select = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raider_collection);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        mPager = (ViewPager) findViewById(R.id.content);

        back.setOnClickListener(this);
        title01 = (TextView)findViewById(R.id.title01);
        title02 = (TextView)findViewById(R.id.title02);
        edit = (TextView)findViewById(R.id.edit);
        edit.setOnClickListener(this);
        title01.setOnClickListener(new MyOnClickListener(0));
        title02.setOnClickListener(new MyOnClickListener(1));
        collectionFragment = new GLCollectionFragment();
        lookFragment = new GLLookFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(collectionFragment);
        fragmentList.add(lookFragment);
        mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragmentList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(onPageChangeListener);

    }


    private class MyOnClickListener implements View.OnClickListener{
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View v) {
            //通过ViewPager的方法setCurrentItem(positon), c此时ViewPager会切换到position所对应item
            if (index == 0){
                edit.setVisibility(View.GONE);
            }else{
                edit.setVisibility(View.VISIBLE);
            }
            mPager.setCurrentItem(index);
        }
    }
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            show(position);
            if (position == 0){
                edit.setVisibility(View.GONE);
                select = 1;
                edit.setText("编辑");
                lookFragment.cc();
            }else{
                edit.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.edit:
                //刷新适配器
                switch (select){
                    case 1:
                        edit.setText("取消");
                        lookFragment.aa();
                        select = 2;
                        break;
                    case 2:
                        edit.setText("编辑");
                        lookFragment.bb();
                        select = 1;
                        break;
                }
                break;
        }
    }



    private void show(int index){
        switch (index){
            case 0:
                title01.setTextColor(getResources().getColor(R.color.ori));
                title01.setBackgroundResource(R.color.white);

                title02.setTextColor(getResources().getColor(R.color.gray2));
                title02.setBackgroundResource(R.color.gray);
                break;
            case 1:
                title01.setTextColor(getResources().getColor(R.color.gray2));
                title01.setBackgroundResource(R.color.gray);

                title02.setTextColor(getResources().getColor(R.color.ori));
                title02.setBackgroundResource(R.color.white);

                break;
        }
    }

//    private void getMyRaider(int type){
//        Map<String,Object> map = new HashMap<>();
//        String account_token = SharePreferenceXutil.getAccountToken();
//        map.put("account_token",account_token);
//        map.put("page",page);
//        map.put("pageCount",pageCount);
//        String time_stamp = SystemUtil.getCurrentDate2();
//        map.put("time_stamp",time_stamp);
//        map.put("type",type);
//        String sign = SignUtil.sign(map);
//        HomeHttp.getMyRaiders(page, pageCount, sign, time_stamp, account_token, type, new CallBack() {
//            @Override
//            public void onResponse(Object response) {
//                try{
//                    JSONObject jsonObject = new JSONObject(response.toString());
//                    int code = jsonObject.getInt("code");
//                    switch (code){
//                        case 0:
//                            Gson gson = new Gson();
//                            List<RaidersBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("myRaidersRets").toString(),new TypeToken<List<RaidersBean>>() {
//                            }.getType());
//                            if (bean.size() > 0){
//                                list.clear();
//                                list.addAll(bean);
//                                adapter.setList(list);
//                            }else{
//                                if (list.size() != 0){
//                                    ToastXutil.show("没有更多数据了");
//                                }else{
//                                    adapter.setList(list);
//                                }
//                            }
//                            break;
//                    }
//                }catch (Exception e){
//
//                }
//            }
//
//            @Override
//            public void onErrorMsg(String errorMsg) {
//
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//
//            }
//        });
//    }
}

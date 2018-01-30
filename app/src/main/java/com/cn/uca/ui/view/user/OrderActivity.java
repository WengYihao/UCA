package com.cn.uca.ui.view.user;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.FragmentAdapter;
import com.cn.uca.adapter.user.OrderTypeAdapter;
import com.cn.uca.bean.user.order.OrderTypeBean;
import com.cn.uca.ui.fragment.user.order.HotelFragment;
import com.cn.uca.ui.fragment.user.order.LvPaiFragment;
import com.cn.uca.ui.fragment.user.order.PlantTicketFragment;
import com.cn.uca.ui.fragment.user.order.SameCityKaFragment;
import com.cn.uca.ui.fragment.user.order.SpotTicketFragment;
import com.cn.uca.ui.fragment.user.order.TravelFragment;
import com.cn.uca.ui.fragment.user.order.YueKaFragment;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.view.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private ArrayList<Fragment> fragmentList;
    private PlantTicketFragment ticketFragment;
    private HotelFragment hotelFragment;
    private TravelFragment travelFragment;
    private SameCityKaFragment sameCityKaFragment;
    private YueKaFragment yueKaFragment;
    private LvPaiFragment lvPaiFragment;
    private SpotTicketFragment spotTicketFragment;
    private ViewPager viewPager;
    private HorizontalListView type;
    private List<OrderTypeBean> list;
    private OrderTypeAdapter adapter;
    private String [] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
        initFragment();
    }


    private void initFragment() {
        ticketFragment = new PlantTicketFragment();
        hotelFragment = new HotelFragment();
        travelFragment = new TravelFragment();
        sameCityKaFragment = new SameCityKaFragment();
        yueKaFragment =  new YueKaFragment();
        lvPaiFragment = new LvPaiFragment();
        spotTicketFragment = new SpotTicketFragment();
        fragmentList.add(ticketFragment);
        fragmentList.add(hotelFragment);
        fragmentList.add(travelFragment);
        fragmentList.add(sameCityKaFragment);
        fragmentList.add(yueKaFragment);
        fragmentList.add(lvPaiFragment);
        fragmentList.add(spotTicketFragment);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(onPageChangeListener);
    }

    private void initView() {
        type = (HorizontalListView)findViewById(R.id.typeName);
        data = getResources().getStringArray(R.array.order_type);
        list = new ArrayList<>();
        OrderTypeBean bean = null;
        for (int i = 0;i<data.length;i++){
            bean = new OrderTypeBean();
            if (i==0){
                bean.setCheck(true);
                bean.setName(data[i]);
            }else{
                bean.setCheck(false);
                bean.setName(data[i]);
            }
            list.add(bean);
        }
        adapter = new OrderTypeAdapter(list,this);
        type.setAdapter(adapter);
        fragmentList = new ArrayList<>();
        back = (TextView)findViewById(R.id.back);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        back.setOnClickListener(this);

        type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (OrderTypeBean typeBean :list){
                    typeBean.setCheck(false);
                }
                list.get(position).setCheck(true);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(position);
            }
        });
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            for (OrderTypeBean typeBean :list){
                typeBean.setCheck(false);
            }
            list.get(position).setCheck(true);
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
        }
    }
}

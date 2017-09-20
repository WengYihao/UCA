package com.cn.uca.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.YueKaAdapter;
import com.cn.uca.bean.yueka.GetEscortBean;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.yueka.YueKaBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.datepicker.OnDoubleSureLisener;
import com.cn.uca.ui.OrderYueActivity;
import com.cn.uca.ui.YueChatActivity;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.view.datepicker.DoubleDatePickDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2017/8/11.
 */

public class YueKaFragment extends Fragment implements View.OnClickListener,OnDoubleSureLisener{

    private View view;
    private ListView listView;
    private YueKaAdapter yueKaAdapter;
    private List<YueKaBean> list;
    private TextView orderYue,startTime,endTime,freeTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yueka, null);

        ininView();
        getEscortRecords();
        return view;
    }

    private void ininView() {
        orderYue = (TextView)view.findViewById(R.id.orderYue);
        listView  = (ListView)view.findViewById(R.id.listView);
        startTime = (TextView)view.findViewById(R.id.startTime);
        endTime = (TextView)view.findViewById(R.id.endTime);
        freeTime = (TextView)view.findViewById(R.id.freeTime);

        orderYue.setOnClickListener(this);
        freeTime.setOnClickListener(this);

        list = new ArrayList<>();
        yueKaAdapter = new YueKaAdapter(list,getActivity());
        listView.setAdapter(yueKaAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),YueChatActivity.class);
                for (int a = 0;a < list.get(i).getEscortRecords().size();a++){
                    int id = list.get(i).getEscortRecords().get(a).getEscort_record_id();
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            }
        });
        startTime.setText(SystemUtil.getCurrentDateOnly2());
        endTime.setText(SystemUtil.getFetureDate(10));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.orderYue:
                startActivity(new Intent(getActivity(), OrderYueActivity.class));
                break;
            case R.id.freeTime:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
        }
    }

    private void getEscortRecords(){
        GetEscortBean bean = new GetEscortBean();
        bean.setPage(1);
        bean.setPageCount(5);
        bean.setAccount_token(SharePreferenceXutil.getAccountToken());
        bean.setGaode_code("0755");
        bean.setBeg_time("");
        bean.setEnd_time("");
        MyApplication.getServer().getEscortRecords(bean, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200){
                    try {
                        JSONObject jsonObject =new JSONObject(new String(bytes,"UTF-8"));
                        int code  = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                JSONObject object = jsonObject.getJSONObject("data");
                                Gson gson = new Gson();
                                YueKaBean bean = gson.fromJson(object.toString(),new TypeToken<YueKaBean>(){}.getType());
                                list.add(bean);
                                yueKaAdapter.setList(list);
                                break;
                        }
                    }catch (Exception e){
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    JSONObject jsonObject =new JSONObject(new String(bytes,"UTF-8"));
                    Log.i("456",jsonObject.toString()+"====");
                }catch (Exception e){

                }

            }
        });
    }

    private void showDatePickDialog(DateType type) {
        DoubleDatePickDialog dialog = new DoubleDatePickDialog(getActivity());
        //设置上下年分限制
        dialog.setYearLimt(0);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnDoubleSureLisener(this);
        dialog.show();
    }
    @Override
    public void onSure(Date dateStart, Date dateEnd) {
        startTime.setText(SystemUtil.UtilDateToString(dateStart));
        endTime.setText(SystemUtil.UtilDateToString(dateEnd));
    }
}

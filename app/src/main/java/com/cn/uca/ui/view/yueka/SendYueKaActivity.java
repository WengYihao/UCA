package com.cn.uca.ui.view.yueka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.yueka.ReleaseEscortRecordBean;
import com.cn.uca.bean.yueka.SendYueKaBean;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.datepicker.OnDoubleSureLisener;
import com.cn.uca.impl.yueka.OnSelectItemListener;
import com.cn.uca.popupwindows.LinePopupWindow;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.HorizontalListView;
import com.cn.uca.view.MyEditText;
import com.cn.uca.view.datepicker.DoubleDatePickDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendYueKaActivity extends BaseBackActivity implements View.OnClickListener,OnDoubleSureLisener {

    private Map<Integer,String> map;
    private SendYueKaBean bean;
    private TextView back,line_name,moreLine,explainMore,moreDetails,price,average_score_content,dayDiscountContent,numberDiscountContent,startTime,endTime,send;
    private MyEditText yueka_name;
    private EditText peopleNum;
    private HorizontalListView lineName;
    private CheckBox baoche;
    private LinearLayout explain, num;
    private RelativeLayout supplement,freeTime;
    private String timeForStart,timeForEnd;
    private ReleaseEscortRecordBean escortRecordBean;
    private int id;
    private List<String> list;
    private boolean isShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_send_yue_ka);

        initView();
        getReleaseInfo();
    }

    private void initView() {
        list = new ArrayList<>();
        map = new HashMap<>();
        bean = new SendYueKaBean();
        escortRecordBean = new ReleaseEscortRecordBean();
        back = (TextView)findViewById(R.id.back);
        line_name = (TextView)findViewById(R.id.line_name);
        moreLine = (TextView)findViewById(R.id.moreLine);
        price = (TextView)findViewById(R.id.price);
        average_score_content = (TextView)findViewById(R.id.average_score_content);
        dayDiscountContent = (TextView)findViewById(R.id.dayDiscountContent);
        numberDiscountContent = (TextView)findViewById(R.id.numberDiscountContent);
        yueka_name = (MyEditText)findViewById(R.id.yueka_name);
        baoche = (CheckBox)findViewById(R.id.baoche);
        peopleNum = (EditText)findViewById(R.id.peopleNum);
        num = (LinearLayout)findViewById(R.id.num);
        explainMore = (TextView)findViewById(R.id.explainMore);
        explain = (LinearLayout)findViewById(R.id.explain);
        moreDetails = (TextView)findViewById(R.id.moreDetails);
        baoche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    num.setVisibility(View.VISIBLE);
                }else{
                    num.setVisibility(View.GONE);
                }
            }
        });

        supplement = (RelativeLayout)findViewById(R.id.supplement);
        freeTime = (RelativeLayout)findViewById(R.id.freeTime);
        startTime = (TextView)findViewById(R.id.startTime);
        endTime  = (TextView)findViewById(R.id.endTime);

        send = (TextView)findViewById(R.id.send);
        send.setOnClickListener(this);
        explainMore.setOnClickListener(this);
        back.setOnClickListener(this);
        moreLine.setOnClickListener(this);
        supplement.setOnClickListener(this);
        freeTime.setOnClickListener(this);

        startTime.setText(SystemUtil.getFetureDate(1));
        endTime.setText(SystemUtil.getFetureDate(10));

    }

    private void getReleaseInfo(){
        YueKaHttp.getReleaseInfo(new CallBack() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code  = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject object = jsonObject.getJSONObject("data");
                            Gson gson = new Gson();
                            bean = gson.fromJson(object.toString(),new TypeToken<SendYueKaBean>(){}.getType());
                            price.setText("￥"+bean.getAverage_score_price()+"/天");
                            average_score_content.setText(bean.getAverage_score_content());
                            dayDiscountContent.setText(bean.getDayDiscountContent());
                            numberDiscountContent.setText(bean.getNumberDiscountContent());
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

    private void releaseEscortRecord(){
        String lineName = yueka_name.getText().toString();
        if (!StringXutil.isEmpty(lineName)){
            escortRecordBean.setEscort_record_name(lineName);
        }else{
            ToastXutil.show("约咖名称不能为空");
        }
        if (!StringXutil.isEmpty(peopleNum.getText().toString().trim())){
            int num = Integer.parseInt(peopleNum.getText().toString().trim());
            escortRecordBean.setRequirement_people_number(num);
        }else{
            ToastXutil.show("最大人数不能为空");
        }
        if (!StringXutil.isEmpty(timeForStart)){
            escortRecordBean.setBeg_time(timeForStart);
        }else{
            escortRecordBean.setBeg_time(startTime.getText().toString());
        }
        if (!StringXutil.isEmpty(timeForEnd)){
            escortRecordBean.setEnd_time(timeForEnd);
        }else {
            escortRecordBean.setEnd_time(endTime.getText().toString());
        }
        if (id != 0){
            escortRecordBean.setEscort_details_id(id);
        }else{
            ToastXutil.show("编辑的详情没有保存");
        }
        escortRecordBean.setAccount_token(SharePreferenceXutil.getAccountToken());
        Gson gson = new Gson();
        escortRecordBean.setReleaseServices(gson.toJson(list));
        YueKaHttp.releaseEscortRecord(escortRecordBean, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200){
                        JSONObject jsonObject =new JSONObject(new String(bytes,"UTF-8"));
                        Log.i("123",jsonObject.toString());
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                ToastXutil.show("发布成功");
                                SendYueKaActivity.this.finish();
                                break;
                            case 121:
                                ToastXutil.show("日期格式不合法");
                                break;
                            case 125:
                                ToastXutil.show("发布时间大于15天");
                                break;
                            case 130:
                                ToastXutil.show("人数不能超过40");
                                break;
                            case 138:
                                ToastXutil.show("次路线的路线点过少请添加后再次尝试");
                                break;
                            case 145:
                                ToastXutil.show("伴游用户已发布信息");
                                break;
                            default:
                                ToastXutil.show("发布失败");
                                break;
                        }
                    }
                }catch (Exception e){
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.moreLine:
                for (int i = 0;i<bean.getEscortAllRoutes().size();i++){
                    map.put(bean.getEscortAllRoutes().get(i).getRoute_id(),bean.getEscortAllRoutes().get(i).getRoute_name());
                }
                LinePopupWindow window = new LinePopupWindow(SendYueKaActivity.this,map,line_name);
                window.setOnSelectItemListener(new OnSelectItemListener() {
                    @Override
                    public void selectItem(String name, int type) {
                       line_name.setText(name);
                        escortRecordBean.setRoute_id(type);
                    }
                });
                break;
            case R.id.supplement:
                Intent intent = new Intent();
                intent.setClass(SendYueKaActivity.this,DetailsWebActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.freeTime:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.send:
                releaseEscortRecord();
                break;
            case R.id.explainMore:
                if (isShow){
                    explain.setVisibility(View.VISIBLE);
                    explainMore.setBackgroundResource(R.mipmap.down);
                    isShow = false;
                }else {
                    explain.setVisibility(View.GONE);
                    explainMore.setBackgroundResource(R.mipmap.right_gray);
                    isShow = true;
                }
                break;
        }
    }
    private void showDatePickDialog(DateType type) {
        DoubleDatePickDialog dialog = new DoubleDatePickDialog(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if (data != null){
                    id = Integer.parseInt(data.getStringExtra("id"));
                    moreDetails.setText("详情"+id);
                }else{
                    moreDetails.setText("发布的详情暂未保存");
                    id = 0;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSure(Date dateStart, Date dateEnd) {
        if (dateStart.after(dateEnd)){
            ToastXutil.show("开始时间不能大于结束时间");
        }else{
            Date date = new Date();
            if (dateStart.getTime() < date.getTime()){
                ToastXutil.show("开始时间必须大于今天");
            }else{
                timeForStart = SystemUtil.UtilDateToString(dateStart);
                timeForEnd = SystemUtil.UtilDateToString(dateEnd);
                startTime.setText(timeForStart);
                endTime.setText(timeForEnd);
            }
        }
    }
}

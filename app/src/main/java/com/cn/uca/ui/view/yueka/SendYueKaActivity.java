package com.cn.uca.ui.view.yueka;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.yueka.LineNameAdapter;
import com.cn.uca.adapter.yueka.PointAdapter;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.bean.yueka.LineNameBean;
import com.cn.uca.bean.yueka.ReleaseEscortRecordBean;
import com.cn.uca.bean.yueka.SendYueKaBean;
import com.cn.uca.bean.yueka.SendYueka;
import com.cn.uca.bean.yueka.ServicesBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.impl.datepicker.OnDoubleSureLisener;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StatusMargin;
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

    private SendYueKaBean bean;
    private TextView back,more,line_name,explainMore,moreDetails,price,average_score_content,startTime,endTime,send;
    private MyEditText yueka_name,servicePprice;
    private EditText peopleNum;
    private HorizontalListView lineName;
    private RadioButton one,three;
    private CheckBox baoche;
    private LinearLayout explain, num;
    private RelativeLayout supplement,freeTime;
    private String timeForStart,timeForEnd;
    private ReleaseEscortRecordBean escortRecordBean;
    private int id;
    private List<LineNameBean> list;
    private List<String> linePoint;
    private boolean isShow = true;
    private PointAdapter pointAdapter;
    private int escortId,roadId;
    private String line_Name = "";
    private SendYueka yuekaBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_yue_ka);

        initView();
        getInfo();

    }

    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            escortId = intent.getIntExtra("id",0);
            line_Name = intent.getStringExtra("line");
            roadId = intent.getIntExtra("roadId",0);
            if (escortId != 0){
                getERInfo(escortId);
            }
        }
    }
    private void initView() {
        list = new ArrayList<>();
        linePoint = new ArrayList<>();
        bean = new SendYueKaBean();
        escortRecordBean = new ReleaseEscortRecordBean();
        back = (TextView)findViewById(R.id.back);
        more = (TextView)findViewById(R.id.more);
        line_name = (TextView)findViewById(R.id.line_name);
        lineName = (HorizontalListView)findViewById(R.id.lineName);
        pointAdapter = new PointAdapter(linePoint,this);
        lineName.setAdapter(pointAdapter);
        price = (TextView)findViewById(R.id.price);
        average_score_content = (TextView)findViewById(R.id.average_score_content);
        yueka_name = (MyEditText)findViewById(R.id.yueka_name);
        servicePprice = (MyEditText)findViewById(R.id.servicePprice);
        baoche = (CheckBox)findViewById(R.id.baoche);
        one = (RadioButton)findViewById(R.id.one);
        three = (RadioButton)findViewById(R.id.three);
        one.setChecked(true);
        one.setTextColor(getResources().getColor(R.color.white));
        one.setBackgroundColor(getResources().getColor(R.color.ori));
        peopleNum = (EditText)findViewById(R.id.peopleNum);
        num = (LinearLayout)findViewById(R.id.num);
        explainMore = (TextView)findViewById(R.id.explainMore);
        explain = (LinearLayout)findViewById(R.id.explain);
        moreDetails = (TextView)findViewById(R.id.moreDetails);
        one.setOnClickListener(this);
        three.setOnClickListener(this);
        line_name.setOnClickListener(this);
        baoche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    num.setVisibility(View.VISIBLE);
                    baoche.setBackgroundResource(R.drawable.checkbox_select);
                }else{
                    num.setVisibility(View.GONE);
                    baoche.setBackgroundResource(R.drawable.checkbox_normal);
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
        more.setOnClickListener(this);
        supplement.setOnClickListener(this);
        freeTime.setOnClickListener(this);

        startTime.setText(SystemUtil.getFetureDate(1));
        endTime.setText(SystemUtil.getFetureDate(10));

        getReleaseInfo();
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
                            price.setText("￥"+(int)bean.getAverage_score_price()+"/天");
                            average_score_content.setText(bean.getAverage_score_content()+"\n"+bean.getDayDiscountContent()+"\n"+bean.getNumberDiscountContent());
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
            ToastXutil.show("路线名称不能为空");
        }
        if (escortRecordBean.getRoute_id() == 0){
            ToastXutil.show("请选择一条路线");
        }else{
            if (!StringXutil.isEmpty(peopleNum.getText().toString().trim())){
                try{
                    int num = Integer.parseInt(peopleNum.getText().toString().trim());
                    escortRecordBean.setRequirement_people_number(num);
                }catch (Exception e){
                    ToastXutil.show("请输入数字");
                }
            }else{
                if (one.isChecked()){
                    escortRecordBean.setRequirement_people_number(1);
                }else if(three.isChecked()){
                    escortRecordBean.setRequirement_people_number(3);
                }
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
            if (baoche.isChecked()){
                Gson gson = new Gson();
                ServicesBean bean = new ServicesBean();
                bean.setPrice(Double.parseDouble(servicePprice.getText().toString()));
                bean.setService_function_id(1);
                List<ServicesBean> list = new ArrayList<>();
                list.add(bean);
                String json = gson.toJson(list);
                escortRecordBean.setReleaseServices(json);
            }else{
                escortRecordBean.setReleaseServices("[]");
            }
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
                                case 411:
                                    ToastXutil.show("当前日期已有安排");
                                    break;
                                case 164:
                                    ToastXutil.show("约咖天数不能超过15天 ");
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

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.more:
                startActivity(new Intent(SendYueKaActivity.this,PresetManagerActivity.class));
                break;
            case R.id.one:
                one.setBackgroundColor(getResources().getColor(R.color.ori));
                one.setTextColor(getResources().getColor(R.color.white));
                three.setTextColor(getResources().getColor(R.color.grey2));
                three.setBackgroundColor(getResources().getColor(R.color.gray));
                break;
            case R.id.three:
                three.setBackgroundColor(getResources().getColor(R.color.ori));
                three.setTextColor(getResources().getColor(R.color.white));
                one.setTextColor(getResources().getColor(R.color.grey2));
                one.setBackgroundColor(getResources().getColor(R.color.gray));
                break;
            case R.id.line_name:
                showNameDialog();
                break;
            case R.id.supplement:
                Intent intent = new Intent();
                intent.setClass(SendYueKaActivity.this,SendYueKaDetailctivity.class);
                if (escortId != 0){
                    intent.putParcelableArrayListExtra("list",(ArrayList<? extends Parcelable>) yuekaBean.getParagraphs());
                    startActivityForResult(intent,0);
                }else{
                    startActivityForResult(intent,0);
                }
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

    private void showNameDialog(){
        int totalHeight = 0;
        final Dialog dialog = new Dialog(this,R.style.dialog_style);
        View inflate = LayoutInflater.from(this).inflate(R.layout.choose_city_dialog, null);
        final ListView listView = (ListView)inflate.findViewById(R.id.listView);
        Button btn_cancel = (Button)inflate.findViewById(R.id.btn_cancel);
        btn_cancel.setText("路线预设");
        list.clear();
        for (int i = 0;i<bean.getEscortAllRoutes().size();i++){
            LineNameBean nameBean = new LineNameBean();
            nameBean.setLine_id(bean.getEscortAllRoutes().get(i).getRoute_id());
            nameBean.setLine_name(bean.getEscortAllRoutes().get(i).getRoute_name());
            list.add(nameBean);
        }
        if (list.size() == 0){
            listView.setVisibility(View.GONE);
        }
        LineNameAdapter adapter = new LineNameAdapter(list,this);
        listView.setAdapter(adapter);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(SendYueKaActivity.this,PresetManagerActivity.class));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                line_name.setText(list.get(i).getLine_name());
                escortRecordBean.setRoute_id(list.get(i).getLine_id());
                linePoint.clear();
                for (int a = 0;a < bean.getEscortAllRoutes().size();a++){
                    if (bean.getEscortAllRoutes().get(a).getRoute_id() == list.get(i).getLine_id()){
                        for (int b = 0;b <bean.getEscortAllRoutes().get(a).getPlaces().size();b++){
                            linePoint.add(bean.getEscortAllRoutes().get(a).getPlaces().get(b).getDeparture_address());
                        }
                    }
                }
                pointAdapter.setList(linePoint);
                dialog.dismiss();
            }
        });
        for (int i = 0; i<adapter.getCount();i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            int list_child_item_height = listItem.getMeasuredHeight() + listView.getDividerHeight();
            totalHeight += list_child_item_height; // 统计所有子项的总高度
        }
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        if (totalHeight > MyApplication.height*4/9){
            SetLayoutParams.setFrameLayout(inflate, MyApplication.width,MyApplication.height*4/9);
        }else{
            SetLayoutParams.setFrameLayout(inflate, MyApplication.width,totalHeight+20+SystemUtil.dip2px(45));
        }

        StatusMargin.setFrameLayoutBottom(this,inflate,20);
        dialog.show();//显示对话框
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
                    id = data.getIntExtra("id",0);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }

    private void getERInfo(int id){
        Map<String ,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token",account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("escort_record_id",id);
        String sign = SignUtil.sign(map);
        YueKaHttp.getERInfo(account_token, time_stamp, sign, id, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            yuekaBean = gson.fromJson(jsonObject.getJSONObject("data").toString(),new TypeToken<SendYueka>() {
                            }.getType());
                            yueka_name.setText(yuekaBean.getEscort_record_name());
                            line_name.setText(line_Name);
                            for (int i = 0;i<yuekaBean.getServiceFunctions().size();i++){
                                if (yuekaBean.getServiceFunctions().get(i).getService_function_id() == 1){
                                    baoche.setChecked(true);
                                    num.setVisibility(View.VISIBLE);
                                    servicePprice.setText((int)yuekaBean.getServiceFunctions().get(i).getPrice()+"");
                                }
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

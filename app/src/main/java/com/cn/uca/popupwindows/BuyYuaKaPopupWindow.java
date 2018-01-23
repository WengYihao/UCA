package com.cn.uca.popupwindows;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.bean.yueka.SubmitYuekaBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.yueka.YueKaHttp;
import com.cn.uca.ui.view.user.RechargeSettingActivity;
import com.cn.uca.ui.view.user.WalletPasswordActivity;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.SharePreferenceXutil;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.PickerScrollView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/11/30.
 */

public class BuyYuaKaPopupWindow{
    private PopupWindow popupWindow;
    private Context context;
    private View view;
    private int maxNum;
    private int num = 1;
    private TextView price,end_time,add,reduce,submit,sum;
    private CircleImageView pic;
    private SubmitYuekaBean bean;
    private Date startDate,endDate;
    private List<String> StringData;
    private String selectStartTime,selectEndTime;

    public BuyYuaKaPopupWindow(Context context, View view,List<String>StringData, SubmitYuekaBean bean){
        this.context = context;
        this.view = view;
        this.StringData = StringData;
        this.bean = bean;
        showChatInfo();
    }
    private void showChatInfo(){
        maxNum = bean.getRequirement_people_number();
        View inflate = LayoutInflater.from(context).inflate(R.layout.submit_yueka_dialog, null);
        pic = (CircleImageView)inflate.findViewById(R.id.pic);
        price = (TextView)inflate.findViewById(R.id.price);
        end_time = (TextView)inflate.findViewById(R.id.endTime);
        add = (TextView)inflate.findViewById(R.id.add);
        reduce = (TextView)inflate.findViewById(R.id.reduce);
        submit = (TextView)inflate.findViewById(R.id.submit);
        sum = (TextView)inflate.findViewById(R.id.sum);
        sum.setText("1");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num += 1;
                if (num <= maxNum){
                    ToastXutil.show(num+"");
                    sum.setText(num+"");
                    if (selectStartTime != null && selectEndTime != null){
                        countPrice(num,selectStartTime,selectEndTime,price);
                    }
                }else{
                    ToastXutil.show("人数超限");
                }
            }
        });
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num>1){
                    num -= 1;
                    ToastXutil.show(num+"");
                    sum.setText(num+"");
                    if (selectStartTime != null && selectEndTime != null){
                        countPrice(num,selectStartTime,selectEndTime,price);
                    }
                }else{
                    ToastXutil.show("人数不能为0");
                }

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectStartTime != null){
                    if (selectEndTime != null){
                        purchaseEscor();
                    }else{
                        ToastXutil.show("结束时间不能为空");
                    }
                }else{
                    ToastXutil.show("开始时间不能为空");
                }

            }
        });
        ImageLoader.getInstance().displayImage(bean.getUser_head_portrait(),pic);
        price.setText("￥"+(int)bean.getMin_consumption());
        end_time.setText("截止时间："+bean.getEnd_time());
        PickerScrollView minute_pv = (PickerScrollView)inflate.findViewById(R.id.minute_pv);
        minute_pv.setColor(context.getResources().getColor(R.color.black));
        PickerScrollView second_pv = (PickerScrollView) inflate.findViewById(R.id.second_pv);
        second_pv.setColor(context.getResources().getColor(R.color.black));

        minute_pv.setData(StringData,0);
        second_pv.setData(StringData);
        minute_pv.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(String pickers) {
                try{
                    startDate = SystemUtil.StringToUtilDate(pickers);
                    if (startDate.before(new Date())){
                        ToastXutil.show("该时间已过期");
                    }else{
                        if(startDate.after(SystemUtil.StringToUtilDate(bean.getEnd_time()))){
                            ToastXutil.show("该时间已超截止时间");
                        }else{
                            if(endDate != null){
                                if(startDate.equals(endDate)){
                                    ToastXutil.show("开始时间不能等于结束时间");
                                }else{
                                    if(!startDate.before(endDate)){
                                        ToastXutil.show("开始时间不能大于结束时间");
                                    }else{
                                        selectStartTime = pickers;
//                                        if (selectStartTime != null && selectEndTime != null){
//                                            countPrice(num,selectStartTime,selectEndTime,price);
//                                        }
                                    }
                                }
                            }else{
                                selectStartTime = pickers;
//                                if (selectStartTime != null && selectEndTime != null){
//                                    countPrice(num,selectStartTime,selectEndTime,price);
//                                }
                            }
                        }
                    }
                }catch (Exception e){
                }
            }
        });
        second_pv.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(String pickers) {
                try{
                    endDate = SystemUtil.StringToUtilDate(pickers);
                    if (endDate.before(new Date())){
                        ToastXutil.show("该时间已过期");
                    }else{
                        if(endDate.after(SystemUtil.StringToUtilDate(bean.getEnd_time()))){
                            ToastXutil.show("该时间已超截止时间");
                        }else{
                            if(startDate != null){
                                if (startDate.equals(endDate)){
                                    ToastXutil.show("开始时间不能等于结束时间");
                                }else{
                                    if (!startDate.before(endDate)){
                                        ToastXutil.show("开始时间不能大于结束时间");
                                    }else{
                                        selectEndTime = pickers;
//                                        if (selectStartTime != null && selectEndTime != null){
//                                            countPrice(num,selectStartTime,selectEndTime,price);
//                                        }
                                    }
                                }
                            }else{
                                selectEndTime = pickers;
//                                if (selectStartTime != null && selectEndTime != null){
//                                    countPrice(num,selectStartTime,selectEndTime,price);
//                                }
                            }
                        }
                    }
                }catch (Exception e){
                    Log.i("123",e.getMessage());
                }
            }
        });
        popupWindow = new PopupWindow(inflate,MyApplication.width,MyApplication.height*2/3);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
        popupWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
    }
    private void purchaseEscor(){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("actual_number",num);
        map.put("beg_time",selectStartTime) ;
        map.put("end_time",selectEndTime);
        map.put("escort_record_id",bean.getEscort_record_id()) ;
        map.put("escort_service_ids","") ;
        String sign = SignUtil.sign(map);
        YueKaHttp.purchaseEscor(account_token, num, selectStartTime, selectEndTime, bean.getEscort_record_id(), sign, time_stamp, "", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            ToastXutil.show("下单成功，等待领咖确认。");
                            popupWindow.dismiss();
                            break;
                        case 201:
                            ToastXutil.show("您已订购此陪游");
                            popupWindow.dismiss();
                            break;
                        case 191:
                            ToastXutil.show("不能购买个人商品  ");
                            popupWindow.dismiss();
                            break;
                        case 275:
                            ToastXutil.show("请先认证");
                            break;
                        case 740:
                            ToastXutil.show("请先设置支付密码");
                            Intent intent = new Intent();
                            intent.setClass(context, WalletPasswordActivity.class);
                            intent.putExtra("type",2);
                            context.startActivity(intent);
                            popupWindow.dismiss();
                            break;
                        default:
                            ToastXutil.show("订购失败");
                            break;
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {
                ToastXutil.show(errorMsg);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    private void countPrice(int num,String startTime,String endTime,final TextView view){
        Map<String,Object> map = new HashMap<>();
        String account_token = SharePreferenceXutil.getAccountToken();
        List<String> list = new ArrayList<>();
        Gson gson = new Gson();
        map.put("account_token", account_token);
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("actual_number",num);
        map.put("beg_time",startTime) ;
        map.put("end_time",endTime);
        map.put("escort_record_id",bean.getEscort_record_id()) ;
        map.put("escort_service_ids","") ;
        String sign = SignUtil.sign(map);
        YueKaHttp.calculationPrice(account_token, num, startTime, endTime, bean.getEscort_record_id(), sign, time_stamp, "", new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            double priceCount = jsonObject.getJSONObject("data").getDouble("price");
                            view.setText("￥"+priceCount);
                            break;
                        case 201:
                            ToastXutil.show("你已订购次陪游");
                            break;
                    }
                }catch (Exception e){
                    Log.i("456",e.getMessage());
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

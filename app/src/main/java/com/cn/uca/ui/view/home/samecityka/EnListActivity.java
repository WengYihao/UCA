package com.cn.uca.ui.view.home.samecityka;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.SetTicketInfoBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.samecityka.TicketName;
import com.cn.uca.impl.yusheng.EditItemClick;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.AndroidBug5497Workaround;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.MyEditText;

import java.util.ArrayList;
import java.util.List;

public class EnListActivity extends BaseBackActivity implements View.OnClickListener,EditItemClick,TicketName{

    private TextView back,finish,online,unwanted,notice,charge,free;
    private CheckBox abroad;
    private FluidLayout recommendView;
    private List<String> recommendList;
    private ArrayList<String> selectList;
    private Dialog dialog;
    private View inflate;
    private MyEditText text;
    private TextView btn_cancel,addTicket;
    private ScrollView scrollView;
    private LinearLayout layout,layouy2;
    private List<SetTicketInfoBean> list;
    private int type = 1;
    private int isCharge = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_list);
        AndroidBug5497Workaround.assistActivity(this);
        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        finish = (TextView)findViewById(R.id.finish);
        online= (TextView)findViewById(R.id.online);
        unwanted = (TextView)findViewById(R.id.unwanted);
        notice = (TextView)findViewById(R.id.notice);
        abroad = (CheckBox)findViewById(R.id.abroad);
        addTicket = (TextView)findViewById(R.id.addTicket);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        layout = (LinearLayout)findViewById(R.id.layout);
        layouy2 = (LinearLayout)findViewById(R.id.layout2);
        charge = (TextView)findViewById(R.id.charge);//无需报名-收费
        free = (TextView)findViewById(R.id.free);//无需报名-免费
        back.setOnClickListener(this);
        finish.setOnClickListener(this);
        online.setOnClickListener(this);
        unwanted.setOnClickListener(this);
        notice.setOnClickListener(this);
        addTicket.setOnClickListener(this);
        charge.setOnClickListener(this);
        free.setOnClickListener(this);
        abroad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    abroad.setTextColor(getResources().getColor(R.color.ori));
                }else{
                    abroad.setTextColor(getResources().getColor(R.color.grey2));
                }
            }
        });
        list = new ArrayList<>();
        recommendList = new ArrayList<>();
        selectList = new ArrayList<>();
        recommendView = (FluidLayout)findViewById(R.id.recommendView);
        recommendList.add("姓名");
        recommendList.add("电话");
        recommendList.add("微信");
        recommendList.add("QQ");
        recommendList.add("邮箱");
        recommendList.add("生日");
        recommendList.add("性别");
        recommendList.add("年龄");
        recommendList.add("公司");
        recommendList.add("职业");
        recommendList.add("学校");
        recommendList.add("公司");
        recommendList.add("血型");
        recommendList.add("爱好");
        recommendList.add("添加+");
        genTag(recommendList,recommendView);
        selectList.add("姓名");
        selectList.add("电话");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.finish:
                switch (type){
                    case 1://在线报名
                        if (abroad.isChecked()){
                            for(int i = 0;i<layout.getChildCount();i++){
                                SetTicketInfoBean bean = new SetTicketInfoBean();
                                TextView ticketName = (TextView)layout.getChildAt(i).findViewById(R.id.ticket_name);
                                CheckBox checkPrice = (CheckBox)layout.getChildAt(i).findViewById(R.id.checkPrice);
                                CheckBox checkSum = (CheckBox)layout.getChildAt(i).findViewById(R.id.checkSum);
                                CheckBox checkMax = (CheckBox)layout.getChildAt(i).findViewById(R.id.checkMax);
                                Switch examine = (Switch)layout.getChildAt(i).findViewById(R.id.examine) ;
                                EditText price = (EditText)layout.getChildAt(i).findViewById(R.id.price);
                                EditText sum = (EditText)layout.getChildAt(i).findViewById(R.id.sum);
                                EditText maxNum = (EditText)layout.getChildAt(i).findViewById(R.id.maxNum);
                                bean.setTicket_name(ticketName.getText().toString());
                                if (checkPrice.isChecked()){
                                    bean.setPrice(0);
                                }else{
                                    if (StringXutil.isEmpty(price.getText().toString())){
                                        ToastXutil.show("请填写票价");
                                    }else{
                                        bean.setPrice(Double.parseDouble(price.getText().toString()));
                                    }
                                }
                                if (checkSum.isChecked()){
                                    bean.setSum_ticket(-1);
                                }else{
                                    if (StringXutil.isEmpty(sum.getText().toString())){
                                        ToastXutil.show("请填写票量");
                                    }else{
                                        bean.setSum_ticket(Integer.parseInt(sum.getText().toString()));
                                    }
                                }
                                if (checkMax.isChecked()){
                                    bean.setLimit_ticket(-1);
                                }else{
                                    if (StringXutil.isEmpty(maxNum.getText().toString())){
                                        ToastXutil.show("请限购票量");
                                    }else{
                                        bean.setLimit_ticket(Integer.parseInt(maxNum.getText().toString()));
                                    }
                                }
                                if (examine.isChecked()){
                                    bean.setOpen_examine("true");
                                }else{
                                    bean.setOpen_examine("false");
                                }
                                list.add(bean);
                            }
                            Intent intent = new Intent();
                            intent.putParcelableArrayListExtra("ticketList",(ArrayList<? extends Parcelable>) list);//门票
                            intent.putStringArrayListExtra("infoList",selectList);
                            intent.putExtra("type",type);
                            setResult(3,intent);
                            EnListActivity.this.finish();
                        }else{
                            ToastXutil.show("在线报名需先阅读并接受发布须知");
                        }
                        break;
                    case 2:
                        Intent intent = new Intent();
                        intent.putExtra("type",type);
                        intent.putExtra("isCharge",isCharge);
                        setResult(3,intent);
                        EnListActivity.this.finish();
                        break;
                }
                break;
            case R.id.online:
                type = 1;
                online.setTextColor(getResources().getColor(R.color.white));
                online.setBackgroundResource(R.drawable.twenty_circular_ori_background);
                unwanted.setTextColor(getResources().getColor(R.color.grey));
                unwanted.setBackgroundResource(R.drawable.twenty_circular_white_background);
                scrollView.setVisibility(View.VISIBLE);
                layouy2.setVisibility(View.GONE);
                break;
            case R.id.unwanted:
                type = 2;
                unwanted.setTextColor(getResources().getColor(R.color.white));
                unwanted.setBackgroundResource(R.drawable.twenty_circular_ori_background);
                online.setTextColor(getResources().getColor(R.color.grey));
                online.setBackgroundResource(R.drawable.twenty_circular_white_background);
                scrollView.setVisibility(View.GONE);
                layouy2.setVisibility(View.VISIBLE);
                break;
            case R.id.notice://发布须知
                ToastXutil.show("我知道了");
                break;
            case R.id.addTicket:
                addTicketItem();
                break;
            case R.id.btn_cancel:
                String lable_text = text.getText().toString();
                recommendList.remove(recommendList.size()-1);
                recommendList.add(lable_text);
                recommendList.add("添加+");
                genTag(recommendList,recommendView);
                dialog.dismiss();
                break;
            case R.id.charge://收费
                charge.setBackgroundResource(R.drawable.twenty_circular_ori_background);
                charge.setTextColor(getResources().getColor(R.color.white));
                free.setBackgroundResource(0);
                free.setTextColor(getResources().getColor(R.color.gray));
                isCharge = 1;
                break;
            case R.id.free://免费
                free.setBackgroundResource(R.drawable.twenty_circular_ori_background);
                free.setTextColor(getResources().getColor(R.color.white));
                charge.setBackgroundResource(0);
                charge.setTextColor(getResources().getColor(R.color.gray));
                isCharge = 0;
                break;
        }
    }
    private void addresspopuWindow(final TextView view,final TicketName name){
        View inflate = LayoutInflater.from(this).inflate(R.layout.lvpai_addpic_dialog, null);
        final TextView title = (TextView)inflate.findViewById(R.id.title);
        title.setText("添加门票");
        final EditText albumname = (EditText)inflate.findViewById(R.id.albumname);
        TextView positiveButton = (TextView)inflate.findViewById(R.id.positiveButton);//确定
        TextView negativeButton = (TextView)inflate.findViewById(R.id.negativeButton);//取消


        final PopupWindow popupWindow = new PopupWindow(inflate, MyApplication.width,
                MyApplication.height/3, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (albumname.getText().toString() != null){
                    name.getName(view,albumname.getText().toString());
                    popupWindow.dismiss();
                }else{
                    ToastXutil.show("门票名不能为空");
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    private void addTicketItem(){
        final View itemview = View.inflate(this, R.layout.add_ticket_item,null);
        layout.addView(itemview);
        final TextView ticket_name = (TextView)itemview.findViewById(R.id.ticket_name);
        final TextView delete = (TextView)itemview.findViewById(R.id.delete);
        final CheckBox checkPrice = (CheckBox)itemview.findViewById(R.id.checkPrice);
        final CheckBox checkSum = (CheckBox)itemview.findViewById(R.id.checkSum);
        final CheckBox checkMax = (CheckBox)itemview.findViewById(R.id.checkMax);
        final Switch examine = (Switch)itemview.findViewById(R.id.examine) ;
        ticket_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addresspopuWindow(ticket_name,EnListActivity.this);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(itemview);
            }
        });
        checkPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPrice.isChecked()){
                    checkPrice.setBackgroundResource(R.drawable.text_bg_ori);
                    checkPrice.setTextColor(getResources().getColor(R.color.white));
                }else{
                    checkPrice.setBackgroundResource(R.drawable.text_lable_gray_bg);
                    checkPrice.setTextColor(getResources().getColor(R.color.grey2));
                }
            }
        });
        checkSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSum.isChecked()){
                    checkSum.setBackgroundResource(R.drawable.text_bg_ori);
                    checkSum.setTextColor(getResources().getColor(R.color.white));
                }else{
                    checkSum.setBackgroundResource(R.drawable.text_lable_gray_bg);
                    checkSum.setTextColor(getResources().getColor(R.color.grey2));
                }
            }
        });
        checkMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMax.isChecked()){
                    checkMax.setBackgroundResource(R.drawable.text_bg_ori);
                    checkMax.setTextColor(getResources().getColor(R.color.white));
                }else{
                    checkMax.setBackgroundResource(R.drawable.text_lable_gray_bg);
                    checkMax.setTextColor(getResources().getColor(R.color.grey2));
                }
            }
        });
        examine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                  //门票需要审核
                }else {
                   //门票不需要审核
                }
            }
        });
    }
    private void genTag(final List<String> list, FluidLayout fluidLayout) {
        fluidLayout.removeAllViews();
        fluidLayout.setGravity(Gravity.TOP);
        for (int i = 0; i < list.size(); i++) {
            final CheckBox tv = new CheckBox(this);
            tv.setText(list.get(i));
            tv.setTextSize(13);
            tv.setBackgroundResource(R.drawable.text_lable_gray_bg);
            tv.setTextColor(getResources().getColor(R.color.grey2));
            tv.setButtonDrawable(0);
            tv.setPadding(30,0,30,0);
            for (int a = 0;a < selectList.size();a++){
                if (list.get(i).equals(selectList.get(a))){
                    tv.setChecked(true);
                    tv.setBackgroundResource(R.drawable.twenty_circular_ori_background);
                    tv.setTextColor(getResources().getColor(R.color.white));
                }
            }
            if (i<=1){
                tv.setChecked(true);
                tv.setBackgroundResource(R.drawable.twenty_circular_ori_background);
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setEnabled(false);
            }
            if (list.size() - i == 1){
                tv.setBackgroundResource(R.drawable.text_lable_bg);
                tv.setTextColor(getResources().getColor(R.color.ori));
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv.getText().toString().equals("添加+")){
                        show();
                    }else{
                        if (tv.isChecked()){
                            tv.setBackgroundResource(R.drawable.twenty_circular_ori_background);
                            tv.setTextColor(getResources().getColor(R.color.white));
                            selectList.add(tv.getText().toString());
                        }else{
                            tv.setBackgroundResource(R.drawable.text_lable_gray_bg);
                            tv.setTextColor(getResources().getColor(R.color.grey2));
                            for (int b =0;b < selectList.size();b++){
                                if (selectList.get(b).equals(tv.getText().toString())){
                                    selectList.remove(b);
                                }
                            }
                        }
                    }
                }
            });
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 12, 20, 12);
            fluidLayout.addView(tv,params);
        }
    }

    /**
     * 添加自定义标签
     */
    private void show(){
        dialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.show_text_dialog, null);
        text = (MyEditText)inflate.findViewById(R.id.text);
        btn_cancel = (TextView)inflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = MyApplication.width;
        dialogWindow.setAttributes(params);
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialog.show();//显示对话框
    }

    @Override
    public void click(View view) {
        ToastXutil.show((int)view.getTag()+"---");
    }

    @Override
    public void getName(TextView view, String name) {
        view.setText(name);
    }
}

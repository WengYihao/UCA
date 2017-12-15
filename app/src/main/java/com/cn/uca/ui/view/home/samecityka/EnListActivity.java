package com.cn.uca.ui.view.home.samecityka;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.AddTicketAdapter;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.ItemClick;
import com.cn.uca.impl.yusheng.EditItemClick;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.AndroidBug5497Workaround;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.FluidLayout;
import com.cn.uca.view.MyEditText;
import com.cn.uca.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

public class EnListActivity extends BaseBackActivity implements View.OnClickListener,EditItemClick{

    private TextView back,online,unwanted;
    private FluidLayout recommendView;
    private List<String> recommendList;
    private List<String> selectList;
    private Dialog dialog;
    private View inflate;
    private MyEditText text;
    private TextView btn_cancel,addTicket;
    private List<Integer> list;
    private NoScrollListView listView;
    private AddTicketAdapter adapter;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_list);
        AndroidBug5497Workaround.assistActivity(this);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        back = (TextView)findViewById(R.id.back);
        online= (TextView)findViewById(R.id.online);
        unwanted = (TextView)findViewById(R.id.unwanted);
        addTicket = (TextView)findViewById(R.id.addTicket);
        listView = (NoScrollListView)findViewById(R.id.listView);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        back.setOnClickListener(this);
        online.setOnClickListener(this);
        unwanted.setOnClickListener(this);
        addTicket.setOnClickListener(this);

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
        list.add(1);
        adapter = new AddTicketAdapter(list,this,this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.online:
                online.setTextColor(getResources().getColor(R.color.white));
                online.setBackgroundResource(R.drawable.twenty_circular_ori_background);
                unwanted.setTextColor(getResources().getColor(R.color.grey));
                unwanted.setBackgroundResource(R.drawable.twenty_circular_white_background);
                scrollView.setVisibility(View.VISIBLE);
                break;
            case R.id.unwanted:
                unwanted.setTextColor(getResources().getColor(R.color.white));
                unwanted.setBackgroundResource(R.drawable.twenty_circular_ori_background);
                online.setTextColor(getResources().getColor(R.color.grey));
                online.setBackgroundResource(R.drawable.twenty_circular_white_background);
                scrollView.setVisibility(View.GONE);
                break;
            case R.id.addTicket:
                list.add(list.size()+1);
                adapter.setList(list);
                break;
            case R.id.btn_cancel:
                String lable_text = text.getText().toString();
                recommendList.remove(recommendList.size()-1);
                recommendList.add(lable_text);
                recommendList.add("添加+");
                genTag(recommendList,recommendView);
                dialog.dismiss();
                break;
        }
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
}

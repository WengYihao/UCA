package com.cn.uca.ui.view.home.yusheng;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.datepicker.DateType;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.datepicker.OnSureLisener;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.datepicker.DatePickDialog;
import com.cn.uca.view.dialog.WaveView;

import java.text.ParseException;
import java.util.Date;

public class YuShengActivity extends BaseBackActivity implements View.OnClickListener,OnSureLisener{

    private TextView start,baithDate,sex;
    private LinearLayout layout1,layout2;
    private WaveView waveView;
    private View inflate;
    private TextView man;
    private TextView woman;
    private TextView btn_cancel;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_yu_sheng);

        initView();
    }

    private void initView() {
        start = (TextView)findViewById(R.id.start);
        baithDate = (TextView)findViewById(R.id.baithDate);
        sex = (TextView)findViewById(R.id.sex);
        waveView = (WaveView)findViewById(R.id.wave);

        layout1 = (LinearLayout)findViewById(R.id.layout1);
        layout2 = (LinearLayout)findViewById(R.id.layout2);

        SetLayoutParams.setLinearLayout(layout1, MyApplication.width/2,0);
        SetLayoutParams.setLinearLayout(layout2, MyApplication.width/2,0);
        start.setOnClickListener(this);
        sex.setOnClickListener(this);
        baithDate.setOnClickListener(this);

        waveView.setOnWaveAnimationListener(new WaveView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {
                StatusMargin.setRelativeLayoutBottom(YuShengActivity.this,start,SystemUtil.dip2px(y+17));
            }
        });
    }

    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(60);
        //设置标题
        dialog.setTitle(R.string.infoActivity_date_title_text);
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(this);
        dialog.show();
    }
    private void show(){
        dialog = new Dialog(this,R.style.dialog_style);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.choose_sex_dialog, null);
        //初始化控件
        man = (TextView) inflate.findViewById(R.id.man);
        woman = (TextView) inflate.findViewById(R.id.woman);
        btn_cancel = (TextView)inflate.findViewById(R.id.btn_cancel);
        man.setOnClickListener(this);
        woman.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        StatusMargin.setFrameLayoutBottom(YuShengActivity.this,inflate,20);
        dialog.show();//显示对话框
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                ToastXutil.show("点我干嘛");
                break;
            case R.id.baithDate:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.sex:
                show();
                break;
            case R.id.man:
                sex.setText("男");
                dialog.dismiss();
                break;
            case R.id.woman:
                sex.setText("女");
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onSure(Date date) {
        try {
            Date date1 = SystemUtil.StringToUtilDate(SystemUtil.getCurrentDateOnly());
            if (date.after(date1)){
                ToastXutil.show("出生日期选择不合法！");
            }else{
                String d = SystemUtil.UtilDateToString(date);
                baithDate.setText(d);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

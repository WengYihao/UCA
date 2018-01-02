package com.cn.uca.ui.view.home.lvpai;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.home.raiders.SpotNameAdapter;
import com.cn.uca.config.MyApplication;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;

public class MerchantCommodityActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,more,add_new_commodity,more_operation;
    private LinearLayout layout,layout1,layout2,layout3,layout4;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_commodity);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        more = (TextView)findViewById(R.id.more);
        layout = (LinearLayout)findViewById(R.id.layout);
        back.setOnClickListener(this);
        more.setOnClickListener(this);
        layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.more:
                show();
                break;
            case R.id.add_new_commodity:
                popupWindow.dismiss();
                startActivity(new Intent(MerchantCommodityActivity.this,AddMerchantActivity.class));
                break;
            case R.id.more_operation:
                popupWindow.dismiss();
                ToastXutil.show("还有啥骚操作？");
                break;
            case R.id.layout:
                showMore();
                break ;
            case R.id.layout1:
                startActivity(new Intent(MerchantCommodityActivity.this,MerchantOrderActivity.class));
                break;
            case R.id.layout2:
                ToastXutil.show("推荐");
                break;
            case R.id.layout3:
                ToastXutil.show("编辑");
                break;
            case R.id.layout4:
                ToastXutil.show("下架");
                break;
        }
    }
    private void showMore(){
        Dialog dialog = new Dialog(this,R.style.dialog_style);
        View inflate = LayoutInflater.from(this).inflate(R.layout.lvpai_manager_dialog, null);
        layout1 = (LinearLayout)inflate.findViewById(R.id.layout1);
        layout1.setOnClickListener(this);
        layout2 = (LinearLayout)inflate.findViewById(R.id.layout2);
        layout2.setOnClickListener(this);
        layout3 = (LinearLayout)inflate.findViewById(R.id.layout3);
        layout3.setOnClickListener(this);
        layout4 = (LinearLayout)inflate.findViewById(R.id.layout4);
        layout4.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
//        params.dimAmount = 0f;
        params.width = MyApplication.width;
        params.height = MyApplication.width/2;
        dialog.show();//显示对话框
    }
    private void show(){
		View show = LayoutInflater.from(this).inflate(R.layout.lvpai_commodity_dialog, null);
        add_new_commodity = (TextView)show.findViewById(R.id.add_new_commodity);//添加新品
        more_operation = (TextView)show.findViewById(R.id.more_operation);//批量操作
        add_new_commodity.setOnClickListener(this);
        more_operation.setOnClickListener(this);
		popupWindow = new PopupWindow(show, MyApplication.width/3,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.RIGHT|Gravity.TOP,20,50);
	}
}

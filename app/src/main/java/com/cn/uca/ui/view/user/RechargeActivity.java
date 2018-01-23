package com.cn.uca.ui.view.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.util.pay.PayUtil;

public class RechargeActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView back;
    private RadioButton btn1,btn2,btn3;
    private EditText num;
    private LinearLayout wechatpay_layout,alipay_layout,unionpay_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        btn1 = (RadioButton)findViewById(R.id.btn1);
        btn2 = (RadioButton)findViewById(R.id.btn2);
        btn3 = (RadioButton)findViewById(R.id.btn3);
        num = (EditText)findViewById(R.id.num);
        wechatpay_layout = (LinearLayout)findViewById(R.id.wechatpay_layout);
        alipay_layout = (LinearLayout)findViewById(R.id.alipay_layout);
        unionpay_layout = (LinearLayout)findViewById(R.id.unionpay_layout);

        back.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        wechatpay_layout.setOnClickListener(this);
        alipay_layout.setOnClickListener(this);
        unionpay_layout.setOnClickListener(this);

        btn1.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.btn1:
                if (btn2.isChecked()){
                    btn2.setChecked(false);
                }
                if (btn3.isChecked()){
                    btn3.setChecked(false);
                }
                break;
            case R.id.btn2:
                if (btn1.isChecked()){
                    btn1.setChecked(false);
                }
                if (btn3.isChecked()){
                    btn3.setChecked(false);
                }
                break;
            case R.id. btn3:
                if (btn1.isChecked()){
                    btn1.setChecked(false);
                }
                if (btn2.isChecked()){
                    btn2.setChecked(false);
                }
                break;
            case R.id.wechatpay_layout:
                PayUtil.weChatPay(null,getMoney(),null,0);
                break;
            case R.id.alipay_layout:
                PayUtil.aliPay(this,null,getMoney(),null,0);
                break;
            case R.id.unionpay_layout:
                ToastXutil.show("该功能暂不支持");
                break;
        }
    }

    private String getMoney(){
        if (btn1.isChecked()){
            return "100";
        }else if(btn2.isChecked()){
            return "300";
        }else if (btn3.isChecked()){
            if (StringXutil.isEmpty(num.getText().toString())){
                return null;
            }else{
                String price = num.getText().toString();
                return price;
            }
        }
        return null;
    }
}

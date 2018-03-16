package com.cn.uca.ui.view.home.lvpai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.datechoice.views.DatePicker;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class DatePickerActivity extends BaseBackActivity implements View.OnClickListener,DatePicker.OnDateSelectedListener {

    private TextView back,sure;
    private EditText day;
    private  DatePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        sure = (TextView)findViewById(R.id.sure);
        day = (EditText)findViewById(R.id.day);
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
        picker = (DatePicker) findViewById(R.id.date);
        picker.setFestivalDisplay(false);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mon = calendar.get(Calendar.MONTH)+1;
        picker.setDate(year,mon);
        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(List<String> date) {
                String result = "";
                Iterator iterator = date.iterator();
                while (iterator.hasNext()) {
                    result += iterator.next();
                    if (iterator.hasNext()) {
                        result += "\n";
                    }
                }
                Toast.makeText(DatePickerActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.sure:
                Log.e("456",picker.getDate().toString());
                if (StringXutil.isEmpty(day.getText().toString())){
                    ToastXutil.show("请输入服务时长");
                }else{
                    if (StringXutil.isEmpty(picker.getDate().toString())){
                        ToastXutil.show("请选择忙碌时间");
                    }else{
                        int dayNum = Integer.parseInt(day.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra("day",dayNum);
                        intent.putExtra("list",StringXutil.ListtoString(picker.getDate()    ));
                        setResult(6,intent);
                        this.finish();
                    }
                }

                ;

                break;
        }
    }

    @Override
    public void onDateSelected(List<String> date) {

    }
}

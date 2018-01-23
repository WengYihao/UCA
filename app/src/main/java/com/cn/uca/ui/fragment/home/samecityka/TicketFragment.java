package com.cn.uca.ui.fragment.home.samecityka;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.samecityka.MyTicketCodeBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.impl.CallBackValue;
import com.cn.uca.util.SetLayoutParams;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.zxing.BarcodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by asus on 2017/12/15.
 */

public class TicketFragment extends Fragment implements View.OnClickListener{

    private View view;
    private LinearLayout back_ticket;
    private ImageView pic;
    private TextView name,stateStr;
    private int id,state;
    private String code,ticketname;
    private CallBackValue callBackValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket, null);

        initView();
        return view;
    }

    public static TicketFragment newInstance(int id,String code,int state,String ticketname) {
        TicketFragment fragment = new TicketFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("code", code);
        bundle.putInt("state",state);
        bundle.putString("ticketname",ticketname);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    protected void onAttachToContext(Context context) {
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();
    }
    private void initView() {
        id = getArguments().getInt("id");
        code = getArguments().getString("code");
        state = getArguments().getInt("state");
        ticketname = getArguments().getString("ticketname");
        stateStr = (TextView)view.findViewById(R.id.stateStr);
        name = (TextView)view.findViewById(R.id.name);
        back_ticket = (LinearLayout)view.findViewById(R.id.back_ticket);
        back_ticket.setOnClickListener(this);
        pic = (ImageView) view.findViewById(R.id.pic);
        SetLayoutParams.setLinearLayout(pic, MyApplication.width*3/5,MyApplication.width*3/5);
        pic.setImageBitmap(encodeAsBitmap(id+code,state));
        name.setText("票券编号："+id+code);
        switch (state){
            case 1:
                stateStr.setText("可使用");
                stateStr.setTextColor(getResources().getColor(R.color.grey));
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
        }
    }



    private Bitmap encodeAsBitmap(String str,int state) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 250, 250);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            switch (state){
                case 1://可使用
                    barcodeEncoder.setWHITE(0xFFFFFFFF);
                    barcodeEncoder.setBLACK(0xFF000000);
                    break;
                default://不可使用
                    barcodeEncoder.setWHITE(0xFFFFFFFF);
                    barcodeEncoder.setBLACK(0xDDDDDDDD);
                    break;
            }
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_ticket:
                MyTicketCodeBean bean = new MyTicketCodeBean();
                bean.setNumber(1);
                bean.setCity_cafe_ticket_id(id);
                bean.setTicket_name(ticketname);
                callBackValue.sendMessage(1,bean);
                break;
        }
    }
}
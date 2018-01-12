package com.cn.uca.ui.fragment.home.samecityka;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.zxing.BarcodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by asus on 2017/12/15.
 */

public class TicketFragment extends Fragment {

    private View view;
    private ImageView pic;
    private TextView name;
    private String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket, null);

        initView();
        return view;
    }

    public static TicketFragment newInstance(String type) {
        TicketFragment fragment = new TicketFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView() {
        type = getArguments().getString("name");
        name = (TextView)view.findViewById(R.id.name);
        pic = (ImageView) view.findViewById(R.id.pic);
        pic.setImageBitmap(encodeAsBitmap(type));
        name.setText("票券编号："+type);
    }

    private Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 200, 200);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }
        return bitmap;
    }
}